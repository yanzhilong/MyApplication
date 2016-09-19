package com.englishlearn.myapplication.data.source.remote.bmob;


/**
 * Created by yanzl on 16-9-3.
 * 请求网络用，用户表，删除id字段
 */
public class BmobCreateOrLoginUserByPhoneRequest {

    private String mobilePhoneNumber;
    private String smsCode;
    private String userId;//唯一Id

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
