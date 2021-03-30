package com.cupshe.data.access.aspect;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import com.cupshe.data.access.annotation.OpenDataPermission;
import com.cupshe.data.access.constants.ColumnConstants;
import com.cupshe.data.access.helper.PermissionHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 切面
 * <p>Title: PermissionAspect</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月29日
 */
@Aspect
@Slf4j
public class PermissionAspect {
	
    @Pointcut("@annotation(com.cupshe.data.access.annotation.OpenDataPermission)")
    public void annotationPointCut(){}
    
    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
    	Object resultData = null;
    	try {
            String columnName = getColumnName(joinPoint);
    		PermissionHelper.startPermission(columnName);
    		Object[] args = joinPoint.getArgs();
    		resultData = joinPoint.proceed(args);
		} catch (Throwable e) {
			log.info("PermissionAspect error", e);
		} finally {
			PermissionHelper.stopPermission();
		}
    	return resultData;
    }

	private String getColumnName(ProceedingJoinPoint joinPoint) {
		Signature sig = joinPoint.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
		    throw new IllegalArgumentException("该注解只能用于方法");
		}
		msig = (MethodSignature) sig;
		Method m = msig.getMethod();
		OpenDataPermission annotation = AnnotationUtils.getAnnotation(m, OpenDataPermission.class);
		Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
		String columnName = annotationAttributes.get("columnName").toString();
		columnName = StringUtils.isEmpty(columnName) ? ColumnConstants.DEFAULT_COLUMN_NAME : columnName;
		return columnName;
	}

}
