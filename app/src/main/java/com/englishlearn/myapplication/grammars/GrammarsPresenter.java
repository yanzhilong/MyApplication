package com.englishlearn.myapplication.grammars;


import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.GetGrammars;
import com.englishlearn.myapplication.domain.GetSentences;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarsPresenter extends GrammarsContract.Presenter{

    private GetGrammars getGrammars;
    private GrammarsContract.View mainView;
    @Inject
    Repository repository;
    public GrammarsPresenter(GrammarsContract.View vew){
        mainView = vew;
        getGrammars = new GetGrammars();
        mainView.setPresenter(this);
    }

    @Override
    void getGrammars() {
        getGrammars(null);
    }

    @Override
    void getGrammars(String searchword) {
        GetGrammars.GetGrammarsParame getGrammarsParame = new GetGrammars.GetGrammarsParame(searchword);
        getGrammars.excuteIo(searchword != null ? getGrammarsParame : null).subscribe(new Subscriber<List<Grammar>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.emptyGrammars();
            }

            @Override
            public void onNext(List<Grammar> grammars) {
                if(grammars != null && grammars.size() > 0){
                    mainView.showGrammars(grammars);
                }else{
                    mainView.emptyGrammars();
                }
            }
        });
    }
}
