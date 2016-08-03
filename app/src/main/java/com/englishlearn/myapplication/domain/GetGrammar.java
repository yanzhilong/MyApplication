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
                    String id = getGrammarParame.getId();
                    Grammar grammar = null;
                    if (id != null) {
                        try {
                            grammar = repository.getGrammarById(id);
                        } catch (BmobException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }else if(grammarid != null){
                        try {
                            grammar = repository.getGrammarByGrammarId(grammarid);
                        } catch (BmobException e) {
                            e.printStackTrace();
                            subscriber.onError(e);
                        }
                    }
                    subscriber.onNext(grammar);
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static class GetGrammarParame implements UseCase.Params{

        private String id;
        private String grammarid;

        public GetGrammarParame(String id, String grammarid) {
            this.id = id;
            this.grammarid = grammarid;
        }

        public GetGrammarParame() {
        }

        public String getId() {
            return id;
        }

        public String getGrammarid() {
            return grammarid;
        }

        public void setGrammarid(String grammarid) {
            this.grammarid = grammarid;
        }
    }

}
