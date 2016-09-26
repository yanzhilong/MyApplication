package com.englishlearn.myapplication.home;


/**
 * Created by yanzl on 16-7-20.
 */
public class HomePresenter extends HomeContract.Presenter{

    private HomeContract.View mView;
    public HomePresenter(HomeContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
