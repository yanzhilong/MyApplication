package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.exception.BmobException;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class GetGrammars extends UseCase<List<Grammar>,GetGrammars.GetGrammarsParame> {

    @Inject
    Repository repository;

    public GetGrammars(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<List<Grammar>> execute(final GetGrammars.GetGrammarsParame getGrammarsParame) {

        return Observable.create(new Observable.OnSubscribe<List<Grammar>>() {
            @Override
            public void call(Subscriber<? super List<Grammar>> subscriber) {
                List<Grammar> grammars = null;
                //判断是否有关键词
                if(getGrammarsParame != null){
                    String searchword = getGrammarsParame.getSearchword();
                    grammars = repository.getGrammars(searchword);
                }else{
                    try {
                        grammars = repository.getGrammars();
                    } catch (BmobException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
                subscriber.onNext(grammars);
                subscriber.onCompleted();
            }
        });
    }

    public static class GetGrammarsParame implements UseCase.Params{
        private String searchword;

        public GetGrammarsParame(String searchword){
            this.searchword = searchword;
        }

        public GetGrammarsParame() {
        }

        public String getSearchword() {
            return searchword;
        }

        public void setSearchword(String searchword) {
            this.searchword = searchword;
        }
    }

}
