package com.englishlearn.myapplication.dagger;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yanzl on 16-6-24.
 */
@Module
public class AppModule {

    Application mApplication;
    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    public Context provideContext() {
        return mApplication;
    }





}
