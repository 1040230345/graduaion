package com.jackson.security.entity;

import lombok.Data;

/**
 * @author jackson
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private Boolean rememberMe;

    private Integer id;

}
