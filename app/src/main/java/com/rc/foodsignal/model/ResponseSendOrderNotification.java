package com.rc.foodsignal.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ResponseSendOrderNotification extends ResponseBase {

    private String status = "";
    private Restaurant data;
    private String msg = "";

    public ResponseSendOrderNotification() {
    }

    public ResponseSendOrderNotification(String status, Restaurant data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Restaurant getData() {
        return data;
    }

    public void setData(Restaurant data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseSendOrderNotification{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}