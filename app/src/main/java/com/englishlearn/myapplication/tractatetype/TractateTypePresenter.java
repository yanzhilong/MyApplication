package com.englishlearn.myapplication.tractatetype;


import com.englishlearn.myapplication.data.TractateType;

/**
 * Created by yanzl on 16-7-20.
 */
public class TractateTypePresenter extends TractateTypeContract.Presenter{

    private TractateTypeContract.View mView;
    public TractateTypePresenter(TractateTypeContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void addTractateType(TractateType tractateType) {

    }

    @Override
    void deleteTractateType(String id) {

    }

    @Override
    void updaTractateType(TractateType tractateType) {

    }

    @Override
    void getTractateTypes() {

    }

    @Override
    void getTractateTypeById(String id) {

    }
}
