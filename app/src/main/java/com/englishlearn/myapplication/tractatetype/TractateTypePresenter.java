package com.englishlearn.myapplication.tractatetype;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractateTypePresenter extends TractateTypeContract.Presenter{

    private TractateTypeContract.View mView;
    public TractateTypePresenter(TractateTypeContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
