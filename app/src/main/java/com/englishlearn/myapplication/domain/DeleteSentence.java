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
public class DeleteSentence extends UseCase<Boolean,DeleteSentence.DeleteSentenceParame> {

    @Inject
    Repository repository;

    public DeleteSentence(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final DeleteSentence.DeleteSentenceParame deleteSentenceParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(deleteSentenceParame == null || deleteSentenceParame.getSentence() == null){
                    subscriber.onError(new NullPointerException());
                }
                Sentence sentence = deleteSentenceParame.getSentence();
                String sentenceid = sentence.getSentenceid();
                String id = sentence.getId();
                boolean result = false;
                if(id != null){
                        result = repository.deleteSentenceById(id);
                }else if(sentenceid != null){
                    result = repository.deleteSentence(sentenceid);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void cancelRequest(RequestParam requestParam) {

    }

    public static class DeleteSentenceParame implements UseCase.Params{
        private Sentence sentence;

        public DeleteSentenceParame(Sentence sentence){
            this.sentence = sentence;
        }

        public DeleteSentenceParame() {
        }

        public Sentence getSentence() {
            return sentence;
        }

        public void setSentence(Sentence sentence) {
            this.sentence = sentence;
        }
    }

}
