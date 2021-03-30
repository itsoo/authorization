package com.cupshe.authorization.shiro.constants;

/**
 * 常量
 * <p>Title: ShiroConstants</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月27日
 */
public class ShiroConstants {

	/**
	 * 会话key
	 */
	public static final String SESSION_KEY = "SESSION";

	/**
	 * 授权key
	 */
	public static final String AUTHORIZATIONINFO_KEY = "AUTHORIZATIONINFO";
	
	/**
	 * 权限key
	 */
	public static final String PERMISSION_KEY = "PERMISSION";
	
	/**
	 * token Key
	 */
	public static final String TOKEN_KEY = "TOKEN";
	
	/**
	 * 毫秒/秒
	 */
	public static final long MILLIS_PER_SECOND = 1000;
	/**
	 * 毫秒/分钟
	 */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * 毫秒/小时
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    
    /**
     * 默认全局超时时间，24小时，单位毫秒
     */
    public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT = 24 * MILLIS_PER_HOUR;
    
    /**
     * 默认全局超时时间，24小时，单位分钟
     */
    public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT_MINUTE = 24 * 60;
    
    /**
     * 默认cookie name
     */
    public static final String DEFAULT_COOKIE_NAME = "mirror_token_id";
    
    /**
     * 默认cookie domain
     */
    public static final String DEFAULT_COOKIE_DOMAIN = "";
    
    /**
     * 默认cookie path
     */
    public static final String DEFAULT_COOKIE_PATH = "/";
	
}
