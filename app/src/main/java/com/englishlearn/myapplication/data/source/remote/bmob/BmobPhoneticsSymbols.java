package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * 音标类
 * Created by yanzl on 16-9-29.
 */
public class BmobPhoneticsSymbols {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String phoneticsSymbolsId;//唯一Id
    private String ipaname; //国际音标（IPA）美英
    private String kkname; //国际音标（KK）英音
    private String others;//其它相关的,用"|"分割
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPhoneticsSymbolsId() {
        return phoneticsSymbolsId;
    }

    public void setPhoneticsSymbolsId(String phoneticsSymbolsId) {
        this.phoneticsSymbolsId = phoneticsSymbolsId;
    }

    public String getIpaname() {
        return ipaname;
    }

    public void setIpaname(String ipaname) {
        this.ipaname = ipaname;
    }

    public String getKkname() {
        return kkname;
    }

    public void setKkname(String kkname) {
        this.kkname = kkname;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
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
}
