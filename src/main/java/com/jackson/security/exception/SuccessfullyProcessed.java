package com.jackson.security.exception;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.jackson.model.SysUser;
import com.jackson.myUtils.IpUtils;
import com.jackson.result.Results;
import com.jackson.security.constants.SecurityConstants;
import com.jackson.security.entity.LoginUser;
import org.springframework.security.core.Authentication;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 成功处理
 */
public class SuccessfullyProcessed {

    public static void successfulAuthentication(HttpServletRequest request,
                                                HttpServletResponse response,
                                                FilterChain chain,
                                                Authentication authentication,
                                                String userType) throws IOException {
        //获取用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取用户ip地址，浏览器信息
        String userIp = IpUtils.getIpAddr(request);
        FilterBase filterBase = new FilterBase(loginUser,userIp,userType);
        //生成token
        String token = filterBase.mkToken();
        try {
//            //持久化登录信息
//            int num = filterBase.inputMysql(request,token);

            long expiration = 60*60*60;

            //存到redis
            String key = loginUser.getId()+"=="+userType;
            filterBase.inputRedis(request,key,token,expiration,"tokenDb");

            // Http Response Header 中返回 Token
            response.setHeader(SecurityConstants.TOKEN_HEADER, token);

            //返回
            Results restResult = Results.success("登录成功");
            filterBase.responseMsg(restResult,response);
            response.setStatus(200);

        }catch (Exception e){
//            filterBase.responseMsg(Results.failure(ErrorCode.RUNTIME_EXCEPTION),response);
        }

    }
}
