package com.englishlearn.myapplication.main;


import android.support.v4.app.Fragment;

import com.englishlearn.myapplication.home.HomeFragment;

/**
 * Created by yanzl on 16-9-26.
 */
public enum NavigationActivitysEnum {

    LEARN(HomeFragment.newInstance());

    private Fragment mFragment;

    NavigationActivitysEnum(Fragment fragmen) {
        this.mFragment = fragmen;
    }

    public Fragment getmFragment() {
        return mFragment;
    }
}
