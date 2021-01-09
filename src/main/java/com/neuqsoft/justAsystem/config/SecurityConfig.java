package com.neuqsoft.justAsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuqsoft.justAsystem.model.RespBean;
import com.neuqsoft.justAsystem.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserService userService;
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user"); // 使admin包含user的权限(手写上"ROLE_"前缀)
        return hierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

//    @Autowired
//    DataSource dataSource;
//
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
//        manager.setDataSource(dataSource);
//        if (!manager.userExists("fy")) {
//            manager.createUser(User.withUsername("fy").password("123").roles("admin").build());
//        }
//        if (!manager.userExists("user")) {
//            manager.createUser(User.withUsername("user").password("123").roles("user").build());
//        }
//        return manager;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("fy")
//                .password("123")
//                .roles("admin")
//                .and()
//                .withUser("user")
//                .password("123")
//                .roles("user");
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/images/**", "/fonts/**", "/favicon.ico", "/verifyCode", "/api/user/register",
                "/swagger-ui.html",
                "/v2/api-docs", // swagger api json
                "/swagger-resources/configuration/ui", // 用来获取支持的动作
                "/swagger-resources", // 用来获取api-docs的URI
                "/swagger-resources/configuration/security", // 安全选项
                "/swagger-resources/**",
                "/webjars/**");
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                RespBean ok = RespBean.ok("登陆成功");
                out.write(new ObjectMapper().writeValueAsString(ok));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                RespBean respBean = RespBean.error("登陆失败！");
                if (e instanceof LockedException) {
                    respBean.setMsg("账户被锁定，请联系管理员!");
                } else if (e instanceof CredentialsExpiredException) {
                    respBean.setMsg("密码过期，请联系管理员!");
                } else if (e instanceof AccountExpiredException) {
                    respBean.setMsg("账户过期，请联系管理员!");
                } else if (e instanceof DisabledException) {
                    respBean.setMsg("账户被禁用，请联系管理员!");
                } else if (e instanceof BadCredentialsException) {
                    resp.setStatus(500); // 设置状态码为500
                    respBean.setMsg("用户名或者密码输入错误，请重新输入!");
                }
                out.write(new ObjectMapper().writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        });
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin") // 设置admin访问权限
                .antMatchers("/user/**").hasRole("user") // 设置user访问权限
                .anyRequest().authenticated() // 必须放在antMatchers后面
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/doLogin")
//                .successHandler((req, resp, authentication) -> {
//                    Object principal = authentication.getPrincipal();
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(principal));
//                    out.flush();
//                    out.close();
//                })
//                .failureHandler((req, resp, exception) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.write(exception.getMessage());
//                    out.flush();
//                    out.close();
//                })
//                .permitAll() // 放行相关url
                .and()
                .logout()
                .logoutUrl("/logout") // 默认的get方法注销
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // 用post方法注销
//                .logoutSuccessUrl("/login.html") // 注销成功后跳转的地址
//                .invalidateHttpSession(true) // 注销后session失效，不写也是默认true
//                .clearAuthentication(true) // 清除认证信息，不写也是默认true
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .permitAll() // 放行相关url
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, exception) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(401); // 设置状态码为401
                    PrintWriter out = resp.getWriter();
                    out.write("尚未登录，请登录");
                    out.flush();
                    out.close();
                });
        http.addFilterAfter(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
