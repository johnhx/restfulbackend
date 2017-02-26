package com.restfulbackend.modules.sys.entity;

import java.util.List;

/**
 * Created by hejiang on 14/11/24.
 */
public class UserFriendList {
    private String ownerUuid;
    private List<UserFriend> userFriends;

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public List<UserFriend> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<UserFriend> userFriends) {
        this.userFriends = userFriends;
    }
}
