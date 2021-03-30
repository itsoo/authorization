package com.cupshe.authorization.utils;

import java.util.Base64;

/**
 * Base64工具类
 * <p>Title: Base64Util</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
public class Base64Util {
	
	public static byte[] decode(String src) {
		byte[] decode = Base64.getDecoder().decode(src);
		return decode;
	}
	
	public static String encode(byte[] src) {
		String encode = Base64.getEncoder().encodeToString(src);
		return encode;
	}
	
}
