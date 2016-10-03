package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class PhoneticsDetailPresenter extends PhoneticsDetailContract.Presenter {

    private PhoneticsDetailContract.View mView;
    private PhoneticsSymbols phoneticsSymbols;
    @Inject
    Repository repository;
    public PhoneticsDetailPresenter(PhoneticsDetailContract.View vew,PhoneticsSymbols phoneticsSymbols) {
        mView = vew;
        mView.setPresenter(this);
        this.phoneticsSymbols = phoneticsSymbols;
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