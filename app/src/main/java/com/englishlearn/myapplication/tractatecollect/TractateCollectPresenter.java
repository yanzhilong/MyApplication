package com.englishlearn.myapplication.tractatecollect;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractateCollectPresenter extends TractateCollectContract.Presenter{

    private TractateCollectContract.View mView;
    public TractateCollectPresenter(TractateCollectContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
