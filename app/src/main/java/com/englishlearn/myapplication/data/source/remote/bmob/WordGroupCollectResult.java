package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.WordGroupCollect;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class WordGroupCollectResult {

    private List<WordGroupCollect> results;

    public List<WordGroupCollect> getResults() {
        return results;
    }

    public void setResults(List<WordGroupCollect> results) {
        this.results = results;
    }
}
