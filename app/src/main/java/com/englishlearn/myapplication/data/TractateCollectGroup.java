package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-12.
 */
public class TractateCollectGroup implements Serializable,Cloneable {

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private String name;
    private User userId; //用户Id


    public void setPointer(){
        __type = "Pointer";
        className = "TractateCollectGroup";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "TractateCollectGroup{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
