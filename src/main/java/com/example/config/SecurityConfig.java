package com.example.config;

import com.example.service.AdministratorServcice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AdministratorServcice administratorServcice;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //url拦截设置 以及 登陆
        http
            .authorizeRequests()
            .antMatchers("/css/**", "/index").permitAll()
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/admin/**").access("hasRole('ADMIN') and hasRole('DBA')")
            .and()
            .formLogin().loginPage("/login").failureUrl("/login-error");  //	基于表单的身份验证启用了自定义登录页面和失败网址
        //logout设置
        http
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/logout")
            .invalidateHttpSession(true);
            // .logout()  提供注销支持。使用时会自动应用WebSecurityConfigurerAdapter
            //  .logoutUrl("/ my / logout"）触发注销的URL（默认为/logout）。如果启用CSRF保护（默认），则请求也必须是POST
            //  .logoutSuccessUrl（“/ my / index”）注销后重定向到的URL已发生。默认是/login?logout
            //  .logoutSuccessHandler（logoutSuccessHandler）让我们指定一个自定义LogoutSuccessHandler。如果指定，logoutSuccessUrl()将被忽略
            //  .invalidateHttpSession（true）指定是否HttpSession在注销时失效。这是真正的默认。配置SecurityContextLogoutHandler
            //  .addLogoutHandler（logoutHandler）添加一个LogoutHandler。默认SecurityContextLogoutHandler添加为最后一个LogoutHandler
            //  .deleteCookies（cookieNamesToClear）允许在注销成功时指定要删除的Cookie的名称。这是一个CookieClearingLogoutHandler明确添加的快捷方式
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //自定义用户验证：使用数据库中的用户名密码数据
        auth.userDetailsService(administratorServcice).passwordEncoder(new Md5PasswordEncoder());
    }
}
