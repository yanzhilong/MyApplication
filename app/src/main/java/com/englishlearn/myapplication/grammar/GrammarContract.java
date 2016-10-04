package com.englishlearn.myapplication.grammar;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Grammar;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class GrammarContract {

    public interface View extends BaseView<GrammarContract.Presenter> {
        void showGrammars(List<Grammar> grammars);
        void emptyGrammars();
    }

    abstract static class Presenter extends BasePresenter {
        abstract void getGrammars();
    }
}
