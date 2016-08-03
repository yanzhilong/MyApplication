package com.englishlearn.myapplication.addeditgrammar;


import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.domain.AddGrammar;
import com.englishlearn.myapplication.domain.GetGrammar;
import com.englishlearn.myapplication.domain.UpdateGrammar;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditGrammarPresenter extends AddEditGrammarContract.Presenter{

    private AddGrammar addGrammars;
    private AddEditGrammarContract.View mainView;
    private GetGrammar getGrammar;
    private UpdateGrammar updateGrammar;
    private String grammarid;
    private String id;
    public AddEditGrammarPresenter(AddEditGrammarContract.View vew,String id,String grammarid){
        mainView = vew;
        this.id = id;
        this.grammarid = grammarid;
        getGrammar = new GetGrammar();
        updateGrammar = new UpdateGrammar();
        addGrammars = new AddGrammar();
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
        Grammar grammar = new Grammar(name,content);
        AddGrammar.AddGrammarsParame addGrammarsParame = new AddGrammar.AddGrammarsParame(grammar);
        addGrammars.excuteIo(addGrammarsParame).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                mainView.addGrammarSuccess();
            }
        });
    }

    private void updateGrammar(String name, String content){
        if(grammarid == null || id == null){
            throw new RuntimeException("updateGrammar() was called but grammar is new.");
        }
        Grammar grammar = new Grammar(id,grammarid,name,content);
        UpdateGrammar.UpdateGrammarParame updateGrammarParame = new UpdateGrammar.UpdateGrammarParame(grammar);
        updateGrammar.excuteIo(updateGrammarParame).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                mainView.updateGrammarSuccess();
            }
        });
    }


    private boolean isNewSentence() {
        return grammarid == null;
    }


    @Override
    void start() {
        if(grammarid != null){
            getGrammar.excuteIo(new GetGrammar.GetGrammarParame(id,grammarid)).subscribe(new Subscriber<Grammar>() {
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
        }
    }
}
