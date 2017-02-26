package com.restfulbackend.modules.sys.entity;

import java.util.List;

/**
 * Created by John_He4 on 11/25/2014.
 */
public class MessageByTo {
    private long toId;
    private List<Message> messges;

    public List<Message> getMessges() {
        return messges;
    }

    public void setMessges(List<Message> messges) {
        this.messges = messges;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}
