package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestParam;

import java.util.List;

import javax.inject.Inject;

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
                        grammars = repository.getGrammars();
                }
                subscriber.onNext(grammars);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void cancelRequest(RequestParam requestParam) {

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
