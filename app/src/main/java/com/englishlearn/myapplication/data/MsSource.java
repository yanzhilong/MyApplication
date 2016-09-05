package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-5.
 */
public class MsSource {

    private String id;
    private String msSourceId;
    private String name;

    public MsSource() {
        this.msSourceId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
