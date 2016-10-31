package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-12.
 */
public class TractateGroupCollect implements Serializable,Cloneable {

    private String objectId;
    private String userId; //用户Id
    private String tractategroupId; //文章分组Id


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
        return "TractateGroupCollect{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", tractategroupId='" + tractategroupId + '\'' +
                '}';
    }
}
