package com.englishlearn.myapplication.data.source.remote.bmob.service;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammarResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yanzl on 16-8-11.
 */
public interface BmobService{

    String BMOBAPI = "https://api.bmob.cn";

    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Call<BmobSentence> getSentenceById(@Path("id") String id);

    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<BmobSentence> getSentenceRxById(@Path("id") String id);

    @GET("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Call<BmobGrammar> getGrammarById(@Path("id") String id);


    @POST("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<ResponseBody> addSentence(@Body Sentence sentence);


    @POST("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<ResponseBody> addGrammar(@Body Grammar grammar);


    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<BmobSentenceResult> getSentences();

    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<BmobGrammarResult> getGrammars();
}
