package com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.PhoneticsSymbols;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class PhoneticsDetailsContract {

    public interface View extends BaseView<Presenter> {

        void showWords();
    }

    abstract static class Presenter extends BasePresenter {

        abstract void getWords();//加载相关单词

    }
}
