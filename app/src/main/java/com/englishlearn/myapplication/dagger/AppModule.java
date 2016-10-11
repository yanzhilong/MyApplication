package com.englishlearn.myapplication.dagger;

import android.app.Application;
import android.content.Context;

import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.local.LocalDataSource;
import com.englishlearn.myapplication.data.source.preferences.SharedPreferencesDataSource;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.RemoteDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

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

    @Provides
    public Repository provideRepository() {
        return Repository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(mApplication), SharedPreferencesDataSource.getInstance(mApplication));
    }

    @Provides
    public RemoteData provideRemoteData() {
        return BmobDataSource.getInstance();
    }

}
