package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.SentenceCollect;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class SentenceCollectResult {

    private List<SentenceCollect> results;

    public List<SentenceCollect> getResults() {
        return results;
    }

    public void setResults(List<SentenceCollect> results) {
        this.results = results;
    }
}
