package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class GetSentence extends UseCase<Sentence,GetSentence.GetSentenceParame> {

    @Inject
    Repository repository;

    public GetSentence(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Sentence> execute(final GetSentence.GetSentenceParame getSentenceParame) {

        return Observable.create(new Observable.OnSubscribe<Sentence>() {
            @Override
            public void call(Subscriber<? super Sentence> subscriber) {
                if(getSentenceParame != null) {
                    String sentenceid = getSentenceParame.getSentenceid();
                    Sentence sentence = repository.getSentenceById(sentenceid);
                    subscriber.onNext(sentence);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static class GetSentenceParame implements UseCase.Params{
        private String sentenceid;

        public GetSentenceParame(String sentenceid){
            this.sentenceid = sentenceid;
        }

        public GetSentenceParame() {
        }

        public String getSentenceid() {
            return sentenceid;
        }

    }
}
