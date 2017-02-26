package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.User;
import com.restfulbackend.modules.sys.entity.UserBlock;

/**
 * Created by hejiang on 14/11/23.
 */
@MyBatisDao
public interface UserBlockMapper {
    UserBlock selectByPrimaryKey(Long id);

    UserBlock selectByOwner(String ownerUuid);

    UserBlock selectByBlock(String blockUuid);

    Long insert(UserBlock userBlock);

    void delete(UserBlock userBlock);
}
