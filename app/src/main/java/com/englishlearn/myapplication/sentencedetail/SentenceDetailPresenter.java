package com.englishlearn.myapplication.sentencedetail;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestParam;
import com.englishlearn.myapplication.domain.DeleteSentence;
import com.englishlearn.myapplication.domain.GetSentence;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceDetailPresenter extends SentenceDetailContract.Presenter{

    private SentenceDetailContract.View mView;
    private String sentenceid;
    private String id;
    private GetSentence getSentence;
    private DeleteSentence deleteSentence;
    public SentenceDetailPresenter(SentenceDetailContract.View vew,String id,String sentenceid){
        mView = vew;
        this.sentenceid = sentenceid;
        this.id = id;
        getSentence = new GetSentence();
        deleteSentence = new DeleteSentence();
        mView.setPresenter(this);
    }


    @Override
    void getSentence() {
        if(sentenceid != null && !sentenceid.isEmpty()){
            Subscription subscription = getSentence.excuteIo(new GetSentence.GetSentenceParame(id,sentenceid)).subscribe(new Subscriber<Sentence>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

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
        Subscription subscription = deleteSentence.excuteIo(new DeleteSentence.DeleteSentenceParame(sentence)).subscribe(new Subscriber<Boolean>() {
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
        getSentence.cancelRequest(RequestParam.GETSENTENCEID);
    }
}
