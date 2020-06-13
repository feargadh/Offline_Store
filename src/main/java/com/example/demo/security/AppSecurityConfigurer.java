package com.example.demo.security;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter {

    //依赖注入认证成功类
    @Autowired
    AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;

    //注入服务类
    @Autowired
    private UserService userService;

    //加密接口
    @Autowired
    private PasswordEncoder passwordEncoder;

    //用户认证接口
    @Autowired
    private AuthenticationProvider authenticationProvider;

    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();
        System.out.println("AppSecurityConfigurer configure 调用");
        httpSecurity.authorizeRequests()
                .antMatchers("/login","/js/**","/css/**","/img/*").permitAll()
                .antMatchers("/shop/**").hasRole("SHOP")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .successHandler(appAuthenticationSuccessHandler)
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        System.out.println("AppSecurityConfigurer configureGlobal 调用");
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public  PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        //创建DaoAuthoriticationProvider对象
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;

    }

}

