package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class TractateCollect {

    private String id;
    private String userId; //用户Id
    private String createDate; //创建时间
    private String tractategroupId; //分组Id
    private String tractateId; //(句子，单词，文章)

    public TractateCollect() {
        this.userId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTractategroupId() {
        return tractategroupId;
    }

    public void setTractategroupId(String tractategroupId) {
        this.tractategroupId = tractategroupId;
    }

    public String getTractateId() {
        return tractateId;
    }

    public void setTractateId(String tractateId) {
        this.tractateId = tractateId;
    }
}
