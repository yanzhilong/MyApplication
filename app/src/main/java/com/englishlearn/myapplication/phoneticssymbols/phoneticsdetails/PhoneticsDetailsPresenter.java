package com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

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
        repository.getWordsRxByPhoneticsId(phoneticsSymbols.getObjectId()).
        subscribe(new Subscriber<List<Word>>() {
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