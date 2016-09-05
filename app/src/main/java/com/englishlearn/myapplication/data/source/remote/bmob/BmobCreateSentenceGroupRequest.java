package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobCreateSentenceGroupRequest {

    private String sentencegroupId;
    private String name; //分组名称
    private String userId; //用户Id

    public BmobCreateSentenceGroupRequest() {
        sentencegroupId = UUID.randomUUID().toString();
    }

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
