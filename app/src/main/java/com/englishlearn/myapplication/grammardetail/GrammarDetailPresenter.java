package com.englishlearn.myapplication.grammardetail;


import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.domain.GetGrammar;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarDetailPresenter extends GrammarDetailContract.Presenter{

    private GrammarDetailContract.View mView;
    private String grammarid;
    private GetGrammar getGrammar;
    public GrammarDetailPresenter(GrammarDetailContract.View vew,String grammarid){
        mView = vew;
        this.grammarid = grammarid;
        getGrammar = new GetGrammar();
        mView.setPresenter(this);
    }


    @Override
    void getGrammar() {
        if(grammarid != null && !grammarid.isEmpty()){
            Subscription subscription = getGrammar.excuteIo(new GetGrammar.GetGrammarParame(grammarid)).subscribe(new Subscriber<Grammar>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

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
}
