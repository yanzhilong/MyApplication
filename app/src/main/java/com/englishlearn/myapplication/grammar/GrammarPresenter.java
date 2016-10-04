package com.englishlearn.myapplication.grammar;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarPresenter extends GrammarContract.Presenter{

    private GrammarContract.View mView;
    @Inject
    Repository repository;

    public GrammarPresenter(GrammarContract.View vew){
        mView = vew;
        mView.setPresenter(this);
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    void getGrammars() {
        Subscription subscription = repository.getGrammars()
                .subscribe(new Subscriber<List<Grammar>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.emptyGrammars();
                    }

                    @Override
                    public void onNext(List<Grammar> grammars) {
                        if(grammars != null && grammars.size() > 0){
                            mView.showGrammars(grammars);
                        }else{
                            mView.emptyGrammars();
                        }
                    }
                });
        add(subscription);
    }


}
