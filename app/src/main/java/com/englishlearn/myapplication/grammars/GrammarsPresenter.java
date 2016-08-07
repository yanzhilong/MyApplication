package com.englishlearn.myapplication.grammars;


import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.domain.DeleteGrammar;
import com.englishlearn.myapplication.domain.GetGrammars;

import java.util.List;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarsPresenter extends GrammarsContract.Presenter{

    private GetGrammars getGrammars;
    private GrammarsContract.View mainView;
    private DeleteGrammar deleteGrammar;
    public GrammarsPresenter(GrammarsContract.View vew){
        mainView = vew;
        getGrammars = new GetGrammars();
        deleteGrammar = new DeleteGrammar();
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
                e.printStackTrace();
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

    @Override
    void addGrammar() {
        mainView.showaddGrammar();
    }

    @Override
    void deleteGrammars(final List<Grammar> grammars) {
        final int[] success = {0};
        final int[] fail = {0};
        for(Grammar grammar : grammars){
            deleteGrammar.excuteIo(new DeleteGrammar.DeleteGrammarParame(grammar)).subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    fail[0]++;
                    checkoutDeleteResult(grammars.size(),success[0],fail[0]);
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if(aBoolean){
                        success[0]++;
                    }else{
                        fail[0]++;
                    }
                    checkoutDeleteResult(grammars.size(),success[0],fail[0]);
                }
            });
        }
    }

    private void checkoutDeleteResult(int sum,int success,int fail){
        if((success + fail) == sum){
            mainView.showDeleteResult(success,fail);
        }
    }
}
