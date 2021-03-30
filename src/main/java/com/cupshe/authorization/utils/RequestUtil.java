package com.cupshe.authorization.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cupshe.authorization.shiro.constants.ShiroConstants;

/**
 * RequestUtil
 * <p>Title: RequestUtil</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月27日
 */
public class RequestUtil {

    public static String getToken() {
    	try {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			ServletRequest httpServletRequest = null;
			if(servletRequestAttributes != null) {
				httpServletRequest = servletRequestAttributes.getRequest();
			}else {
				httpServletRequest = RequestThreadlocal.getLocalServletRequest();
			}
			if(httpServletRequest == null) {
				return null;
			}
			String token = (String)httpServletRequest.getAttribute(ShiroConstants.TOKEN_KEY);
			return token;
		} catch (Exception e) {
			return null;
		} finally {
			RequestThreadlocal.clearLocalServletRequest();
		}
    }
    
	
}
