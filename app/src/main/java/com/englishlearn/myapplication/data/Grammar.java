package com.englishlearn.myapplication.data;

import java.util.List;
import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 语法
 */
public class Grammar {
    private final String mId;//id

    private String mLabel;//语法标签

    private String mContent;//说明

    public Grammar(){
        mId = UUID.randomUUID().toString();
    }

    public Grammar(String content,String label){
        mId = UUID.randomUUID().toString();
        mContent = content;
    }
}
