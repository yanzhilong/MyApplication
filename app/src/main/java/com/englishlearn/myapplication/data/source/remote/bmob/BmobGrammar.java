package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobGrammar {

    private String name;
    private String createdAt;
    private String objectId;
    private String grammarid;
    private String content;
    private String updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getGrammarid() {
        return grammarid;
    }

    public void setGrammarid(String grammarid) {
        this.grammarid = grammarid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BmobGrammar{" +
                "name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", objectId='" + objectId + '\'' +
                ", grammarid='" + grammarid + '\'' +
                ", content='" + content + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
