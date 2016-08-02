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
public class AddGrammar extends UseCase<Boolean,AddGrammar.AddGrammarsParame> {

    @Inject
    Repository repository;

    public AddGrammar(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final AddGrammar.AddGrammarsParame addGrammarsParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(addGrammarsParame == null || addGrammarsParame.getGrammar() == null){
                    subscriber.onError(new Exception());
                }
                Grammar grammar = addGrammarsParame.getGrammar();
                try {
                    repository.addGrammar(grammar);
                } catch (BmobException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    public static class AddGrammarsParame implements UseCase.Params{
        private Grammar grammar;

        public AddGrammarsParame(Grammar grammar){
            this.grammar = grammar;
        }

        public AddGrammarsParame() {
        }

        public Grammar getGrammar() {
            return grammar;
        }

        public void setGrammar(Grammar grammar) {
            this.grammar = grammar;
        }
    }

}
