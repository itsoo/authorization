package com.cupshe.authorization.shiro.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "auth.config", ignoreUnknownFields = true)
public class AuthConfigProperties {

	private Map<String, String> filter;
	
}
