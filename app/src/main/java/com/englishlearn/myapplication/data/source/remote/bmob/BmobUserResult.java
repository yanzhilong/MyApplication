package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobUserResult {

    private List<BmobUser> results;

    public List<BmobUser> getResults() {
        return results;
    }

    public void setResults(List<BmobUser> results) {
        this.results = results;
    }
}
