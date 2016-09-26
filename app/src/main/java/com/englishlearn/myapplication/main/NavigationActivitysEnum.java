package com.englishlearn.myapplication.main;


import android.app.Fragment;

import com.englishlearn.myapplication.home.HomeMainFragment;

/**
 * Created by yanzl on 16-9-26.
 */
public enum NavigationActivitysEnum {

    LEARN(HomeMainFragment.newInstance());

    private Fragment mFragment;

    NavigationActivitysEnum(Fragment fragmen) {
        this.mFragment = fragmen;
    }

    public Fragment getmFragment() {
        return mFragment;
    }
}
