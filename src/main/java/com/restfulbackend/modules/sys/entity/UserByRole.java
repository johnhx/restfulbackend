package com.restfulbackend.modules.sys.entity;

import java.util.List;

/**
 * Created by hejiang on 14/12/21.
 */
public class UserByRole {
    private long roleId;
    private List<User> users;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
