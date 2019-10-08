package com.app.controller;

import com.app.domain.Role;
import com.app.domain.User;
import com.app.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration"; //если пользователь уже существует выводим сообщение на страницу registration
        }

        user.setActive(true); //действия если пользователя нужно регистрировать, активность ставим true
        user.setRoles(Collections.singleton(Role.USER)); //создаем set с одним значением и передаем в setRoles
        userRepo.save(user);

        return "redirect:/login"; //при успешной регистрации переходим на страницу login
    }
}
