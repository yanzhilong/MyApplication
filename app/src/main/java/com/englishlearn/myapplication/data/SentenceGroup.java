package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class SentenceGroup implements Serializable,Cloneable {

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;;
    private boolean open; //是否公开
    private String name; //分组名称
    private User user; //用户Id

    public void setPointer(){
        __type = "Pointer";
        className = "SentenceGroup";
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
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
        return "SentenceGroup{" +
                "objectId='" + objectId + '\'' +
                ", open=" + open +
                ", name='" + name + '\'' +
                ", user=" + user +
                '}';
    }
}
