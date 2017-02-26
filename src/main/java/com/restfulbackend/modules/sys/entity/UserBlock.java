package com.restfulbackend.modules.sys.entity;

/**
 * Created by hejiang on 14/11/23.
 */
public class UserBlock {
    private int id;
    private String ownerUuid;
    private String blockUuid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getBlockUuid() {
        return blockUuid;
    }

    public void setBlockUuid(String blockUuid) {
        this.blockUuid = blockUuid;
    }
}
