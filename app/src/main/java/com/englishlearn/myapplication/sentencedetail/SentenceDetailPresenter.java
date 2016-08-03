package com.englishlearn.myapplication.sentencedetail;


import com.englishlearn.myapplication.data.Sentence;
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
    public SentenceDetailPresenter(SentenceDetailContract.View vew,String id,String sentenceid){
        mView = vew;
        this.sentenceid = sentenceid;
        this.id = id;
        getSentence = new GetSentence();
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
}
