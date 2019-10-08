package com.app.controller;

import com.app.domain.Role;
import com.app.domain.User;
import com.app.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user") //маппинг на уровне класса, все ниже будет ходить по этому меппингу
@PreAuthorize("hasAuthority('ADMIN')") //эта аннотация проверяет перед выполнение каждого из методов в этом контроллере наличие у пользователя прав
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{user}") //тут по мимо "/user" через / будет идти id пользователя
    public String userEditForm(@PathVariable User user, Model model){ //@PathVariable User user получаем пользоватля из бд, без использования репозитория
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username, //получение данных с сервера
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){
        user.setUsername(username); //берем пользователя и устанавливаем ему новое имя

        Set<String> roles = Arrays.stream(Role.values()) //список ролей, его делаем что бы отфильтровать не нужные значения типо токена и id, которые сейчас не нужны
                .map(Role::name)
                .collect(Collectors.toSet()); //переводим enum в строковый вид

        user.getRoles().clear();

        for(String key : form.keySet()){ //проверяем что форма содержит роли для пользователя
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
        return "redirect:/user";
    }

}
