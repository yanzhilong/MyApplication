package com.englishlearn.myapplication.sentences;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class SelectContract {

    public interface View  {
        void notifyDataSetChanged();
        void setPresenter(Presenter presenter);
        void showSentencesEdit();
        void hideSentencesEdit();
        void showEditCount(int count);
        List<Sentence> getSentences();
        void showDeleteEnabled(Boolean enabled);
        void showAllSelect();
        void hideAllSelect();
    }

    abstract static class Presenter extends BasePresenter {
        abstract boolean isEdit();
        abstract void edit();
        abstract void unedit();
        abstract void onClick(Sentence sentence);
        abstract void allSelectClick();
        abstract boolean isSelect(Sentence sentence);
        abstract List<Sentence> getSelects();
    }
}
