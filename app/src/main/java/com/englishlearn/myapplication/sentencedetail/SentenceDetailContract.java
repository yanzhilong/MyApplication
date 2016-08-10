package com.englishlearn.myapplication.sentencedetail;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.Sentence;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class SentenceDetailContract {

    public interface View extends BaseView<Presenter> {

        void showSentence(Sentence sentence);//显示Sentence内容
        void showEditSentence();//显示编辑句子
        void showEmptySentence();//句子信息未查到
        void showDeleteSentenceAffirm();//显示删除确认对话框
        void showDeleteSuccess();
        void showDeleteFail();

    }

    abstract static class Presenter extends BasePresenter {

        abstract void getSentence();
        abstract void editSentence();
        abstract void deleteSentence(Sentence sentence);
    }
}
