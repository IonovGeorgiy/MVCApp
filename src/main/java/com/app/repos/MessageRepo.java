package com.app.repos;

import com.app.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
// CRUD refers Create, Read, Update, Delete
public interface MessageRepo extends CrudRepository<Message, Long> {
/*
руководство по Spring jpa
    https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
*/
Page<Message> findAll(org.springframework.data.domain.Pageable pageable);
    Page<Message> findByTag(String tag, Pageable pageable);
}
