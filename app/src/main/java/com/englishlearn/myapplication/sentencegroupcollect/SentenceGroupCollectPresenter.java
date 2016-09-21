package com.englishlearn.myapplication.sentencegroupcollect;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceGroupCollectPresenter extends SentenceGroupCollectContract.Presenter{

    private SentenceGroupCollectContract.View mView;
    public SentenceGroupCollectPresenter(SentenceGroupCollectContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
