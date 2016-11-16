package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.SentenceGroup;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class SentenceCollectPointerResult {

    private List<SentenceGroup> results;

    public List<SentenceGroup> getResults() {
        return results;
    }

    public void setResults(List<SentenceGroup> results) {
        this.results = results;
    }
}
