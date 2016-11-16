package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class SentenceCollect implements Serializable,Cloneable{

    private String objectId;
    private User user; //用户Id
    private SentenceCollectGroup sentenceCollectGroup; //句子收藏分组Id
    private Sentence sentence; //(句子，单词，文章)

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

    public SentenceCollectGroup getSentenceCollectGroup() {
        return sentenceCollectGroup;
    }

    public void setSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup) {
        this.sentenceCollectGroup = sentenceCollectGroup;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
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
                ", user=" + user +
                ", sentenceCollectGroup=" + sentenceCollectGroup +
                ", sentence=" + sentence +
                '}';
    }
}
