package com.app.config;

import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
этот класс при старте приложения конфигурирует WebSecurity
*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //эта аннотация нужна для того чтобы заработало @PreAuthorize("hasAuthority('ADMIN')") в userController
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

/*    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8); //этот параметр характеризует надежность ключа шифрования
    }*/

    @Bean
    public PasswordEncoder getPasswordEncode() {
        return new MessageDigestPasswordEncoder("MD5");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //  сюда передается обьект в котором мы включаем авторизацию
                    .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll() //указываем что на главную страницу и страницу регистрации мы разрешаем полный доступ без авторизации. * означает что url может содержать еще один сегмент
                    .anyRequest().authenticated() //для всех остальных запросов мы требуем авторизацию
                .and()
                    .formLogin() //включаем логин
                    .loginPage("/login") // указываем меппинг для формы логин и разрешаем пользоваться всем
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout() //включаем logout
                    .permitAll(); //разрешаем пользоваться всем
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //метод для того, чтобы брать пользователей из бд
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
