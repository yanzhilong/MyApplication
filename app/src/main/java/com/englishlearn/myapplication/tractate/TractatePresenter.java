package com.englishlearn.myapplication.tractate;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractatePresenter extends TractateContract.Presenter{

    private TractateContract.View mView;
    public TractatePresenter(TractateContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
