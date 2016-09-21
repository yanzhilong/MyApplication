package com.englishlearn.myapplication.messagesource;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.MsSource;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class MessageSourceContract {

    public interface View extends BaseView<Presenter> {
        void addMsSourceSuccess();
        void addMsSourceFail();
        void addMsSourceFail(String message);

        void deleteMsSourceSuccess();
        void deleteMsSourceFail();
        void deleteMsSourceFail(String message);

        void updaMsSourceSuccess();
        void updaMsSourceFail();
        void updaMsSourceFail(String message);

        void getMsSourcesSuccess();
        void getMsSourcesFail();
        void getMsSourcesFail(String message);

        void getMsSourceByIdSuccess();
        void getMsSourceByIdFail();
        void getMsSourceByIdFail(String message);

    }

    abstract static class Presenter extends BasePresenter {

        abstract void addMsSource(MsSource msSource);
        abstract void deleteMsSource(String id);
        abstract void updaMsSource(MsSource msSource);
        abstract void getMsSources();
        abstract void getMsSourceById(String id);

    }
}
