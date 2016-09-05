package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 * 信息分类表
 */
public class BmobTractateType {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String tractatetypeId;
    private String name;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTractatetypeId() {
        return tractatetypeId;
    }

    public void setTractatetypeId(String tractatetypeId) {
        this.tractatetypeId = tractatetypeId;
    }
}
