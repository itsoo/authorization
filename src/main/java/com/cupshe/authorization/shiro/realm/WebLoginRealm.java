package com.cupshe.authorization.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.cupshe.authorization.utils.RequestUtil;
import com.cupshe.authorization.utils.TokenUtils;



/**
 *  自定义Realm处理登录认证和权限认证
 * <p>Title: WebLoginRealm</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年6月22日
 */
public class WebLoginRealm extends AuthorizingRealm {

	/**
	 * 认证
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
   		return null;
    }
        
    /**
     * 授权
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return TokenUtils.getAuthorizationInfo(RequestUtil.getToken());
	}

}
