package com.englishlearn.myapplication.main;


/**
 * Created by yanzl on 16-7-20.
 */
public class MainPresenter extends MainContract.Presenter{

    private MainContract.View mainView;
    public MainPresenter(MainContract.View vew){
        mainView = vew;
        mainView.setPresenter(this);
    }


}
