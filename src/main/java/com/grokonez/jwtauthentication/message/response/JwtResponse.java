package com.grokonez.jwtauthentication.message.response;

/**
 * JwtResponse.java成功验证后由SpringBoot服务器返回，它包含2部分：
 *
 * JWT令牌
 * 架构的模式类型
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}