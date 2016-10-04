package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class SentenceResult {

    private List<Sentence> results;

    public List<Sentence> getResults() {
        return results;
    }

    public void setResults(List<Sentence> results) {
        this.results = results;
    }
}
