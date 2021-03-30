package com.cupshe.authorization.common.constants;

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
 * @date 2019-11-14 20:03
 * 文件描述:
 *
 */
public enum SystemErrorEnum {


    // 系统级别
    K_000000("000000", "success"),
    K_000001("000001", "系统异常"),
    K_000004("000004", "参数异常"),

    // Index
    K_100001("100001", "用户名不存在"),
    K_100002("100001", "用户密码错误"),

    // Product
    K_200001("200001", "您暂时无权限获取该信息"),

    // UserCenter
    K_300001("300001", "用户信息")
    ,
    K_400001("400001", "未登录"),
    K_400002("400002", "未授权")

    ;

    private String errorCode;
    private String errorDesc;

    SystemErrorEnum(String errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
