package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 * 信息分类表
 */
public class BmobCreateTractateTypeRequest {

    private String tractatetypeId;
    private String name;

    public BmobCreateTractateTypeRequest() {
        tractatetypeId = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTractatetypeId() {
        return tractatetypeId;
    }

    public void setTractatetypeId(String tractatetypeId) {
        this.tractatetypeId = tractatetypeId;
    }
}
