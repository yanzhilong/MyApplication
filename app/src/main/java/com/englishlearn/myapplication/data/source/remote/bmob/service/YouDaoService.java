package com.englishlearn.myapplication.data.source.remote.bmob.service;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yanzl on 16-11-25.
 */

public interface YouDaoService {

    String YOUDAOAPI = "http://dict.youdao.com";

    //指操作数据

    @POST("/w/eng/{wordname}/")
    Observable<Response<ResponseBody>> getWordByHtml(@Path("wordname")String wordname);
}
