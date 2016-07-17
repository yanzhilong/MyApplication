package com.englishlearn.myapplication.data;

import java.util.List;
import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class Sentence {
    private final String mId;//id

    private String content;//内容

    private String translate;//翻译

    private List<Grammar> grammarList;//相关语法

    public Sentence(){
        mId = UUID.randomUUID().toString();
    }

    public Sentence(String content,String translate,List<Grammar> grammars){
        mId = UUID.randomUUID().toString();
        this.content = content;
        this.translate = translate;
        this.grammarList = grammarList;
    }
}
