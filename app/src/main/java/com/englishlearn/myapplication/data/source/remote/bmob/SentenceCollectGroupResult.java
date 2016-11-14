package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.SentenceCollectGroup;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class SentenceCollectGroupResult {

    private List<SentenceCollectGroup> results;

    public List<SentenceCollectGroup> getResults() {
        return results;
    }

    public void setResults(List<SentenceCollectGroup> results) {
        this.results = results;
    }
}
