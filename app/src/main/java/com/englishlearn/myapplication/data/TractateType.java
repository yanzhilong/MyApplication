package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 * 信息分类表
 */
public class TractateType {

    private String id;
    private String tractatetypeId;
    private String name;

    public TractateType() {
        tractatetypeId = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTractatetypeId() {
        return tractatetypeId;
    }

    public void setTractatetypeId(String tractatetypeId) {
        this.tractatetypeId = tractatetypeId;
    }
}
