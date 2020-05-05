package com.jackson.constants;

/**
 * jackson
 * 2020年03月03日01:51:20
 */
public class SecurityConstants {

    /**
     * 登录的地址
     */
    public static final String AUTH_LOGIN_URL = "/auth/login";
    /**
     * 登录的地址
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/auth/verificationCode";

    /**
     * 用户端
     */
    public static final String USER_ADDRESS = "userType";

    /**
     * 机构id
     */
    public static final String SCHOOL_ID = "schoolId";

    /**
     * 用户ip
     */
    public static final String USER_IP = "userIp";

    /**
     * 角色的key
     **/
    public static final String ROLE_CLAIMS = "rol";
    /**
     * web 过期时间是1个小时
     */
    public static final long EXPIRATION_WEB = 60 * 60 * 60;
    /**
     * h5 过期时间是7天
     */
    public static final long EXPIRATION_H5 = 60 * 60 * 60 * 24 * 7;
    /**
     * 小程序，依靠大佬的说法是3小时内，这里设置2小时40分
     */
    public static final long EXPIRATION_PROGRAM = 60 * 60 * 60 * 2 + 60 * 60 * 40;

    /**
     * JWT签名密钥硬编码到应用程序代码中，应该存放在环境变量或.properties文件中。
     */
    public static final String JWT_SECRET_KEY = "C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";


    // JWT token defaults

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";

    private SecurityConstants() {
    }
}
