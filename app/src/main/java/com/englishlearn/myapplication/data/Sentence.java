package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class Sentence implements Serializable,Cloneable {

    private String objectId;
    private String content; //内容
    private String translation; //译文
    private String userId; //用户Id
    private String source; //来源 (拉取来源id选择再提交来源的名称)
    private String tractateId; //来自的文章Id
    private String remark; //备注


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Object clone(){

        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "objectId='" + objectId + '\'' +
                ", content='" + content + '\'' +
                ", translation='" + translation + '\'' +
                ", userId='" + userId + '\'' +
                ", source='" + source + '\'' +
                ", tractateId='" + tractateId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
