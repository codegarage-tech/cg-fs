package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseAddUserBasicInfo extends ResponseBase {

    private String status = "";
    private ArrayList<Location> data = new ArrayList<Location>();

    public ResponseAddUserBasicInfo() {
    }

    public ResponseAddUserBasicInfo(String status, ArrayList<Location> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Location> getData() {
        return data;
    }

    public void setData(ArrayList<Location> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAddUserBasicInfo{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
