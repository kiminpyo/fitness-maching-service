package com.kang.backup.model;

public class RequestModel {

    private String requestData;

    private String requestStime;

    private String requestEtime;

    private String requestTxt;

    private String currentTime;

    private String sendUser;

    private String receiveUser;

    private String state;

    private String major;

    private String area;

    private String requestCnt;

    private String memoKey;

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestStime() {
        return requestStime;
    }

    public void setRequestStime(String requestStime) {
        this.requestStime = requestStime;
    }

    public String getRequestEtime() {
        return requestEtime;
    }

    public void setRequestEtime(String requestEtime) {
        this.requestEtime = requestEtime;
    }

    public String getRequestTxt() {
        return requestTxt;
    }

    public void setRequestTxt(String requestTxt) {
        this.requestTxt = requestTxt;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRequestCnt() {
        return requestCnt;
    }

    public void setRequestCnt(String requestCnt) {
        this.requestCnt = requestCnt;
    }

    public String getMemoKey() {
        return memoKey;
    }

    public void setMemoKey(String memoKey) {
        this.memoKey = memoKey;
    }
}
