package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobCreateSentenceCollectRequest {

    private String sentenceCollectId;
    private String userId; //用户Id
    private String createDate; //创建时间
    private String sentencegroupId; //分组Id
    private String sentenceId; //(句子Id)

    public String getSentenceCollectId() {
        return sentenceCollectId;
    }

    public void setSentenceCollectId(String sentenceCollectId) {
        this.sentenceCollectId = sentenceCollectId;
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

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }
}
