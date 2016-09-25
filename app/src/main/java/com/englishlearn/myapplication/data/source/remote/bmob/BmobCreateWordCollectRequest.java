package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobCreateWordCollectRequest {

    private String wordcollectId;//收藏id
    private String userId; //用户Id
    private String createDate; //创建时间
    private String wordGroupId; //分组Id
    private String wordId; //(单词Id)

    public String getWordcollectId() {
        return wordcollectId;
    }

    public void setWordcollectId(String wordcollectId) {
        this.wordcollectId = wordcollectId;
    }

    public BmobCreateWordCollectRequest() {
        this.userId = UUID.randomUUID().toString();
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

    public String getWordGroupId() {
        return wordGroupId;
    }

    public void setWordGroupId(String wordGroupId) {
        this.wordGroupId = wordGroupId;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }
}
