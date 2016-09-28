package com.englishlearn.myapplication.intermediate;


/**
 * Created by yanzl on 16-7-20.
 */
public class IntermediatePresenter extends IntermediateContract.Presenter{

    private IntermediateContract.View mView;
    public IntermediatePresenter(IntermediateContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
