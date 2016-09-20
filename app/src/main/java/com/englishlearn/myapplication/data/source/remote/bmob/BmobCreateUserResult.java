package com.englishlearn.myapplication.data.source.remote.bmob;


/**
 * Created by yanzl on 16-9-3.
 * 创建用户的返回pojo
 */
public class BmobCreateUserResult {

    private String objectId;
    private String createdAt;
    private String sessionToken;//注册和登陆都有

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString() {
        return "BmobCreateUserResult{" +
                "objectId='" + objectId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
