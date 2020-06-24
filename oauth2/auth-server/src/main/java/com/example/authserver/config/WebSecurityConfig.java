package com.example.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description:
 * Author: hy.bu
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * auth2 密码模式必须配置
     * 用户身份的管理者, 认证的入口, 因此,我们需要通过这个配置,向security提供真实的用户身份
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 安全过滤器配置：自定义安全访问策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        每个模块配置使用and结尾。
//        authorizeRequests()配置路径拦截，表明路径访问所对应的权限，角色，认证信息。
//        formLogin()对应表单认证相关的配置
//        logout()对应了注销相关的配置
//        httpBasic()可以配置basic登录
        http.formLogin().permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .and().authorizeRequests()
                .antMatchers("/oauth/**").authenticated();
    }
}
