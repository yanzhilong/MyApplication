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
        void addSentences(List<Sentence> sentences);
        void emptySentences();
        void showaddSentence();
        void showDeleteResult(int success,int fail);
    }

    abstract static class Presenter extends BasePresenter {
        abstract void getSentences();
        abstract void filterSentences(CharSequence constraint);
        abstract void getSentencesNextPage();
        abstract void getSentences(String searchword);
        abstract void addSentence();
        abstract void deleteSentences(List<Sentence> sentences);
    }
}
