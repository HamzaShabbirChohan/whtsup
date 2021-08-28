package com.example.whatsup.Model;

public class messsagemodel {
    String uid,message,messageid;
    Long timestamp;

    public messsagemodel(String uid, String message, Long timestamp) {

        this.uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public messsagemodel(String messageid) {
        this.messageid = messageid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public messsagemodel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }
    public messsagemodel()
    {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


}
