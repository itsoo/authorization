package com.cupshe.authorization.shiro.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cupshe.authorization.common.base.ResponseVO;
import com.cupshe.authorization.common.constants.SystemErrorEnum;

/**
 * 全局异常处理
 * <p>Title: GlobalExceptionInterceptor</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionInterceptor {

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseVO unauthenticatedException(UnauthenticatedException e) {
        return ResponseVO.build(SystemErrorEnum.K_400001.getErrorCode(), SystemErrorEnum.K_400001.getErrorDesc());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseVO unauthorizedException(UnauthorizedException e) {
    	return ResponseVO.build(SystemErrorEnum.K_400002.getErrorCode(), SystemErrorEnum.K_400002.getErrorDesc());
    }
    
}
