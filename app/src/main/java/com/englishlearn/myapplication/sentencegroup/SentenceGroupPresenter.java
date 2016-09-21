package com.englishlearn.myapplication.sentencegroup;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceGroupPresenter extends SentenceGroupContract.Presenter{

    private SentenceGroupContract.View mView;
    public SentenceGroupPresenter(SentenceGroupContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
