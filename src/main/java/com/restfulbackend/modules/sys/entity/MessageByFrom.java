package com.restfulbackend.modules.sys.entity;

import java.util.List;

/**
 * Created by John_He4 on 11/25/2014.
 */
public class MessageByFrom {
    private long fromId;
    private List<Message> messges;


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public List<Message> getMessges() {
        return messges;
    }

    public void setMessges(List<Message> messges) {
        this.messges = messges;
    }
}
