package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-12.
 */
public class SentenceGroupCollect implements Serializable,Cloneable {

    private String objectId;
    private User user; //用户Id
    private SentenceGroup sentenceGroup; //句子分组Id


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SentenceGroup getSentenceGroup() {
        return sentenceGroup;
    }

    public void setSentenceGroup(SentenceGroup sentenceGroup) {
        this.sentenceGroup = sentenceGroup;
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
        return "SentenceGroupCollect{" +
                "objectId='" + objectId + '\'' +
                ", user=" + user +
                ", sentenceGroup=" + sentenceGroup +
                '}';
    }
}
