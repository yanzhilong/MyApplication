package com.englishlearn.myapplication.messagesource;


import com.englishlearn.myapplication.data.MsSource;

/**
 * Created by yanzl on 16-7-20.
 */
public class MessageSourcePresenter extends MessageSourceContract.Presenter{

    private MessageSourceContract.View mView;
    public MessageSourcePresenter(MessageSourceContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void addMsSource(MsSource msSource) {

    }

    @Override
    void deleteMsSource(String id) {

    }

    @Override
    void updaMsSource(MsSource msSource) {

    }

    @Override
    void getMsSources() {

    }

    @Override
    void getMsSourceById(String id) {

    }
}
