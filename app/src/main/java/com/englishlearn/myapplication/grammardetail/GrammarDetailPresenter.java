package com.englishlearn.myapplication.grammardetail;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarDetailPresenter extends GrammarDetailContract.Presenter{

    private GrammarDetailContract.View mView;
    private String id;
    private String grammarid;
    @Inject
    Repository repository;
    public GrammarDetailPresenter(GrammarDetailContract.View vew,String id,String grammarid){
        mView = vew;
        this.id = id;
        this.grammarid = grammarid;
        MyApplication.instance.getAppComponent().inject(this);
        mView.setPresenter(this);
    }


    @Override
    void getGrammar() {
        if(id != null && !id.isEmpty()){
            Subscription subscription = repository.getGrammarRxById(id)
                    .subscribe(new Subscriber<Grammar>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showEmptyGrammar();
                        }

                        @Override
                        public void onNext(Grammar grammar) {
                            mView.showGrammar(grammar);
                        }
                    });
            add(subscription);
        }
    }

    @Override
    void editGrammar() {
        mView.showEditGrammar();
    }

    @Override
    void deleteGrammar(Grammar grammar) {
        Subscription subscription = repository.deleteGrammarRxById(id)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDeleteFail();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            mView.showDeleteSuccess();
                        }else {
                            mView.showDeleteFail();
                        }
                    }
                });
        add(subscription);
    }
}
