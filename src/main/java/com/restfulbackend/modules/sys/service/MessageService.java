package com.restfulbackend.modules.sys.service;

import com.restfulbackend.modules.sys.entity.Message;
import com.restfulbackend.modules.sys.entity.TextMessage;

import java.util.List;

/**
 * Created by John_He4 on 11/29/2014.
 */
public interface MessageService {

    public Message getMessageById(long id);

    public List<Message> getMessageByTo(String toUuid);

    public List<Message> getMessageByFrom(String fromUuid);

    public long insertTextMessage(Message message, TextMessage textMessage);
}
