package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
Этот класс содержит конфигурацию нашего веб слоя
Spring сам предоставляет логику работы логина, нам ее нужно всего лишь активировать
и показать "setViewName("login")" что будем писать свой шаблон страницы логин
*/

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
