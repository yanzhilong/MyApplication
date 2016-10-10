package com.englishlearn.myapplication.sentence;

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
        void showGetSentencesFail();//获取失败
        void showaddSentence();
        void setQuery(String query);
        void showDeleteResult(int success,int fail);

        //下拉刷新功能模块
        void setLoadingIndicator(boolean active);//加载指示器
    }

    abstract static class Presenter extends BasePresenter {

        abstract void getSentences();
        abstract void setQuery(String query);
        abstract void filterSentences(CharSequence constraint);
        abstract void getSentencesNextPage();
        abstract void addSentence();
        abstract void deleteSentences(List<Sentence> sentences);
        //分页加载模块
        abstract boolean hasMore();//判断是否还有下一页
    }


}
