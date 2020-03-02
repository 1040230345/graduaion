//package com.jackson.myUtils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * JwtToken的生成工具类
// * Created by zzb on 2019/11/21 10:23
// */
//@Component
//public class JwtTokenUtil {
//    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
//    private static final String CLAIM_KEY_USERNAME = "sub";
//    private static final String CLAIM_KEY_CREATED = "created";
//    @Value("${jwt.secret}")
//    private String secret;
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    /**
//     * 封装用户信息，并生成token
//     * @param userDetails
//     * @return
//     */
//    public String generateToken(UserDetails userDetails){
//        Map<String,Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
//        claims.put(CLAIM_KEY_CREATED,new Date());
//        return this.generateTokenByClaims(claims);
//    }
//
//    /**
//     * 根据负载生成token
//     * @param claims
//     * @return
//     */
//    public String generateTokenByClaims(Map<String,Object> claims){
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(this.generationExpirationDate())
//                .signWith(SignatureAlgorithm.HS512,secret)
//                .compact();
//    }
//
//    /**
//     * 解析token，获取负载主体
//     * @param token
//     * @return
//     */
//    public Claims getClaimsFromToken(String token){
//        Claims claims = null;
//        try {
//            claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            logger.info("JWT格式验证失败:{}",token);
//        }
//
//        return claims;
//    }
//
//    /**
//     * 解析token，获取负载主体中的用户名
//     * @param token
//     * @return
//     */
//    public String getUserNameFromToken(String token){
//        String username = null;
//        try {
//            Claims claims = this.getClaimsFromToken(token);
//            username = claims.getSubject();
//        } catch (Exception e) {
//            logger.info("JWT解析token失败:{}",token);
//        }
//
//        return username;
//    }
//
//    /**
//     * 生成token的过期时间
//     * @return
//     */
//    public Date generationExpirationDate(){
//        return new Date(System.currentTimeMillis() + expiration * 1000);
//    }
//
//    /**
//     * 验证token是否有效
//     * @param token
//     * @param userDetails
//     * @return
//     */
//    public boolean validateToken(String token, UserDetails userDetails){
//        String username = this.getUserNameFromToken(token);
//        if (StringUtils.isEmpty(username)) {
//            return false;
//        }
//        if (username.equals(userDetails.getUsername()) && this.isTokenExpired(token)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 验证token是否过期
//     * @param token
//     * @return
//     */
//    public boolean isTokenExpired(String token){
//        Claims claims = this.getClaimsFromToken(token);
//        Date expired = claims.getExpiration();
//        return new Date().before(expired);
//    }
//
//    /**
//     * 刷新token
//     * @param token
//     * @return
//     */
//    public String refreshToken(String token){
//        if (!this.isTokenExpired(token)) {
//            logger.info("token已过期:{}",token);
//            return null;
//        } else {
//            Claims claims = this.getClaimsFromToken(token);
//            claims.replace(CLAIM_KEY_CREATED,new Date());
//            return this.generateTokenByClaims(claims);
//        }
//    }
//
//
//}