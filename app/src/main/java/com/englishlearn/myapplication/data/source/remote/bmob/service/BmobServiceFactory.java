package com.englishlearn.myapplication.data.source.remote.bmob.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yanzl on 16-8-11.
 */
public class BmobServiceFactory {

    private static BmobService bmobService;

    public static BmobService createBmobService(){
        if(bmobService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BmobService.BMOBAPI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            bmobService = retrofit.create(BmobService.class);
        }
        return bmobService;
    }
}
