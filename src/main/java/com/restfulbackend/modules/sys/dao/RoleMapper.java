package com.restfulbackend.modules.sys.dao;

import com.restfulbackend.common.persistence.annotation.MyBatisDao;
import com.restfulbackend.modules.sys.entity.Role;

/**
 * Created by hejiang on 14/12/21.
 */
@MyBatisDao
public interface RoleMapper {
    Role selectByPrimaryKey(long id);

    Role selectByName(String name);
}
