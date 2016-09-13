package com.englishlearn.myapplication.updateuser;


/**
 * Created by yanzl on 16-7-20.
 */
public class UpdateUserPresenter extends UpdateUserContract.Presenter{

    private UpdateUserContract.View mView;
    public UpdateUserPresenter(UpdateUserContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
