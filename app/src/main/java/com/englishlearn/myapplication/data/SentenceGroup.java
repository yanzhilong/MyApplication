package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class SentenceGroup {

    private String id;
    private String sentencegroupId;
    private String name; //分组名称
    private String userId; //用户Id

    public SentenceGroup() {
        sentencegroupId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
