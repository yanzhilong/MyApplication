package com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails;


import com.englishlearn.myapplication.data.PhoneticsSymbols;

/**
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsDetailsPresenter extends PhoneticsDetailsContract.Presenter{

    private PhoneticsDetailsContract.View mView;
    private PhoneticsSymbols phoneticsSymbols;
    public PhoneticsDetailsPresenter(PhoneticsDetailsContract.View vew,PhoneticsSymbols phoneticssymbolsId){
        mView = vew;
        mView.setPresenter(this);
        this.phoneticsSymbols = phoneticsSymbols;
    }

    @Override
    void getWords() {

    }
}
