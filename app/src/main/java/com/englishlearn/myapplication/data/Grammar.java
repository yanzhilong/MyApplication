package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-7-17.
 * 语法
 */
public class Grammar implements Serializable,Cloneable {

    private String objectId;
    private String title; //标题
    private String content; //内容
    private SentenceGroup sentenceGroup; //相关示例句子
    private WordGroup wordGroup;//相关单词列表
    private User user;//添加用户
    private String remark; //备注

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SentenceGroup getSentenceGroup() {
        return sentenceGroup;
    }

    public void setSentenceGroup(SentenceGroup sentenceGroup) {
        this.sentenceGroup = sentenceGroup;
    }

    public WordGroup getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(WordGroup wordGroup) {
        this.wordGroup = wordGroup;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return "Grammar{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sentenceGroup=" + sentenceGroup +
                ", wordGroup=" + wordGroup +
                ", user=" + user +
                ", remark='" + remark + '\'' +
                '}';
    }
}
