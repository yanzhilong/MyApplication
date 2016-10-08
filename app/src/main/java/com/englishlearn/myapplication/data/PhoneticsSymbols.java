package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * 音标类
 * Created by yanzl on 16-9-29.
 */
public class PhoneticsSymbols implements Serializable,Cloneable{

    private String objectId;
    //国际音标（IPA）
    private String ipaname; //国际音标（DJ）英音
    private String kkname; //国际音标（KK）美音
    private String soundurl;//读音
    private String videourl;//视频
    private String content;//发音方法,及相关说明
    private int isvowel;// 1 0

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

    public int getIsvowel() {
        return isvowel;
    }

    public void setIsvowel(int isvowel) {
        this.isvowel = isvowel;
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
}
