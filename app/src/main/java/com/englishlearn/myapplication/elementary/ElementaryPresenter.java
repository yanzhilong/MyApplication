package com.englishlearn.myapplication.elementary;


/**
 * Created by yanzl on 16-7-20.
 */
public class ElementaryPresenter extends ElementaryContract.Presenter{

    private ElementaryContract.View mView;
    public ElementaryPresenter(ElementaryContract.View vew){
        mView = vew;
        mView.setPresenter(this);
    }

    @Override
    void register() {

    }
}
