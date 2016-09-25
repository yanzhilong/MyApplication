package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-12.
 * 单词分级收藏表
 */
public class WordGroupCollect {

    private String id;
    private String wordGroupcollectId;
    private String userId; //用户Id
    private String wordgroupId; //单词分组Id

    public WordGroupCollect() {
        this.wordGroupcollectId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "WordGroupCollect{" +
                "id='" + id + '\'' +
                ", wordGroupcollectId='" + wordGroupcollectId + '\'' +
                ", userId='" + userId + '\'' +
                ", wordgroupId='" + wordgroupId + '\'' +
                '}';
    }
}
