package com.jackson.security.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jackson
 * 2020年03月02日15:52:11
 * AuthenticationEntryPoint 用来解决匿名用户访问需要权限才能访问的资源时的异常
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 当用户尝试访问需要权限才能的REST资源而不提供Token或者Token错误或者过期时，
     * 将调用此方法发送401响应以及错误信息
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
//        System.out.println("im coming");
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        //如果访问需要授权的资源，跳转到登录页面
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
