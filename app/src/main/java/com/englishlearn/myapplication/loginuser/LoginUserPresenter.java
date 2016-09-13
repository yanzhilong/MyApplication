package com.englishlearn.myapplication.loginuser;


/**
 * Created by yanzl on 16-7-20.
 */
public class LoginUserPresenter extends LoginUserContract.Presenter{

    private LoginUserContract.View mView;
    public LoginUserPresenter(LoginUserContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
