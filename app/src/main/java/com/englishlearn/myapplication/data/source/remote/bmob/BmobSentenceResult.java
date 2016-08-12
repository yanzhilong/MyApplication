package com.englishlearn.myapplication.data.source.remote.bmob;

import java.util.List;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobSentenceResult {

    private List<BmobSentence> results;

    public List<BmobSentence> getResults() {
        return results;
    }

    public void setResults(List<BmobSentence> results) {
        this.results = results;
    }
}
