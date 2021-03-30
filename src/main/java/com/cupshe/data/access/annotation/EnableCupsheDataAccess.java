package com.cupshe.data.access.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.cupshe.data.access.config.PermissionHelperAutoConfiguration;

/**
 * 开启数据访问通知
 * <p>Title: EnableCupsheDataAccess</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月29日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(PermissionHelperAutoConfiguration.class)
public @interface EnableCupsheDataAccess {

}
