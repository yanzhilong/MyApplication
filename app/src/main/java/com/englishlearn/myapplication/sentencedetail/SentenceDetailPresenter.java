package com.englishlearn.myapplication.sentencedetail;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceDetailPresenter extends SentenceDetailContract.Presenter{

    private SentenceDetailContract.View mView;
    private String id;
    @Inject
    Repository repository;
    public SentenceDetailPresenter(SentenceDetailContract.View vew,String id){
        mView = vew;
        this.id = id;
        MyApplication.instance.getAppComponent().inject(this);
        mView.setPresenter(this);
    }


    @Override
    void getSentence() {
        if(id != null && !id.isEmpty()){
            Subscription subscription = repository.getSentenceById(id)
                    .subscribe(new Subscriber<Sentence>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showEmptySentence();
                        }

                        @Override
                        public void onNext(Sentence sentence) {
                            mView.showSentence(sentence);
                        }
                    });
            add(subscription);
        }
    }

    @Override
    void editSentence() {
        mView.showEditSentence();
    }

    @Override
    void deleteSentence(Sentence sentence) {
        Subscription subscription = repository.deleteSentenceById(sentence.getObjectId())
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

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }
}
