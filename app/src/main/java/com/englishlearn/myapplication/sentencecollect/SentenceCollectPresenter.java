package com.englishlearn.myapplication.sentencecollect;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceCollectPresenter extends SentenceCollectContract.Presenter{

    private SentenceCollectContract.View mView;
    public SentenceCollectPresenter(SentenceCollectContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
