package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-12.
 * 单词分级收藏表
 */
public class BmobWordGroupCollect {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String wordGroupcollectId;
    private String userId; //用户Id
    private String wordgroupId; //单词分组Id

    public BmobWordGroupCollect() {
    }

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

    public String getWordGroupcollectId() {
        return wordGroupcollectId;
    }

    public void setWordGroupcollectId(String wordGroupcollectId) {
        this.wordGroupcollectId = wordGroupcollectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWordgroupId() {
        return wordgroupId;
    }

    public void setWordgroupId(String wordgroupId) {
        this.wordgroupId = wordgroupId;
    }
}
