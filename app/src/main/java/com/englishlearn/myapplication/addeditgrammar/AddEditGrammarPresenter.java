package com.englishlearn.myapplication.addeditgrammar;


import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.AddGrammars;
import com.englishlearn.myapplication.domain.AddSentences;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditGrammarPresenter extends AddEditGrammarContract.Presenter{

    private AddGrammars addGrammars;
    private AddEditGrammarContract.View mainView;
    @Inject
    Repository repository;
    public AddEditGrammarPresenter(AddEditGrammarContract.View vew){
        mainView = vew;
        addGrammars = new AddGrammars();
        mainView.setPresenter(this);
    }

    @Override
    void saveSentence(String name, String content) {
        Grammar grammar = new Grammar();
        grammar.setName(name);
        grammar.setContent(content);
        AddGrammars.AddGrammarsParame addGrammarsParame = new AddGrammars.AddGrammarsParame(grammar);
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
}
