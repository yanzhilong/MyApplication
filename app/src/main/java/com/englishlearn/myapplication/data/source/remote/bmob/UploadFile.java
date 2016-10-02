package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-10-2.
 */

public class UploadFile {

    private String filename;
    private String url;
    private String cdn;

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
