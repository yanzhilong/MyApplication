package com.englishlearn.myapplication.sentences;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesPresenter extends SentencesContract.Presenter{

    private SentencesContract.View mainView;
    public SentencesPresenter(SentencesContract.View vew){
        mainView = vew;
        mainView.setPresenter(this);
    }


    @Override
    void getSentences() {

    }

    @Override
    void getSentences(String searchword) {

    }
}
