package com.englishlearn.myapplication.grammars;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.domain.GetSentences;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class GrammarsContract {

    public interface View extends BaseView<Presenter> {
        void showGrammars(List<Grammar> grammars);
        void emptyGrammars();
    }

    abstract static class Presenter extends BasePresenter {
        abstract void getGrammars();
        abstract void getGrammars(String searchword);
    }
}
