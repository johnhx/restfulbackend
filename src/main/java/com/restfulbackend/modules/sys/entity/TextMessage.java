package com.restfulbackend.modules.sys.entity;

/**
 * Created by John_He4 on 11/25/2014.
 */
public class TextMessage {
    private long id;

    private String msg;

    private String createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
