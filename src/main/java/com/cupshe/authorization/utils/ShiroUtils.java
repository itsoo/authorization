package com.cupshe.authorization.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.cupshe.dc.service.domain.dto.DingUserInfoDTO;

/**
 * ShiroUtils
 * <p>Title: ShiroUtils</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年6月23日
 */
public class ShiroUtils {

	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static DingUserInfoDTO getLoginUser() {
		try {
			Subject subject = SecurityUtils.getSubject();
			DingUserInfoDTO dingUserInfoDTO = (DingUserInfoDTO)subject.getPrincipal();
			return dingUserInfoDTO;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取登录用户的id
	 * @return
	 */
	public static Long getLoginUserId() {
		DingUserInfoDTO loginUser = getLoginUser();
		if(loginUser != null) {
			return loginUser.getId();
		}
		return null;
	}
	
}
