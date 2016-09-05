package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.List;

/**
 * Created by yanzl on 16-7-17.
 * 句子
 */
public class BmobWordCollectResult {

    private List<BmobWordCollect> results;

    public List<BmobWordCollect> getResults() {
        return results;
    }

    public void setResults(List<BmobWordCollect> results) {
        this.results = results;
    }
}
