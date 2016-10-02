package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-9-29.
 */

public class PhoneticsWords implements Serializable,Cloneable{

    private String objectId;
    private String phoneticsSymbolsId; //音标Id
    private String wordgroupId;//单词分组Id


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
        return "PhoneticsWords{" +
                "objectId='" + objectId + '\'' +
                ", phoneticsSymbolsId='" + phoneticsSymbolsId + '\'' +
                ", wordgroupId='" + wordgroupId + '\'' +
                '}';
    }
}
