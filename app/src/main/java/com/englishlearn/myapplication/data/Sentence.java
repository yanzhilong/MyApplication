package com.englishlearn.myapplication.data;

import java.util.List;
import java.util.UUID;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class Sentence {

    private String id;//数据庫唯一id

    private final String sentenceid;//句子唯一id

    private String content;//内容

    private String translation;//翻译

    private List<Grammar> grammarList;//相关语法

    public Sentence(){
        sentenceid = UUID.randomUUID().toString();
    }

    public Sentence(String mId, String content, String translation, List<Grammar> grammarList) {
        this.sentenceid = mId;
        this.content = content;
        this.translation = translation;
        this.grammarList = grammarList;
    }

    public Sentence(String content, String translation, List<Grammar> grammarList){
        sentenceid = UUID.randomUUID().toString();
        this.content = content;
        this.translation = translation;
        this.grammarList = grammarList;
    }

    public Sentence(String id, String sentenceid, String content, String translation, List<Grammar> grammarList) {
        this.id = id;
        this.sentenceid = sentenceid;
        this.content = content;
        this.translation = translation;
        this.grammarList = grammarList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

    public List<Grammar> getGrammarList() {
        return grammarList;
    }

    public void setGrammarList(List<Grammar> grammarList) {
        this.grammarList = grammarList;
    }

    public String getSentenceid() {
        return sentenceid;
    }
}
