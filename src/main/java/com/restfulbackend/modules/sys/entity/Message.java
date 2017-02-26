package com.restfulbackend.modules.sys.entity;

/**
 * Created by John_He4 on 11/25/2014.
 */
public class Message {
    private long id;

    private long fromId;

    private long toId;

    private int toTypeId;

    private long msgTypeId;

    private long msgId;

    private String createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getToTypeId() {
        return toTypeId;
    }

    public void setToTypeId(int toTypeId) {
        this.toTypeId = toTypeId;
    }

    public long getMsgTypeId() {
        return msgTypeId;
    }

    public void setMsgTypeId(long msgTypeId) {
        this.msgTypeId = msgTypeId;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
