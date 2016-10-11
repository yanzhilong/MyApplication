package com.englishlearn.myapplication.registeruser;


import android.util.Log;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yanzl on 16-7-20.
 */
public class RegisterUserPresenter extends RegisterUserContract.Presenter{

    private static final String TAG = RegisterUserPresenter.class.getSimpleName();

    private User registerUser;

    private User user;
    private String smsId;

    @Inject
    RemoteData remoteData;
    @Inject
    Repository repository;

    private RegisterUserContract.View mView;

    public RegisterUserPresenter(RegisterUserContract.View vew){
        mView = vew;
        mView.setPresenter(this);
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Override
    void register(User user) {
        this.registerUser = user;
        Subscription subscription = remoteData.register(user).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.registerFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.registerFail();
                }
            }

            @Override
            public void onNext(User user) {
                Log.d(TAG,"register" + Thread.currentThread().getName());
                //注册成功
                mView.registerSuccess();
                Log.d(TAG,user.toString());
            }
        });
        add(subscription);
    }

    @Override
    void requestSmsCode(String mobile) {
        Subscription subscription = remoteData.requestSmsCode(mobile).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.requestSmsCodeFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.requestSmsCodeFail();
                }
            }

            @Override
            public void onNext(String s) {
                RegisterUserPresenter.this.smsId = s;
                mView.requestSmsCodeSuccess();
            }
        });
        add(subscription);
    }

    @Override
    void register(String mobile, String smsCode) {
        Subscription subscription = remoteData.createOrLoginUserByPhoneRx(mobile,smsCode).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.registerFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.registerFail();
                }
            }

            @Override
            public void onNext(User user) {
                RegisterUserPresenter.this.user = user;
                //注册成功
                mView.registerAndLoginSuccess();
                mView.showUser(user);
                Log.d(TAG,user.toString());
            }
        });
        add(subscription);
    }

    @Override
    void emailVerify(String email) {
        Subscription subscription = remoteData.emailVerify(email).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.emailVerifyFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.emailVerifyFail();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                mView.emailVerifySuccess();
            }
        });
        add(subscription);
    }

    @Override
    void smsCodeVerify(String code, String mobile) {
        Subscription subscription = remoteData.smsCodeVerify(code,mobile).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.smsCodeVerifyFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.smsCodeVerifyFail();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                mView.smsCodeVerifySuccess();
            }
        });
        add(subscription);
    }

    @Override
    void resetPassword(String mail) {
        Subscription subscription = remoteData.pwdResetByEmail(mail).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.resetPasswordByMailSuccess();
                }else{
                    e.printStackTrace();
                    mView.resetPasswordByMailFail();
                }

            }

            @Override
            public void onNext(Boolean b) {
                mView.resetPasswordByMailSuccess();
            }
        });
        add(subscription);
    }

    @Override
    void resetPassword(String smscode, String newpwd) {
        Subscription subscription = remoteData.pwdResetByMobile(smscode,newpwd).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.resetPasswordByMobileFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.resetPasswordByMobileFail();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                mView.resetPasswordByMobileSuccess();
            }
        });
        add(subscription);
    }

    @Override
    void resetPassword(String username, final String oldpwd, final String newpwd) {
        //需要先登陆
        if(RegisterUserPresenter.this.user == null){
            return;
        }
        Subscription subscription = remoteData.pwdResetByOldPwd(RegisterUserPresenter.this.user.getSessionToken(),RegisterUserPresenter.this.user.getObjectId(),oldpwd,newpwd)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof BmobRequestException){
                            mView.resetPasswordByOldPwdFail(e.getMessage());
                        }else{
                            e.printStackTrace();
                            mView.resetPasswordByOldPwdFail();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.resetPasswordByOldPwdSuccess();
                    }
                });
        add(subscription);
    }

    @Override
    void loginByName(String username, String password) {
        Subscription subscription = remoteData.login(username,password).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.loginFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.loginFail();
                }
            }

            @Override
            public void onNext(User user) {
                RegisterUserPresenter.this.user = user;
                mView.loginSuccess(user);
                repository.saveUserInfo(user);

            }
        });
        add(subscription);
    }

    @Override
    void loginByEmail(String email, String password) {
        Subscription subscription = remoteData.login(email,password).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.loginFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.loginFail();
                }
            }

            @Override
            public void onNext(User user) {
                RegisterUserPresenter.this.user = user;
                mView.loginSuccess(user);
            }
        });
        add(subscription);
    }

    @Override
    void loginByMobile(String mobile, String password) {
        Subscription subscription = remoteData.login(mobile,password).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.loginFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.loginFail();
                }
            }

            @Override
            public void onNext(User user) {
                RegisterUserPresenter.this.user = user;
                mView.loginSuccess(user);
            }
        });
        add(subscription);
    }


    @Override
    void updateuser(User user) {
        //需要先登陆
        if(RegisterUserPresenter.this.user == null){
            return;
        }

        Subscription subscription = remoteData.updateUser(user).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    mView.loginFail(e.getMessage());
                }else{
                    e.printStackTrace();
                    mView.loginFail();
                }
            }

            @Override
            public void onNext(User user) {
                RegisterUserPresenter.this.user = user;
                mView.updateSuccess(user);
            }
        });
        add(subscription);
    }

    @Override
    User getLoginUser() {
        return user;
    }

    @Override
    void login() {
        if(this.registerUser == null){
            return;
        }
        if(registerUser.getUsername() != null && !registerUser.getUsername().equals("")){
            loginByName(registerUser.getUsername(),registerUser.getPassword());
        }else if(user.getEmail() != null && !user.getEmail().equals("")) {
            loginByEmail(registerUser.getEmail(),registerUser.getPassword());
        }
    }

    @Override
    void logout() {
        this.user = null;
        mView.logout();
        repository.cleanUserInfo();
    }

    @Override
    void checkoutUser() {
        User user = repository.getUserInfo();
        if(user != null){
            mView.showUser(user);
        }
    }
}
