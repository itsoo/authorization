package com.cupshe.authorization.shiro.session;


import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import com.cupshe.authorization.shiro.constants.ShiroConstants;
import com.cupshe.authorization.utils.RequestThreadlocal;
import com.cupshe.authorization.utils.TokenUtils;

/**
 * 自定义shiro会话管理器（getSessionId方法获取会话id，默认获取cookie中jsessionId的值）
 * <p>Title: CustomWebSessionManager</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年6月22日
 */
public class CustomWebSessionManager extends DefaultWebSessionManager {
	
	/**
	 * 请求参数token
	 */
	private static final String PARAMETER_TOKEN = "token";
	
	/**
	 * 请求头Authorization
	 */
	private static final String HEADER_AUTHORIZATION = "Authorization";
	
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 从request中获取SessionId
		String id = WebUtils.toHttp(request).getParameter(PARAMETER_TOKEN);
		// 判断是否有值
		if (StringUtils.isNotEmpty(id)) {
			return processToken(request, id);
		}
		// 从请求头中获取SessionId
		id = WebUtils.toHttp(request).getHeader(HEADER_AUTHORIZATION);
		// 判断是否有值
		if (StringUtils.isNotEmpty(id)) {
			return processToken(request, id);
		}
		// 从Cookie中获取SessionId
		return super.getSessionId(request, response);
	}

	private Serializable processToken(ServletRequest request, String id) {
		// 1、token
		String token = id;
		// 2、session
		SimpleSession session = TokenUtils.getSimpleSession(token);
		this.getSessionDAO().update(session);
		RequestThreadlocal.setLocalServletRequest(request);
		// 设置当前session状态
		// 3、sessionId
		Serializable sessionId = session.getId();
		request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
		request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
		request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
		request.setAttribute(ShiroConstants.TOKEN_KEY, token);
		return sessionId;
	}
	

}

