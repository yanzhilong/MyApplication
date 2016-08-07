package com.englishlearn.myapplication.data.source.remote.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobSentence extends BmobObject {
    private final String sentenceid;//id

    private String content;//内容

    private String translation;//翻译

    public BmobSentence(String sentenceid) {
        this.sentenceid = sentenceid;
    }

    public BmobSentence(String sentenceid, String content, String translation) {
        this.sentenceid = sentenceid;
        this.content = content;
        this.translation = translation;
    }

    public String getSentenceid() {
        return sentenceid;
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
}
