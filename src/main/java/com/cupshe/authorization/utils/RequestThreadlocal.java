package com.cupshe.authorization.utils;

import javax.servlet.ServletRequest;

/**
 * RequestThreadlocal
 * <p>Title: RequestThreadlocal</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月29日
 */
public class RequestThreadlocal {

    protected static final ThreadLocal<ServletRequest> LOCAL_SERVLETREQUEST = new ThreadLocal<ServletRequest>();

    /**
     * 设置ServletRequest
     *
     * @param page
     */
    public static void setLocalServletRequest(ServletRequest servletRequest) {
    	LOCAL_SERVLETREQUEST.set(servletRequest);
    }

    /**
     * 获取ServletRequest
     *
     * @return
     */
    public static ServletRequest getLocalServletRequest() {
        return LOCAL_SERVLETREQUEST.get();
    }

    /**
     * 移除ServletRequest
     */
    public static void clearLocalServletRequest() {
    	LOCAL_SERVLETREQUEST.remove();
    }
	
}
