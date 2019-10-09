package com.app.config;

import com.app.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/*
этот класс при старте приложения конфигурирует WebSecurity
*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //эта аннотация нужна для того чтобы заработало @PreAuthorize("hasAuthority('ADMIN')") в userController
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserSevice userSevice;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //  сюда передается обьект в котором мы включаем авторизацию
                    .antMatchers("/", "/registration", "/static/**").permitAll() //указываем что на главную страницу и страницу регистрации мы разрешаем полный доступ без авторизации
                    .anyRequest().authenticated() //для всех остальных запросов мы требуем авторизацию
                .and()
                    .formLogin() //включаем логин
                    .loginPage("/login") // указываем меппинг для формы логин и разрешаем пользоваться всем
                    .permitAll()
                .and()
                    .logout() //включаем logout
                    .permitAll(); //разрешаем пользоваться всем
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //метод для того, чтобы брать пользователей из бд
        auth.userDetailsService(userSevice)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
