package com.restfulbackend.modules.sys.service;

import com.restfulbackend.modules.sys.entity.Role;

/**
 * Created by hejiang on 14/12/21.
 */
public interface RoleService {
    public Role getRoleById(long id);

    public Role getRoleByName(String name);
}
