package com.jackson.security.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.result.Results;
import com.jackson.security.constants.SecurityConstants;
import com.jackson.security.entity.LoginUser;
import com.jackson.security.utils.JwtTokenUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020年03月02日14:19:11
 * 如果用户名和密码正确，那么过滤器将创建一个JWT Token 并在HTTP Response 的header中返回它，格式：token: "Bearer +具体token值"
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        //super();
        this.authenticationManager = authenticationManager;
        // 设置URL，以确定是否需要身份验证
        super.setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }


    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
//            String username = request.getParameter("username");
//            String password = request.getParameter("password");
////            System.out.println(username);
//            LoginUser loginUser = new LoginUser();
//            loginUser.setPassword(password);
//            loginUser.setUsername(username);
//            loginUser.setRememberMe(true);
            //System.out.println(request.toString());
            // 从输入流中获取到登录的信息
            LoginUser loginUser = objectMapper.readValue(request.getInputStream(), LoginUser.class);
//            System.out.println(loginUser.toString());
            rememberMe.set(loginUser.getRememberMe());
            // 这部分和attemptAuthentication方法中的源码是一样的，
            // 只不过由于这个方法源码的是把用户名和密码这些参数的名字是死的，所以我们重写了一下
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginUser.getUsername(), loginUser.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            //e.printStackTrace();
            //throw new CustomizeException(CustomizeErrorCode.SYS_USER_NOFOUND);
            this.unsuccessfulAuthentication(request, response, (AuthenticationException)e);
            return null;
        }
    }

    /**
     * 如果验证成功，就生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        User jwtUser = (User) authentication.getPrincipal();
        List<String> roles = jwtUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // 创建 Token
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), roles, rememberMe.get());

        // Http Response Header 中返回 Token
        response.setHeader(SecurityConstants.TOKEN_HEADER, token);

        Results resultDTO = Results.failure(CustomizeErrorCode.SUCCESS);
        response.setContentType("application/json");
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(resultDTO));
        writer.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        Results resultDTO ;
        logger.info("登录失败了呀弟弟");
        if(authenticationException.getMessage().equals("Bad credentials")){
            resultDTO = Results.failure(4003,CustomizeErrorCode.SYS_USER_NOFOUND.getMessage());
        }else {
            resultDTO = Results.failure(4003,authenticationException.getMessage());
        }
        response.setContentType("application/json");
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(resultDTO));
        writer.close();
    }
}
