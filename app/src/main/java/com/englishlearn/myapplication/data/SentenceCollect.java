package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class SentenceCollect implements Serializable,Cloneable{

    private String objectId;
    private String userId; //用户Id
    private String scollectgroupId; //句子收藏分组Id
    private String sentenceId; //(句子，单词，文章)


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScollectgroupId() {
        return scollectgroupId;
    }

    public void setScollectgroupId(String scollectgroupId) {
        this.scollectgroupId = scollectgroupId;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Override
    public Object clone(){

        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String toString() {
        return "SentenceCollect{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", scollectgroupId='" + scollectgroupId + '\'' +
                ", sentenceId='" + sentenceId + '\'' +
                '}';
    }
}
