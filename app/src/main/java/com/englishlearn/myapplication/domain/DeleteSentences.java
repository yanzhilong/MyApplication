package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yanzl on 16-7-20.
 */
public class DeleteSentences extends UseCase<DeleteSentences.DeleteSentensResult,DeleteSentences.DeleteSentencesParame> {

    @Inject
    Repository repository;

    public DeleteSentences(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<DeleteSentensResult> execute(final DeleteSentences.DeleteSentencesParame deleteSentencesParame) {

        return Observable.from(deleteSentencesParame.getSentences().toArray()).flatMap(new Func1<Object, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Object o) {
                Sentence sentence = (Sentence) o;
                return repository.deleteSentenceRxById(sentence.getId());
            }
        })
                .onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return false;
                    }
                })
                .toList().flatMap(new Func1<List<Boolean>, Observable<DeleteSentensResult>>() {
            @Override
            public Observable<DeleteSentensResult> call(List<Boolean> booleen) {

                int successcount = 0;
                int failcount = 0;
                for(Boolean b:booleen){
                    if(b){
                        successcount++;
                    }else{
                        failcount++;
                    }
                }
                DeleteSentensResult d = new DeleteSentensResult(successcount,failcount);
                return Observable.just(d);
            }
        });
    }

    public static class DeleteSentensResult{
        private int successCount;
        private int failCount;

        public DeleteSentensResult(int successCount, int failCount) {
            this.successCount = successCount;
            this.failCount = failCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailCount() {
            return failCount;
        }
    }

    public static class DeleteSentencesParame implements UseCase.Params{
        private List<Sentence> sentences;

        public DeleteSentencesParame(List<Sentence> sentences) {
            this.sentences = sentences;
        }

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void setSentences(List<Sentence> sentences) {
            this.sentences = sentences;
        }
    }

}
