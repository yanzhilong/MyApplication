package com.englishlearn.myapplication.data.source.remote.bmob.service;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yanzl on 16-11-25.
 */

public interface BaiDuSearchService {

    String BAIDUSEARCHPI = "http://www.baidu.com";


    @GET("/s")
    Observable<Response<ResponseBody>> getSearchListByName(@Query("wd") String name,@Query("pn") int skip);
}
