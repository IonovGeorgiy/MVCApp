package com.app;

import com.app.controller.MainController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class) //указываем раннер (окружение в котором будут стартовать тесты)
@SpringBootTest //для корректной работы со Spring Boot
@AutoConfigureMockMvc //позволяет обходить слои в MVC к примеру без веб слоя, обратится к контроллеру
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller; //его тестируем

    @Test
    public void contextLoads() throws Exception { //проверка, не пустой ли контекст
        this.mockMvc.perform(get("/"))
                .andDo(print()) //вывод результатов
                .andExpect(status().isOk()) //надстройка над ассертами. Сравниваем ожидаемый результат с реальным. Ожидаем статус 200
                .andExpect(content().string(containsString("Hello, guest")))
                .andExpect(content().string(containsString("Please, sign in")));
    }

    @Test
    public void accessDeniedTest() throws Exception { //проверка авторизации
        this.mockMvc.perform(get("/main")) //страница которая точно потребует авторизации
                .andDo(print())
                .andExpect(status().is3xxRedirection()) //проверяем, что сиситема оидает статус отличный от 200, сейчас статус 302
                .andExpect(redirectedUrl("http://localhost/login")); //перенаправление на логин
    }

    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user("u").password("1")) //обращение к форме логина Spring Security. Метод смотрит, как мы отпределили в контексте логин страницу и обращается к ней
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("user", "uSeR"))
                .andDo(print())
                .andExpect(status().isForbidden());// ожидаем статус 403
    }
}
