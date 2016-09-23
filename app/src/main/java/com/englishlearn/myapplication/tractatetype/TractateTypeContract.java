package com.englishlearn.myapplication.tractatetype;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.TractateType;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class TractateTypeContract {

    public interface View extends BaseView<Presenter> {
        void addTractateTypeSuccess();
        void addTractateTypeFail();
        void addTractateTypeFail(String message);

        void deleteTractateTypeSuccess();
        void deleteTractateTypeFail();
        void deleteTractateTypeFail(String message);

        void updaTractateTypeSuccess();
        void updaTractateTypeFail();
        void updaTractateTypeFail(String message);

        void getTractateTypesSuccess();
        void getTractateTypesFail();
        void getTractateTypesFail(String message);

        void getTractateTypeByIdSuccess();
        void getTractateTypeByIdFail();
        void getTractateTypeByIdFail(String message);

    }

    abstract static class Presenter extends BasePresenter {

        abstract void addTractateType(TractateType tractateType);
        abstract void deleteTractateType(String id);
        abstract void updaTractateType(TractateType tractateType);
        abstract void getTractateTypes();
        abstract void getTractateTypeById(String id);

    }
}
