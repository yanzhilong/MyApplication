package com.englishlearn.myapplication.addeditsentence;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class AddEditSentenceContract {

    public interface View extends BaseView<Presenter> {

        void setContent();
        void settranslate();
        void showSentences();
    }

    abstract static class Presenter extends BasePresenter {
        abstract void saveSentence(String content,String translate);
    }
}
