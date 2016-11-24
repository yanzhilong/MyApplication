package com.englishlearn.myapplication.data.source.remote.bmob.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yanzl on 16-8-11.
 */
    public class ServiceFactory {

        private static ServiceFactory INSTANCE;

        private RetrofitService retrofitService;

        public static synchronized ServiceFactory getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new ServiceFactory();
            }
            return INSTANCE;
        }

        public synchronized RetrofitService createRetrofitService(){
            if(retrofitService == null){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RetrofitService.BMOBAPI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                retrofitService = retrofit.create(RetrofitService.class);
            }
            return retrofitService;
        }
    }
