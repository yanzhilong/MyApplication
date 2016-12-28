package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * 音标类
 * Created by yanzl on 16-9-29.
 */
public class PhoneticsSymbols implements Serializable,Cloneable{

    //标记一对多关系
    private String __type;
    private String className;

    private String objectId;
    private String ipaname; //英音国际标准（DJ）
    private String kkname; //美音国际标准（KK）
    private String alias; //别名
    private String content; //发音方法,及相关说明
    private String type; //音标类型:元音,单元音，前元音;辅音,爆破音,清辅音;

    public void setPointer(){
        __type = "Pointer";
        className = "PhoneticsSymbols";
    }

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

    public String getKkname() {
        return kkname;
    }

    public void setKkname(String kkname) {
        this.kkname = kkname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", alias='" + alias + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
