package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestParam;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class GetSentences extends UseCase<List<Sentence>,GetSentences.GetSentencesParame> {

    @Inject
    Repository repository;

    public GetSentences(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<List<Sentence>> execute(final GetSentences.GetSentencesParame getSentencesParame) {

        return Observable.create(new Observable.OnSubscribe<List<Sentence>>() {
            @Override
            public void call(Subscriber<? super List<Sentence>> subscriber) {
                List<Sentence> sentences = null;
                //判断是否有关键词
                if(getSentencesParame != null){
                    String searchword = getSentencesParame.getSearchword();
                    sentences = repository.getSentences(searchword);
                }else{
                      sentences = repository.getSentences();
                }
                subscriber.onNext(sentences);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void cancelRequest(RequestParam requestParam) {

    }

    public static class GetSentencesParame implements UseCase.Params{
        private String searchword;

        public GetSentencesParame(String searchword){
            this.searchword = searchword;
        }

        public GetSentencesParame() {
        }

        public String getSearchword() {
            return searchword;
        }

        public void setSearchword(String searchword) {
            this.searchword = searchword;
        }
    }

}
