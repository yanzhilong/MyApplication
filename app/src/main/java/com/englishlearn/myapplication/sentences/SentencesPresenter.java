package com.englishlearn.myapplication.sentences;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.domain.DeleteSentence;
import com.englishlearn.myapplication.domain.GetSentences;

import java.util.List;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesPresenter extends SentencesContract.Presenter{

    private GetSentences getSentences;
    private SentencesContract.View mainView;
    private DeleteSentence deleteSentence;
    public SentencesPresenter(SentencesContract.View vew){
        mainView = vew;
        getSentences = new GetSentences();
        deleteSentence = new DeleteSentence();
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

    @Override
    void deleteSentences(final List<Sentence> sentences) {
        final int[] success = {0};
        final int[] fail = {0};
        for(Sentence sentence : sentences){
            deleteSentence.excuteIo(new DeleteSentence.DeleteSentenceParame(sentence)).subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    fail[0]++;
                    checkoutDeleteResult(sentences.size(),success[0],fail[0]);
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if(aBoolean){
                        success[0]++;
                    }else{
                        fail[0]++;
                    }
                    checkoutDeleteResult(sentences.size(),success[0],fail[0]);
                }
            });
        }
    }

    private void checkoutDeleteResult(int sum,int success,int fail){
        if((success + fail) == sum){
            mainView.showDeleteResult(success,fail);
        }
    }
}
