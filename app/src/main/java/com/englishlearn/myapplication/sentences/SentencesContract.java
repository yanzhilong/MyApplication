package com.englishlearn.myapplication.sentences;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class SentencesContract {

    public interface View extends BaseView<Presenter> {
        void showSentences(List<Sentence> sentences);
        void emptySentences(List<Sentence> sentences);
    }

    abstract static class Presenter extends BasePresenter {
        abstract void getSentences();
        abstract void getSentences(String searchword);
    }
}
