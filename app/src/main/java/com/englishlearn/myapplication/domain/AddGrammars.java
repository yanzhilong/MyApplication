package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddGrammars extends UseCase<Boolean,AddGrammars.AddGrammarsParame> {

    @Inject
    Repository repository;

    public AddGrammars(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<Boolean> execute(final AddGrammars.AddGrammarsParame addGrammarsParame) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if(addGrammarsParame == null || addGrammarsParame.getGrammar() == null){
                    subscriber.onError(new Exception());
                }
                Grammar grammar = addGrammarsParame.getGrammar();
                repository.addGrammar(grammar);
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
