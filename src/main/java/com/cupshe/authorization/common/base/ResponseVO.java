package com.cupshe.authorization.common.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import lombok.Data;

/***
 *
 *          ┌─┐       ┌─┐
 *       ┌──┘ ┴───────┘ ┴──┐
 *       │                 │
 *       │       ───       │
 *       │  ─┬┘       └┬─  │
 *       │                 │
 *       │       ─┴─       │
 *       │                 │
 *       └───┐         ┌───┘
 *           │         │
 *           │         │
 *           │         │
 *           │         └──────────────┐
 *           │                        │
 *           │                        ├─┐
 *           │                        ┌─┘    
 *           │                        │
 *           └─┐  ┐  ┌───────┬──┐  ┌──┘         
 *             │ ─┤ ─┤       │ ─┤ ─┤         
 *             └──┴──┘       └──┴──┘ 
 *               神兽保佑  代码无BUG!
 *
 *
 * @author : wangjia
 * @date 2019-11-14 19:57
 * 文件描述:
 *
 */
@Data
public class ResponseVO implements Serializable {

    /**
     * 是否成功标识
     */
    private Boolean success;

    /**
     * 数据返回时的服务器时间戳
     */
    private Long timeStamp;

    /**
     * 若请求发生错误，则返回错误信息给前端
     */
    private String retCode;

    /**
     * 若请求发生错误，则返回错误信息给前端
     */
    private String retInfo;

    /**
     * 返回客户端的数据
     */
    private Object data;

    /**
     * 获取时间戳
     * @return
     */
    public Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 正常返回查询成功的信息，默认赋值Success：true
     * 这里一般是一个对象，DTO数据在这里，直接和msg一个层级
     * @return
     */
    public static ResponseVO build() {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.TRUE);
        responseVO.setRetCode("000000");
        responseVO.setRetInfo("success");
        return responseVO;
    }

    /**
     * 正常返回查询成功的信息，默认赋值Success：true
     * 这里一般是一个对象，DTO数据在这里，直接和msg一个层级
     * @param data
     * @return
     */
    public static ResponseVO build(Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.TRUE);
        responseVO.setRetCode("000000");
        responseVO.setRetInfo("success");
        responseVO.setData(data);
        return responseVO;
    }

    /**
     * 在返回List的时候，这里需要对list进行一次封装，
     * 用list来封装整个list后，按照2层数据返回给前端
     * @param list the date to set
     */
    public static ResponseVO build(List list) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.TRUE);
        responseVO.setRetCode("000000");
        responseVO.setRetInfo("success");
        Map<String, Object> map = new HashMap<>(1);
        map.put("list", list);
        responseVO.setData(map);
        return responseVO;
    }

    /**
     * 一般用于业务逻辑错误时候的返回类型，
     * 前端的报错信息直接用返回的retinfo
     *
     * @param e
     */
    public static ResponseVO build(Exception e) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.FALSE);
        responseVO.setRetCode("000001");
        responseVO.setRetInfo(e.getMessage());
        return responseVO;
    }
    /**
     * 一般用于业务逻辑错误时候的返回类型，
     * 前端的报错信息直接用返回的retinfo
     *
     * @param e
     */
    public static ResponseVO build(LoginException e) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.FALSE);
        responseVO.setRetCode("000002");
        responseVO.setRetInfo(e.getMessage());
        return responseVO;
    }


    /**
     * 一般用于系统错误时候返回的信息，
     * 前端不会直接把报错信息抛给用户
     *
     * @param retCode
     * @param retInfo
     */
    public static ResponseVO build(String retCode, String retInfo) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(Boolean.FALSE);
        responseVO.setRetCode(retCode);
        responseVO.setRetInfo(retInfo);
        return responseVO;
    }

    public static ResponseVO build(String retCode, String retInfo, Boolean falg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(falg);
        responseVO.setRetCode(retCode);
        responseVO.setRetInfo(retInfo);
        return responseVO;
    }


}
