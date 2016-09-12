package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-12.
 */
public class BmobSentenceGroupCollect {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String sentencegroupcollectId;
    private String userId; //用户Id
    private String sentencegroupId; //句子分组Id

    public BmobSentenceGroupCollect() {
    }


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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
