package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-12.
 * 单词分级收藏表
 */
public class BmobCreateWordGroupCollectRequest {

    private String wordGroupcollectId;
    private String userId; //用户Id
    private String wordgroupId; //单词分组Id

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
