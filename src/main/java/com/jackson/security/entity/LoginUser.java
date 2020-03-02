package com.jackson.security.entity;

import com.jackson.model.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author jackson
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private Boolean rememberMe;

}
