package com.englishlearn.myapplication.data.source.remote.bmob.service;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.TractateGroupCollect;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.WordGroupCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateOrLoginUserByPhoneRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.EmailVerify;
import com.englishlearn.myapplication.data.source.remote.bmob.GrammarResult;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetEmail;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetMobile;
import com.englishlearn.myapplication.data.source.remote.bmob.PasswordResetOldPwd;
import com.englishlearn.myapplication.data.source.remote.bmob.PhoneticsSymbolsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.PhoneticsWordsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.QuerySmsResult;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestSmsCode;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestSmsCodeResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SentenceCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SentenceGroupCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SentenceGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SentenceResult;
import com.englishlearn.myapplication.data.source.remote.bmob.SmsCodeVerify;
import com.englishlearn.myapplication.data.source.remote.bmob.TractateCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.TractateGroupCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.TractateGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.TractateResult;
import com.englishlearn.myapplication.data.source.remote.bmob.TractateTypeResult;
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;
import com.englishlearn.myapplication.data.source.remote.bmob.UserResult;
import com.englishlearn.myapplication.data.source.remote.bmob.WordCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.WordGroupCollectResult;
import com.englishlearn.myapplication.data.source.remote.bmob.WordGroupResult;
import com.englishlearn.myapplication.data.source.remote.bmob.WordResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Observable<Response<User>> createUserRx(@Body User user);

    //注册或登陆用户（手机号+验证码）
    @POST("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<User>> createOrLoginUserByPhoneRx(@Body BmobCreateOrLoginUserByPhoneRequest bmobCreateOrLoginUserByPhoneRequest);

    //修改用户
    @PUT("/1/users/{objectId}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<User>> updateUserRx(@Header("X-Bmob-Session-Token") String sessionToken,@Path("objectId")String objectId, @Body User user);

    //登陆用户(用户名密码)
    @GET("/1/login")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<User>> loginRx(@Query("username") String username,@Query("password") String password);

    //根据Id获取用户
    @GET("/1/users/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<User>> getUserByIdRx(@Path("id") String id);

    //根据用户名获取用户,或者邮箱，手机
    @GET("/1/users")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<UserResult>> getUserByNameRx(@Query("where")String usernameJson);


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
    @POST("/1/classes/PhoneticsSymbols")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<PhoneticsSymbols>> addPhoneticsSymbols(@Body PhoneticsSymbols phoneticsSymbols);

    //删除音标
    @DELETE("/1/classes/PhoneticsSymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<ResponseBody>> deletePhoneticsSymbolsById(@Path("id") String id);


    //修改音标
    @PUT("/1/classes/PhoneticsSymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updatePhoneticsSymbolsRxById(@Path("id") String id,@Body PhoneticsSymbols phoneticsSymbols);

    //根据id获取音标
    @GET("/1/classes/PhoneticsSymbols/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<PhoneticsSymbols>> getPhoneticsSymbolsRxById(@Path("id") String id);

    //获取所有音标
    @GET("/1/classes/PhoneticsSymbols")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<PhoneticsSymbolsResult>> getPhoneticsSymbolsRx();


    //音标单词模块****************************************************************************

    //增加音标单词
    @POST("/1/classes/PhoneticsWords")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<PhoneticsWords>> addPhoneticsWords(@Body PhoneticsWords phoneticsWords);

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
    Observable<Response<ResponseBody>> updatePhoneticsWordsRxById(@Path("id") String id, @Body PhoneticsWords phoneticsWords);

    //根据id获取音标单词
    @GET("/1/classes/PhoneticsWords/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<PhoneticsWords>> getPhoneticsWordsRxById(@Path("id") String id);

    //根据音标id获取音标单词
    @GET("/1/classes/PhoneticsWords/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<List<PhoneticsWords>>> getPhoneticsWordsRxByPhoneticsId(@Query("where")String phoneidticsjson);


    //获取所有音标单词
    @GET("/1/classes/PhoneticsWords")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<PhoneticsWordsResult>> getPhoneticsWordsRx();

    //文章分类模块****************************************************************************

    //增加文章分类
    @POST("/1/classes/TractateType")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<TractateType>> addTractateType(@Body TractateType tractateType);

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
    Observable<Response<ResponseBody>> updateTractateTypeRxById(@Path("id") String id,@Body TractateType tractateType);

    //根据id获取文章分类
    @GET("/1/classes/TractateType/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<TractateType>> getTractateTypeRxById(@Path("id") String id);

    //获取所有文章分类
    @GET("/1/classes/TractateType")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<TractateTypeResult>> getTractateTypeRx();

    //单词模块****************************************************************************

    //添加单词
    @POST("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<Word>> addWord(@Body Word word);

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
    Observable<Response<ResponseBody>> updateWordRxById(@Path("id") String id,@Body Word word);

    //根据id获取单词
    @GET("/1/classes/Word/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<Word>> getWordRxById(@Path("id") String id);

    //根据名称获取单词
    @GET("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordResult>> getWordRxByName(@Query("where")String wordjson);

    //查询音标关联收藏分组的所有单词
    @GET("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordResult>> getWordsRxByPhoneticsId( @Query("where") String phoneticsJson);


    //查询收藏分组的所有单词
    @GET("/1/classes/Word")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordResult>> getWordsRxByWordGroupId( @Query("where") String wordgroupidJson, @Query("limit")int limit, @Query("skip")int skip);


    /**
     * 如果您的查询条件某个列值要匹配另一个查询的返回值，举例有一个队伍(Team)保存了每个城市的得分情况且用户表中有一列为用户家乡(hometown), 您可以创建一个查询来寻找用户的家乡是得分大于0.5的城市的所有运动员， 就像这样查询:

     curl -X GET \
     -H "X-Bmob-Application-Id: Your Application ID" \
     -H "X-Bmob-REST-API-Key: Your REST API Key" \
     -G \
     --data-urlencode 'where={"hometown":{"$select":{"query":{"className":"Team","where":{"winPct":{"$gt":0.5}}},"key":"city"}}}' \
     https://api.bmob.cn/1/users
     反之查询Team中得分小于等于0.5的城市的所有运动员，构造查询如下：

     curl -X GET \
     -H "X-Bmob-Application-Id: Your Application ID" \
     -H "X-Bmob-REST-API-Key: Your REST API Key" \
     -G \
     --data-urlencode 'where={"hometown":{"$dontSelect":{"query":{"className":"Team","where":{"winPct":{"$gt":0.5}}},"key":"city"}}}' \
     https://api.bmob.cn/1/users
     * @param
     * @return
     */

    //句子模块****************************************************************************

    //添加句子
    @POST("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<Sentence>> addSentenceRx(@Body Sentence sentence);

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
    Observable<Response<ResponseBody>> updateSentencRxById(@Path("id") String id,@Body Sentence sentence);

    //根据Id获取句子(Observable)
    @GET("/1/classes/Sentence/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<Sentence>> getSentenceRxById(@Path("id") String id);

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
    Observable<Response<SentenceResult>> getSentencesRx(@Query("limit") int limit, @Query("skip")int skip);

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
    Observable<Response<SentenceResult>> getSentencesRx(@Query("where") String regex, @Query("limit")int limit, @Query("skip")int skip);


    //查询句子分组的所有句子
    @GET("/1/classes/Sentence")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<SentenceResult>> getSentencesRxBySentenceGroupId( @Query("where") String sentencegroupidJson, @Query("limit")int limit, @Query("skip")int skip);

    //语法模块****************************************************************************

    //添加语法
    @POST("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<Grammar>> addGrammarRx(@Body Grammar grammar);

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
    Observable<Response<ResponseBody>> updateGrammarRxById(@Path("id") String id,@Body Grammar grammar);

    //根据Id获取语法(Observable)
    @GET("/1/classes/Grammar/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<Grammar>> getGrammarRxById(@Path("id") String id);

    //获取所有语法
    @GET("/1/classes/Grammar")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<GrammarResult>> getGrammarsRx();

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
    Observable<Response<GrammarResult>> getGrammarsRx(@Query("limit") int limit, @Query("skip")int skip);

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
    Observable<Response<GrammarResult>> getGrammarsRx(@Query("where") String regex, @Query("limit")int limit, @Query("skip")int skip);


    //文章模块****************************************************************************

    //添加文章
    @POST("/1/classes/Tractate")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<Tractate>> addTractate(@Body Tractate tractate);

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
    Observable<Response<ResponseBody>> updateTractateRxById(@Path("id") String id,@Body Tractate tractate);

    //根据文章id获取文章
    @GET("/1/classes/Tractate/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<Tractate>> getTractateRxById(@Path("id") String id);


    //根据分类id获取文章列表分页展示
    @GET("/1/classes/Tractate")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<TractateResult>> getTractateRxByTractateTypeId(@Query("where")String tractatetypeidjson, @Query("limit")int limit, @Query("skip")int skip);

    /**
     * 根据分类id,关键词的正则,获取文章列表分页展示
     * @param serachjson 搜索的tractatetypeid
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/Tractate?order=sort")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<TractateResult>> getTractatesRx(@Query("where") String serachjson, @Query("limit")int limit, @Query("skip")int skip);

    //单词收藏分组模块****************************************************************************

    //添加单词收藏分组
    @POST("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<WordGroup>> addWordGroup(@Body WordGroup wordGroup);

    //修改单词收藏分组
    @PUT("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateWordGroupRxById(@Path("id") String id,@Body WordGroup wordGroup);

    //删除单词收藏分组
    @DELETE("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteWordGroupRxById(@Path("id") String id);

    //根据id获取单词收藏分组
    @GET("/1/classes/WordGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<WordGroup>> getWordGroupRxById(@Path("id") String id);

    //根据userId获取单词收藏分组分页展示
    @GET("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getWordGroupRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据userId获取用户所收藏的单词分组分页展示
    @GET("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getCollectWordGroupRxByUserId(@Query("where")String collectAnduserIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据userId获取单词收藏分组分页展示
    @GET("/1/classes/WordGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getWordGroupRxByUserId(@Query("where")String userIdjson);


    //获取所有公开的单词分组分页展示,按时间降序(从近到远)
    @GET("/1/classes/WordGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getWordGroupsByOpenRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);


    //获取所有公开的未收藏单词分组分页展示,按时间降序(从近到远)
    @GET("/1/classes/WordGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getWordGroupsByOpenAndNotCollectRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);


    //单词分組收藏模块****************************************************************************

    //添加单词分組收藏
    @POST("/1/classes/WordGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<WordGroupCollect>> addWordGroupCollect(@Body WordGroupCollect wordGroupCollect);

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
    Observable<Response<WordGroupCollectResult>> getWordGroupCollectRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);

    //根据userId和单詞wvxe获取单词分組收藏,分页展示
    @GET("/1/classes/WordGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupCollectResult>> getWordGroupCollectRxByUserIdAndwordGroupId(@Query("where")String userIdjson);


    //句子收藏分组模块****************************************************************************

    //添加句子收藏分组
    @POST("/1/classes/SentenceGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<SentenceGroup>> addSentenceGroup(@Body SentenceGroup sentenceGroup);

    //修改句子收藏分组
    @PUT("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateSentenceGroupRxById(@Path("id") String id,@Body SentenceGroup sentenceGroup);

    //删除句子收藏分组
    @DELETE("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteSentenceGroupRxById(@Path("id") String id);


    //根据id获取句子收藏分组
    @GET("/1/classes/SentenceGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<SentenceGroup>> getSentenceGroupRxById(@Path("id") String id);


    //根据userId获取句子收藏分组，分页展示
    @GET("/1/classes/SentenceGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<SentenceGroupResult>> getSentenceGroupRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


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
    Observable<Response<SentenceGroupResult>> getSentenceGroupsByOpenRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);


    //获取所有公开的未收藏单词分组分页展示,按时间降序(从近到远)
    @GET("/1/classes/SentenceGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordGroupResult>> getSentenceGroupsByOpenAndNotCollectRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);

    //句子分組收藏模块****************************************************************************

    //添加句子分組收藏
    @POST("/1/classes/SentenceGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<SentenceGroupCollect>> addSentenceGroupCollect(@Body SentenceGroupCollect sentenceGroupCollect);

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
    Observable<Response<SentenceGroupCollectResult>> getSentenceGroupCollectRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据userId和单詞wvxe获取单词分組收藏,分页展示
    @GET("/1/classes/SentenceGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<SentenceGroupCollectResult>> getSentenceGroupCollectRxByUserIdAndsentenceGroupId(@Query("where")String userIdAndwordgroupIdjson);

    //文章收藏分组模块****************************************************************************

    //添加文章收藏分组
    @POST("/1/classes/TractateGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<TractateGroup>> addTractateGroup(@Body TractateGroup tractateGroup);

    //修改文章收藏分组
    @PUT("/1/classes/TractateGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json",
    })
    Observable<Response<ResponseBody>> updateTractateGroupRxById(@Path("id") String id,@Body TractateGroup tractateGroup);

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
    Observable<Response<TractateGroupResult>> getTractateGroupsRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据userId获取文章收藏分组
    @GET("/1/classes/TractateGroup")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<TractateGroupResult>> getTractateGroupsRxByUserId(@Query("where")String userIdjson);


    //根据id获取文章收藏分组
    @GET("/1/classes/TractateGroup/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<TractateGroup>> getTractateGroupRxById(@Path("id") String id);


    //获取所有公开的未收藏单词分组分页展示,按时间降序(从近到远)
    @GET("/1/classes/TractateGroup?order=-createdAt")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<TractateGroupResult>> getTractateGroupsByOpenAndNotCollectRx(@Query("where") String openjson, @Query("limit")int limit, @Query("skip")int skip);

    //文章分組收藏模块****************************************************************************

    //增加文章分組收藏
    @POST("/1/classes/TractateGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<TractateGroupCollect>> addTractateGroupCollect(@Body TractateGroupCollect tractateGroupCollect);

    //删除文章分組收藏
    @DELETE("/1/classes/TractateGroupCollect/{id}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
    })
    Observable<Response<ResponseBody>> deleteTractateGroupCollectRxById(@Path("id") String id);

    //根据userId获取文章分组收藏分页展示
    @GET("/1/classes/TractateGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<TractateGroupCollectResult>> getTractateGroupCollectRxByUserId(@Query("where")String userIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //根据userId和句子分组id获取文章分组收藏分页展示(一般是只返回一个的)
    @GET("/1/classes/TractateGroupCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<TractateGroupCollectResult>> getTractateGroupCollectRxByUserIdAndsentenceGroupId(@Query("where")String userIdAndtractategroupIdjson);



    //单词收藏模块****************************************************************************

    //添加单词收藏
    @POST("/1/classes/WordCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<WordCollect>> addWordCollect(@Body WordCollect wordCollect);

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
    Observable<Response<WordCollectResult>> getWordCollectRxByUserIdAndWordGroupId(@Query("where")String userIdwordgroupIdjson, @Query("limit")int limit, @Query("skip")int skip);

    /**
     * 根据单词分組Id获取单词收藏，分页
     * @param wordgroupIdjson 单词分組Id
     * @param limit 取几条
     * @param skip 从第几条取
     * @return
     */
    @GET("/1/classes/WordCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09"
    })
    Observable<Response<WordCollectResult>> getWordCollectRxByWordGroupId(@Query("where")String wordgroupIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //句子收藏模块****************************************************************************

    //添加句子收藏
    @POST("/1/classes/SentenceCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<SentenceCollect>> addSentenceCollect(@Body SentenceCollect sentenceCollect);

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
    Observable<Response<SentenceCollectResult>> getSentenceCollectRxByUserIdAndSentenceGroupId(@Query("where")String userIdsentencegroupIdjson, @Query("limit")int limit, @Query("skip")int skip);


    //文章收藏模块****************************************************************************

    //添加文章收藏
    @POST("/1/classes/TractateCollect")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: application/json"
    })
    Observable<Response<TractateCollect>> addTractateCollect(@Body TractateCollect tractateCollect);

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
    Observable<Response<TractateCollectResult>> getTractateCollectRxByUserIdAndTractateGroupId(@Query("where")String userIdwordgroupIdjson, @Query("limit")int limit, @Query("skip")int skip);

    //上传模块****************************************************************************

    @Multipart
    @POST("/2/files/{fileName}/")
    @Headers({
            "X-Bmob-Application-Id: 02b18803d9dbb1956c99ef7896fe4466",
            "X-Bmob-REST-API-Key: 4c7b2adda2785883c546efdfbfd6ca09",
            "Content-Type: audio/mp3"
    })
    Observable<Response<UploadFile>> uploadFile(@Path("fileName") String fileName, @Part MultipartBody.Part file);
}
