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
    private boolean open;//是否公开
    private String userId; //用户Id
    private String sentencegroupId; //分组Id
    private String soundurl;//声音
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSoundurl() {
        return soundurl;
    }

    public void setSoundurl(String soundurl) {
        this.soundurl = soundurl;
    }

    public String getSentencegroupId() {
        return sentencegroupId;
    }

    public void setSentencegroupId(String sentencegroupId) {
        this.sentencegroupId = sentencegroupId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
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
                ", open=" + open +
                ", userId='" + userId + '\'' +
                ", sentencegroupId='" + sentencegroupId + '\'' +
                ", soundurl='" + soundurl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
