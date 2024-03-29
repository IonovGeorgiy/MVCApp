package com.app.controller;

import com.app.domain.Message;
import com.app.domain.User;
import com.app.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired  /*This means to get the bean called messageRepo, Which is auto-generated by Spring, we will use it to handle the data*/
    private MessageRepo messageRepo;

    @Value("${upload.path}") /*этой аннотацией указываем Spring, что мы хотим получить переменную. "${upload.path}" - выдергивает из контекста значения либо конструкции, сейчас он ищет upload.path в application.properties и вставляет его в переменную private String uploadPath;*/
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    /*
    модель это место куда мы будем складывать данные которые хотим вернуть пользователю
    */
    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) org.springframework.data.domain.Pageable pageable
    ) { /*filter записываем required = false потому, что мы его будем передавать не всегда *///*задаем поле по которому будем сортировать сообщения и порядок сортировки(по убыванию, с начала показываем сообщения, которые были созданы последними), если эти параметры не задать то сообщения принимаемые с бд, будут приходить в рандомном порядке*//*
        Page<Message> page;

        if (filter != null && !filter.isEmpty()) {
            page = messageRepo.findByTag(filter, pageable); /*2 findByTag возвращает List*/
        } else {
            page = messageRepo.findAll(pageable); /*1 findAll возвращает Iterable*/
        }

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult, /*BindingResult это список аргументов и сообщений ошибок валидации, этот аргумент всегда идет перед Model иначе все ошибки валидации будет лететь во вью без обработки*/
            Model model,
            @RequestParam("file") MultipartFile file /*нужно для сохранеия картинок, @RequestParam эта аннотация выдергивает значения либо из url если мы используем get либо из формы если мы используем post*/
    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()){ //сохранение в бд произойдет только в том случае если валидация прошла без ошибок
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);

            model.addAttribute("message", null); //удаляем из модели сообщение, иначе после добавления мы получим открытую форму с сообщением
            messageRepo.save(message); /*1 сохраняем*/
        }
        Iterable<Message> messages = messageRepo.findAll(); /*2 взяли из репозитория*/
        model.addAttribute("messages", messages); /*3 положили в модель*/

        return "main"; /*4 отдали пользователю*/
    }

    protected void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException { //метод сохранения файла
        if (file != null && !file.getOriginalFilename().isEmpty()) { /*будем сохранять файл, только если у него задано имя !file.getOriginalFilename().isEmpty()*/
            File uploadDit = new File(uploadPath);

            if (!uploadDit.exists()) { /*если uploadDit не сужествует*/
                uploadDit.mkdir(); /*то мы создаем его. Обезописили себя от ошибок связанных с тем что дирректории не существует*/
            }

            String uuidFile = UUID.randomUUID().toString(); /*Обезопасим себя от коллизий и созданим уникальное имя файла*/
            String resultFilename = uuidFile + "." + file.getOriginalFilename(); /*конечное имя файла*/

            file.transferTo(new File(uploadPath + "/" + resultFilename)); /*загружаем файл*/

            message.setFilename(resultFilename);
        }
    }
}
