package com.englishlearn.myapplication.grammars;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.domain.DeleteGrammars;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarsPresenter extends GrammarsContract.Presenter{

    private GrammarsContract.View mainView;
    private int page = 1;
    private final int PAGESIZE = 10;
    @Inject
    Repository repository;
    DeleteGrammars deleteGrammars;
    public GrammarsPresenter(GrammarsContract.View vew){
        mainView = vew;
        MyApplication.instance.getAppComponent().inject(this);
        deleteGrammars = new DeleteGrammars();
        mainView.setPresenter(this);
    }

    @Override
    void getGrammars() {
        getGrammars(null);
    }

    @Override
    void getGrammarsNextPage() {
        Subscription subscription = repository.getGrammarsRx(page++,PAGESIZE)
                .subscribe(new Subscriber<List<Grammar>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Grammar> grammars) {
                        mainView.addGrammars(grammars);
                    }
                });
        add(subscription);
    }

    @Override
    void getGrammars(String searchword) {
        if(searchword == null){
            Subscription subscription = repository.getGrammarsRx()
                    .subscribe(new Subscriber<List<Grammar>>() {
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
            add(subscription);
        }
    }

    @Override
    void addGrammar() {
        mainView.showaddGrammar();
    }

    @Override
    void deleteGrammars(final List<Grammar> grammars) {
        Subscription subscription = deleteGrammars.excuteIo(new DeleteGrammars.DeleteGrammarsParame(grammars))
                .subscribe(new Subscriber<DeleteGrammars.DeleteGrammarsResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeleteGrammars.DeleteGrammarsResult deleteGrammarsResult) {
                        mainView.showDeleteResult(deleteGrammarsResult.getSuccessCount(),deleteGrammarsResult.getFailCount());
                    }
                });
        add(subscription);
    }
}
