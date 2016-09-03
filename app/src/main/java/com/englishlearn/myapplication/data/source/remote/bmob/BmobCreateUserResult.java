package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobCreateUserResult {

    private String createAt;
    private String objectId;
    private String sessionToken;

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
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
}
