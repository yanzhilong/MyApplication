package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.SentenceGroupCollect;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class SentenceGroupCollectResult {

    private List<SentenceGroupCollect> results;

    public List<SentenceGroupCollect> getResults() {
        return results;
    }

    public void setResults(List<SentenceGroupCollect> results) {
        this.results = results;
    }
}
