package com.grokonez.jwtauthentication.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.grokonez.jwtauthentication.security.services.UserDetailsServiceImpl;

/**
 * Jwt身份验证过滤器
 * <p>
 * 每个请求执行一次。这是一个过滤器基类，用于保证每个请求调度的单次执行。
 * 它提供了一个doFilterInternal方法HttpServletRequest和HttpServletResponse参数。
 *
 * <p>
 * 该doFilterInternal方法将：
 * 1. JWT从标头获取令牌
 * 2. 验证 JWT
 * 3. username从验证中解析JWT
 * 4. 从users表中加载数据，然后构建一个authentication对象
 * 5. 将authentication对象设置为安全上下文
 *
 * <p>
 *     这个类是我们JWT认证过程的切入点;
 *     过滤器从请求标头中提取JWT令牌，并将身份验证委托给注入AuthenticationManager。
 *     如果未找到令牌，则抛出异常以停止处理请求。
 *     我们还需要覆盖以成功进行身份验证，
 *     因为默认的Spring流程会停止过滤器链并继续进行重定向。请记住，我们需要链完全执行，包括生成响应，如上所述。
 * </p>
 */
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    /**
     * 令牌提供者
     */
    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1、过滤器从请求标头中提取JWT令牌
            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                // 2、从jwt令牌中取出用户名称
                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                // 3、提供用户的核心信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 4、一种身份验证实现，旨在简单地显示用户名和密码。应使用Object设置主体和凭证，
                // 该Object通过其Object.toString（）方法提供相应的属性。 最简单的这样的Object是String。
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails
                        , null
                        , userDetails.getAuthorities());
                /**
                 * {@link WebAuthenticationDetailsSource} : AuthenticationDetailsSource的实现，
                 * 它从HttpServletRequest对象构建详细信息对象，创建WebAuthenticationDetails。
                 *
                 * {@link WebAuthenticationDetailsSource#buildDetails(HttpServletRequest)} :
                 * WebAuthenticationDetails包含有关当前请求的信息
                 */
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                /**
                 * {@link org.springframework.security.core.context.SecurityContext#setAuthentication(Authentication)}
                 * 更改当前已验证的主体，或删除身份验证信息。
                 * 参数：
                 * authentication  - 新的身份验证令牌，如果不存储其他身份验证信息，则为null
                 */
                // 获取当前的SecurityContext
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
            }
        } catch (Exception e) {
            // 1.2 如果未找到令牌，则抛出异常以停止处理请求
            logger.error("无法设置用户身份验证 -> Message: {}", e);
        }
        // 由于此身份验证位于HTTP标头中，因此成功后我们需要正常继续请求并返回响应，就像资源根本没有安全一样
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中取得jwt令牌
     * @param request
     * @return
     */
    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
