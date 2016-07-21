package com.englishlearn.myapplication.addeditsentence;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.AddSentences;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditSentencePresenter extends AddEditSentenceContract.Presenter{

    private AddSentences addSentences;
    private AddEditSentenceContract.View mainView;
    @Inject
    Repository repository;
    public AddEditSentencePresenter(AddEditSentenceContract.View vew){
        mainView = vew;
        addSentences = new AddSentences();
        mainView.setPresenter(this);
    }

    @Override
    void saveSentence(String content, String translate) {
        Sentence sentence = new Sentence();
        sentence.setContent(content);
        sentence.setTtranslation(translate);
        AddSentences.AddSentencesParame addSentencesParame = new AddSentences.AddSentencesParame(sentence);
        addSentences.excuteIo(addSentencesParame).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                mainView.addSentencesSuccess();
            }
        });
    }
}
