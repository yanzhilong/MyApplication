package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class WordCollect {

    private String id;
    private String wordcollectId;//收藏id
    private String userId; //用户Id
    private String createDate; //创建时间
    private String wordgroupId; //分组Id
    private String wordId; //(句子，单词，文章)

    public WordCollect() {
        this.wordcollectId = UUID.randomUUID().toString();
    }

    public String getWordcollectId() {
        return wordcollectId;
    }

    public void setWordcollectId(String wordcollectId) {
        this.wordcollectId = wordcollectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getWordgroupId() {
        return wordgroupId;
    }

    public void setWordgroupId(String wordgroupId) {
        this.wordgroupId = wordgroupId;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }
}
