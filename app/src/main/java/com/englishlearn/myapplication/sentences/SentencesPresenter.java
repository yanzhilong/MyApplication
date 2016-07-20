package com.englishlearn.myapplication.sentences;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.GetSentences;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesPresenter extends SentencesContract.Presenter{

    private GetSentences getSentences;
    private SentencesContract.View mainView;
    @Inject
    Repository repository;
    public SentencesPresenter(SentencesContract.View vew){
        mainView = vew;
        getSentences = new GetSentences();
        mainView.setPresenter(this);
    }

    @Override
    void getSentences() {
        getSentences(null);
    }

    @Override
    void getSentences(String searchword) {
        GetSentences.GetSentencesParame getSentencesParame = new GetSentences.GetSentencesParame(searchword);
        getSentences.excuteIo(searchword != null ? getSentencesParame : null).subscribe(new Subscriber<List<Sentence>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.emptySentences();
            }

            @Override
            public void onNext(List<Sentence> sentences) {
                if(sentences != null && sentences.size() > 0){
                    mainView.showSentences(sentences);
                }else{
                    mainView.emptySentences();
                }
            }
        });
    }

    @Override
    void addSentence() {
        mainView.showaddSentence();
    }
}
