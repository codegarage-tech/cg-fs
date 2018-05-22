package com.rc.foodsignal.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ResponseDeleteFoodMenuItem extends ResponseBase {

    private String status = "";
    private String message = "";

    public ResponseDeleteFoodMenuItem() {
    }

    public ResponseDeleteFoodMenuItem(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseDeleteFoodMenuItem{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
