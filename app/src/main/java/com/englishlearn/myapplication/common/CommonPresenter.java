package com.englishlearn.myapplication.common;


/**
 * Created by yanzl on 16-7-20.
 */
public class CommonPresenter extends CommonContract.Presenter{

    private CommonContract.View mView;
    public CommonPresenter(CommonContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

}
