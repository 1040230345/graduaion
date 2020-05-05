package com.jackson.security.exception;

import com.alibaba.fastjson.JSON;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.result.Results;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

        //如果访问需要授权的资源，跳转到登录页面
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

//            request.getRequestDispatcher("/index").forward(request, response);
        //抛一个异常
//        throw new CustomizeException(CustomizeErrorCode.UNAUTHORIZED);
        Results resultDTO = Results.failure(CustomizeErrorCode.UNAUTHORIZED);
        response.setContentType("application/json");
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(resultDTO));
        writer.close();

    }
}
