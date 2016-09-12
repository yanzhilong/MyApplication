package com.englishlearn.myapplication.addeditsentence;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditSentencePresenter extends AddEditSentenceContract.Presenter{

    private AddEditSentenceContract.View mainView;
    private String sentenceid;
    private String id;
    @Inject
    Repository repository;
    public AddEditSentencePresenter(AddEditSentenceContract.View vew,String id,String sentenceid){
        mainView = vew;
        this.id = id;
        this.sentenceid = sentenceid;
        MyApplication.instance.getAppComponent().inject(this);
        mainView.setPresenter(this);
    }



    @Override
    void saveSentence(String content, String translate) {
       if(isNewSentence()){
           createSentence(content,translate);
       }else{
           updateSentence(content,translate);
       }
    }

    private void createSentence(String content, String translate){
        Sentence sentence = new Sentence();
        sentence.setContent(content);
        sentence.setTranslation(translate);
        Subscription subscription = repository.addSentenceRx(sentence)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.addSentenceFail();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            mainView.addSentenceSuccess();
                        }else {
                            mainView.addSentenceFail();
                        }
                    }
                });
        add(subscription);
    }

    private void updateSentence(String content, String translate){
        if(sentenceid == null || id == null){
            throw new RuntimeException("updateSentence() was called but sentence is new.");
        }
        Sentence sentence = new Sentence();
        sentence.setId(id);
        sentence.setSentenceId(sentenceid);
        sentence.setContent(content);
        sentence.setTranslation(translate);
        Subscription subscription = repository.updateSentenceRx(sentence)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.updateSentenceFail();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            mainView.updateSentenceSuccess();
                        }else {
                            mainView.updateSentenceFail();
                        }
                    }
                });
        add(subscription);
    }


    private boolean isNewSentence() {
        return sentenceid == null;
    }


    @Override
    void start() {
        if(id != null){
            Subscription subscription = repository.getSentenceRxById(id)
                    .subscribe(new Subscriber<Sentence>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mainView.showSentenceFail();
                        }

                        @Override
                        public void onNext(Sentence sentence) {
                            mainView.showSentence(sentence);
                        }
                    });
            add(subscription);
        }
    }
}
