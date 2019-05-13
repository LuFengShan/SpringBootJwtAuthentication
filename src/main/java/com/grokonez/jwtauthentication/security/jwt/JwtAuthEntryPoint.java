package com.grokonez.jwtauthentication.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * JwtAuthEntryPoint用于处理unauthorized（未经授权）请求时处理错误异常。
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    /**
     * 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法
     * 当认证失败时，此类只返回HTTP代码401（未授权），覆盖默认的Spring重定向。
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException, ServletException {

        logger.error("未经授权 ： error. Message - {}", e.getMessage());
        // 当认证失败时，此类只返回HTTP代码401（未授权），覆盖默认的Spring重定向。
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> 未授权");
    }
}