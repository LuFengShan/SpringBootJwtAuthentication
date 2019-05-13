package com.grokonez.jwtauthentication.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 代理类，代理用户的登录的信息，传送到rest api
 */
public class LoginForm {
    @NotBlank // 不允许为空
    @Size(min = 3, max = 60) // 字节长度在3-60之间
    private String username;

    @NotBlank // 密码当然也不能为空
    @Size(min = 6, max = 40) // 密码长度在6-40之间
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}