package com.englishlearn.myapplication.searchresults;


import javax.inject.Inject;

/**
 * Created by yanzl on 16-7-20.
 */
public class SearchResultsPresenter extends SearchResultsContract.Presenter{

    private SearchResultsContract.View mView;
    @Inject
    public SearchResultsPresenter(SearchResultsContract.View vew, String id, String sentenceid){
        mView = vew;
        mView.setPresenter(this);
    }


}
