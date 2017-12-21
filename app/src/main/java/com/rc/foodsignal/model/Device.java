package com.rc.foodsignal.model;

/**
 * Author: Abir Ahmed
 * Email: abir.eol@gmail.com
 */

public class Device extends ResponseBase {

    private String id = "";
    private String device = "";

    public Device(String id, String device) {
        this.id = id;
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
