package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-11-29.
 * 离线词典实体类
 */

public class Dict implements Serializable,Cloneable {

    private String objectId;
    private String name; //标题
    private String content;//说明
    private String size; //
    private int version;//版本号，用于更新词典
    private String remark; //备注
    private BmobFile file;//文件

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "Dict{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", size='" + size + '\'' +
                ", version=" + version +
                ", remark='" + remark + '\'' +
                ", file=" + file +
                '}';
    }
}
