package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-12.
 */
public class BmobCreateSentenceGroupCollectRequest {

    private String sentenceGroupcollId;
    private String userId; //用户Id
    private String sentencegroupId; //句子分组Id

    public String getSentenceGroupcollId() {
        return sentenceGroupcollId;
    }

    public void setSentenceGroupcollId(String sentenceGroupcollId) {
        this.sentenceGroupcollId = sentenceGroupcollId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }
}
