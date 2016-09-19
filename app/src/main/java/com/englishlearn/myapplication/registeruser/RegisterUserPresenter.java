package com.englishlearn.myapplication.registeruser;


import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserResult;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class RegisterUserPresenter extends RegisterUserContract.Presenter{

    @Inject
    RemoteData remoteData;

    private RegisterUserContract.View mView;

    public RegisterUserPresenter(RegisterUserContract.View vew){
        mView = vew;
        mView.setPresenter(this);
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    void register(User user) {
        Subscription subscription = remoteData.register(user).subscribe(new Subscriber<BmobCreateUserResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.registerFail(e.getMessage());
            }

            @Override
            public void onNext(BmobCreateUserResult bmobCreateUserResult) {

                if(bmobCreateUserResult.getCode() != null){
                    //注册失败
                    mView.registerFail(bmobCreateUserResult.getError());
                }else{
                    //注册成功
                    mView.registerSuccess();
                }
            }
        });
        add(subscription);
    }

    @Override
    void register(String mobile, String smsCode) {

    }
}
