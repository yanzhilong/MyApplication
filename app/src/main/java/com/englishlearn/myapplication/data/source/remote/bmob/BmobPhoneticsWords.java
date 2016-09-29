package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-29.
 */

public class BmobPhoneticsWords {

    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String phoneticswordsId;
    private String phoneticsSymbolsId; //音标Id
    private String wordgroupId;//单词分组Id


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

    public BmobPhoneticsWords() {

    }

    public String getPhoneticswordsId() {
        return phoneticswordsId;
    }

    public void setPhoneticswordsId(String phoneticswordsId) {
        this.phoneticswordsId = phoneticswordsId;
    }

    public String getPhoneticsSymbolsId() {
        return phoneticsSymbolsId;
    }

    public void setPhoneticsSymbolsId(String phoneticsSymbolsId) {
        this.phoneticsSymbolsId = phoneticsSymbolsId;
    }

    public String getWordgroupId() {
        return wordgroupId;
    }

    public void setWordgroupId(String wordgroupId) {
        this.wordgroupId = wordgroupId;
    }
}
