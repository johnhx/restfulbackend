package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.Message;
import com.restfulbackend.modules.sys.entity.MessageByFrom;
import com.restfulbackend.modules.sys.entity.MessageByTo;
import com.restfulbackend.modules.sys.entity.TextMessage;

/**
 * Created by John_He4 on 11/25/2014.
 */
@MyBatisDao
public interface TextMessageMapper {
    TextMessage selectByPrimaryKey(Long id);

    int insert(TextMessage message);
}
