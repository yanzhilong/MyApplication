package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.PhoneticsWords;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class PhoneticsWordsResult {

    private List<PhoneticsWords> results;

    public List<PhoneticsWords> getResults() {
        return results;
    }

    public void setResults(List<PhoneticsWords> results) {
        this.results = results;
    }
}
