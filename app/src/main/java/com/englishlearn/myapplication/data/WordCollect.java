package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class WordCollect implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private User user; //用户Id
    private WordGroup wordGroup; //分组Id
    private Word word; //(句子，单词，文章)

    public void setPointer(){
        __type = "Pointer";
        className = "WordCollect";
    }

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

    public WordGroup getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(WordGroup wordGroup) {
        this.wordGroup = wordGroup;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
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
        return "WordCollect{" +
                "objectId='" + objectId + '\'' +
                ", user=" + user +
                ", wordGroup=" + wordGroup +
                ", word=" + word +
                '}';
    }
}
