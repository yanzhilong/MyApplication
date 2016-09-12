package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-12.
 */
public class SentenceGroupCollect {

    private String id;
    private String sentencegroupcollectId;
    private String userId; //用户Id
    private String sentencegroupId; //句子分组Id

    public SentenceGroupCollect() {
        sentencegroupcollectId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentencegroupcollectId() {
        return sentencegroupcollectId;
    }

    public void setSentencegroupcollectId(String sentencegroupcollectId) {
        this.sentencegroupcollectId = sentencegroupcollectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }
}
