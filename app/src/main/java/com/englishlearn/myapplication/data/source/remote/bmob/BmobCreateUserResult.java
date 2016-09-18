package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-8-1.
 *
 * 正确
 * {
 "createdAt": "2016-09-18 14:00:41",
 "objectId": "5b78e84d67",
 "sessionToken": "ae56d4824028d2b0804aca3adab33eba"
 }
 错误
 {
 "code": 202,
 "error": "username 'gfdonx2' already taken."
 }
 */
public class BmobCreateUserResult {


    private String createAt;
    private String objectId;
    private String sessionToken;
    private String code;
    private String error;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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
