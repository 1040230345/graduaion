package com.jackson.security.exception;


import com.alibaba.fastjson.JSON;
import com.jackson.result.Results;
import com.jackson.security.entity.LoginUser;
import com.jackson.security.utils.JwtTokenUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 过滤器的一些公共方法
 * 2020年03月26日17:01:12
 * jackson
 */
public class FilterBase {

    private LoginUser loginUser;
    private String userIp;
    private String userType;

    public FilterBase(LoginUser loginUser, String userIp, String userType) {
        this.loginUser = loginUser;
        this.userIp = userIp;
        this.userType = userType;
    }

    /**
     * 成功生成token
     */
    public String mkToken(){
//        List<String> roles = loginUser.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
        List<String> roles = new ArrayList<>();
        roles.add("sys:permissionsTree:get");
        roles.add("sys:basicMsg:get");

        String token = JwtTokenUtils.createToken(loginUser.getUsername(), roles, true);
        return token;
    }


    /**
     * 将某些需要缓存的东西存在redis
     */
    public void inputRedis(HttpServletRequest request, String key, String value, Long time, String dbName){
        //获取容器
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

        //存redis
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) ctx.getBean(dbName);

        stringRedisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间
    }



    /**
     * 返回数据
     */
    public void responseMsg (Results restResult, HttpServletResponse response) throws IOException {
        //返回错误
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(restResult));
        writer.close();
    }

}
