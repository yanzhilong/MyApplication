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
public class AddSentence extends UseCase<Boolean,AddSentence.AddSentencesParame> {

    @Inject
    Repository repository;

    public AddSentence(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final AddSentence.AddSentencesParame addSentencesParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(addSentencesParame == null || addSentencesParame.getSentence() == null){
                    subscriber.onError(new Exception());
                }
                Sentence sentence = addSentencesParame.getSentence();
                repository.addSentence(sentence);
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    public static class AddSentencesParame implements UseCase.Params{
        private Sentence sentence;

        public AddSentencesParame(Sentence sentence){
            this.sentence = sentence;
        }

        public AddSentencesParame() {
        }

        public Sentence getSentence() {
            return sentence;
        }

        public void setSentence(Sentence sentence) {
            this.sentence = sentence;
        }
    }

}
