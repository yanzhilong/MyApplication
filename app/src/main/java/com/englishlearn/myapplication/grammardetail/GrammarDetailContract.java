package com.englishlearn.myapplication.grammardetail;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Grammar;

/**
 * Created by yanzl on 16-7-28.
 * 用于约定view 和 presenter的实现接口
 */
public class GrammarDetailContract {

    public interface View extends BaseView<Presenter> {

        void showGrammar(Grammar grammar);//显示Grammar内容
        void showEditGrammar();//显示编辑语法
        void showEmptyGrammar();//语法信息未查到
        void showDeleteGrammarAffirm();//显示删除确认对话框
        void showDeleteSuccess();
        void showDeleteFail();
    }

    abstract static class Presenter extends BasePresenter {

        abstract void getGrammar();
        abstract void editGrammar();
        abstract void deleteGrammar(Grammar grammar);
    }
}
