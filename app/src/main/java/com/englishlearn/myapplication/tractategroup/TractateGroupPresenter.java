package com.englishlearn.myapplication.tractategroup;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractateGroupPresenter extends TractateGroupContract.Presenter{

    private TractateGroupContract.View mView;
    public TractateGroupPresenter(TractateGroupContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
