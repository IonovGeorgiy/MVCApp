package com.app.service;

import com.app.domain.Role;
import com.app.domain.User;
import com.app.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service //тоже рассматривается как компонет
public class UserSevice implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username); //просто возвращаем пользователя
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true); //действия если пользователя нужно регистрировать, активность ставим true
        user.setRoles(Collections.singleton(Role.USER)); //создаем set с одним значением и передаем в setRoles
        user.setActivationCode(UUID.randomUUID().toString()); //генерация кода активации

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {//только для не пустой строчки отправляем сообщение
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to app. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;

    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null); //пользователь подтвертил свой почтовый ящик, если он его указал

        userRepo.save(user);

        return true;
    }
}
