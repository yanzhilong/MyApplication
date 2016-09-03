package com.englishlearn.myapplication.data.source.remote.bmob.service;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammarResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUser;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUserResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yanzl on 16-8-11.
 */
public interface BmobService{

    String BMOBAPI = "https://api.bmob.cn";

    //注册用户
    @POST("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobCreateUserResult> createUserRx(@Body BmobCreateUserRequest bmobRequestUser);

    @GET("/1/login")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobCreateUserResult> loginRx(@Query("username") String username,@Query("password") String password);

    //根据Id获取用户
    @GET("/1/users/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<BmobUser> getUserByIdRx(@Path("id") String id);

    //根据用户名获取用户
    @GET("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<BmobUserResult> getUserByNameRx(@Query("where")String usernameJson);

    //根据Id获取句子
    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Call<BmobSentence> getSentenceById(@Path("id") String id);

    //根据Id获取句子(Observable)
    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<BmobSentence> getSentenceRxById(@Path("id") String id);

    //根据Id获取语法
    @GET("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Call<BmobGrammar> getGrammarById(@Path("id") String id);

    //根据Id获取语法(Observable)
    @GET("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<BmobGrammar> getGrammarRxById(@Path("id") String id);

    //添加句子
    @POST("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<ResponseBody> addSentence(@Body Sentence sentence);

    //添加句子
    @POST("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> addSentenceRx(@Body Sentence sentence);

    //添加语法
    @POST("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<ResponseBody> addGrammar(@Body Grammar grammar);

    //添加语法
    @POST("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> addGrammarRx(@Body Grammar grammar);

    //获取所有句子
    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<BmobSentenceResult> getSentences();

    //获取所有句子
    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx();

    /**
     * 获取句子(分页)
     * @param limit 取几条
     * @param skip　从第几条开始取
     * @return
     */
    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Query("limit") int limit, @Query("skip")int skip);


    /**
     * 分页搜索一个不完整单词
     * @param searchword 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    /*@GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword}\"}}&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("searchword") String searchword,@Path("limit") int limit, @Query("skip")int skip);*/


    /**
     * 分页搜索自定义的正则
     * @param regex 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Query("where") String regex,@Query("limit")int limit, @Query("skip")int skip);


    /**
     * 分页搜索1个完整单词
     * @param searchword 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword}[., ]\"}}&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("limit") int limit, @Query("skip")int skip,@Path("searchword") String searchword);

    /**
     * 分页搜索2个完整单词
     * @param searchword1 搜索的单词
     * @param searchword2 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword1} .* {searchword2}[., ]\"} }&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("limit") int limit, @Query("skip")int skip,@Path("searchword1") String searchword1,@Path("searchword2") String searchword2);

    /**
     * 分页搜索3个完整单词
     * @param searchword1 搜索的单词
     * @param searchword2 搜索的单词
     * @param searchword3 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword1} .* {searchword2} .* {searchword3}[., ]\"} }&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("limit") int limit, @Query("skip")int skip,@Path("searchword1") String searchword1,@Path("searchword2") String searchword2,@Path("searchword2") String searchword3);

    /**
     * 分页搜索4个完整单词
     * @param searchword1 搜索的单词
     * @param searchword2 搜索的单词
     * @param searchword3 搜索的单词
     * @param searchword4 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword1} .* {searchword2} .* {searchword3} .* {searchword4}[., ]\"} }&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("limit") int limit, @Query("skip")int skip,@Path("searchword1") String searchword1,@Path("searchword2") String searchword2,@Path("searchword2") String searchword3,@Path("searchword2") String searchword4);

    /**
     * 分页搜索4个完整单词
     * @param searchword1 搜索的单词
     * @param searchword2 搜索的单词
     * @param searchword3 搜索的单词
     * @param searchword4 搜索的单词
     * @param searchword5 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Sentence?where={\"content\":{\"$regex\":\"{searchword1} .* {searchword2} .* {searchword3} .* {searchword4} .* {searchword5}[., ]\"} }&limit={limit}&skip={skip})")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobSentenceResult> getSentencesRx(@Path("limit") int limit, @Query("skip")int skip,@Path("searchword1") String searchword1,@Path("searchword2") String searchword2,@Path("searchword2") String searchword3,@Path("searchword2") String searchword4,@Path("searchword2") String searchword5);


    //获取所有语法
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Call<BmobGrammarResult> getGrammars();

    //获取所有语法
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobGrammarResult> getGrammarsRx();

    /**
     * 获取语法(分页)
     * @return
     */
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<BmobGrammarResult> getGrammarsRx(@Query("limit") int limit, @Query("skip")int skip);

    //删除句子
    @DELETE("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentencRxById(@Path("id") String id);

    //删除讲法
    @DELETE("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteGrammarRxById(@Path("id") String id);

    //修改句子
    @PUT("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateSentencRxById(@Path("id") String id,@Body Sentence sentence);

    //修改讲法
    @PUT("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateGrammarRxById(@Path("id") String id,@Body Grammar grammar);

}
