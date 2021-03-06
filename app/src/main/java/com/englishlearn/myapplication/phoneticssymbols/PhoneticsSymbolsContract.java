package com.englishlearn.myapplication.phoneticssymbols;


import com.englishlearn.myapplication.BasePresenter;
import com.englishlearn.myapplication.BaseView;
import com.englishlearn.myapplication.data.PhoneticsSymbols;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 * 用于约定view 和 presenter的实现接口
 */
public class PhoneticsSymbolsContract {

    public interface View extends BaseView<Presenter> {

        void showPhoneticsSymbols(List<PhoneticsSymbols> list);
        void showPhoneticsSymbolsFail();
    }

    abstract static class Presenter extends BasePresenter {

        abstract void getPhoneticsSymbols();//获得音标

    }
}
