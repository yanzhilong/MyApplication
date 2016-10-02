package com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsDetailsPresenter extends PhoneticsDetailsContract.Presenter{

    private PhoneticsDetailsContract.View mView;
    private PhoneticsSymbols phoneticsSymbols;
    @Inject
    Repository repository;
    public PhoneticsDetailsPresenter(PhoneticsDetailsContract.View vew,PhoneticsSymbols phoneticssymbolsId){
        mView = vew;
        mView.setPresenter(this);
        this.phoneticsSymbols = phoneticssymbolsId;
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    void getWords() {
        repository.getPhoneticsWordsRxByPhoneticsId(phoneticsSymbols.getObjectId())
                .flatMap(new Func1<PhoneticsWords, Observable<List<WordCollect>>>() {
                    @Override
                    public Observable<List<WordCollect>> call(PhoneticsWords phoneticsWords) {
                        return repository.getWordCollectRxByWordGroupId(phoneticsWords.getWordgroupId(),0,100);
                    }
                }).flatMap(new Func1<List<WordCollect>, Observable<WordCollect>>() {
            @Override
            public Observable<WordCollect> call(List<WordCollect> wordCollects) {
                return Observable.from(wordCollects);
            }
        }).flatMap(new Func1<WordCollect, Observable<Word>>() {
            @Override
            public Observable<Word> call(WordCollect wordCollect) {
                return repository.getWordRxById(wordCollect.getWordId());
            }
        }).toList().subscribe(new Subscriber<List<Word>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showWordsFail();
            }

            @Override
            public void onNext(List<Word> words) {
                mView.showWords(words);
            }
        });
    }
}