package com.englishlearn.myapplication.registeruser;


/**
 * Created by yanzl on 16-7-20.
 */
public class RegisterUserPresenter extends RegisterUserContract.Presenter{

    private RegisterUserContract.View mView;
    public RegisterUserPresenter(RegisterUserContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
