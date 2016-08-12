package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestParam;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class UpdateSentence extends UseCase<Boolean,UpdateSentence.UpdateSentenceParame> {

    @Inject
    Repository repository;

    public UpdateSentence(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final UpdateSentence.UpdateSentenceParame updateSentenceParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(updateSentenceParame == null || updateSentenceParame.getSentence() == null){
                    subscriber.onError(new NullPointerException());
                }
                Sentence sentence = updateSentenceParame.getSentence();
                boolean result = false;
                    result = repository.updateSentence(sentence);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void cancelRequest(RequestParam requestParam) {

    }

    public static class UpdateSentenceParame implements UseCase.Params{
        private Sentence sentence;

        public UpdateSentenceParame(Sentence sentence){
            this.sentence = sentence;
        }

        public UpdateSentenceParame() {
        }

        public Sentence getSentence() {
            return sentence;
        }

        public void setSentence(Sentence sentence) {
            this.sentence = sentence;
        }
    }

}
