package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobSentence {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String sentenceId; //句子唯一id
    private String content; //内容
    private String translation; //译文
    private String userId; //用户Id
    private String source; //来源 (拉取来源id选择再提交来源的名称)
    private String tractateId; //来自的文章Id
    private String createDate; //创建时间
    private String remark; //备注

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
