package com.example.demo.controller.request;

public class ResultEntity {

    public String statusMessage;
    public int statusCode;
    public String localTime;
    public String data;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getDatas() {
        return data;
    }

    public void setDatas(String data) {
        this.data = data;
    }
}
