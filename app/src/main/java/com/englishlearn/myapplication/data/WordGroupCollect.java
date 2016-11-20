package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-12.
 * 单词分级收藏表
 */
public class WordGroupCollect  implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private User user; //用户Id
    private WordGroup wordGroup; //单词分组Id

    public void setPointer(){
        __type = "Pointer";
        className = "WordGroupCollect";
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

    @Override
    public String toString() {
        return "WordGroupCollect{" +
                "objectId='" + objectId + '\'' +
                ", user=" + user +
                ", wordGroup=" + wordGroup +
                '}';
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
}
