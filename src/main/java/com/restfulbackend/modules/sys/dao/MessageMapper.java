package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.*;

/**
 * Created by John_He4 on 11/25/2014.
 */
@MyBatisDao
public interface MessageMapper {
    Message selectByPrimaryKey(Long id);

    MessageByFrom selectByFrom(String ownerUuid);

    MessageByTo selectByTo(String friendUuid);

    Long insert(Message message);
}
