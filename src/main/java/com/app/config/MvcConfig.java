package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
Этот класс содержит конфигурацию нашего веб слоя
Spring сам предоставляет логику работы логина, нам ее нужно всего лишь активировать
и показать "setViewName("login")" что будем писать свой шаблон страницы логин
*/

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}") /*этой аннотацией указываем Spring, что мы хотим получить переменную. "${upload.path}" - выдергивает из контекста значения либо конструкции, сейчас он ищет upload.path в application.properties и вставляет его в переменную private String uploadPath;*/
    private String uploadPath;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { /*нужно для раздачи картикон*/
        registry.addResourceHandler("/img/**") /*каждое обращение к верверу по пути img с последующими данными*/
                .addResourceLocations("file:///" + uploadPath + "/"); /*будет перенаправлять запросы по этому пути. file - указывает на то, что место где то в файловой системе. Добавляем абсолютный путь uploadPath*/
        registry.addResourceHandler("/static/**") /*при обращении по этому пути "/static/**" ресурс будет искаться не где то в файловой системе*/
                .addResourceLocations("classpath:/static/"); /*а в дереве проекта*/
    }
}
