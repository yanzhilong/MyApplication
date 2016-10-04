package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yanzl on 16-7-20.
 */
public class DeleteGrammars extends UseCase<DeleteGrammars.DeleteGrammarsResult,DeleteGrammars.DeleteGrammarsParame> {

    @Inject
    Repository repository;

    public DeleteGrammars(){
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    protected Observable<DeleteGrammarsResult> execute(final DeleteGrammars.DeleteGrammarsParame deleteGrammarsParame) {

        return Observable.from(deleteGrammarsParame.getGrammars().toArray()).flatMap(new Func1<Object, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Object o) {
                Grammar grammar = (Grammar) o;
                return repository.deleteGrammarById(grammar.getObjectId());
            }
        })
                .onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return false;
                    }
                })
                .toList().flatMap(new Func1<List<Boolean>, Observable<DeleteGrammarsResult>>() {
                    @Override
                    public Observable<DeleteGrammarsResult> call(List<Boolean> booleen) {

                        int successcount = 0;
                        int failcount = 0;
                        for(Boolean b:booleen){
                            if(b){
                                successcount++;
                            }else{
                                failcount++;
                            }
                        }
                        DeleteGrammarsResult d = new DeleteGrammarsResult(successcount,failcount);
                        return Observable.just(d);
                    }
                });
    }

    public static class DeleteGrammarsResult{
        private int successCount;
        private int failCount;

        public DeleteGrammarsResult(int successCount, int failCount) {
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

    public static class DeleteGrammarsParame implements UseCase.Params{
        private List<Grammar> grammars;

        public DeleteGrammarsParame(List<Grammar> grammars) {
            this.grammars = grammars;
        }

        public List<Grammar> getGrammars() {
            return grammars;
        }

        public void setGrammars(List<Grammar> grammars) {
            this.grammars = grammars;
        }
    }

}
