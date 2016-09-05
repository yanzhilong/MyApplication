package com.englishlearn.myapplication.data;

import java.util.UUID;

/**
 * Created by yanzl on 16-9-3.
 */
public class WordGroup {

    private String id;
    private String wordgroupId;
    private String name; //分组名称
    private String userId; //用户Id

    public WordGroup() {
        wordgroupId = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordgroupId() {
        return wordgroupId;
    }

    public void setWordgroupId(String wordgroupId) {
        this.wordgroupId = wordgroupId;
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
