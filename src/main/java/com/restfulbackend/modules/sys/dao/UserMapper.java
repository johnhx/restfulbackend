package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.User;
import com.restfulbackend.modules.sys.entity.UserByRole;
import com.restfulbackend.modules.sys.entity.UserProfile;

import java.util.List;

@MyBatisDao
public interface UserMapper {
    User selectByPrimaryKey(long id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    User selectByUUID(String uuid);

    UserByRole selectByRole(String roleId);

    String selectNickByUserName(String userName);

    int insert(User user);

    int insertProfile(UserProfile userProfile);

    int update(User user);

    int updatePassword(User user);

    int updateUserProfileId(User user);

    int updateAccessLevel(User user);

    User validate(User user);
}
