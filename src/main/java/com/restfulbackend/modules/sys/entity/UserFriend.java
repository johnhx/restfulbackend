package com.restfulbackend.modules.sys.entity;

/**
 * Created by hejiang on 14/11/23.
 */
public class UserFriend {
    private long id;
    private String ownerUuid;
    private String friendUuid;
    private String friendRemark;

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getFriendUuid() {
        return friendUuid;
    }

    public void setFriendUuid(String friendUuid) {
        this.friendUuid = friendUuid;
    }

    public String getFriendRemark() {
        return friendRemark;
    }

    public void setFriendRemark(String friendRemark) {
        this.friendRemark = friendRemark;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
