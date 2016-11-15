package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.List;

/**
 * Created by yanzl on 16-11-15.
 * 指操作类
 */
public class BatchRequest {


    private List<BatchRequestChild> requests;//存储多个数据类

    public List<BatchRequestChild> getRequests() {
        return requests;
    }

    public void setRequests(List<BatchRequestChild> requests) {
        this.requests = requests;
    }

    public static class BatchRequestChild<T>{
        private String method;
        private String path;
        private String token;
        private T body;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }
    }
}
