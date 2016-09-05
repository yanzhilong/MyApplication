package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobCreateTractateGroupRequest {

    private String tractategroupId;
    private String name; //分组名称
    private String userId; //用户Id

    public BmobCreateTractateGroupRequest() {
        tractategroupId = UUID.randomUUID().toString();
    }

    public String getTractategroupId() {
        return tractategroupId;
    }

    public void setTractategroupId(String tractategroupId) {
        this.tractategroupId = tractategroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
