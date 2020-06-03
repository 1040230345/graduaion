package com.jackson.security.filter;

import com.alibaba.fastjson.JSON;
import com.jackson.constants.SecurityConstants;
import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.myUtils.IpUtils;
import com.jackson.result.Results;
import com.jackson.security.utils.JwtTokenUtils;
import com.jackson.threadLocal.RequestHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 过滤器处理所有HTTP请求，并检查是否存在带有正确令牌的Authorization标头。例如，如果令牌未过期或签名密钥正确。
 *
 * @author jackson
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {



    private static final Logger logger = Logger.getLogger(JWTAuthorizationFilter.class.getName());

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    private HttpServletResponse httpServletResponse;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        this.httpServletResponse = response;

        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);

        // 如果请求头中没有token信息则直接放行了
        if (authorization == null || !authorization.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        StringRedisTemplate stringRedisTemplate  = ctx.getBean(StringRedisTemplate.class);

        if(stringRedisTemplate.opsForSet().isMember("BlackToken",token)){
            chain.doFilter(request, response);
            return;
        }


        Object usernamePasswordAuthenticationToken = getAuthentication(authorization,response,request);

        if(usernamePasswordAuthenticationToken instanceof CustomizeException){
            this.responseMsg(Results.failure((CustomizeException)usernamePasswordAuthenticationToken),response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置授权信息
        SecurityContextHolder.getContext().setAuthentication((Authentication) usernamePasswordAuthenticationToken);

        super.doFilterInternal(request, response, chain);
    }

    /**
     * 获取用户认证信息 Authentication
     */
    private Object getAuthentication(String authorization,HttpServletResponse response,HttpServletRequest request) throws IOException {
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        try {

            //获取容器
            ServletContext context = request.getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
            StringRedisTemplate stringRedisTemplate = ctx.getBean(StringRedisTemplate.class);

            String username = JwtTokenUtils.getUsernameByToken(token);

            /**
             * 如果说，redis中存在值且时间大于一分钟
             */
            if(stringRedisTemplate.hasKey(username)&&stringRedisTemplate.getExpire(username)>60){

                String localToken = stringRedisTemplate.opsForValue().get(username);

                if(localToken.equals(authorization)){

                    //解析token
                    Claims claims = JwtTokenUtils.getTokenBody1(token);
                    //如果ip地址解析不一样
                    if (!IpUtils.getIpAddr(request).equals(claims.get(SecurityConstants.USER_IP))) {
                        return new CustomizeException(CustomizeErrorCode.UNAUTHORIZED);
                    }
                    //把用户信息存在threadLocal中
                    RequestHolder.add(username);

                    //刷新
                    stringRedisTemplate.opsForValue().set(username,authorization,60*60, TimeUnit.SECONDS);

                    logger.info("checking username:" + username);
                    // 通过 token 获取用户具有的角色
                    List<SimpleGrantedAuthority> userRolesByToken = JwtTokenUtils.getUserRolesByToken(token);
                    if (!StringUtils.isEmpty(username)) {
                        return new UsernamePasswordAuthenticationToken(username, null, userRolesByToken);
                    }
                }
            }
            return new CustomizeException(CustomizeErrorCode.UNAUTHORIZED);
//            this.responseMsg(Results.failure(CustomizeErrorCode.SYS_ERROR_403),response);

        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | IllegalArgumentException exception) {
            //已经过期捕获
            if (exception instanceof ExpiredJwtException) {
                Results resultDTO = Results.failure(CustomizeErrorCode.SYS_TOKEN_NOTIME);
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }
            logger.warning("Request to parse JWT with invalid signature . Detail : " + exception.getMessage());
        }
        return null;
    }


    /**
     * 返回数据
     */
    public void responseMsg (Results restResult,HttpServletResponse response) throws IOException {
        //返回错误
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(restResult));
        writer.close();
    }
}
