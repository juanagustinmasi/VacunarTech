package com.unlam.vacunartech.models;

public class LoginResponse {
    private String success;
    private String msg;
    private String token;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }
}
