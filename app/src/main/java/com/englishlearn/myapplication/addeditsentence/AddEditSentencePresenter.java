package com.englishlearn.myapplication.addeditsentence;


import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.domain.AddSentence;
import com.englishlearn.myapplication.domain.GetSentence;
import com.englishlearn.myapplication.domain.UpdateSentence;

import rx.Subscriber;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditSentencePresenter extends AddEditSentenceContract.Presenter{

    private AddSentence addSentences;
    private AddEditSentenceContract.View mainView;
    private GetSentence getSentence;
    private UpdateSentence updateSentence;
    private String sentenceid;
    private String id;
    public AddEditSentencePresenter(AddEditSentenceContract.View vew,String id,String sentenceid){
        mainView = vew;
        this.id = id;
        this.sentenceid = sentenceid;
        addSentences = new AddSentence();
        getSentence = new GetSentence();
        updateSentence = new UpdateSentence();
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
        Sentence sentence = new Sentence(content,translate,null);
        AddSentence.AddSentencesParame addSentencesParame = new AddSentence.AddSentencesParame(sentence);
        addSentences.excuteIo(addSentencesParame).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                mainView.addSentenceSuccess();
            }
        });
    }

    private void updateSentence(String content, String translate){
        if(sentenceid == null || id == null){
            throw new RuntimeException("updateSentence() was called but sentence is new.");
        }
        Sentence sentence = new Sentence(id,sentenceid,content,translate,null);
        UpdateSentence.UpdateSentenceParame updateSentenceParame = new UpdateSentence.UpdateSentenceParame(sentence);
        updateSentence.excuteIo(updateSentenceParame).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                mainView.updateSentenceSuccess();
            }
        });
    }


    private boolean isNewSentence() {
        return sentenceid == null;
    }


    @Override
    void start() {
        if(sentenceid != null){
            getSentence.excuteIo(new GetSentence.GetSentenceParame(id,sentenceid)).subscribe(new Subscriber<Sentence>() {
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
        }
    }
}
