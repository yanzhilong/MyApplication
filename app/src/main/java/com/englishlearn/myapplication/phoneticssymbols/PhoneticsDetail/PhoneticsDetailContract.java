package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Word;

import java.util.List;

public interface PhoneticsDetailContract {

    public interface View extends BaseView<PhoneticsDetailContract.Presenter> {

        void showWords(List<Word> list);
        void showWordsFail();
    }

    abstract static class Presenter extends BasePresenter {

        abstract void getWords();//加载相关单词

    }
}
