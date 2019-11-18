package com.app.service;

import com.app.domain.Role;
import com.app.domain.User;
import com.app.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service //тоже рассматривается как компонет
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true); //действия если пользователя нужно регистрировать, активность ставим true
        user.setRoles(Collections.singleton(Role.USER)); //создаем set с одним значением и передаем в setRoles
        user.setActivationCode(UUID.randomUUID().toString()); //генерация кода активации
        user.setPassword(passwordEncoder.encode(user.getPassword())); //шифруем пароль с помошью passwordEncoder

        userRepo.save(user);

        sendMessage(user);
        return true;

    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {//только для не пустой строчки отправляем сообщение
            String message = String.format("Hello, %s! \n" + "Welcome to app. Please, visit next link: http://localhost:8080/activate/%s", user.getUsername(), user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }
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

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username); //берем пользователя и устанавливаем ему новое имя

        Set<String> roles = Arrays.stream(Role.values()) //список ролей, его делаем что бы отфильтровать не нужные значения типо токена и id, которые сейчас не нужны
                .map(Role::name).collect(Collectors.toSet()); //переводим enum в строковый вид

        user.getRoles().clear();

        for (String key : form.keySet()) { //проверяем что форма содержит роли для пользователя
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email)); //email.equals(userEmail) - текущие емаил пользователя, если будем сравнивать только через equals то мы поймаем nullPointerExeption
        boolean isPasswordChanged = (password != null && !password.equals(userPassword)) || (userPassword != null && !userPassword.equals(password));

        if (isEmailChanged || isPasswordChanged) { //если менял емайл
            user.setEmail(email);

            if (!StringUtils.isEmpty(password)) { // установил ли пароль
                user.setPassword(passwordEncoder.encode(password));
            }

            if (!StringUtils.isEmpty(email)) { //если установил новый емайл то генеринуем новый код активации
                user.setActivationCode(UUID.randomUUID().toString());
            }

            userRepo.save(user);

            if (isEmailChanged) { //отправляем если емайл был изменен
                sendMessage(user);
            }
        }
    }
}
