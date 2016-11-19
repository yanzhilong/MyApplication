package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class TractateCollect implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private User userId; //用户Id
    private TractateCollectGroup tractateCollectgId; //分组Id
    private Tractate tractateId; //(句子，单词，文章)

    public void setPointer(){
        __type = "Pointer";
        className = "TractateCollect";
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public TractateCollectGroup getTractateCollectgId() {
        return tractateCollectgId;
    }

    public void setTractateCollectgId(TractateCollectGroup tractateCollectgId) {
        this.tractateCollectgId = tractateCollectgId;
    }

    public Tractate getTractateId() {
        return tractateId;
    }

    public void setTractateId(Tractate tractateId) {
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
                ", userId=" + userId +
                ", tractateCollectgId=" + tractateCollectgId +
                ", tractateId=" + tractateId +
                '}';
    }
}
