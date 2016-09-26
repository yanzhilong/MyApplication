package com.englishlearn.myapplication.testmain;


/**
 * Created by yanzl on 16-7-20.
 */
public class TestMainPresenter extends TestMainContract.Presenter{

    private TestMainContract.View mainView;
    public TestMainPresenter(TestMainContract.View vew){
        mainView = vew;
        mainView.setPresenter(this);
    }


}
