package com.restfulbackend.modules.sys.service;

import com.restfulbackend.modules.sys.dao.RoleMapper;
import com.restfulbackend.modules.sys.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hejiang on 14/12/21.
 */
@Service
public class RoleServiceImpl implements RoleService  {
    private RoleMapper roleMapper;

    @Override
    public Role getRoleById(long id){
        return roleMapper.selectByPrimaryKey(id);
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Role getRoleByName(String name){
        return roleMapper.selectByName(name);
    }
}
