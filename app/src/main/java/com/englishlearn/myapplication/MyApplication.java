package com.englishlearn.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.englishlearn.myapplication.core.LearnEnglishManager;
import com.englishlearn.myapplication.dagger.AppComponent;
import com.englishlearn.myapplication.dagger.AppModule;
import com.englishlearn.myapplication.dagger.DaggerAppComponent;
import com.englishlearn.myapplication.service.LearnService;


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
        LearnEnglishManager.newInstance(this).init();
        init();

    }

    private void init(){
        startService(new Intent(this, LearnService.class));
    }
}
