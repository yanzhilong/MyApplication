package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobSentence {

    private String content;
    private String createdAt;
    private String objectId;
    private String sentenceid;
    private String translation;
    private String updatedAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSentenceid() {
        return sentenceid;
    }

    public void setSentenceid(String sentenceid) {
        this.sentenceid = sentenceid;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BmobSentenceNew{" +
                "content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", objectId='" + objectId + '\'' +
                ", sentenceid='" + sentenceid + '\'' +
                ", translation='" + translation + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
