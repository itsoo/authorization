package com.cupshe.dc.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Description TODO
 * @Author qinjinwei
 * @Date 2020/4/9 14:42
 * @Version 1.0
 */
@AllArgsConstructor //全参构造
@NoArgsConstructor
@Data //set get方法
@Accessors(chain = true)//链式风格
public class DingUserInfoDTO implements Serializable{

    private Long id;
    private String userId;
    private String unionId;
    private String name;
    private String mobile;
    private String orgEmail;
    private String position;
    private String jobnumber;
    private String departList;
    private Date hiredDate;

    private String systemCode;
    
    // shiro的sessionId
    private String token;
    
    private String username;
    
    @JsonIgnore
    private String password;
    
    private List<Integer> dataScope;

}


