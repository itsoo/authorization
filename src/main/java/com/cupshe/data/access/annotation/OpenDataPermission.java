package com.cupshe.data.access.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.cupshe.data.access.constants.ColumnConstants;

/**
 * 开启数据权限
 * <p>Title: OpenDataPermission</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年11月3日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenDataPermission {

	/**
	 * 列名
	 * @return
	 */
	@AliasFor("value")
	String columnName() default ColumnConstants.DEFAULT_COLUMN_NAME;
	
	/**
	 * value
	 * @return
	 */
	@AliasFor("columnName")
	String value() default ColumnConstants.DEFAULT_COLUMN_NAME;
	
}
