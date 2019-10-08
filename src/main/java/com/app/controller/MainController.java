package com.app.controller;

import com.app.domain.Message;
import com.app.domain.User;
import com.app.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired  /*This means to get the bean called messageRepo, Which is auto-generated by Spring, we will use it to handle the data*/
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    /*
    модель это место куда мы будем складывать данные которые хотим вернуть пользователю
    */
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) { /*filter записываем required = false потому, что мы его будем передавать не всегда*/

        Iterable<Message> messages = messageRepo.findAll(); /*тут пишем Iterable т.к. и 1 и 2 наследуется от Iterable (как общай знаменатель)*/

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter); /*2 findByTag возвращает List*/
        } else {
            messages = messageRepo.findAll(); /*1 findAll возвращает Iterable*/
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model /*@RequestParam эта аннотация выдергивает значения либо из url если мы используем get либо из формы если мы используем post*/
    ) {
        Message message = new Message(text, tag, user);

        messageRepo.save(message); /*1 сохраняем*/
        Iterable<Message> messages = messageRepo.findAll(); /*2 взяли из репозитория*/
        model.put("messages", messages); /*3 положили в модель*/

        return "main"; /*4 отдали пользователю*/
    }
}
