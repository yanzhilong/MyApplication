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

    private String translation;//翻译

    private List<Grammar> grammarList;//相关语法

    public Sentence(){
        mId = UUID.randomUUID().toString();
    }

    public Sentence(String id,String content,String translation,List<Grammar> grammars){
        mId = UUID.randomUUID().toString();
        this.content = content;
        this.translation = translation;
        this.grammarList = grammarList;
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

    public void setTtranslation(String translation) {
        this.translation = translation;
    }

    public List<Grammar> getGrammarList() {
        return grammarList;
    }

    public void setGrammarList(List<Grammar> grammarList) {
        this.grammarList = grammarList;
    }

    public String getmId() {
        return mId;
    }
}
