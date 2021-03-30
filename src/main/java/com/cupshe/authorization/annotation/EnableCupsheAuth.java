package com.cupshe.authorization.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.cupshe.authorization.shiro.config.ShiroConfiguration;

/**
 * 开启权限控制
 * <p>Title: EnableCupsheAuth</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ShiroConfiguration.class)
public @interface EnableCupsheAuth {

}
