package com.englishlearn.myapplication.sentencedetail;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Sentence;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class SentenceDetailContract {

    public interface View extends BaseView<Presenter> {

        void showSentence(Sentence sentence);//显示Sentence内容

    }

    abstract static class Presenter extends BasePresenter {

        abstract void getSentence();

    }
}
