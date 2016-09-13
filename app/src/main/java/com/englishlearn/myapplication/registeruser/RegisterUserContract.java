package com.englishlearn.myapplication.registeruser;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class RegisterUserContract {

    public interface View extends BaseView<Presenter> {


    }

    abstract static class Presenter extends BasePresenter {

        abstract void register();

    }
}
