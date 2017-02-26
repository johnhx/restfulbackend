package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.UserProfile;

/**
 * Created by hejiang on 14/11/14.
 */
@MyBatisDao
public interface UserProfileMapper {
    UserProfile selectByPrimaryKey(long id);

    UserProfile selectByUserId(long userId);

    UserProfile selectByEmail(String email);

    int deleteByPrimaryKey(long id);

    int insert(UserProfile userProfile);

    int update(UserProfile userProfile);

    int updateAvatar(UserProfile userProfile);
}
