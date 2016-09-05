package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-5.
 */
public class BmobCreateMsSourceRequest {

    private String msSourceId;
    private String name;

    public BmobCreateMsSourceRequest() {
        this.msSourceId = UUID.randomUUID().toString();
    }

    public String getMsSourceId() {
        return msSourceId;
    }

    public void setMsSourceId(String msSourceId) {
        this.msSourceId = msSourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
