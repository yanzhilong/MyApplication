package com.englishlearn.myapplication.data.source.remote.bmob;


import com.englishlearn.myapplication.util.TimeUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 * 请求网络用，用户表，删除id字段
 */
public class BmobCreateUserRequest {

    private final String userId;//唯一Id
    private String username;
    private String password;
    private String mobile;
    private String mail;
    private String nickname;
    private String iconurl;
    private String birthday;
    private String createDate;

    public BmobCreateUserRequest() {
        userId = UUID.randomUUID().toString();
        this.createDate = TimeUtil.formatTime(new Date());
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
