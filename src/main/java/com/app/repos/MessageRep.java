package com.app.repos;

import com.app.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRep extends CrudRepository <Message, Integer> {
}
