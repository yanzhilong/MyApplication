package com.englishlearn.myapplication;

import android.app.Application;
import android.content.Context;

import com.englishlearn.myapplication.dagger.AppComponent;
import com.englishlearn.myapplication.dagger.AppModule;
import com.englishlearn.myapplication.dagger.DaggerAppComponent;

import cn.bmob.v3.Bmob;


/**
 * Created by yanzl on 16-6-24.
 */
public class MyApplication extends Application{

    public static Context mContext;
    public static MyApplication instance = null;//

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        Bmob.initialize(this, "02b18803d9dbb1956c99ef7896fe4466");
    }
}
