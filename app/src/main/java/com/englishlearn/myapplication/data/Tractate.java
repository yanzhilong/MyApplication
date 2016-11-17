package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 * 文章
 */
public class Tractate implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private User userId; //用户Id
    private String title; //标题
    private String content; //内容
    private String translation; //译文
    private TractateType tractatetypeId; //分类Id
    private TractateGroup tractateGroupId;//分组Id
    private boolean open; //是否公开
    private String remark; //备注
    private int sort;//排序字段

    public void setPointer(){
        __type = "Pointer";
        className = "Tractate";
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public TractateType getTractatetypeId() {
        return tractatetypeId;
    }

    public void setTractatetypeId(TractateType tractatetypeId) {
        this.tractatetypeId = tractatetypeId;
    }

    public TractateGroup getTractateGroupId() {
        return tractateGroupId;
    }

    public void setTractateGroupId(TractateGroup tractateGroupId) {
        this.tractateGroupId = tractateGroupId;
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
        return "Tractate{" +
                "objectId='" + objectId + '\'' +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", translation='" + translation + '\'' +
                ", tractatetypeId=" + tractatetypeId +
                ", open=" + open +
                ", remark='" + remark + '\'' +
                ", sort=" + sort +
                '}';
    }
}
