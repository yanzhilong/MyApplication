package com.englishlearn.myapplication.searchsentences;


import android.util.Log;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class SearchSentencesPresenter extends SearchSentencesContract.Presenter{

    private SearchSentencesContract.View mainView;
    private int page = 0;
    private final int PAGESIZE = 10;
    private List<Sentence> mSentences;
    private boolean more = true;
    private String searchWord = "";

    public static final String TAG = SearchSentencesPresenter.class.getSimpleName();

    @Inject
    Repository repository;

    public SearchSentencesPresenter(SearchSentencesContract.View vew){
        mainView = vew;
        MyApplication.instance.getAppComponent().inject(this);
        mSentences = new ArrayList<>();
        mainView.setPresenter(this);
    }


    @Override
    void getSentencesNextPage() {

        Subscription subscription = repository.getSentences(searchWord,page++,PAGESIZE)
        .subscribe(new Subscriber<List<Sentence>>() {

            @Override
            public void onCompleted() {
                mainView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mainView.setLoadingIndicator(false);
                if((page - 1) == 0){
                    page--;
                }
            }

            @Override
            public void onNext(List<Sentence> sentences) {
                Log.d(TAG,"getSentencesNextPage" + sentences.size());
                if(sentences != null){
                    mSentences.addAll(sentences);
                    more = sentences.size() == 0 ? false : true;
                }
                mainView.showSentences(mSentences);
            }
        });
        add(subscription);
    }

    @Override
    void getSentences(String searchword) {
        page = 0;
        mSentences.clear();
        mainView.setLoadingIndicator(true);
        searchWord = searchword;
        getSentencesNextPage();
        /*if(searchword == null){
            Subscription subscription = repository.getSentencesRx(searchword,page,PAGESIZE)
                    .subscribe(new Subscriber<List<Sentence>>() {
                        @Override
                        public void onCompleted() {
                            mainView.setLoadingIndicator(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mainView.showGetSentencesFail();
                            mainView.setLoadingIndicator(false);
                        }

                        @Override
                        public void onNext(List<Sentence> sentences) {
                            if(sentences != null && sentences.size() > 0){
                                more = true;
                                mSentences.addAll(sentences);
                                mainView.showSentences(mSentences);
                            }else{
                                mainView.emptySentences();
                            }
                        }
                    });
            add(subscription);
        }*/
    }

    @Override
    void refreshSentences() {
        getSentences(searchWord);
    }


    @Override
    public boolean hasMore() {
        return more;
    }


}
