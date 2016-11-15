package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-11-15.
 * 指操作类
 */
public class BatchResult {

    private SuccessResult success;
    private BmobDefaultError error;

    public SuccessResult getSuccess() {
        return success;
    }

    public void setSuccess(SuccessResult success) {
        this.success = success;
    }

    public BmobDefaultError getError() {
        return error;
    }

    public void setError(BmobDefaultError error) {
        this.error = error;
    }

    public static class SuccessResult{
        private String createdAt;
        private String objectId;
        private String msg;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
