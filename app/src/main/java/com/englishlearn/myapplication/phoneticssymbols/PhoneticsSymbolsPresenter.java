package com.englishlearn.myapplication.phoneticssymbols;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subjects.Subject;

/**
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsSymbolsPresenter extends PhoneticsSymbolsContract.Presenter{

    private PhoneticsSymbolsContract.View mView;

    @Inject
    Repository repository;

    public PhoneticsSymbolsPresenter(PhoneticsSymbolsContract.View vew){
        mView = vew;
        mView.setPresenter(this);
        MyApplication.instance.getAppComponent().inject(this);
    }


    @Override
    void getPhoneticsSymbols() {
        Subscription subscription = repository.getPhoneticsSymbolsRx().subscribe(new Subscriber<List<PhoneticsSymbols>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showPhoneticsSymbolsFail();
            }

            @Override
            public void onNext(List<PhoneticsSymbols> phoneticsSymbolses) {
                mView.showPhoneticsSymbols(phoneticsSymbolses);
            }
        });
        add(subscription);
    }
}
