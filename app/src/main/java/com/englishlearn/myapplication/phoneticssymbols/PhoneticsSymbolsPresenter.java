package com.englishlearn.myapplication.phoneticssymbols;


/**
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsSymbolsPresenter extends PhoneticsSymbolsContract.Presenter{

    private PhoneticsSymbolsContract.View mView;
    public PhoneticsSymbolsPresenter(PhoneticsSymbolsContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
