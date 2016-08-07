package com.englishlearn.myapplication.data.source.remote.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobGrammar extends BmobObject {
    private final String grammarid;//id

    private String name;//语法标签

    private String content;//说明

    public BmobGrammar(String grammarid) {
        this.grammarid = grammarid;
    }

    public BmobGrammar(String grammarid, String name, String content) {
        this.grammarid = grammarid;
        this.name = name;
        this.content = content;
    }

    public String getGrammarid() {
        return grammarid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
