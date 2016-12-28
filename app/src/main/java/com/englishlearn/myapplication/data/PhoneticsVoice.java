package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-12-23.
 * 音标关联读音类
 */
public class PhoneticsVoice implements Serializable,Cloneable{

    private String objectId;
    private String name; //单词名称
    private BmobFile file;//读音链接
    private boolean isSymbols;//是否是音标读音
    private String remark;//备注
    private PhoneticsSymbols phoneticsSymbols;//音标

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public boolean isSymbols() {
        return isSymbols;
    }

    public void setSymbols(boolean symbols) {
        isSymbols = symbols;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PhoneticsSymbols getPhoneticsSymbols() {
        return phoneticsSymbols;
    }

    public void setPhoneticsSymbols(PhoneticsSymbols phoneticsSymbols) {
        this.phoneticsSymbols = phoneticsSymbols;
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
        return "PhoneticsVoice{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", file=" + file +
                ", isSymbols=" + isSymbols +
                ", remark='" + remark + '\'' +
                ", phoneticsSymbols=" + phoneticsSymbols +
                '}';
    }
}
