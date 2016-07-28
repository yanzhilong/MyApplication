package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class GetGrammar extends UseCase<Grammar,GetGrammar.GetGrammarParame> {

    @Inject
    Repository repository;

    public GetGrammar(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Grammar> execute(final GetGrammar.GetGrammarParame getGrammarParame) {

        return Observable.create(new Observable.OnSubscribe<Grammar>() {
            @Override
            public void call(Subscriber<? super Grammar> subscriber) {
                if(getGrammarParame != null) {
                    String grammarid = getGrammarParame.getGrammarid();
                    Grammar grammar = repository.getGrammarById(grammarid);
                    subscriber.onNext(grammar);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static class GetGrammarParame implements UseCase.Params{
        private String grammarid;

        public GetGrammarParame(String grammarid){
            this.grammarid = grammarid;
        }

        public GetGrammarParame() {
        }

        public String getGrammarid() {
            return grammarid;
        }

        public void setGrammarid(String grammarid) {
            this.grammarid = grammarid;
        }
    }

}
