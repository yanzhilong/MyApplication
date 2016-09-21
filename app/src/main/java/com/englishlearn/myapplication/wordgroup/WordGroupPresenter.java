package com.englishlearn.myapplication.wordgroup;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordGroupPresenter extends WordGroupContract.Presenter{

    private WordGroupContract.View mView;
    public WordGroupPresenter(WordGroupContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
