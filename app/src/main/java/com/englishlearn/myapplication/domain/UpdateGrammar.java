package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class UpdateGrammar extends UseCase<Boolean,UpdateGrammar.UpdateGrammarParame> {

    @Inject
    Repository repository;

    public UpdateGrammar(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final UpdateGrammar.UpdateGrammarParame updateGrammarParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(updateGrammarParame == null || updateGrammarParame.getGrammar() == null){
                    subscriber.onError(new NullPointerException());
                }
                Grammar grammar = updateGrammarParame.getGrammar();
                boolean result = false;
                try {
                    result = repository.updateGrammar(grammar);
                } catch (BmobException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

    public static class UpdateGrammarParame implements UseCase.Params{
        private Grammar grammar;

        public UpdateGrammarParame(Grammar grammar){
            this.grammar = grammar;
        }

        public UpdateGrammarParame() {
        }

        public Grammar getGrammar() {
            return grammar;
        }

        public void setGrammar(Grammar grammar) {
            this.grammar = grammar;
        }
    }

}
