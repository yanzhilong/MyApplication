package com.englishlearn.myapplication.wordcollect;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordCollectPresenter extends WordCollectContract.Presenter{

    private WordCollectContract.View mView;
    public WordCollectPresenter(WordCollectContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
