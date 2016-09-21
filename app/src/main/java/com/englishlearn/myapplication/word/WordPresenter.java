package com.englishlearn.myapplication.word;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordPresenter extends WordContract.Presenter{

    private WordContract.View mView;
    public WordPresenter(WordContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
