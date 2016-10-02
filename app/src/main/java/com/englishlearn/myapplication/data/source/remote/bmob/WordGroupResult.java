package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.WordGroup;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class WordGroupResult {

    private List<WordGroup> results;

    public List<WordGroup> getResults() {
        return results;
    }

    public void setResults(List<WordGroup> results) {
        this.results = results;
    }
}
