package com.rc.foodsignal.model;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ResponseOrderList extends ResponseBase {

    private String status = "";
    private ArrayList<OrderListItem> data = new ArrayList<>();

    public ResponseOrderList() {
    }

    public ResponseOrderList(String status, ArrayList<OrderListItem> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<OrderListItem> getData() {
        return data;
    }

    public void setData(ArrayList<OrderListItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
