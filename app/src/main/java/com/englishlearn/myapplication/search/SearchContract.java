package com.englishlearn.myapplication.search;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class SearchContract {

    public interface View extends BaseView<Presenter> {

        void setQuery(String query);
    }

    abstract static class Presenter extends BasePresenter {
        abstract void filterSentences(CharSequence constraint);
        abstract void setQuery(String query);
    }
}
