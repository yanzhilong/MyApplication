package com.englishlearn.myapplication.addeditgrammar;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class AddEditGrammarContract {

    public interface View extends BaseView<Presenter> {

        void setName();
        void setContent();
        void showGrammars();
        void addGrammarSuccess();
    }

    abstract static class Presenter extends BasePresenter {
        abstract void saveSentence(String name,String content);
    }
}
