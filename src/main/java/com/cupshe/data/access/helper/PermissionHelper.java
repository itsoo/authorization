package com.cupshe.data.access.helper;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.util.CollectionUtils;

import com.cupshe.authorization.common.constants.SystemErrorEnum;
import com.cupshe.authorization.utils.ShiroUtils;
import com.cupshe.data.access.common.CreateIdColumn;
import com.cupshe.data.access.common.Permission;
import com.cupshe.data.access.common.PermissionControl;
import com.cupshe.data.access.common.User;
import com.cupshe.data.access.constants.ColumnConstants;
import com.cupshe.data.access.utils.CollectionUtil;
import com.cupshe.dc.service.domain.dto.DingUserInfoDTO;

/**
 * 权限帮助类
 * <p>Title: PermissionHelper</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月29日
 */
public class PermissionHelper extends PermissionHelperThreadlocal{
	
	/**
	 * 开启权限
	 */
	public static void startPermission() {
		startPermission(ColumnConstants.DEFAULT_COLUMN_NAME);
	}
	
	public static void startPermission(@NotNull int dataScope, @NotNull List<Long> userIdList) {
		startPermission(dataScope, ColumnConstants.DEFAULT_COLUMN_NAME, userIdList);
	}
	
	public static void startPermission(@NotNull String columnName) {
		DingUserInfoDTO loginUser = ShiroUtils.getLoginUser();
		if(loginUser == null) {
			throw new UnauthorizedException(SystemErrorEnum.K_400002.getErrorDesc());
		}
		Long userId = loginUser.getId();
		List<Integer> dataScopeList = loginUser.getDataScope();
		if(CollectionUtils.isEmpty(dataScopeList)) {
			throw new UnauthorizedException(SystemErrorEnum.K_400002.getErrorDesc());
		}
		Integer dataScope = CollectionUtil.min(dataScopeList);
		List<Long> userIdList = new ArrayList<Long>();
		userIdList.add(userId);
		startPermission(dataScope, columnName, userIdList);
	}
	
	private static void startPermission(@NotNull int dataScope, @NotNull String columnName, @NotNull List<Long> userIdList) {
    	Permission permission = new Permission(dataScope);
    	CreateIdColumn createIdColumn = new CreateIdColumn(columnName);
    	List<User> userList = new ArrayList<User>();
    	userIdList.stream().forEach(userId -> {
    		User user = new User(userId);
    		userList.add(user);
    	});
    	startPermission(permission, createIdColumn, userList);
    }
    
    private static void startPermission(Permission permission, CreateIdColumn createIdColumn, List<User> userList) {
    	PermissionControl permissionControl = new PermissionControl(permission, createIdColumn, userList);
    	startPermission(permissionControl);
    }
    
    private static void startPermission(PermissionControl permissionControl) {
    	setLocalPermissionControl(permissionControl);
    }
    
    /**
     * 关闭权限
     */
    public static void stopPermission() {
    	clearLocalPermissionControl();
    }
    
    /**
     * 获取权限
     * @return
     */
    public static PermissionControl getPermission() {
    	return getLocalPermissionControl();
    }
    
}
