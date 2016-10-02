package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.PhoneticsSymbols;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class PhoneticsSymbolsResult {

    private List<PhoneticsSymbols> results;

    public List<PhoneticsSymbols> getResults() {
        return results;
    }

    public void setResults(List<PhoneticsSymbols> results) {
        this.results = results;
    }
}
