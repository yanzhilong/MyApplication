package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class Sentence implements Serializable,Cloneable {

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private String content; //内容
    private String translation; //译文
    private boolean open;//是否公开
    private User user; //用户Id
    private SentenceGroup sentenceGroup; //分组Id
    private String soundurl;//声音
    private String remark; //备注


    public void setPointer(){
        __type = "Pointer";
        className = "Sentence";
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public SentenceGroup getSentenceGroup() {
        return sentenceGroup;
    }

    public void setSentenceGroup(SentenceGroup sentenceGroup) {
        this.sentenceGroup = sentenceGroup;
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
                ", objectId='" + objectId + '\'' +
                ", content='" + content + '\'' +
                ", translation='" + translation + '\'' +
                ", open=" + open +
                ", user=" + user +
                ", sentenceGroup=" + sentenceGroup +
                ", soundurl='" + soundurl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
