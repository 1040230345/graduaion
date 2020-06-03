package com.jackson.controller;

import com.jackson.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@Api(tags = "测试接口")
public class TestController {
//    private Logger logger = LoggerFactory.getLogger(TestController.class);
//
//    @RequestMapping("/")
//    public String showHome() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("当前登陆用户：" + name);
//
//        return "home.html";
//    }
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('123123')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public String printUser(String test) {
        System.out.println(test);
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

//    @RequestMapping("/test")
//    @ResponseBody
//    public Results testController(HttpServletRequest request,HttpServletResponse response) throws IOException {
//
//        FilterChain chain = new FilterChain() {
//            @Override
//            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
//
//            }
//        };
//        Authentication authentication = new Authentication() {
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return null;
//            }
//
//            @Override
//            public Object getCredentials() {
//                return null;
//            }
//
//            @Override
//            public Object getDetails() {
//                return null;
//            }
//
//            @Override
//            public Object getPrincipal() {
//                return null;
//            }
//
//            @Override
//            public boolean isAuthenticated() {
//                return false;
//            }
//
//            @Override
//            public void setAuthenticated(boolean b) throws IllegalArgumentException {
//
//            }
//
//            @Override
//            public String getName() {
//                return null;
//            }
//        };
//        String userType = "null";
//
//        SuccessfullyProcessed.successfulAuthentication(request,response,chain,authentication,userType);
//
//
//        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(new AuthenticationManager() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                return null;
//            }
//        });
//        jwtAuthorizationFilter.validationToken(request,userType);
//        return null;
//    }

}
