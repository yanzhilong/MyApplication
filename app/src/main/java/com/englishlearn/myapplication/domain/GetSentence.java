package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
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
                    String id = getSentenceParame.getId();
                    Sentence sentence = null;
                    if(id != null){
                        try {
                            sentence = repository.getSentenceById(id);
                        } catch (BmobException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }else if(sentenceid != null){
                        try {
                            sentence = repository.getSentenceBySentenceId(sentenceid);
                        } catch (BmobException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                    subscriber.onNext(sentence);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static class GetSentenceParame implements UseCase.Params{
        private String id;
        private String sentenceid;

        public GetSentenceParame(String id, String sentenceid) {
            this.id = id;
            this.sentenceid = sentenceid;
        }

        public GetSentenceParame() {
        }

        public String getId() {
            return id;
        }

        public String getSentenceid() {
            return sentenceid;
        }

    }
}
