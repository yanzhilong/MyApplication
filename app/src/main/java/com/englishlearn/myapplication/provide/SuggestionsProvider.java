package com.englishlearn.myapplication.provide;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by yanzl on 16-8-17.
 */
public class SuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = SuggestionsProvider.class.getName();
    public final static int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES
            | SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;//为每个建议项提供两行文本

    public SuggestionsProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }
}
