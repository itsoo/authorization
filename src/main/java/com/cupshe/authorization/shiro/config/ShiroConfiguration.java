package com.cupshe.authorization.shiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cupshe.authorization.shiro.constants.ShiroConstants;
import com.cupshe.authorization.shiro.controller.ShiroController;
import com.cupshe.authorization.shiro.exception.GlobalExceptionInterceptor;
import com.cupshe.authorization.shiro.properties.AuthConfigProperties;
import com.cupshe.authorization.shiro.properties.ShiroConfigProperties;
import com.cupshe.authorization.shiro.realm.WebLoginRealm;
import com.cupshe.authorization.shiro.session.CustomWebSessionManager;

/**
 * shiro配置类
 * <p>Title: ShiroConfiguration</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
@Configuration
public class ShiroConfiguration {

	private static final String ANON = "anon";

	private static final String SHIRO_UNLOGIN = "/shiro/unlogin";
	
	private static final String SHIRO_UNAUTH = "/shiro/unauth";

	/**
     * #工厂bean
     * 1、管理安全管理器
     * 2、配置url拦截规则
     * @return
     */
	@Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager, AuthConfigProperties authConfigProperties) {
		// 1、新建ShiroFilterFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 2、配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 3、配置url拦截规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        // 3.1、配置退出过滤器
        // 3.2、配置登录过滤器
        filterChainDefinitionMap.put(SHIRO_UNLOGIN, ANON);
        filterChainDefinitionMap.put(SHIRO_UNAUTH, ANON);
        parseProperties(authConfigProperties, filterChainDefinitionMap);
        // 3.3、其他接口不需要登录访问
        filterChainDefinitionMap.putIfAbsent("/**", ANON);
        // 3.4、配置未登录返回的接口，接口返回json数据
        shiroFilterFactoryBean.setLoginUrl(SHIRO_UNLOGIN);
        //没有权限操作时跳转页面
        // 3.5、登录未授权返回的数据
        shiroFilterFactoryBean.setUnauthorizedUrl(SHIRO_UNAUTH);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

	/**
	 * 解析properties
	 * @param authConfigProperties
	 * @param filterChainDefinitionMap
	 */
	private void parseProperties(AuthConfigProperties authConfigProperties,
			Map<String, String> filterChainDefinitionMap) {
		Map<String, String> filter = authConfigProperties.getFilter();
        if(MapUtils.isNotEmpty(filter)) {
        	for(Map.Entry<String, String> entry : filter.entrySet()) {
        		String key = entry.getKey();
        		String value = entry.getValue();
        		if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
        			String[] values = value.split(",");
        			for (String v : values) {
        				filterChainDefinitionMap.put(v, key);
					}
        		}
        	}
        }
	}
	

    /**
     * Shiro安全管理器
     * 1、配置会话管理器（getSessionId方法获取会话id，默认获取cookie中jsessionId的值）
     * 2、配置缓存管理（权限数据缓存）
     * 3、配置认证和授权realm（自定义）
     * 4、配置多realm管理（如果有必要的话）
     * @return
     */
    @Bean(name = "securityManager")
	public SecurityManager getSecurityManager(CustomWebSessionManager customWebSessionManager,
			MemoryConstrainedCacheManager memoryConstrainedCacheManager,  WebLoginRealm webLoginRealm) {
    	// 新建Shiro安全管理器DefaultWebSecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 1、配置会话管理器
        securityManager.setSessionManager(customWebSessionManager);
        // 2、配置缓存管理（权限数据缓存）
        securityManager.setCacheManager(memoryConstrainedCacheManager);
        // 3、配置认证和授权realm（自定义）
        securityManager.setRealm(webLoginRealm);
        return securityManager;
    }
    
    /**
     * shiro会话管理器（getSessionId方法获取会话id，默认获取cookie中jsessionId的值）
     * 1、配置会话数据访问层redisSessionDAO
     * 2、配置会话工厂
     * 3、配置会话存储的cookie（默认jsessionId）
     * @return
     */
    @Bean(name = "customWebSessionManager")
    public CustomWebSessionManager customWebSessionManager(ShiroConfigProperties shiroConfigProperties, MemorySessionDAO memorySessionDAO, SimpleCookie simpleCookie) {
    	// 新建自定义shiro会话管理器
    	CustomWebSessionManager sessionManager = new CustomWebSessionManager();
        // 避免url中出现jsessionid
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 1、配置会话数据访问层redisSessionDAO
        sessionManager.setSessionDAO(memorySessionDAO);
        // 2、配置会话工厂
//         sessionManager.setSessionFactory(sessionFactory);
        // 3、配置会话存储的cookie（默认jsessionId）
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        // 设置全局超时
        Long sessionTimeout = shiroConfigProperties.getSessionTimeout();
        if(sessionTimeout == null) {
        	sessionTimeout = ShiroConstants.DEFAULT_GLOBAL_SESSION_TIMEOUT_MINUTE;
        }
        sessionManager.setGlobalSessionTimeout(sessionTimeout * ShiroConstants.MILLIS_PER_MINUTE);
        return sessionManager;
    }
    
	/**
     * 会话数据访问层memorySessionDAO/redisSessionDAO
     * 1、配置会话id生成策略sessionIdGenerator
     * 2、redis-client：redisTemplate
     * @return
     */
    @Bean(name = "memorySessionDAO")
    public MemorySessionDAO memorySessionDAO(JavaUuidSessionIdGenerator sessionIdGenerator) {
    	MemorySessionDAO memorySessionDAO = new MemorySessionDAO();
    	memorySessionDAO.setSessionIdGenerator(sessionIdGenerator);
        //session过期时间及前缀
        return memorySessionDAO;
    }
    
    /**
     * 会话id生成策略sessionIdGenerator
     * @return
     */
    @Bean(name = "sessionIdGenerator")
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
        return sessionIdGenerator;
    }
	
    /**
     * 配置会话存储的cookie（默认jsessionId）
     * @return
     */
    @Bean(name = "simpleCookie")
    public SimpleCookie simpleCookie(ShiroConfigProperties shiroConfigProperties) {
        SimpleCookie cookie = new SimpleCookie(shiroConfigProperties.getCookieName() == null ? 
        		ShiroConstants.DEFAULT_COOKIE_NAME: shiroConfigProperties.getCookieName());
        //设置Cookie的域名 默认空，即当前访问的域名
        cookie.setDomain(shiroConfigProperties.getCookieDomain() == null ? 
        		ShiroConstants.DEFAULT_COOKIE_DOMAIN : shiroConfigProperties.getCookieDomain());
        //设置cookie的有效访问路径
        cookie.setPath(shiroConfigProperties.getCookiePath() == null ? 
        		ShiroConstants.DEFAULT_COOKIE_PATH : shiroConfigProperties.getCookiePath());
        //若此属性为true，则只有在http请求头中会带有此cookie的信息，而不能通过document.cookie来访问此cookie。
        cookie.setHttpOnly(true);
        return cookie;
    }
    
    /**
     * Web端登录认证授权Realm
     * @return
     */
    @Bean(name = "webLoginRealm")
    public WebLoginRealm getWebLoginRealm(MemoryConstrainedCacheManager memoryConstrainedCacheManager) {
        WebLoginRealm realm = new WebLoginRealm();
//        realm.setCredentialsMatcher(new WebCredentialsMatcher());
        realm.setCachingEnabled(true);
        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        realm.setAuthenticationCachingEnabled(true);
        // 启用授权缓存，即缓存AuthorizationInfo信息，默认false,一旦配置了缓存管理器，授权缓存默认开启
        realm.setAuthorizationCachingEnabled(true);
        realm.setCacheManager(memoryConstrainedCacheManager);
        return realm;
    }
    
    @Bean("shiroConfigProperties")
    public ShiroConfigProperties getShiroConfigProperties() {
    	return new ShiroConfigProperties();
    }
    
    @Bean("authConfigProperties")
    public AuthConfigProperties getAuthConfigProperties() {
    	return new AuthConfigProperties();
    }
    
    @Bean("shiroController")
    public ShiroController getShiroController() {
    	return new ShiroController();
    }
    
    @Bean("shiroGlobalExceptionInterceptor")
    public GlobalExceptionInterceptor getGlobalExceptionInterceptor() {
    	return new GlobalExceptionInterceptor();
    }
    
    @Bean("memoryConstrainedCacheManager")
    public MemoryConstrainedCacheManager getMemoryConstrainedCacheManager() {
        MemoryConstrainedCacheManager memoryConstrainedCacheManager = new MemoryConstrainedCacheManager();
        return memoryConstrainedCacheManager;
    }
    
    
    /**
     * 下面2个支持controller层注解实现权限控制
     *
     * @return
     */
//    @Bean(name = "advisorAutoProxyCreator")
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }

    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
}
