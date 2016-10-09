package com.englishlearn.myapplication.data;

/**
 * Created by yanzl on 16-9-12.
 * 单词分级收藏表
 */
public class WordGroupCollect {

    private String objectId;
    private String userId; //用户Id
    private String wordgroupId; //单词分组Id


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

    public String getWordgroupId() {
        return wordgroupId;
    }

    public void setWordgroupId(String wordgroupId) {
        this.wordgroupId = wordgroupId;
    }

    @Override
    public String toString() {
        return "WordGroupCollect{" +
                "objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", wordgroupId='" + wordgroupId + '\'' +
                '}';
    }
}
