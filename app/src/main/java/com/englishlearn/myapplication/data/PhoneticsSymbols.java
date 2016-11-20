package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * 音标类
 * Created by yanzl on 16-9-29.
 */
public class PhoneticsSymbols implements Serializable,Cloneable{

    private String objectId;
    private String ipaname; //英音国际标准（DJ）
    private String kkname; //美音国际标准（KK）
    private String soundurl;//读音
    private String videourl;//视频
    private String content;//发音方法,及相关说明
    private WordGroup wordGroup;//相关的示例单词
    private Boolean vowel;//元音

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getKkname() {
        return kkname;
    }

    public void setKkname(String kkname) {
        this.kkname = kkname;
    }

    public String getIpaname() {
        return ipaname;
    }

    public void setIpaname(String ipaname) {
        this.ipaname = ipaname;
    }

    public String getSoundurl() {
        return soundurl;
    }

    public void setSoundurl(String soundurl) {
        this.soundurl = soundurl;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getVowel() {
        return vowel;
    }

    public void setVowel(Boolean vowel) {
        this.vowel = vowel;
    }

    public WordGroup getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(WordGroup wordGroup) {
        this.wordGroup = wordGroup;
    }

    @Override
    public Object clone(){

        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String toString() {
        return "PhoneticsSymbols{" +
                "objectId='" + objectId + '\'' +
                ", ipaname='" + ipaname + '\'' +
                ", kkname='" + kkname + '\'' +
                ", soundurl='" + soundurl + '\'' +
                ", videourl='" + videourl + '\'' +
                ", content='" + content + '\'' +
                ", vowel=" + vowel +
                '}';
    }
}
