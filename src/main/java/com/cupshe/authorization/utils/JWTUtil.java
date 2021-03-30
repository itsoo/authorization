package com.cupshe.authorization.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cupshe.authorization.shiro.constants.ShiroConstants;

/**
 * JWT工具类
 * <p>Title: JWTUtil</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
public class JWTUtil {

	public static final String SERCET = "ra50FAav5vB1AP6A";
	
	public static final Long TOKEN_EXPIRE_TIME = ShiroConstants.DEFAULT_GLOBAL_SESSION_TIMEOUT;
	
    /**
     * 生成签名
     * @param claim
     * @return
     */
    public static String sign(Map<String, String> claim) {
        try {
            //token过期时间
            Date date = new Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SERCET);
            Builder create = JWT.create();
            for (Map.Entry<String, String> entry : claim.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				create = create.withClaim(key, value);
			}
            String sign = create.withExpiresAt(date).sign(algorithm);
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * getClaim
     * @param token
     * @param key
     * @return 
     */
    public static String getClaim(String token, String key) {
    	try {
//    		verify(token);
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 验证token
     * @param token
     * @return
     * @throws Exception 
     */
    public static void verify(String token) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(SERCET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
    
//    public static void main(String[] args) {
////    	String token = extracted1();
////    	System.out.println(token);
//    	String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEyMzQ1IiwiZXhwIjoxNjA0MzkwNTQ4LCJ1c2VybmFtZSI6InpoYW5nc2FuIn0.L0EL_37nOZ1fUSIkCpVfPXxvO-ojh4KwTvD1QBUTD3A";
//    	boolean verify = verify(token);
////    	extracted2(token);
//	}
//
//	private static void extracted2(String token) {
//		String username = getClaim(token, "username");
//    	String id = getClaim(token, "id");
//    	System.out.println(id);
//    	System.out.println(username);
//	}
//
//	private static String extracted1() {
//		Map<String, String> claim = new HashMap<String, String>();
//    	claim.put("username", "zhangsan");
//    	claim.put("id", "12345");
//    	String token = sign(claim);
//		return token;
//	}
    
//    public static void main(String[] args) {
//    	String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9uIjoick8wQUJYTnlBQ3B2Y21jdVlYQmhZMmhsTG5Ob2FYSnZMbk5sYzNOcGIyNHViV2QwTGxOcGJYQnNaVk5sYzNOcGIyNmRIS0c0MVl4aWJnTUFBSGh3ZHdJQTIzUUFKREU0TldSa05USmtMV1k1TmprdE5EUXhZaTA1TkdJMUxUQm1NMlZpTmpVNFpHSTRabk55QUE1cVlYWmhMblYwYVd3dVJHRjBaV2hxZ1FGTFdYUVpBd0FBZUhCM0NBQUFBWFZvcDdFVGVITnhBSDRBQTNjSUFBQUJkV2l4K1o1NGR4TUFBQUFBQlNaY0FBQUpNVEkzTGpBdU1DNHhjM0lBRVdwaGRtRXVkWFJwYkM1SVlYTm9UV0Z3QlFmYXdjTVdZTkVEQUFKR0FBcHNiMkZrUm1GamRHOXlTUUFKZEdoeVpYTm9iMnhrZUhBL1FBQUFBQUFBREhjSUFBQUFFQUFBQUFKMEFGQnZjbWN1WVhCaFkyaGxMbk5vYVhKdkxuTjFZbXBsWTNRdWMzVndjRzl5ZEM1RVpXWmhkV3gwVTNWaWFtVmpkRU52Ym5SbGVIUmZRVlZVU0VWT1ZFbERRVlJGUkY5VFJWTlRTVTlPWDB0RldYTnlBQkZxWVhaaExteGhibWN1UW05dmJHVmhiczBnY29EVm5QcnVBZ0FCV2dBRmRtRnNkV1Y0Y0FGMEFFMXZjbWN1WVhCaFkyaGxMbk5vYVhKdkxuTjFZbXBsWTNRdWMzVndjRzl5ZEM1RVpXWmhkV3gwVTNWaWFtVmpkRU52Ym5SbGVIUmZVRkpKVGtOSlVFRk1VMTlUUlZOVFNVOU9YMHRGV1hOeUFESnZjbWN1WVhCaFkyaGxMbk5vYVhKdkxuTjFZbXBsWTNRdVUybHRjR3hsVUhKcGJtTnBjR0ZzUTI5c2JHVmpkR2x2YnFoL1dDWEdvd2hLQXdBQlRBQVBjbVZoYkcxUWNtbHVZMmx3WVd4emRBQVBUR3BoZG1FdmRYUnBiQzlOWVhBN2VIQnpjZ0FYYW1GMllTNTFkR2xzTGt4cGJtdGxaRWhoYzJoTllYQTB3RTVjRUd6QSt3SUFBVm9BQzJGalkyVnpjMDl5WkdWeWVIRUFmZ0FHUDBBQUFBQUFBQXgzQ0FBQUFCQUFBQUFCZEFBVFpHRjBZVU5sYm5SbGNqcDZhR0Z1WjNOaGJuTnlBQmRxWVhaaExuVjBhV3d1VEdsdWEyVmtTR0Z6YUZObGROaHMxMXFWM1NvZUFnQUFlSElBRVdwaGRtRXVkWFJwYkM1SVlYTm9VMlYwdWtTRmxaYTR0elFEQUFCNGNIY01BQUFBQWo5QUFBQUFBQUFCYzNJQU1HTnZiUzVqZFhCemFHVXVaR011YzJWeWRtbGpaUzVrYjIxaGFXNHVaSFJ2TGtScGJtZFZjMlZ5U1c1bWIwUlVUMUFNNFNPS2RvSUpBZ0FPVEFBS1pHVndZWEowVEdsemRIUUFFa3hxWVhaaEwyeGhibWN2VTNSeWFXNW5PMHdBQ1docGNtVmtSR0YwWlhRQUVFeHFZWFpoTDNWMGFXd3ZSR0YwWlR0TUFBSnBaSFFBRUV4cVlYWmhMMnhoYm1jdlRHOXVaenRNQUFscWIySnVkVzFpWlhKeEFINEFGa3dBQm0xdlltbHNaWEVBZmdBV1RBQUVibUZ0WlhFQWZnQVdUQUFJYjNKblJXMWhhV3h4QUg0QUZrd0FDSEJoYzNOM2IzSmtjUUIrQUJaTUFBaHdiM05wZEdsdmJuRUFmZ0FXVEFBS2MzbHpkR1Z0UTI5a1pYRUFmZ0FXVEFBRmRHOXJaVzV4QUg0QUZrd0FCM1Z1YVc5dVNXUnhBSDRBRmt3QUJuVnpaWEpKWkhFQWZnQVdUQUFJZFhObGNtNWhiV1Z4QUg0QUZuaHdjSEJ6Y2dBT2FtRjJZUzVzWVc1bkxreHZibWM3aStTUXpJOGozd0lBQVVvQUJYWmhiSFZsZUhJQUVHcGhkbUV1YkdGdVp5NU9kVzFpWlhLR3JKVWRDNVRnaXdJQUFIaHdBQUFBQUFBQUFBRndjSFFBRDBKaGNtOXVMZW1aaU9XN3V1UzRuSFFBR1dwcFlXNWtiMjVuTG1Ob1pXNUFhMkZ3WldsNGFTNWpiMjEwQUNCRk1UQkJSRU16T1RRNVFrRTFPVUZDUWtVMU5rVXdOVGRHTWpCR09EZ3pSWEIwQUFwa1lYUmhRMlZ1ZEdWeWNIQjBBQkl3TVRBM01UVTFOekUyTXpjM05EVXpNakowQUFoNmFHRnVaM05oYm5oNEFIY0JBWEVBZmdBUWVIaDQiLCJleHAiOjE2MDM3ODA3NDV9.rVn_bDAdjj_fhLWNoURjxy07nLo4OoE11QOvn1NlzVc";
//		String sessionString = getUsername(token);
//		byte[] decode = Base64.getDecoder().decode(sessionString);
//		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
//		SimpleSession simpleSession = (SimpleSession)jdkSerializationRedisSerializer.deserialize(decode);
//		System.out.println(simpleSession);
//	}
	
}
