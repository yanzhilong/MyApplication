package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class Sentence {

    private String id;
    private String sentenceId; //句子唯一id
    private String content; //内容
    private String translation; //译文
    private String userId; //用户Id
    private String source; //来源 (拉取来源id选择再提交来源的名称)
    private String tractateId; //来自的文章Id
    private String createDate; //创建时间
    private String remark; //备注

    public Sentence() {
        sentenceId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTractateId() {
        return tractateId;
    }

    public void setTractateId(String tractateId) {
        this.tractateId = tractateId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
