package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * 音标类
 * Created by yanzl on 16-9-29.
 */
public class PhoneticsSymbols {

    private String id;
    private String phoneticsSymbolsId;//唯一Id
    private String ipaname; //国际音标（IPA）美英
    private String kkname; //国际音标（KK）英音
    private String soundurl;//读音
    private String videourl;//视频
    private String content;//发音方法,及相关说明
    private int isvowel;// 1 0

    public PhoneticsSymbols() {
        phoneticsSymbolsId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneticsSymbolsId() {
        return phoneticsSymbolsId;
    }

    public void setPhoneticsSymbolsId(String phoneticsSymbolsId) {
        this.phoneticsSymbolsId = phoneticsSymbolsId;
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
    public String toString() {
        return "PhoneticsSymbols{" +
                "id='" + id + '\'' +
                ", phoneticsSymbolsId='" + phoneticsSymbolsId + '\'' +
                ", ipaname='" + ipaname + '\'' +
                ", kkname='" + kkname + '\'' +
                ", soundurl='" + soundurl + '\'' +
                ", videourl='" + videourl + '\'' +
                ", content='" + content + '\'' +
                ", isvowel=" + isvowel +
                '}';
    }
}
