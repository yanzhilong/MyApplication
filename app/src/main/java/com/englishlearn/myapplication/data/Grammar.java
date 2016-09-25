package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 语法
 */
public class Grammar {

    private String id;
    private String grammarId; //语法唯一id
    private String title; //标题
    private String content; //内容
    private String userId; //用户Id
    private String createDate; //创建时间
    private String remark; //备注

    public Grammar() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrammarId() {
        return grammarId;
    }

    public void setGrammarId(String grammarId) {
        this.grammarId = grammarId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "id='" + id + '\'' +
                ", grammarId='" + grammarId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", createDate='" + createDate + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
