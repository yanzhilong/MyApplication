package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 */
public class WordGroup implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private String name; //分组名称
    private boolean open;//是否公开
    private User user; //用户

    public void setPointer(){
        __type = "Pointer";
        className = "WordGroup";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "WordGroup{" +
                ", objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", open=" + open +
                ", user=" + user +
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
