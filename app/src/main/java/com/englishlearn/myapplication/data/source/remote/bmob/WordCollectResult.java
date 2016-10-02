package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.WordCollect;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class WordCollectResult {

    private List<WordCollect> results;

    public List<WordCollect> getResults() {
        return results;
    }

    public void setResults(List<WordCollect> results) {
        this.results = results;
    }
}
