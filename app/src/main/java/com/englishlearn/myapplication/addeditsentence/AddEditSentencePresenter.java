package com.englishlearn.myapplication.addeditsentence;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.GetSentences;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditSentencePresenter extends AddEditSentenceContract.Presenter{

    private AddEditSentenceContract.View mainView;
    @Inject
    Repository repository;
    public AddEditSentencePresenter(AddEditSentenceContract.View vew){
        mainView = vew;
        mainView.setPresenter(this);
    }

    @Override
    void saveSentence(String content, String translate) {

    }
}
