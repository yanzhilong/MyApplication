package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-29.
 */

public class BmobCreatePhoneticsWordsRequest {

    private String phoneticswordsId;
    private String phoneticsSymbolsId; //音标Id
    private String wordgroupId;//单词分组Id



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
