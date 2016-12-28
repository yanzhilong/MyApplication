package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.PhoneticsVoice;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class PhoneticsSymbolsVoicesResult {

    private List<PhoneticsVoice> results;

    public List<PhoneticsVoice> getResults() {
        return results;
    }

    public void setResults(List<PhoneticsVoice> results) {
        this.results = results;
    }
}
