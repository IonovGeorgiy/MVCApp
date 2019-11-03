package com.app.controller;

import com.app.domain.Role;
import com.app.domain.User;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user") //маппинг на уровне класса, все ниже будет ходить по этому меппингу
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')") //эта аннотация проверяет перед выполнение каждого из методов в этом (ранее) контроллере (сейчас в каждлм маппинге) наличие у пользователя прав
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}") //тут по мимо "/user" через / будет идти id пользователя
    public String userEditForm(@PathVariable User user, Model model){ //@PathVariable User user получаем пользоватля из бд, без использования репозитория
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username, //получение данных с сервера
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) { //принимает модель и ожидает пользователя из контекста, что бы мы не получили его из бд
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
            ) {
        userService.updateProfile(user, password, email); //передаем все в сервис для дальнейшей обработки

        return "redirect:/user/profile";
    }
}
