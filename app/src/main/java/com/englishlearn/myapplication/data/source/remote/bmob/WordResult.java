package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.Word;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class WordResult {

    private List<Word> results;

    public List<Word> getResults() {
        return results;
    }

    public void setResults(List<Word> results) {
        this.results = results;
    }
}
