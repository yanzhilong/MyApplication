package com.englishlearn.myapplication.addeditsentence;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class AddEditSentenceContract {

    public interface View extends BaseView<Presenter> {

        void setContent(String content);
        void setTranslate(String translate);
        void showSentences();
        void addSentenceSuccess();
    }

    abstract static class Presenter extends BasePresenter {
        abstract void saveSentence(String content,String translate);
    }
}
