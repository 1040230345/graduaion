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
//            System.out.println(authorization);
            chain.doFilter(request, response);
            return;
////            response.sendRedirect("localhost:8090/login");
//            PrintWriter writer = response.getWriter();
//            writer.close();
//            return;
        }
        String token = authorization.split(" ")[1];
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        StringRedisTemplate stringRedisTemplate  = ctx.getBean(StringRedisTemplate.class);

        if(stringRedisTemplate.opsForSet().isMember("BlackToken",token)){
            chain.doFilter(request, response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置授权信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorization,response));
        //将token放在响应头里
//        response.setHeader(SecurityConstants.TOKEN_HEADER, token);

        super.doFilterInternal(request, response, chain);
    }

    /**
     * 获取用户认证信息 Authentication
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String authorization,HttpServletResponse response) throws IOException {
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        try {
            String username = JwtTokenUtils.getUsernameByToken(token);

            //把用户信息存在threadLocal中
            RequestHolder.add(username);

            logger.info("checking username:" + username);
            // 通过 token 获取用户具有的角色
            List<SimpleGrantedAuthority> userRolesByToken = JwtTokenUtils.getUserRolesByToken(token);
            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, userRolesByToken);
            }
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
     * 解析token进行比对
     */
    public static Object validationToken(HttpServletRequest request, String token){

        String userToken = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        //获取容器
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        try {
            //解析token
            Claims claims = JwtTokenUtils.getTokenBody1(userToken);
            //如果ip地址解析不一样或者用户类别不一样
            if (!IpUtils.getIpAddr(request).equals(claims.get(SecurityConstants.USER_IP)) ) {
                return new CustomizeException(CustomizeErrorCode.SYS_TOKEN_ERROR);
            }
            //与redis上的token进行比对
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) ctx.getBean("tokenDb");

            if(stringRedisTemplate.hasKey(claims.getSubject()+"=="+claims.get("userType"))&&
                    stringRedisTemplate.getExpire(claims.getSubject()+"=="+claims.get("userType"))>0){
                //获取存储的token
                String localToken = stringRedisTemplate.opsForValue().get(claims.getSubject()+"=="+claims.get("userType"));
//                System.out.println(localToken);
                if(token.equals(localToken)){
                    //刷新token
                    long expiration = 60*60*60;
                    try {
                        stringRedisTemplate.expire(claims.getSubject(),expiration , TimeUnit.MILLISECONDS);//设置过期时间
                    }catch (Exception E){
                        logger.warning("redis is error :"+E);
                        return new CustomizeException(CustomizeErrorCode.SYS_TOKEN_ERROR);
                    }
                    //把用户信息存在threadLocal中
                    Map map = new HashMap();
                    map.put("userId",claims.getSubject());
                    RequestHolder.add(map);
                    return true;
                }
            }
            return new CustomizeException(CustomizeErrorCode.SYS_TOKEN_ERROR);

        }catch (ExpiredJwtException|MalformedJwtException | IllegalArgumentException exception){
            logger.warning("Request to parse JWT with invalid signature . Detail : " + exception.getMessage());

            if(exception instanceof ExpiredJwtException){
                return new CustomizeException(CustomizeErrorCode.SYS_TOKEN_ERROR);
            }
            return new CustomizeException(CustomizeErrorCode.SYS_TOKEN_ERROR);
        }

    }
}
