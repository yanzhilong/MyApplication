package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-3.
 * 文章
 */
public class Tractate implements Serializable,Cloneable{

    private String objectId;
    private String userId; //用户Id
    private String title; //标题
    private String content; //内容
    private String translation; //译文
    private String tractatetypeId; //分类Id
    private String remark; //备注
    private int sort;//排序字段

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTractatetypeId() {
        return tractatetypeId;
    }

    public void setTractatetypeId(String tractatetypeId) {
        this.tractatetypeId = tractatetypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", translation='" + translation + '\'' +
                ", tractatetypeId='" + tractatetypeId + '\'' +
                ", remark='" + remark + '\'' +
                ", sort=" + sort +
                '}';
    }
}
