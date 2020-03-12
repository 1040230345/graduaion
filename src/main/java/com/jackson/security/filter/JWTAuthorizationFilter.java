package com.jackson.security.filter;

import com.jackson.exception.CustomizeErrorCode;
import com.jackson.exception.CustomizeException;
import com.jackson.myUtils.RedisUtils;
import com.jackson.security.constants.SecurityConstants;
import com.jackson.security.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
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
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorization));
        //将token放在响应头里
//        response.setHeader(SecurityConstants.TOKEN_HEADER, token);

        super.doFilterInternal(request, response, chain);
    }

    /**
     * 获取用户认证信息 Authentication
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
        try {
            String username = JwtTokenUtils.getUsernameByToken(token);
            logger.info("checking username:" + username);
            // 通过 token 获取用户具有的角色
            List<SimpleGrantedAuthority> userRolesByToken = JwtTokenUtils.getUserRolesByToken(token);
//            System.out.println(userRolesByToken);
            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, userRolesByToken);
            }
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | IllegalArgumentException exception) {
            //已经过期捕获
//            if (exception instanceof ExpiredJwtException) {
////                throw new CustomizeException(CustomizeErrorCode.SYS_TOKEN_NOTIME);
//                //生成新的token
////                ((ExpiredJwtException) exception).getClaims().get("rol");
//                //System.out.println(((ExpiredJwtException) exception).getClaims().get("sub"));
//                List<String> list = new ArrayList<>();
//                list.add((String) ((ExpiredJwtException) exception).getClaims().get("rol"));
//                String newtoken = JwtTokenUtils.createToken((String)((ExpiredJwtException) exception).getClaims().get("sub"),list , false);
//                System.out.println(newtoken);
////                System.out.println(JwtTokenUtils.getUserRolesByToken(newtoken));
////                // 通过 token 获取用户具有的角色
////                List<SimpleGrantedAuthority> userRolesByToken = JwtTokenUtils.getUserRolesByToken(newtoken);
//                // Http Response Header 中返回 Token
//                httpServletResponse.setHeader(SecurityConstants.TOKEN_HEADER, newtoken);
////
////                return new UsernamePasswordAuthenticationToken((String) ((ExpiredJwtException) exception).getClaims().get("rol"),
////                        null, userRolesByToken);
//            }
            logger.warning("Request to parse JWT with invalid signature . Detail : " + exception.getMessage());
        }
        return null;
    }
}
