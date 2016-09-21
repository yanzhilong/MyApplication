package com.englishlearn.myapplication.wordgroupcollect;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordGroupCollectPresenter extends WordGroupCollectContract.Presenter{

    private WordGroupCollectContract.View mView;
    public WordGroupCollectPresenter(WordGroupCollectContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
