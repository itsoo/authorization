package com.cupshe.authorization.shiro.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * <p>Title: ShiroConfigProperties</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
@Data
@ConfigurationProperties(prefix = "shiro.config", ignoreUnknownFields = true)
public class ShiroConfigProperties {

	private Long sessionTimeout;
	
	private String cookieName;
	
	private String cookieDomain;
	
	private String cookiePath;
	
}
