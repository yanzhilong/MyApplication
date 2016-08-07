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
public class DeleteGrammar extends UseCase<Boolean,DeleteGrammar.DeleteGrammarParame> {

    @Inject
    Repository repository;

    public DeleteGrammar(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final DeleteGrammar.DeleteGrammarParame deleteGrammarParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(deleteGrammarParame == null || deleteGrammarParame.getGrammar() == null){
                    subscriber.onError(new NullPointerException());
                }
                Grammar grammar = deleteGrammarParame.grammar;
                String grammarid = grammar.getGrammarid();
                String id = grammar.getId();
                boolean result = false;
                if(id != null){
                    try {
                        result = repository.deleteGrammarById(id);
                    } catch (BmobException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }else if(grammarid != null){
                    result = repository.deleteGrammar(grammarid);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

    public static class DeleteGrammarParame implements UseCase.Params{
        private Grammar grammar;

        public DeleteGrammarParame(Grammar grammar){
            this.grammar = grammar;
        }

        public DeleteGrammarParame() {
        }

        public Grammar getGrammar() {
            return grammar;
        }

        public void setGrammar(Grammar grammar) {
            this.grammar = grammar;
        }
    }

}
