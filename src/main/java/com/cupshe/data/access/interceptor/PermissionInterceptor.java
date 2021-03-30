package com.cupshe.data.access.interceptor;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cupshe.data.access.common.CreateIdColumn;
import com.cupshe.data.access.common.PermissionControl;
import com.cupshe.data.access.common.User;
import com.cupshe.data.access.constants.DataScopeConstants;
import com.cupshe.data.access.helper.PermissionHelper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;

@Intercepts({
	@Signature(type = Executor.class, method = "query", 
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = Executor.class, method = "query", 
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
@Slf4j
public class PermissionInterceptor implements Interceptor {

	private static final int FIRST_QUERY_ARGS_COUNT = 4;
	
	public Object intercept(Invocation invocation) throws Throwable {
		Object returnObject = null;
		try {
			// 1、解析参数
			Object[] args = invocation.getArgs();
			MappedStatement ms = (MappedStatement) args[0];
			// 方法参数
			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds) args[2];
			ResultHandler resultHandler = (ResultHandler) args[3];
			// 执行sql的执行器
			Executor executor = (Executor) invocation.getTarget();
			CacheKey cacheKey;
			// sql封装的对象
			BoundSql boundSql;
			if (isFirstQuery(args)) {
			    boundSql = ms.getBoundSql(parameter);
			    cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
			} else {
			    cacheKey = (CacheKey) args[4];
			    boundSql = (BoundSql) args[5];
			}
			
			// 2、判断
			// 获取权限控制
			PermissionControl permissionControl = PermissionHelper.getPermission();
			if(!shouldSkip(permissionControl)) {
				// 不跳过
				String name = ms.getId();
				// 3、获取查询的sql语句
				String sql = boundSql.getSql();
				// 注意：parameter和parameterObject是一样的 。parameter==和parameterObject
				Object parameterObject = boundSql.getParameterObject();
				// log.info("拦截的方法名是:" + name + ",sql是" + sql );
				// 4、处理sql
				String newSql = processSelectSql(sql, permissionControl).getSql();
				// 5、封装新sql
				BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
				MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
				for (ParameterMapping mapping : boundSql.getParameterMappings()) {
				    String prop = mapping.getProperty();
				    if (boundSql.hasAdditionalParameter(prop)) {
				        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
				    }
				}
				args[0] = newMs;
				if (!isFirstQuery(args)) {
					args[5] = newBoundSql;
				} else {
					// log.info("执行sql，isFirstQuery sql = 【{}】", sql);
				}
			}
			returnObject = invocation.proceed();
		} catch (Exception e) {
			log.error("PermissionInterceptor error", e);
		} finally {
			finallyProcess();
		}
		return returnObject;
	}

	/**
	 * finally处理
	 */
	protected void finallyProcess() {
	}

	/**
	 * 是否是第一个query方法
	 * @param args
	 * @return
	 */
	private boolean isFirstQuery(Object[] args) {
		return args.length == FIRST_QUERY_ARGS_COUNT;
	}
	
	/**
	 * 是否应该跳过
	 * @return
	 */
	protected boolean shouldSkip(PermissionControl permissionControl) {
		return permissionControl == null 
				|| permissionControl.getPermission().getDataScope() == null
				|| permissionControl.getPermission().getDataScope() == DataScopeConstants.ALL_SCOPE
				|| StringUtils.isEmpty(permissionControl.getCreateIdColumn().getColumnName())
				|| CollectionUtils.isEmpty(permissionControl.getUserList());
	}

	/**
	 * 复制MappedStatement
	 * @param ms
	 * @param newSqlSource
	 * @return
	 */
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 处理sql
     * @param sql
     * @return
     */
	protected SelectBodySql processSelectSql(String sql, PermissionControl permissionControl) {
		SelectBodySql selectBodySql = new SelectBodySql();
		PlainSelect selectBody = null;
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            selectBody = (PlainSelect) select.getSelectBody();
            String mainTable = null;
            SelectBodySql subSelectBodySql = null;
            if (selectBody.getFromItem() instanceof Table) {
                mainTable = ((Table) selectBody.getFromItem()).getName().replace("`", "");
            } else if (selectBody.getFromItem() instanceof SubSelect) {
            	subSelectBodySql = processSelectSql(((SubSelect) selectBody.getFromItem()).getSelectBody().toString(), permissionControl);
            }
            if (subSelectBodySql != null) {
                SubSelect subSelect = (SubSelect) selectBody.getFromItem();
                subSelect.setSelectBody(subSelectBodySql.getSelectBody());
                selectBody.setFromItem(subSelect);
            }
            
            String mainTableAlias = mainTable;
            try {
                mainTableAlias = selectBody.getFromItem().getAlias().getName();
            } catch (Exception e) {
                log.debug("当前sql中， " + mainTable + " 没有设置别名");
            }
			if (!StringUtils.isEmpty(mainTableAlias) 
					&& selectBody.getFromItem() instanceof Table) {
				CreateIdColumn createIdColumn = permissionControl.getCreateIdColumn();
				List<User> userList = permissionControl.getUserList();
				String condExpr = jointCondExpr(mainTableAlias, createIdColumn, userList);
                if (selectBody.getWhere() == null) {
                    selectBody.setWhere(CCJSqlParserUtil.parseCondExpression(condExpr));
                } else {
                    AndExpression and = new AndExpression(selectBody.getWhere(), CCJSqlParserUtil.parseCondExpression(condExpr));
                    selectBody.setWhere(and);
                }
        	}
            sql = select.toString();
        } catch (JSQLParserException e) {
            log.error("processSelectSql error", e);
        }
        selectBodySql.setSql(sql);
        selectBodySql.setSelectBody(selectBody);
        return selectBodySql;
    }

	/**
	 * 拼接表达式
	 * @param mainTableAlias
	 * @param createIdColumn
	 * @param userList
	 * @return
	 */
	private String jointCondExpr(String mainTableAlias, CreateIdColumn createIdColumn, List<User> userList) {
		String condExpr;
		condExpr = mainTableAlias + "." + createIdColumn.getColumnName() + " ";
		condExpr = condExpr += processUserList(userList);
		return condExpr;
	}
	
	/**
	 * 处理用户数据
	 * @param userList
	 * @return
	 */
	private String processUserList(List<User> userList) {
		String condExpr = "";
		if(!CollectionUtils.isEmpty(userList)) {
			int size = userList.size();
			if(size == 1) {
				User user = userList.get(0);
				condExpr += " = " + user.getUserId();
			}else {
				int index = 1;
				condExpr += " in ( ";
				for (User user : userList) {
					condExpr += user.getUserId();
					if(index < size) {
						condExpr += ",";
					}
					index++;
				}
				condExpr += " )";
			}
		}
		return condExpr;
	}
	
	
	
	@Override
	public Object plugin(Object target) {
		return Interceptor.super.plugin(target);
	}

	@Override
	public void setProperties(Properties properties) {
		Interceptor.super.setProperties(properties);
	}
	
	/**
	 * 静态内部类，封装SelectBody和sql
	 * <p>Title: SelectBodySql</p>
	 * <p>Description: </p>
	 * @author zhoutaoping
	 * @date 2020年10月29日
	 */
	@Data
	private static class SelectBodySql{
		private SelectBody selectBody;
		private String sql;
	}
    
    /**
     * 静态内部类，封装BoundSql
     * <p>Title: BoundSqlSqlSource</p>
     * <p>Description: </p>
     * @author zhoutaoping
     * @date 2020年10月29日
     */
    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
    
}