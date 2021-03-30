package com.cupshe.authorization.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;

import com.cupshe.authorization.shiro.constants.ShiroConstants;

/**
 * TokenUtils
 * <p>Title: TokenUtils</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
public class TokenUtils {

	public static SimpleSession getSimpleSession(String token) {
		String key = ShiroConstants.SESSION_KEY;
		return (SimpleSession)getClaimByKey(token, key);
	}
	
	public static Serializable getSessionId(String token) {
		SimpleSession simpleSession = getSimpleSession(token);
		if(simpleSession != null) {
			return simpleSession.getId();
		}
		 return null;
	}
	
	public static String getToken(Session session, AuthorizationInfo authorizationInfo) {
		String sessionEncode = getEncode(session);
		String authorizationInfoEncode = getEncode(authorizationInfo);
      	Map<String, String> claim = new HashMap<String, String>();
      	claim.put(ShiroConstants.SESSION_KEY, sessionEncode);
      	claim.put(ShiroConstants.AUTHORIZATIONINFO_KEY, authorizationInfoEncode);
      	String sign = JWTUtil.sign(claim);
		return sign;
	}

	private static String getEncode(Object object) {
		JdkSerializationSerializer jdkSerializationRedisSerializer = new JdkSerializationSerializer();
      	byte[] serialize = jdkSerializationRedisSerializer.serialize(object);
      	String encode = Base64Util.encode(serialize);
		return encode;
	}

	public static AuthorizationInfo getAuthorizationInfo(String token) {
		String key = ShiroConstants.AUTHORIZATIONINFO_KEY;
		return (AuthorizationInfo)getClaimByKey(token, key);
	}

	private static Object getClaimByKey(String token, String key) {
		String claim = JWTUtil.getClaim(token, key);
		byte[] decode = Base64Util.decode(claim);
		Object deserialize = new JdkSerializationSerializer().deserialize(decode);
		return deserialize;
	}
	
}
