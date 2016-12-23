package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-12-23.
 * 音标关联读音类
 */
public class PhoneticsSymbolsVoice implements Serializable,Cloneable{

    private String objectId;
    private String ipaname; //音标名称
    private String wordname; //单词名称
    private String soundurl;//读音链接

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getIpaname() {
        return ipaname;
    }

    public void setIpaname(String ipaname) {
        this.ipaname = ipaname;
    }

    public String getWordname() {
        return wordname;
    }

    public void setWordname(String wordname) {
        this.wordname = wordname;
    }

    public String getSoundurl() {
        return soundurl;
    }

    public void setSoundurl(String soundurl) {
        this.soundurl = soundurl;
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
        return "PhoneticsSymbolsVoice{" +
                "objectId='" + objectId + '\'' +
                ", ipaname='" + ipaname + '\'' +
                ", wordname='" + wordname + '\'' +
                ", soundurl='" + soundurl + '\'' +
                '}';
    }
}
