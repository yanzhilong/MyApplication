package com.englishlearn.myapplication.data.source.remote.bmob.service;

import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateGrammarRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateMsSourceRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateOrLoginUserByPhoneRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreatePhoneticsSymbolsRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreatePhoneticsWordsRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateSentenceCollectRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateSentenceGroupCollectRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateSentenceGroupRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateSentenceRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateTractateCollectRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateTractateGroupRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateTractateRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateTractateTypeRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateWordCollectRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateWordGroupCollectRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateWordGroupRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateWordRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammarResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobMsSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobMsSourceResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobPhoneticsSymbols;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobPhoneticsSymbolsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobPhoneticsWords;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobPhoneticsWordsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceGroup;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceGroupCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceGroupCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentenceResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractate;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateGroup;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateType;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobTractateTypeResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUser;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUserResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWord;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordGroup;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordGroupCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordGroupCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobWordResult;
import com.englishlearn.myapplication.data.source.remote.bmob.EmailVerify;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetEmail;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetMobile;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetOldPwd;
import com.englishlearn.myapplication.data.source.remote.bmob.QuerySmsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestSmsCode;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestSmsCodeResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SmsCodeVerify;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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


    //用户模块****************************************************************************
    //注册用户
    @POST("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobCreateUserResult>> createUserRx(@Body BmobCreateUserRequest bmobRequestUser);

    //注册或登陆用户（手机号+验证码）
    @POST("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobUser>> createOrLoginUserByPhoneRx(@Body BmobCreateOrLoginUserByPhoneRequest bmobCreateOrLoginUserByPhoneRequest);

    //修改用户
    @PUT("/1/users/{objectId}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobUser>> updateUserRx(@Header("X-Bmob-Session-Token") String sessionToken,@Path("objectId")String objectId, @Body BmobCreateUserRequest bmobCreateUserRequest);

    //登陆用户(用户名密码)
    @GET("/1/login")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobUser>> loginRx(@Query("username") String username,@Query("password") String password);

    //根据Id获取用户
    @GET("/1/users/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobUser>> getUserByIdRx(@Path("id") String id);
    
    //根据用户名获取用户,或者邮箱，手机
    @GET("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobUserResult>> getUserByNameRx(@Query("where")String usernameJson);


    /**
     * 通过邮件重置密码
     * @param passwordResetEmail
     * @return
     */
    @POST("/1/requestPasswordReset")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> passwordResetByEmail(@Body PasswordResetEmail passwordResetEmail);

    /**
     * 通过手机验证码重置密码
     * @param smsCode
     * @param passwordResetMobile
     * @return
     */
    @PUT("/1/resetPasswordBySmsCode/{smsCode}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> passwordResetByMobile(@Path("smsCode")String smsCode, @Body PasswordResetMobile passwordResetMobile);

    /**
     * 修改密码，使用旧密码
     * @param sessionToken
     * @param objectId
     * @param passwordResetOldPwd
     * @return
     */
    @PUT("/1/updateUserPassword/{objectId}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> passwordResetByOldPassword(@Header("X-Bmob-Session-Token") String sessionToken, @Path("objectId")String objectId, @Body PasswordResetOldPwd passwordResetOldPwd);

    /**
     * 请求短信验证码
     * @param requestSmsCode
     * @return
     */
    @POST("/1/requestSmsCode")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<RequestSmsCodeResult>> requestSmsCode(@Body RequestSmsCode requestSmsCode);


    /**
     * 查询短信发送状态
     * @param smsId
     * @return
     */
    @GET("/1/querySms/{smsId}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<QuerySmsResult> querySms(@Path("smsId")String smsId);

    /**
     * 验证邮箱
     * @param emailVerify
     * @return
     */
    @POST("/1/requestEmailVerify")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> emailVerify(@Body EmailVerify emailVerify);

    /**
     * 验证短信验证码
     * @param smsCodeVerify
     * @return
     */
    @POST("/1/verifySmsCode/{smsCode}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> smsCodeVerify(@Path("smsCode") String smsCode,@Body SmsCodeVerify smsCodeVerify);

    //音标模块****************************************************************************

    //增加音标
    @POST("/1/classes/phoneticssymbols")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobPhoneticsSymbols>> addPhoneticsSymbols(@Body BmobCreatePhoneticsSymbolsRequest bmobCreatePhoneticsSymbolsRequest);

    //删除音标
    @DELETE("/1/classes/phoneticssymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deletePhoneticsSymbolsById(@Path("id") String id);


    //修改音标
    @PUT("/1/classes/phoneticssymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updatePhoneticsSymbolsRxById(@Path("id") String id,@Body BmobCreatePhoneticsSymbolsRequest bmobCreatePhoneticsSymbolsRequest);

    //根据id获取音标
    @GET("/1/classes/phoneticssymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobPhoneticsSymbols>> getPhoneticsSymbolsRxById(@Path("id") String id);

    //获取所有音标
    @GET("/1/classes/phoneticssymbols")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobPhoneticsSymbolsResult>> getPhoneticsSymbolsRx();


    //音标单词模块****************************************************************************

    //增加音标单词
    @POST("/1/classes/PhoneticsWords")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobPhoneticsWords>> addPhoneticsWords(@Body BmobCreatePhoneticsWordsRequest bmobCreatePhoneticsWordsRequest);

    //删除音标单词
    @DELETE("/1/classes/PhoneticsWords/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deletePhoneticsWordsById(@Path("id") String id);


    //修改音标单词
    @PUT("/1/classes/PhoneticsWords/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updatePhoneticsWordsRxById(@Path("id") String id, @Body BmobCreatePhoneticsWordsRequest bmobCreatePhoneticsWordsRequest);

    //根据id获取音标单词
    @GET("/1/classes/PhoneticsWords/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobPhoneticsWords>> getPhoneticsWordsRxById(@Path("id") String id);

    //根据音标id获取音标单词
    @GET("/1/classes/PhoneticsWords/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobPhoneticsWords>> getPhoneticsWordsRxByPhoneticsId(@Query("where")String phoneidticsjson);


    //获取所有音标单词
    @GET("/1/classes/PhoneticsWords")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobPhoneticsWordsResult>> getPhoneticsWordsRx();



    //信息来源模块****************************************************************************

    //增加信息来源
    @POST("/1/classes/Mssource")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobMsSource>> addMssource(@Body BmobCreateMsSourceRequest bmobCreateMsSourceRequest);

    //删除信息来源
    @DELETE("/1/classes/Mssource/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deleteMssourceById(@Path("id") String id);


    //修改信息来源
    @PUT("/1/classes/Mssource/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateMssourceRxById(@Path("id") String id,@Body BmobCreateMsSourceRequest bmobCreateMsSourceRequest);

    //根据id获取信息来源
    @GET("/1/classes/Mssource/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobMsSource>> getMssourceRxById(@Path("id") String id);

    //获取所有信息来源
    @GET("/1/classes/Mssource")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobMsSourceResult>> getMssourceRx();

    //文章分类模块****************************************************************************

    //增加文章分类
    @POST("/1/classes/TractateType")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobTractateType>> addTractateType(@Body BmobCreateTractateTypeRequest bmobCreateTractateTypeRequest);

    //删除文章分类
    @DELETE("/1/classes/TractateType/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deleteTractateTypeById(@Path("id") String id);

    //修改文章分类
    @PUT("/1/classes/TractateType/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateTractateTypeRxById(@Path("id") String id,@Body BmobCreateTractateTypeRequest bmobCreateTractateTypeRequest);

    //根据id获取文章分类
    @GET("/1/classes/TractateType/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobTractateType>> getTractateTypeRxById(@Path("id") String id);

    //获取所有文章分类
    @GET("/1/classes/TractateType")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobTractateTypeResult>> getTractateTypeRx();

    //单词模块****************************************************************************

    //添加单词
    @POST("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobWord>> addWord(@Body BmobCreateWordRequest bmobCreateWordRequest);

    //删除单词
    @DELETE("/1/classes/Word/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deleteWordById(@Path("id") String id);

    //修改单词
    @PUT("/1/classes/Word/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateWordRxById(@Path("id") String id,@Body BmobCreateWordRequest bmobCreateWordRequest);

    //根据id获取单词
    @GET("/1/classes/Word/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobWord>> getWordRxById(@Path("id") String id);

    //根据名称获取单词
    @GET("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobWordResult>> getWordRxByName(@Query("where")String wordjson);


    //句子模块****************************************************************************

    //添加句子
    @POST("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobSentence>> addSentenceRx(@Body BmobCreateSentenceRequest bmobCreateSentenceRequest);

    //删除句子
    @DELETE("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentenceRxById(@Path("id") String id);

    //修改句子
    @PUT("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateSentencRxById(@Path("id") String id,@Body BmobCreateSentenceRequest bmobCreateSentenceRequest);

    //根据Id获取句子(Observable)
    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobSentence>> getSentenceRxById(@Path("id") String id);

    /**
     * 获取所有句子(分页)
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
    Observable<Response<BmobSentenceResult>> getSentencesRx(@Query("limit") int limit, @Query("skip")int skip);

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
    Observable<Response<BmobSentenceResult>> getSentencesRx(@Query("where") String regex,@Query("limit")int limit, @Query("skip")int skip);

    //语法模块****************************************************************************

    //添加语法
    @POST("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobGrammar>> addGrammarRx(@Body BmobCreateGrammarRequest bmobCreateGrammarRequest);

    //删除語法
    @DELETE("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteGrammarRxById(@Path("id") String id);

    //修改讲法
    @PUT("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateGrammarRxById(@Path("id") String id,@Body BmobCreateGrammarRequest bmobCreateGrammarRequest);

    //根据Id获取语法(Observable)
    @GET("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobGrammar>> getGrammarRxById(@Path("id") String id);

    //获取所有语法
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobGrammarResult>> getGrammarsRx();

    /**
     * 获取所有语法(分页)
     * @return
     */
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobGrammarResult>> getGrammarsRx(@Query("limit") int limit, @Query("skip")int skip);

    /**
     * 分页搜索自定义的正则
     * @param regex 搜索的单词
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobGrammarResult>> getGrammarsRx(@Query("where") String regex,@Query("limit")int limit, @Query("skip")int skip);


    //文章模块****************************************************************************

    //添加文章
    @POST("/1/classes/Tractate")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobTractate>> addTractate(@Body BmobCreateTractateRequest bmobCreateTractateRequest);

    //删除文章
    @DELETE("/1/classes/Tractate/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteTractateRxById(@Path("id") String id);

    //修改文章
    @PUT("/1/classes/Tractate/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateTractateRxById(@Path("id") String id,@Body BmobCreateTractateRequest bmobCreateTractateRequest);

    //根据文章id获取文章
    @GET("/1/classes/Tractate/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobTractate>> getTractateRxById(@Path("id") String id);


    //根据分类id获取文章列表分页展示
    @GET("/1/classes/Tractate")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobTractateResult>> getTractateRxByTractateTypeId(@Query("where")String tractatetypeidjson,@Query("limit")int limit, @Query("skip")int skip);

    /**
     * 根据分类id,关键词的正则,获取文章列表分页展示
     * @param serachjson 搜索的tractatetypeid
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Tractate")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobTractateResult>> getTractatesRx(@Query("where") String serachjson,@Query("limit")int limit, @Query("skip")int skip);

    //单词收藏分组模块****************************************************************************

    //添加单词收藏分组
    @POST("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobWordGroup>> addWordGroup(@Body BmobCreateWordGroupRequest bmobCreateWordGroupRequest);

    //修改单词收藏分组
    @PUT("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateWordGroupRxById(@Path("id") String id,@Body BmobCreateWordGroupRequest bmobCreateWordGroupRequest);

    //删除单词收藏分组
    @DELETE("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteWordGroupRxById(@Path("id") String id);

    //根据userId获取单词收藏分组分页展示
    @GET("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobWordGroupResult>> getWordGroupRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);

    //获取所有公开的单词收藏分组分页展示,按时间降序(从近到远)
    @GET("/1/classes/WordGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobWordGroupResult>> getWordGroupsByOpenRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);

    //根据id获取单词收藏分组
    @GET("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobWordGroup>> getWordGroupRxById(@Path("id") String id);

    //单词分組收藏模块****************************************************************************

    //添加单词分組收藏
    @POST("/1/classes/WordGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobWordGroupCollect>> addWordGroupCollect(@Body BmobCreateWordGroupCollectRequest bmobCreateWordGroupCollectRequest);

    //删除单词分組收藏
    @DELETE("/1/classes/WordGroupCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteWordGroupCollectRxById(@Path("id") String id);

    //根据userId获取单词分組收藏,分页展示
    @GET("/1/classes/WordGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobWordGroupCollectResult>> getWordGroupCollectRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);

    //句子收藏分组模块****************************************************************************

    //添加句子收藏分组
    @POST("/1/classes/SentenceGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobSentenceGroup>> addSentenceGroup(@Body BmobCreateSentenceGroupRequest bmobCreateSentenceGroupRequest);

    //修改句子收藏分组
    @PUT("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateSentenceGroupRxById(@Path("id") String id,@Body BmobCreateSentenceGroupRequest bmobCreateSentenceGroupRequest);

    //删除句子收藏分组
    @DELETE("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentenceGroupRxById(@Path("id") String id);

    //根据userId获取句子收藏分组，分页展示
    @GET("/1/classes/SentenceGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobSentenceGroupResult>> getSentenceGroupRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    /**
     * 获取所有公开的句子收藏分组
     * @param openjson 公开的json
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/SentenceGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobSentenceGroupResult>> getSentenceGroupsByOpenRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);

    //根据id获取句子收藏分组
    @GET("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobSentenceGroup>> getSentenceGroupRxById(@Path("id") String id);

    //句子分組收藏模块****************************************************************************

    //添加句子分組收藏
    @POST("/1/classes/SentenceGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobSentenceGroupCollect>> addSentenceGroupCollect(@Body BmobCreateSentenceGroupCollectRequest bmobCreateSentenceGroupCollectRequest);

    //删除句子分組收藏
    @DELETE("/1/classes/SentenceGroupCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentenceGroupCollectRxById(@Path("id") String id);

    //根据userId获取句子分組收藏
    @GET("/1/classes/SentenceGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobSentenceGroupCollectResult>> getSentenceGroupCollectRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //文章收藏分组模块****************************************************************************

    //添加文章收藏分组
    @POST("/1/classes/TractateGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobTractateGroup>> addTractateGroup(@Body BmobCreateTractateGroupRequest bmobCreateTractateGroupRequest);

    //修改文章收藏分组
    @PUT("/1/classes/TractateGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateTractateGroupRxById(@Path("id") String id,@Body BmobCreateTractateGroupRequest bmobCreateTractateGroupRequest);

    //删除文章收藏分组
    @DELETE("/1/classes/TractateGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteTractateGroupRxById(@Path("id") String id);

    //根据userId获取文章收藏分组
    @GET("/1/classes/TractateGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobTractateGroupResult>> getTractateGroupsRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据id获取文章收藏分组
    @GET("/1/classes/TractateGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<BmobTractateGroup>> getTractateGroupRxById(@Path("id") String id);


    //单词收藏模块****************************************************************************

    //添加单词收藏
    @POST("/1/classes/WordCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobWordCollect>> addWordCollect(@Body BmobCreateWordCollectRequest bmobCreateWordCollectRequest);

    //删除单词收藏
    @DELETE("/1/classes/WordCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteWordCollectRxById(@Path("id") String id);


    /**
     * 根据userId和单词分組Id获取单词收藏，分页
     * @param userIdwordgroupIdjson userId和单词分組Id
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/WordCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobWordCollectResult>> getWordCollectRxByUserIdAndWordGroupId(@Query("where")String userIdwordgroupIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //句子收藏模块****************************************************************************

    //添加句子收藏
    @POST("/1/classes/SentenceCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobSentenceCollect>> addSentenceCollect(@Body BmobCreateSentenceCollectRequest bmobCreateSentenceCollectRequest);

    //删除句子收藏
    @DELETE("/1/classes/SentenceCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentenceCollectRxById(@Path("id") String id);


    /**
     * 根据userId和句子分組Id获取句子收藏，分页
     * @param userIdsentencegroupIdjson userId和句子分組Id
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/SentenceCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobSentenceCollectResult>> getSentenceCollectRxByUserIdAndSentenceGroupId(@Query("where")String userIdsentencegroupIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //文章收藏模块****************************************************************************

    //添加文章收藏
    @POST("/1/classes/TractateCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<BmobTractateCollect>> addTractateCollect(@Body BmobCreateTractateCollectRequest bmobCreateTractateCollectRequest);

    //删除文章收藏
    @DELETE("/1/classes/TractateCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteTractateCollectRxById(@Path("id") String id);


    /**
     * 根据userId和文章分組Id获取文章，分页
     * @param userIdwordgroupIdjson userId和单词分組Id
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/TractateCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<BmobTractateCollectResult>> getTractateCollectRxByUserIdAndTractateGroupId(@Query("where")String userIdwordgroupIdjson, @Query("limit")int limit, @Query("skip")int skip);

}
