package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 * 单詞
 */
public class BmobCreateWordRequest {

    private String wordId;
    private String name;
    private String british_phonogram; //英式发音音标(多个用"|"分割)
    private String british_soundurl; //英式发音(下面同上)
    private String american_phonogram; //英式发音音标
    private String american_soundurl; //美式发音
    private String translate;//解释，各种词类型用"|"分割
    private String correlation; //其它相关的（第三人称单数，复数....）
    private String remark; //备注

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBritish_phonogram() {
        return british_phonogram;
    }

    public void setBritish_phonogram(String british_phonogram) {
        this.british_phonogram = british_phonogram;
    }

    public String getBritish_soundurl() {
        return british_soundurl;
    }

    public void setBritish_soundurl(String british_soundurl) {
        this.british_soundurl = british_soundurl;
    }

    public String getAmerican_phonogram() {
        return american_phonogram;
    }

    public void setAmerican_phonogram(String american_phonogram) {
        this.american_phonogram = american_phonogram;
    }

    public String getAmerican_soundurl() {
        return american_soundurl;
    }

    public void setAmerican_soundurl(String american_soundurl) {
        this.american_soundurl = american_soundurl;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
