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
        void registerFail(String message);
    }

    abstract static class Presenter extends BasePresenter {

        abstract void register(User user);//邮箱或用户名密码注册

        abstract void register(String mobile,String smsCode);//手机号和验证码注册

    }
}
