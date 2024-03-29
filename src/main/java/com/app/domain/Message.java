package com.app.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 255, message = "Message to long (255)")
    private String text;
    @Length(max = 255, message = "Tag to long (255)")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER) //EAGER потому, что каждый раз когда мы получаем сообщение мы хотим получать и информацию об авторе вместе с сообщением
    @JoinColumn(name = "user_id") //теперь в об поле будет называться user_id, а не author, как было бы по умолчанию
    private User author;

    private String filename; /*тут будем хранить названия картинки*/

    public Message() { //нужно обязательно создавать пустой конструктор если это Entity т.к. если его не создать, то Spring не сможет обращаться к этому классу
    }

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>"; //проверяем есть автор у сообщения или его нет, если нет то пишем <none>
    }

    /*
    геттеры и сеттеры нужны для приватных полей
    */

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
