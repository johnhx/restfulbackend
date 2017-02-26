package com.restfulbackend.modules.sys.service;

import com.restfulbackend.modules.sys.dao.MessageMapper;
import com.restfulbackend.modules.sys.dao.TextMessageMapper;
import com.restfulbackend.modules.sys.entity.Message;
import com.restfulbackend.modules.sys.entity.MessageByFrom;
import com.restfulbackend.modules.sys.entity.MessageByTo;
import com.restfulbackend.modules.sys.entity.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by John_He4 on 11/29/2014.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private MessageMapper messageMapper;

    private TextMessageMapper textMessageMapper;

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setTextMessageMapper(TextMessageMapper textMessageMapper) {
        this.textMessageMapper = textMessageMapper;
    }

    @Override
    public Message getMessageById(long id) {
//        return null;
        return messageMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<Message> getMessageByTo(String toUuid) {
        MessageByTo messageByTo = messageMapper.selectByTo(toUuid);

        if(messageByTo == null){
            return null;
        }

        return messageByTo.getMessges();
    }

    @Override
    public List<Message> getMessageByFrom(String fromUuid) {
        MessageByFrom messageByFrom = messageMapper.selectByFrom(fromUuid);

        if(messageByFrom == null){
            return null;
        }

        return messageByFrom.getMessges();
    }

    @Override
    public long insertTextMessage(Message message, TextMessage textMessage) {
//        return 0;
        int result = textMessageMapper.insert(textMessage);
//        long textMessageId = 1;
        Long messageId = Long.valueOf(0);
        if(result > 0){
            message.setMsgId(textMessage.getId());
            messageId = messageMapper.insert(message);
        }

        return messageId;
    }
}
