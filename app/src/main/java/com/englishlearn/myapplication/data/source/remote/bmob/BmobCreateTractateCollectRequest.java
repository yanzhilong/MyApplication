package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-3.
 */
public class BmobCreateTractateCollectRequest {

    private String tractatecollectId;//
    private String userId; //用户Id
    private String createDate; //创建时间
    private String tractategroupId; //分组Id
    private String tractateId; //(文章Id)

    public String getTractatecollectId() {
        return tractatecollectId;
    }

    public void setTractatecollectId(String tractatecollectId) {
        this.tractatecollectId = tractatecollectId;
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
