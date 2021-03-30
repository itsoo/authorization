package com.cupshe.authorization.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cupshe.authorization.common.base.ResponseVO;
import com.cupshe.authorization.common.constants.SystemErrorEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ResponseBody
@RequestMapping("/shiro")
public class ShiroController {

	/**
     * 未授权
     * @return
     */
    @RequestMapping(value = "/unauth")
    public ResponseVO unauth() {
        return ResponseVO.build(SystemErrorEnum.K_400002.getErrorCode(), SystemErrorEnum.K_400002.getErrorDesc());
    }
    
	/**
     * 未登录
     * @return
     */
    @RequestMapping(value = "/unlogin")
    public ResponseVO unlogin() {
        return ResponseVO.build(SystemErrorEnum.K_400001.getErrorCode(), SystemErrorEnum.K_400001.getErrorDesc());
    }
	
}
