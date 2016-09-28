package com.englishlearn.myapplication.advanced;


/**
 * Created by yanzl on 16-7-20.
 */
public class AdvancedPresenter extends AdvancedContract.Presenter{

    private AdvancedContract.View mView;
    public AdvancedPresenter(AdvancedContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
