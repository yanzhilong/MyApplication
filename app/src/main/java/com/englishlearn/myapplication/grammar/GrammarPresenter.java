package com.englishlearn.myapplication.grammar;


/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarPresenter extends GrammarContract.Presenter{

    private GrammarContract.View mView;
    public GrammarPresenter(GrammarContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
