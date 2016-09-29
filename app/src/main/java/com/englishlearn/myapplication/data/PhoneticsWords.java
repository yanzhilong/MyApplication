package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-29.
 */

public class PhoneticsWords {

    private String id;
    private String phoneticswordsId;
    private String phoneticsSymbolsId; //音标Id
    private String wordgroupId;//单词分组Id

    public PhoneticsWords() {
        phoneticswordsId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "PhoneticsWords{" +
                "id='" + id + '\'' +
                ", phoneticswordsId='" + phoneticswordsId + '\'' +
                ", phoneticsSymbolsId='" + phoneticsSymbolsId + '\'' +
                ", wordgroupId='" + wordgroupId + '\'' +
                '}';
    }
}
