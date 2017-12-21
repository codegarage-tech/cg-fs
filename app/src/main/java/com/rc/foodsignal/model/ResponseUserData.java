package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ResponseUserData extends ResponseBase {

    private String status = "";
    private String msg = "";
    private ArrayList<UserData> data = new ArrayList<UserData>();

    public ResponseUserData() {
    }

    public ResponseUserData(String status, String msg, ArrayList<UserData> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<UserData> getData() {
        return data;
    }

    public void setData(ArrayList<UserData> user_details) {
        this.data = user_details;
    }
}
