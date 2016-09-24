package com.englishlearn.myapplication.addeditgrammar;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditGrammarPresenter extends AddEditGrammarContract.Presenter{

    private AddEditGrammarContract.View mainView;
    private String grammarid;
    private String id;
    @Inject
    Repository repository;
    public AddEditGrammarPresenter(AddEditGrammarContract.View vew,String id,String grammarid){
        mainView = vew;
        this.id = id;
        this.grammarid = grammarid;
        MyApplication.instance.getAppComponent().inject(this);
        mainView.setPresenter(this);
    }

    @Override
    void saveSentence(String name, String content) {
        if(isNewSentence()){
            createGrammar(name,content);
        }else{
            updateGrammar(name,content);
        }
    }

    private void createGrammar(String name, String content){
        Grammar grammar = new Grammar();
        grammar.setTitle(name);
        grammar.setContent(content);
        Subscription subscription = repository.addGrammar(grammar)
                .subscribe(new Subscriber<Grammar>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.addGrammarFail();
                    }

                    @Override
                    public void onNext(Grammar grammar) {
                        if(grammar != null){
                            mainView.addGrammarSuccess();
                        }else {
                            mainView.addGrammarFail();
                        }
                    }
                });
        add(subscription);
    }

    private void updateGrammar(String name, String content){
        if(grammarid == null || id == null){
            throw new RuntimeException("updateGrammar() was called but grammar is new.");
        }
        Grammar grammar = new Grammar();
        grammar.setId(id);
        grammar.setGrammarId(grammarid);
        grammar.setTitle(name);
        grammar.setContent(content);
        Subscription subscription = repository.updateGrammarRxById(grammar)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.updateGrammarFail();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            mainView.updateGrammarSuccess();
                        }else {
                            mainView.updateGrammarFail();
                        }
                    }
                });
        add(subscription);
    }


    private boolean isNewSentence() {
        return grammarid == null;
    }


    @Override
    void start() {
        if(id != null){
            Subscription subscription = repository.getGrammarById(id)
                    .subscribe(new Subscriber<Grammar>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mainView.showGrammarFail();
                        }

                        @Override
                        public void onNext(Grammar grammar) {
                            mainView.showGrammar(grammar);
                        }
                    });
            add(subscription);
        }
    }
}
