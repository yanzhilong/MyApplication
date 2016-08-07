package com.englishlearn.myapplication.grammars;

import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.data.Grammar;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class GrammarsSelectContract {

    public interface View  {
        void notifyDataSetChanged();
        void setPresenter(Presenter presenter);
        void showGrammarsEdit();
        void hideGrammarsEdit();
        void showEditCount(int count);
        List<Grammar> getGrammars();
        void showDeleteEnabled(Boolean enabled);
        void showAllSelect();
        void hideAllSelect();
    }

    abstract static class Presenter extends BasePresenter {
        abstract boolean isEdit();
        abstract void edit();
        abstract void unedit();
        abstract void onClick(Grammar grammar);
        abstract void allSelectClick();
        abstract boolean isSelect(Grammar grammar);
        abstract List<Grammar> getSelects();
    }
}
