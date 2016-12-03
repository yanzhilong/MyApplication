package com.englishlearn.myapplication.data;

/**
 * Created by yanzl on 16-10-2.
 */

public class BmobFile {

    //标记一对多关系
    private String __type;
    private String group;

    private String filename;
    private String url;
    private String cdn;

    public void setPointer(){
        __type = "File";
        group = "upyun";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }
}
