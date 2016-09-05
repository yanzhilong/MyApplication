package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobSentenceCollect {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String userId; //用户Id
    private String createDate; //创建时间
    private String sentencegroupId; //分组Id
    private String sentenceId; //(句子，单词，文章)

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }
}
