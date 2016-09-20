package com.englishlearn.myapplication.registeruser;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.User;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class RegisterUserContract {

    public interface View extends BaseView<Presenter> {

        void registerSuccess();
        void registerAndLoginSuccess();
        void resetPasswordByMailSuccess();//发送重置邮件成功
        void resetPasswordByMailFail();//发送重置邮件成功
        void resetPasswordByMobileSuccess();//短信重置密码成功
        void resetPasswordByMobileFail();//短信重置密码失败
        void resetPasswordByMobileFail(String message);//短信重置密码失败
        void resetPasswordByOldPwdSuccess();//旧密码重置密码成功
        void resetPasswordByOldPwdFail();//旧密码重置密码失败
        void resetPasswordByOldPwdFail(String message);//旧密码重置密码失败
        void registerFail(String message);
        void registerFail();
        void loginFail();
        void loginFail(String message);
        void loginSuccess(User user);
        void updateSuccess(User user);
        void showUser(User user);
        void logout();//注销
    }

    abstract static class Presenter extends BasePresenter {

        abstract void register(User user);//邮箱或用户名密码注册

        abstract void requestSmsCode(String mobile);//获得验证码

        abstract void register(String mobile,String smsCode);//手机号和验证码注册

        abstract void resetPassword(String mail);//重置密码 邮箱

        abstract void resetPassword(String smscode,String newpwd);//重置密码 验证码

        abstract void resetPassword(String username,String oldpwd,String newpwd);//重置密码 旧密码

        abstract void loginByName(String username,String password);

        abstract void loginByEmail(String email,String password);

        abstract void loginByMobile(String mobile,String password);

        abstract void updateuser(User user);

        abstract User getLoginUser();

        abstract void login();//注册成功后登陆

        abstract void logout();//注销
    }
}
