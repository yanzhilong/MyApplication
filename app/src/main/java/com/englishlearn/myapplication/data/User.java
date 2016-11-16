package com.englishlearn.myapplication.data;


import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 * 用户表
 */
public class User implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;//数据庫唯一id
    private String username;
    private String password;
    private String sessionToken;//登陆才有
    private String mobilePhoneNumber;
    private String mobilePhoneVerified;//手机是否验证
    private String emailVerified;//邮箱是否验证
    private String email;
    private String nickname;
    private String sex;
    private String iconurl;
    private String birthday;

    public void setPointer(){
        __type = "Pointer";
        className = "_User";
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(String mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", mobilePhoneVerified='" + mobilePhoneVerified + '\'' +
                ", emailVerified='" + emailVerified + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
