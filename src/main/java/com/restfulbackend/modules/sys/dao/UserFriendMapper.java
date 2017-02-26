package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.UserBlock;
import com.restfulbackend.modules.sys.entity.UserFriend;
import com.restfulbackend.modules.sys.entity.UserFriendList;

/**
 * Created by hejiang on 14/11/23.
 */
@MyBatisDao
public interface UserFriendMapper {
    UserFriend selectByPrimaryKey(Long id);

    UserFriendList selectByOwner(String ownerUuid);

    UserFriend selectByBlock(String friendUuid);

    Long insert(UserFriend userBlock);

    void delete(UserFriend userBlock);
}
