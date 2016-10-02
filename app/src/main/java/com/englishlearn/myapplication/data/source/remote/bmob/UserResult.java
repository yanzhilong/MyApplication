package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.User;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class UserResult {

    private List<User> results;

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }
}
