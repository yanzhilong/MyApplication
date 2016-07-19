package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 语法
 */
public class Grammar {
    private final String mId;//id

    private String name;//语法标签

    private String content;//说明

    public Grammar(){
        mId = UUID.randomUUID().toString();
    }

    public Grammar(String content,String name){
        mId = UUID.randomUUID().toString();
        this.name = name;
        this.content = content;
    }

    public Grammar(String id,String content,String name){
        mId = id;
        this.name = name;
        this.content = content;
    }

    public String getmId() {
        return mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
