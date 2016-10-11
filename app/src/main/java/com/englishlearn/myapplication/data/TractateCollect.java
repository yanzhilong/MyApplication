package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class TractateCollect implements Serializable,Cloneable{

    private String objectId;
    private String userId; //用户Id
    private String tractategroupId; //分组Id
    private String tractateId; //(句子，单词，文章)


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

    public String getTractategroupId() {
        return tractategroupId;
    }

    public void setTractategroupId(String tractategroupId) {
        this.tractategroupId = tractategroupId;
    }

    public String getTractateId() {
        return tractateId;
    }

    public void setTractateId(String tractateId) {
        this.tractateId = tractateId;
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
        return "TractateCollect{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", tractategroupId='" + tractategroupId + '\'' +
                ", tractateId='" + tractateId + '\'' +
                '}';
    }
}
