package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 语法
 */
public class Grammar {
    private String id;//数据庫唯一id

    private final String grammarid;//语法唯一id

    private String name;//语法标签

    private String content;//说明

    public Grammar(){
        grammarid = UUID.randomUUID().toString();
    }

    public Grammar(String name,String content){
        grammarid = UUID.randomUUID().toString();
        this.name = name;
        this.content = content;
    }

    public Grammar(String grammarid,String name,String content){
        this.grammarid = grammarid;
        this.name = name;
        this.content = content;
    }

    public Grammar(String id, String grammarid, String name, String content) {
        this.id = id;
        this.grammarid = grammarid;
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrammarid() {
        return grammarid;
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
