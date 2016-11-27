package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateCollectGroup;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.TractateGroupCollect;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.WordGroupCollect;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.service.IcibaService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.RetrofitService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;
import com.englishlearn.myapplication.data.source.remote.bmob.service.YouDaoService;
import com.englishlearn.myapplication.util.RxUtil;
import com.englishlearn.myapplication.util.SearchUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobDataSource implements RemoteData {

    private static final String TAG = BmobDataSource.class.getSimpleName();

    private static BmobDataSource INSTANCE;
    private RetrofitService bmobService;//请求接口
    private YouDaoService youDaoService;//请求接口
    private IcibaService baiDuService;//百度接口

    private SearchUtil searchUtil;

    public static BmobDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BmobDataSource();
        }
        return INSTANCE;
    }

    public BmobDataSource(){
        bmobService = ServiceFactory.getInstance().createRetrofitService();
        youDaoService = ServiceFactory.getInstance().createYouDaoService();
        baiDuService = ServiceFactory.getInstance().createIcibaService();
        searchUtil = SearchUtil.getInstance();
    }

    public BmobDataSource(RetrofitService bmobService) {
        this.bmobService = bmobService;
        searchUtil = SearchUtil.getInstance();
    }

    /*@Override
    public Observable<List<Sentence>> getSentencesRx(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip);
        return bmobService.getSentencesRx(limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Response<BmobSentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<BmobSentenceResult> bmobSentenceResult) {
                        List<BmobSentence> list = bmobSentenceResult.getResults();
                        List<Sentence> sentences = new ArrayList<>();
                        for(BmobSentence bmobSentence : list){
                            Sentence sentence = new Sentence();
                            sentence.setId(bmobSentence.getObjectId());
                            sentence.setSentenceId(bmobSentence.getSentenceId());
                            sentence.setContent(bmobSentence.getContent());
                            sentence.setTranslation(bmobSentence.getTranslation());
                            sentence.setCreateDate(bmobSentence.getCreateDate());
                            sentence.setRemark(bmobSentence.getRemark());
                            sentence.setSource(bmobSentence.getSource());
                            sentence.setTractateId(bmobSentence.getTractateId());
                            sentence.setUserId(bmobSentence.getUserId());
                            sentences.add(sentence);
                        }
                        return Observable.just(sentences);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(String searchword, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSearchSentenceRegex(searchword);
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + searchword);
        return bmobService.getSentencesRx(regex,limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BmobSentenceResult, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(BmobSentenceResult bmobSentenceResult) {
                        List<BmobSentence> list = bmobSentenceResult.getResults();
                        List<Sentence> sentences = new ArrayList<>();
                        for(BmobSentence bmobSentence : list){
                            Sentence sentence = new Sentence();
                            sentence.setId(bmobSentence.getObjectId());
                            sentence.setSentenceId(bmobSentence.getSentenceId());
                            sentence.setContent(bmobSentence.getContent());
                            sentence.setTranslation(bmobSentence.getTranslation());
                            sentence.setCreateDate(bmobSentence.getCreateDate());
                            sentence.setRemark(bmobSentence.getRemark());
                            sentence.setSource(bmobSentence.getSource());
                            sentence.setTractateId(bmobSentence.getTractateId());
                            sentence.setUserId(bmobSentence.getUserId());
                            sentences.add(sentence);
                        }
                        return Observable.just(sentences);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx() {
        return bmobService.getGrammarsRx()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BmobGrammarResult, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(BmobGrammarResult bmobGrammarResult) {
                        List<BmobGrammar> list = bmobGrammarResult.getResults();
                        List<Grammar> grammars = new ArrayList<>();
                        for(BmobGrammar bmobGrammar : list){
                            Grammar grammar = new Grammar();
                            grammar.setId(bmobGrammar.getObjectId());
                            grammar.setGrammarId(bmobGrammar.getGrammarId());
                            grammar.setContent(bmobGrammar.getContent());
                            grammar.setCreateDate(bmobGrammar.getCreateDate());
                            grammar.setTitle(bmobGrammar.getTitle());
                            grammar.setUserId(bmobGrammar.getUserId());
                            grammar.setRemark(bmobGrammar.getRemark());
                            grammars.add(grammar);
                        }
                        return Observable.just(grammars)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx(int page, int pageSize) {
        if(page < 1){
            throw new RuntimeException("The page shoule be above 0");
        }
        int limit = pageSize;
        int skip = (page - 1) * pageSize;
        return bmobService.getGrammarsRx(limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BmobGrammarResult, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(BmobGrammarResult bmobGrammarResult) {
                        List<BmobGrammar> list = bmobGrammarResult.getResults();
                        List<Grammar> grammars = new ArrayList<>();
                        for(BmobGrammar bmobGrammar : list){
                            Grammar grammar = new Grammar();
                            grammar.setId(bmobGrammar.getObjectId());
                            grammar.setGrammarId(bmobGrammar.getGrammarId());
                            grammar.setContent(bmobGrammar.getContent());
                            grammar.setCreateDate(bmobGrammar.getCreateDate());
                            grammar.setTitle(bmobGrammar.getTitle());
                            grammar.setUserId(bmobGrammar.getUserId());
                            grammar.setRemark(bmobGrammar.getRemark());
                            grammars.add(grammar);
                        }
                        return Observable.just(grammars)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    @Override
    public Observable<Sentence> getSentenceRxById(String id) {
        return bmobService.getSentenceRxById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BmobSentence, Observable<Sentence>>() {
                    @Override
                    public Observable<Sentence> call(BmobSentence bmobSentence) {
                        Sentence sentence = new Sentence();
                        sentence.setId(bmobSentence.getObjectId());
                        sentence.setSentenceId(bmobSentence.getSentenceId());
                        sentence.setContent(bmobSentence.getContent());
                        sentence.setTranslation(bmobSentence.getTranslation());
                        sentence.setCreateDate(bmobSentence.getCreateDate());
                        sentence.setRemark(bmobSentence.getRemark());
                        sentence.setSource(bmobSentence.getSource());
                        sentence.setTractateId(bmobSentence.getTractateId());
                        sentence.setUserId(bmobSentence.getUserId());
                        return Observable.just(sentence);
                    }
                });
    }

    @Override
    public Observable<Grammar> getGrammarRxById(String id) {
        return bmobService.getGrammarRxById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BmobGrammar, Observable<Grammar>>() {
                    @Override
                    public Observable<Grammar> call(BmobGrammar bmobGrammar) {
                        Grammar grammar = new Grammar();
                        grammar.setId(bmobGrammar.getObjectId());
                        grammar.setGrammarId(bmobGrammar.getGrammarId());
                        grammar.setContent(bmobGrammar.getContent());
                        grammar.setCreateDate(bmobGrammar.getCreateDate());
                        grammar.setTitle(bmobGrammar.getTitle());
                        grammar.setUserId(bmobGrammar.getUserId());
                        grammar.setRemark(bmobGrammar.getRemark());
                        return Observable.just(grammar);
                    }
                });
    }

    @Override
    public Observable<Boolean> addSentenceRx(Sentence sentence) {

        BmobCreateSentenceRequest bmobCreateSentenceRequest = new BmobCreateSentenceRequest();

        bmobCreateSentenceRequest.setSentenceId(sentence.getSentenceId());
        bmobCreateSentenceRequest.setContent(sentence.getContent());
        bmobCreateSentenceRequest.setTranslation(sentence.getTranslation());
        bmobCreateSentenceRequest.setCreateDate(sentence.getCreateDate());
        bmobCreateSentenceRequest.setRemark(sentence.getRemark());
        bmobCreateSentenceRequest.setSource(sentence.getSource());
        bmobCreateSentenceRequest.setTractateId(sentence.getTractateId());
        bmobCreateSentenceRequest.setUserId(sentence.getUserId());

        return bmobService.addSentenceRx(bmobCreateSentenceRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<BmobSentence>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<BmobSentence> responseBodyResponse) {
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> addGrammarRx(Grammar grammar) {

        BmobCreateGrammarRequest bmobCreateGrammarRequest = new BmobCreateGrammarRequest();
        bmobCreateGrammarRequest.setGrammarId(grammar.getGrammarId());
        bmobCreateGrammarRequest.setContent(grammar.getContent());
        bmobCreateGrammarRequest.setCreateDate(grammar.getCreateDate());
        bmobCreateGrammarRequest.setTitle(grammar.getTitle());
        bmobCreateGrammarRequest.setUserId(grammar.getUserId());
        bmobCreateGrammarRequest.setRemark(grammar.getRemark());

        return bmobService.addGrammarRx(bmobCreateGrammarRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<BmobGrammar>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<BmobGrammar> responseBodyResponse) {
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> updateSentenceRx(Sentence sentence) {

        BmobCreateSentenceRequest bmobCreateSentenceRequest = new BmobCreateSentenceRequest();

        bmobCreateSentenceRequest.setSentenceId(sentence.getSentenceId());
        bmobCreateSentenceRequest.setContent(sentence.getContent());
        bmobCreateSentenceRequest.setTranslation(sentence.getTranslation());
        bmobCreateSentenceRequest.setCreateDate(sentence.getCreateDate());
        bmobCreateSentenceRequest.setRemark(sentence.getRemark());
        bmobCreateSentenceRequest.setSource(sentence.getSource());
        bmobCreateSentenceRequest.setTractateId(sentence.getTractateId());
        bmobCreateSentenceRequest.setUserId(sentence.getUserId());

        return bmobService.updateSentencRxById(sentence.getId(),bmobCreateSentenceRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> updateGrammarRx(Grammar grammar) {

        BmobCreateGrammarRequest bmobCreateGrammarRequest = new BmobCreateGrammarRequest();
        bmobCreateGrammarRequest.setGrammarId(grammar.getGrammarId());
        bmobCreateGrammarRequest.setContent(grammar.getContent());
        bmobCreateGrammarRequest.setCreateDate(grammar.getCreateDate());
        bmobCreateGrammarRequest.setTitle(grammar.getTitle());
        bmobCreateGrammarRequest.setUserId(grammar.getUserId());
        bmobCreateGrammarRequest.setRemark(grammar.getRemark());

        return bmobService.updateGrammarRxById(grammar.getId(),bmobCreateGrammarRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }

    @Override
    public Observable<Boolean> deleteSentenceRxById(String id) {
        return bmobService.deleteSentencRxById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    return Observable.just(false);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> deleteGrammarRxById(String id) {
        return bmobService.deleteGrammarRxById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    return Observable.just(false);
                }
            }
        });
    }*/

    @Override
    public Observable<User> register(final User user) {

        return bmobService.createUserRx(user).flatMap(new Func1<Response<User>, Observable<User>>() {
            @Override
            public Observable<User> call(Response<User> userResponse) {
                Log.d(TAG,"register->call" + Thread.currentThread().getName());

                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.CREATEUSER.getDefauleError().getMessage());
                if(userResponse.isSuccessful()){
                    User user = userResponse.body();
                    return Observable.just(user);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  userResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.CREATEUSER createuser = RemoteCode.CREATEUSER.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<User> createOrLoginUserByPhoneRx(String phone, String smscode) {

        BmobCreateOrLoginUserByPhoneRequest bmobCreateOrLoginUserByPhoneRequest = new BmobCreateOrLoginUserByPhoneRequest();
        bmobCreateOrLoginUserByPhoneRequest.setMobilePhoneNumber(phone);
        bmobCreateOrLoginUserByPhoneRequest.setSmsCode(smscode);

        return bmobService.createOrLoginUserByPhoneRx(bmobCreateOrLoginUserByPhoneRequest).flatMap(new Func1<Response<User>, Observable<User>>() {
            @Override
            public Observable<User> call(Response<User> userResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.CREATEUSER.getDefauleError().getMessage());
                if(userResponse.isSuccessful()){
                    User user = userResponse.body();
                    return Observable.just(user);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  userResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.CREATEUSER createuser = RemoteCode.CREATEUSER.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<Boolean> checkUserName(String name) {
        return null;
    }

    @Override
    public Observable<Boolean> checkEmail(String email) {
        return null;
    }

    @Override
    public Observable<Boolean> checkMobile(String mobile) {
        return null;
    }

    @Override
    public Observable<User> getUserByIdRx(String id) {
        return bmobService.getUserByIdRx(id)
                .flatMap(new Func1<Response<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<User> userResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(userResponse.isSuccessful()){
                            User user = userResponse.body();
                            return Observable.just(user);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  userResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON updateuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(updateuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<User> getUserByName(String name) {
        String namejson = searchUtil.getUserByNameRegex(name);
        return bmobService.getUserByNameRx(namejson)
                .flatMap(new Func1<Response<UserResult>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<UserResult> userResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(userResultResponse.isSuccessful()){
                            UserResult userResult = userResultResponse.body();
                            List<User> users = userResult.getResults();
                            if(users != null && users.size() > 0){
                                User user = users.get(0);

                                return Observable.just(user);
                            }else{
                                return Observable.error(new NullPointerException());
                            }
                            
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  userResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<User> getUserByEmail(String email) {
        return null;
    }

    @Override
    public Observable<User> getUserByMobile(String mobile) {
        return null;
    }

    @Override
    public Observable<User> login(String name, String password) {
        return bmobService.loginRx(name,password)
                .flatMap(new Func1<Response<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<User> userResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                        if(userResponse.isSuccessful()){
                            User user = userResponse.body();
                            return Observable.just(user);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  userResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.LOGINUSER loginuser = RemoteCode.LOGINUSER.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(loginuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<User> updateUser(User user) {

        return bmobService.updateUserRx(user.getSessionToken(),user.getObjectId(),user)
        .flatMap(new Func1<Response<User>, Observable<User>>() {
            @Override
            public Observable<User> call(Response<User> userResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(userResponse.isSuccessful()){
                    User user = userResponse.body();
                    return Observable.just(user);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  userResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.UPDATEUSER updateuser = RemoteCode.UPDATEUSER.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(updateuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<User>applySchedulers());
    }

    @Override
    public Observable<Boolean> pwdResetByEmail(String email) {
        PasswordResetEmail passwordResetEmail = new PasswordResetEmail();
        passwordResetEmail.setEmail(email);
        return bmobService.passwordResetByEmail(passwordResetEmail).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.PASSWORDRESET passwordreset= RemoteCode.PASSWORDRESET.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(passwordreset.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> pwdResetByMobile(String smsCode, String newpwd) {

        PasswordResetMobile passwordResetMobile = new PasswordResetMobile();
        passwordResetMobile.setPassword(newpwd);
        return bmobService.passwordResetByMobile(smsCode,passwordResetMobile).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.PASSWORDRESET passwordreset = RemoteCode.PASSWORDRESET.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(passwordreset.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> pwdResetByOldPwd(String sessionToken, String objectId, String oldPwd, String newPwd) {
        PasswordResetOldPwd passwordResetOldPwd = new PasswordResetOldPwd();
        passwordResetOldPwd.setOldPassword(oldPwd);
        passwordResetOldPwd.setNewPassword(newPwd);

        return bmobService.passwordResetByOldPassword(sessionToken,objectId,passwordResetOldPwd)
        .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.PASSWORDRESET passwordreset = RemoteCode.PASSWORDRESET.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(passwordreset.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<String> requestSmsCode(String phone) {
        RequestSmsCode requestSmsCode = new RequestSmsCode();
        requestSmsCode.setMobilePhoneNumber(phone);
        return bmobService.requestSmsCode(requestSmsCode)
                .flatMap(new Func1<Response<RequestSmsCodeResult>, Observable<String>>() {
                    @Override
                    public Observable<String> call(Response<RequestSmsCodeResult> requestSmsCodeResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.REQUESTSMSCODE.getDefauleError().getMessage());
                        if(requestSmsCodeResultResponse.isSuccessful()){
                            RequestSmsCodeResult requestSmsCodeResult = requestSmsCodeResultResponse.body();
                            if(requestSmsCodeResult != null){
                                return Observable.just(requestSmsCodeResult.getSmsId());
                            }else{
                                return Observable.error(bmobRequestException);
                            }
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  requestSmsCodeResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.REQUESTSMSCODE requestsmscode = RemoteCode.REQUESTSMSCODE.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(requestsmscode.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<String>applySchedulers());
    }

    @Override
    public Observable<Boolean> emailVerify(String email) {

        EmailVerify emailVerify = new EmailVerify();
        emailVerify.setEmail(email);
        return bmobService.emailVerify(emailVerify).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.EMAILVERIFY passwordreset = RemoteCode.EMAILVERIFY.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(passwordreset.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> smsCodeVerify(String smsCode,String mobile) {
        SmsCodeVerify smsCodeVerify = new SmsCodeVerify();
        smsCodeVerify.setMobilePhoneNumber(mobile);
        return bmobService.smsCodeVerify(smsCode,smsCodeVerify).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.SMSCODEVERIFY smscodeverify = RemoteCode.SMSCODEVERIFY.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(smscodeverify.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<PhoneticsSymbols> addPhoneticsSymbols(PhoneticsSymbols phoneticsSymbols) {

        WordGroup wordGroup = phoneticsSymbols.getWordGroup();
        wordGroup.setPointer();
        phoneticsSymbols.setWordGroup(wordGroup);

        return bmobService.addPhoneticsSymbols(phoneticsSymbols)
                .flatMap(new Func1<Response<PhoneticsSymbols>, Observable<PhoneticsSymbols>>() {
                    @Override
                    public Observable<PhoneticsSymbols> call(Response<PhoneticsSymbols> phoneticsSymbolsResponse) {
                        Log.d(TAG,"addPhoneticsSymbols->call" + Thread.currentThread().getName());
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsSymbolsResponse.isSuccessful()){

                            PhoneticsSymbols phoneticsSymbols = phoneticsSymbolsResponse.body();

                            return Observable.just(phoneticsSymbols);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsSymbolsResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<PhoneticsSymbols>applySchedulers());
    }

    @Override
    public Observable<Boolean> deletePhoneticsSymbolsById(String phoneticsSymbolsId) {
        return bmobService.deletePhoneticsSymbolsById(phoneticsSymbolsId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updatePhoneticsSymbolsRxById(PhoneticsSymbols phoneticsSymbols) {

        String objectId = phoneticsSymbols.getObjectId();
        PhoneticsSymbols phoneticsSymbols1 = (PhoneticsSymbols) phoneticsSymbols.clone();
        phoneticsSymbols1.setObjectId(null);

        return bmobService.updatePhoneticsSymbolsRxById(objectId,phoneticsSymbols1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<PhoneticsSymbols> getPhoneticsSymbolsRxById(String phoneticsSymbolsId) {

        return bmobService.getPhoneticsSymbolsRxById(phoneticsSymbolsId)
                .flatMap(new Func1<Response<PhoneticsSymbols>, Observable<PhoneticsSymbols>>() {
                    @Override
                    public Observable<PhoneticsSymbols> call(Response<PhoneticsSymbols> phoneticsWordsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsWordsResponse.isSuccessful()){

                            PhoneticsSymbols phoneticsSymbols = phoneticsWordsResponse.body();

                            return Observable.just(phoneticsSymbols);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsWordsResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<PhoneticsSymbols>applySchedulers());
    }

    @Override
    public Observable<List<PhoneticsSymbols>> getPhoneticsSymbolsRx() {
        return bmobService.getPhoneticsSymbolsRx()
                .flatMap(new Func1<Response<PhoneticsSymbolsResult>, Observable<List<PhoneticsSymbols>>>() {
                    @Override
                    public Observable<List<PhoneticsSymbols>> call(Response<PhoneticsSymbolsResult> phoneticsSymbolsResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsSymbolsResultResponse.isSuccessful()){
                            PhoneticsSymbolsResult bmobPhoneticsSymbolsResult = phoneticsSymbolsResultResponse.body();

                            List<PhoneticsSymbols> phoneticsSymbolses = bmobPhoneticsSymbolsResult.getResults();
                            return Observable.just(phoneticsSymbolses);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsSymbolsResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<PhoneticsSymbols>>applySchedulers());
    }

    @Override
    public Observable<PhoneticsWords> addPhoneticsWords(PhoneticsWords phoneticsWords) {

        return bmobService.addPhoneticsWords(phoneticsWords)
                .flatMap(new Func1<Response<PhoneticsWords>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<PhoneticsWords> phoneticsWordsResponse) {
                        Log.d(TAG,"addPhoneticsSymbols->call" + Thread.currentThread().getName());
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsWordsResponse.isSuccessful()){

                            PhoneticsWords phoneticsWords = phoneticsWordsResponse.body();

                            return Observable.just(phoneticsWords);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsWordsResponse.errorBody().string();
                                Log.d(TAG,errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<PhoneticsWords>applySchedulers());
    }

    @Override
    public Observable<Boolean> deletePhoneticsWordsById(String phoneticsWordsId) {
        return bmobService.deletePhoneticsWordsById(phoneticsWordsId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updatePhoneticsWordsRxById(PhoneticsWords phoneticsWords) {

        String objectId = phoneticsWords.getObjectId();
        PhoneticsWords phoneticsWords1 = (PhoneticsWords) phoneticsWords.clone();
        phoneticsWords1.setObjectId(null);

        return bmobService.updatePhoneticsWordsRxById(objectId,phoneticsWords1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<PhoneticsWords> getPhoneticsWordsRxById(String phoneticsWordsId) {

        return bmobService.getPhoneticsWordsRxById(phoneticsWordsId)
                .flatMap(new Func1<Response<PhoneticsWords>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<PhoneticsWords> phoneticsSymbolsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsSymbolsResponse.isSuccessful()){

                            PhoneticsWords phoneticsWords = phoneticsSymbolsResponse.body();

                            return Observable.just(phoneticsWords);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsSymbolsResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<PhoneticsWords>applySchedulers());
    }

    @Override
    public Observable<PhoneticsWords> getPhoneticsWordsRxByPhoneticsId(String phoneticsWordsId) {

        String regex = searchUtil.getBmobEquals("phoneticsSymbolsId",phoneticsWordsId);
        return bmobService.getPhoneticsWordsRxByPhoneticsId(regex)
                .flatMap(new Func1<Response<List<PhoneticsWords>>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<List<PhoneticsWords>> phoneticsWordsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsWordsResponse.isSuccessful()){

                            List<PhoneticsWords> phoneticsWordses = phoneticsWordsResponse.body();
                            if(phoneticsWordses != null && phoneticsWordses.size() > 0){
                                PhoneticsWords phoneticsWords = phoneticsWordses.get(0);
                                return Observable.just(phoneticsWords);
                            }
                            return Observable.error(new NullPointerException());
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsWordsResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<PhoneticsWords>applySchedulers());
    }

    @Override
    public Observable<List<PhoneticsWords>> getPhoneticsWordsRx() {
        return bmobService.getPhoneticsWordsRx()
                .flatMap(new Func1<Response<PhoneticsWordsResult>, Observable<List<PhoneticsWords>>>() {
                    @Override
                    public Observable<List<PhoneticsWords>> call(Response<PhoneticsWordsResult> phoneticsWordsResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(phoneticsWordsResultResponse.isSuccessful()){
                            PhoneticsWordsResult bmobPhoneticsWordsResult = phoneticsWordsResultResponse.body();

                            List<PhoneticsWords> phoneticsWordses = bmobPhoneticsWordsResult.getResults();

                            return Observable.just(phoneticsWordses);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  phoneticsWordsResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<PhoneticsWords>>applySchedulers());
    }


    @Override
    public Observable<TractateType> addTractateType(TractateType tractateType) {

        return bmobService.addTractateType(tractateType)
                .flatMap(new Func1<Response<TractateType>, Observable<TractateType>>() {
                    @Override
                    public Observable<TractateType> call(Response<TractateType> bmobMsSourceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobMsSourceResponse.isSuccessful()){

                            TractateType tractateType = bmobMsSourceResponse.body();

                            return Observable.just(tractateType);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobMsSourceResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateType>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateTypeById(String tractateTypeId) {
        return bmobService.deleteTractateTypeById(tractateTypeId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateTractateTypeRxById(TractateType tractateType) {

        String objectId = tractateType.getObjectId();
        tractateType.setObjectId(null);
        return bmobService.updateTractateTypeRxById(objectId,tractateType).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<TractateType> getTractateTypeRxById(String tractateTypeId) {
        return bmobService.getTractateTypeRxById(tractateTypeId)
                .flatMap(new Func1<Response<TractateType>, Observable<TractateType>>() {
                    @Override
                    public Observable<TractateType> call(Response<TractateType> bmobTractateTypeResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateTypeResponse.isSuccessful()){

                            TractateType tractateType = bmobTractateTypeResponse.body();

                            return Observable.just(tractateType);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateTypeResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateType>applySchedulers());
    }

    @Override
    public Observable<List<TractateType>> getTractateTypesRx() {
        return bmobService.getTractateTypeRx()
                .flatMap(new Func1<Response<TractateTypeResult>, Observable<List<TractateType>>>() {
                    @Override
                    public Observable<List<TractateType>> call(Response<TractateTypeResult> bmobTractateTypeResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateTypeResultResponse.isSuccessful()){
                            TractateTypeResult bmobTractateTypeResult = bmobTractateTypeResultResponse.body();

                            List<TractateType> tractateTypes = bmobTractateTypeResult.getResults();

                            return Observable.just(tractateTypes);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateTypeResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateType>>applySchedulers());
    }

    @Override
    public Observable<Word> addWord(Word word) {

        return bmobService.addWord(word)
                .flatMap(new Func1<Response<Word>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<Word> wordResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResponse.isSuccessful()){
                            Word word = wordResponse.body();
                            return Observable.just(word);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                });
    }

    @Override
    public Observable<Boolean> addWords(List<Word> words) {
        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < words.size(); i ++){
            Word word = words.get(i);

            BatchRequest.BatchRequestChild<Word> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("POST");
            batchRequestChild.setPath("/1/classes/Word");
            batchRequestChild.setBody(word);
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                return Observable.error(e);
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                });
    }

    @Override
    public Observable<Boolean> addWordByYouDao(final String wordName) {
        return getWordRxByYouDao(wordName)
                .flatMap(new Func1<Word, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Word word) {
                List<Word> list = new ArrayList<>();
                if(!word.getTranslate().trim().equals("")){
                    if(word.getName().equals(wordName)){
                        list.add(word);
                    }else {
                        Word wordnew = (Word) word.clone();
                        wordnew.setName(wordName);
                        list.add(word);
                        list.add(wordnew);
                    }
                }
                return addWords(list);
            }
        });
    }

    @Override
    public Observable<Boolean> addWordByIciba(final String wordName) {
        return getWordRxByIciba(wordName)
                .flatMap(new Func1<Word, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Word word) {
                        List<Word> list = new ArrayList<>();
                        if(!word.getTranslate().trim().equals("")){
                            if(word.getName().equals(wordName)){
                                list.add(word);
                            }else {
                                Word wordnew = (Word) word.clone();
                                wordnew.setName(wordName);
                                list.add(word);
                                list.add(wordnew);
                            }
                        }
                        return addWords(list);
                    }
                });
    }

    @Override
    public Observable<Boolean> deleteWordById(String wordId) {
        return bmobService.deleteWordById(wordId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWords(List<Word> words) {

        BatchRequest batchRequestget = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildrenget = new ArrayList<>();
        for(int i = 0; i < words.size(); i ++){
            BatchRequest.BatchRequestChild<SentenceCollect> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/Word/" + words.get(i).getObjectId());
            batchRequestChildrenget.add(batchRequestChild);
        }
        batchRequestget.setRequests(batchRequestChildrenget);

        return bmobService.batchPost(batchRequestget)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                return Observable.error(e);
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                });
    }

    @Override
    public Observable<Boolean> updateWordRxById(Word word) {

        String objectId = word.getObjectId();
        Word word1 = (Word) word.clone();
        word1.setObjectId(null);
        return bmobService.updateWordRxById(objectId,word1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        Log.d(TAG,"updateWordRxById(Word word):" + errjson);
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Word> getWordRxById(String wordId) {
        return bmobService.getWordRxById(wordId)
                .flatMap(new Func1<Response<Word>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<Word> wordResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResponse.isSuccessful()){


                            Word word = wordResponse.body();
                            return Observable.just(word);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Word>applySchedulers());
    }

    @Override
    public Observable<Word> getWordRxByYouDao(final String wordname) {
        return youDaoService.getWordByHtml(wordname).flatMap(new Func1<Response<ResponseBody>, Observable<Word>>() {
            @Override
            public Observable<Word> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    String wordhtml = null;
                    ResponseBody responseBody = responseBodyResponse.body();
                    try {
                        wordhtml = new String(responseBody.bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Word word = null;
                    if(wordhtml != null){

                        word = new Word();
                        String name = "";
                        String alias = "";
                        String british_phonogram = ""; // 英式发音音标(多个用"|"分割)
                        String british_soundurl = ""; // 英式发音(下面同上)
                        String american_phonogram = ""; // 英式发音音标
                        String american_soundurl = ""; // 美式发音
                        StringBuffer translate = new StringBuffer();// 解释，各种词类型用"|"分割
                        String correlation = ""; // 其它相关的（第三人称单数，复数....）
                        String remark = ""; // 备注

                        Document doc = Jsoup.parse(wordhtml);
                        // 名称
                        Element nameclass = doc.getElementsByAttributeValue("class",
                                "keyword").first();
                        if (nameclass != null) {
                            name = nameclass.text();
                        }
                        // 音标
                        Element phoneticdiv = doc.select("div[class=baav]").first();
                        if (phoneticdiv != null) {
                            Elements phoneticdivchile = phoneticdiv
                                    .getElementsByAttributeValue("class", "phonetic");
                            if (phoneticdivchile != null) {
                                for (int i = 0; i < phoneticdivchile.size(); i++) {
                                    if (i == 0) {
                                        british_phonogram = phoneticdivchile.get(i)
                                                .text();
                                    }
                                    if (i == 1) {
                                        american_phonogram = phoneticdivchile.get(i)
                                                .text();
                                    }
                                }
                            }
                        }
                        // 其它相关的
                        Element correlationdiv = doc.getElementById("phrsListTab");
                        if (correlationdiv != null) {

                            Element translateclass = correlationdiv
                                    .getElementsByAttributeValue("class",
                                            "trans-container").first();
                            if (translateclass != null) {
                                // 其它相关
                                Element correlationclass = translateclass
                                        .getElementsByAttributeValue("class",
                                                "additional").first();
                                if (correlationclass != null) {
                                    correlation = correlationclass.text();
                                }
                                // 解释
                                Elements translateclassli = translateclass
                                        .select("li");
                                for (Element emement : translateclassli) {
                                    String tran = emement.text();
                                    translate.append(tran
                                            + System.getProperty("line.separator"));
                                }
                            }
                        }

                        word.setName(wordname);
                        word.setAliasName(name);
                        word.setBritish_phonogram(british_phonogram);
                        word.setAmerican_phonogram(american_phonogram);
                        word.setTranslate(translate.toString());
                        word.setCorrelation(correlation);
                    }
                    return Observable.just(word);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        bmobRequestException = new BmobRequestException(errjson);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        });
    }

    @Override
    public Observable<Word> getWordRxByIciba(final String wordname) {
        return baiDuService.getWordByHtml(wordname).flatMap(new Func1<Response<ResponseBody>, Observable<Word>>() {
            @Override
            public Observable<Word> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    String wordhtml = null;
                    ResponseBody responseBody = responseBodyResponse.body();
                    try {
                        wordhtml = new String(responseBody.bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Word word = null;
                    if(wordhtml != null){

                        word = new Word();
                        String name = "";
                        String alias = "";
                        String british_phonogram = ""; // 英式发音音标(多个用"|"分割)
                        String british_soundurl = ""; // 英式发音(下面同上)
                        String american_phonogram = ""; // 英式发音音标
                        String american_soundurl = ""; // 美式发音
                        StringBuffer translate = new StringBuffer();// 解释，各种词类型用"|"分割
                        String correlation = ""; // 其它相关的（第三人称单数，复数....）
                        String remark = ""; // 备注

                        Document doc = Jsoup.parse(wordhtml);
                        // 名称
                        Element nameclass = doc.getElementsByAttributeValue("class",
                                "keyword").first();
                        if (nameclass != null) {
                            name = nameclass.text();
                        }
                        // 音标
                        Element phoneticdiv = doc.select("div[class=base-speak]").first();
                        if (phoneticdiv != null) {
                            Elements phoneticdivchile = phoneticdiv.getElementsByTag("span");

                            Elements newSpeak = phoneticdiv.getElementsByAttributeValue("class","new-speak-step");
                            for(int i = 0; i < newSpeak.size(); i++){
                                Element element = newSpeak.get(i);
                                String sound1 = element.attr("ms-on-mouseover");
                            }



                            for(int i = 0; i < phoneticdivchile.size(); i++){
                                Element span = phoneticdivchile.get(i);
                                span.childNodes();

                                String spanstr = span.text();
                                if(span.text().contains("英")){
                                    String cntitleRegex = "\\[.*\\]";
                                    Pattern cntitlepattern = Pattern.compile(cntitleRegex);
                                    Matcher cntitlematcher = cntitlepattern.matcher(spanstr);
                                    if(cntitlematcher.find()){
                                        int start = cntitlematcher.start();
                                        int end = cntitlematcher.end();
                                        british_phonogram = spanstr.substring(start,end);
                                    }

                                    String sound1 = span.getElementsByAttributeValue("class","new-speak-step").attr("ms-on-mouseover");

                                    String cntitleRegex1 = "http.*\\.\\w+";
                                    Pattern cntitlepattern1 = Pattern.compile(cntitleRegex1);
                                    Matcher cntitlematcher1 = cntitlepattern1.matcher(sound1);
                                    if(cntitlematcher1.find()){
                                        int start = cntitlematcher1.start();
                                        int end = cntitlematcher1.end();
                                        british_soundurl = sound1.substring(start,end);
                                    }
                                }


                                if(span.text().contains("美")){
                                    String cntitleRegex = "\\[.*\\]";
                                    Pattern cntitlepattern = Pattern.compile(cntitleRegex);
                                    Matcher cntitlematcher = cntitlepattern.matcher(spanstr);
                                    if(cntitlematcher.find()){
                                        int start = cntitlematcher.start();
                                        int end = cntitlematcher.end();
                                        american_phonogram = spanstr.substring(start,end);
                                    }

                                    String sound1 = span.getElementsByAttributeValue("class","new-speak-step").attr("ms-on-mouseover");

                                    String cntitleRegex1 = "http.*\\.\\w+";
                                    Pattern cntitlepattern1 = Pattern.compile(cntitleRegex1);
                                    Matcher cntitlematcher1 = cntitlepattern1.matcher(sound1);
                                    if(cntitlematcher1.find()){
                                        int start = cntitlematcher1.start();
                                        int end = cntitlematcher1.end();
                                        american_soundurl = sound1.substring(start,end);
                                    }
                                }

                            }

                            /* Elements phoneticdivchile = phoneticdiv
                                    .getElementsByAttributeValue("class", "phonetic");
                            if (phoneticdivchile != null) {
                                for (int i = 0; i < phoneticdivchile.size(); i++) {
                                    if (i == 0) {
                                        british_phonogram = phoneticdivchile.get(i)
                                                .text();
                                    }
                                    if (i == 1) {
                                        american_phonogram = phoneticdivchile.get(i)
                                                .text();
                                    }
                                }
                            }*/
                        }
                        // 其它相关的
                        Elements elements = doc.select("li[class=clearfix]");
                        for(int i = 0; i < elements.size(); i++){
                            Element element = elements.get(i);
                            Element elementspan = element.select("span[class=prop]").first();
                            if(elementspan == null){
                                continue;
                            }else{

                                translate.append(elementspan.text());
                                Element elements1 = element.getElementsByTag("p").first();
                                Elements spanele = elements1.getElementsByTag("span");
                                for(Element e:spanele){
                                    translate.append(e.text());
                                }
                                translate.append(System.getProperty("line.separator"));
                            }

                        }

                        correlation = doc.select("li[class=change clearfix]").first().getElementsByTag("p").text();
                        /*if (correlationdiv != null) {

                            Element translateclass = correlationdiv
                                    .getElementsByAttributeValue("class",
                                            "trans-container").first();
                            if (translateclass != null) {
                                // 其它相关
                                Element correlationclass = translateclass
                                        .getElementsByAttributeValue("class",
                                                "additional").first();
                                if (correlationclass != null) {
                                    correlation = correlationclass.text();
                                }
                                // 解释
                                Elements translateclassli = translateclass
                                        .select("li");
                                for (Element emement : translateclassli) {
                                    String tran = emement.text();
                                    translate.append(tran
                                            + System.getProperty("line.separator"));
                                }
                            }
                        }*/

                        word.setName(wordname);
                        word.setAliasName(name);
                        word.setBritish_soundurl(british_soundurl);
                        word.setAmerican_soundurl(american_soundurl);
                        word.setBritish_phonogram(british_phonogram);
                        word.setAmerican_phonogram(american_phonogram);
                        word.setTranslate(translate.toString());
                        word.setCorrelation(correlation);
                    }
                    return Observable.just(word);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        bmobRequestException = new BmobRequestException(errjson);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        });
    }

    @Override
    public Observable<Word> getWordRxByName(String name) {
        String regex = searchUtil.getBmobEquals("name",name);

        Log.d(TAG,"getWordRxByName regex:" + regex);
        return bmobService.getWordRxByName(regex)
                .flatMap(new Func1<Response<WordResult>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<WordResult> wordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResultResponse.isSuccessful()){
                            WordResult bmobWordResult = wordResultResponse.body();
                            List<Word> bmobWords = bmobWordResult.getResults();
                            if(bmobWords != null && bmobWords.size() > 0){
                                Word word  = bmobWords.get(0);

                                return Observable.just(word);
                            }else{
                                bmobRequestException = new BmobRequestException(RemoteCode.COMMON.CUSTOM_EMPTYWORD.getMessage());
                                return Observable.error(bmobRequestException);
                            }

                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);

                    }
                }).compose(RxUtil.<Word>applySchedulers());
    }

    @Override
    public Observable<List<Word>> getWordsRxByPhoneticsId(String phoneticsId) {

        String json = SearchUtil.getInstance().getWordJsonByPhoneticsId(phoneticsId);

        return bmobService.getWordsRxByPhoneticsId(json)
                .flatMap(new Func1<Response<WordResult>, Observable<List<Word>>>() {
                    @Override
                    public Observable<List<Word>> call(Response<WordResult> wordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResultResponse.isSuccessful()){
                            WordResult wordResult = wordResultResponse.body();
                            List<Word> words = wordResult.getResults();
                            return Observable.just(words);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResultResponse.errorBody().string();
                                Log.d(TAG,"getWordsRxByPhoneticsId(String phoneticsId):" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Word>>applySchedulers());
    }

    @Override
    public Observable<List<Word>> getWordsRx() {

        return bmobService.getWordsRx()
                .flatMap(new Func1<Response<WordResult>, Observable<List<Word>>>() {
                    @Override
                    public Observable<List<Word>> call(Response<WordResult> wordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResultResponse.isSuccessful()){
                            WordResult wordResult = wordResultResponse.body();
                            List<Word> words = wordResult.getResults();
                            return Observable.just(words);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResultResponse.errorBody().string();
                                Log.d(TAG,"getWordsRxByPhoneticsId(String phoneticsId):" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Word>>applySchedulers());
    }

    @Override
    public Observable<List<Word>> getWordsRx(int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        return bmobService.getWordsRx(limit,skip)
                .flatMap(new Func1<Response<WordResult>, Observable<List<Word>>>() {
                    @Override
                    public Observable<List<Word>> call(Response<WordResult> wordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResultResponse.isSuccessful()){
                            WordResult wordResult = wordResultResponse.body();
                            List<Word> words = wordResult.getResults();
                            return Observable.just(words);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResultResponse.errorBody().string();
                                Log.d(TAG,"getWordsRxByPhoneticsId(String phoneticsId):" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Word>>applySchedulers());
    }

    @Override
    public Observable<List<Word>> getWordsRxByWordGroupId(String wordgroupId,int page,int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        String json = SearchUtil.getInstance().getWordsRxByWordGroupId(wordgroupId);

        return bmobService.getWordsRxByWordGroupId(json,limit,skip)
                .flatMap(new Func1<Response<WordResult>, Observable<List<Word>>>() {
                    @Override
                    public Observable<List<Word>> call(Response<WordResult> wordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordResultResponse.isSuccessful()){
                            WordResult wordResult = wordResultResponse.body();
                            List<Word> words = wordResult.getResults();
                            return Observable.just(words);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordResultResponse.errorBody().string();
                                Log.d(TAG,"getWordsRxByPhoneticsId(String phoneticsId):" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Word>>applySchedulers());
    }

    @Override
    public Observable<Sentence> addSentence(Sentence sentence) {

        sentence.getUser().setPointer();
        return bmobService.addSentenceRx(sentence)
                .flatMap(new Func1<Response<Sentence>, Observable<Sentence>>() {
                    @Override
                    public Observable<Sentence> call(Response<Sentence> sentenceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResponse.isSuccessful()){

                            Sentence sentence = sentenceResponse.body();

                            return Observable.just(sentence);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Sentence>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceById(String sentenceId) {
        return bmobService.deleteSentenceRxById(sentenceId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentences(List<Sentence> sentences) {

        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < sentences.size(); i ++){
            BatchRequest.BatchRequestChild<Sentence> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/Sentence/" + sentences.get(i).getObjectId());
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());

    }

    @Override
    public Observable<Boolean> updateSentenceById(Sentence sentence) {

        String sentenceId = sentence.getObjectId();
        Sentence sentence1 = (Sentence) sentence.clone();
        sentence1.setObjectId(null);
        sentence1.getUser().setPointer();
        sentence1.getSentenceGroup().setPointer();

        return bmobService.updateSentencRxById(sentenceId,sentence1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Sentence> getSentenceById(String sentenceId) {
        return bmobService.getSentenceRxById(sentenceId)
                .flatMap(new Func1<Response<Sentence>, Observable<Sentence>>() {
                    @Override
                    public Observable<Sentence> call(Response<Sentence> sentenceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResponse.isSuccessful()){

                            Sentence sentence = sentenceResponse.body();

                            return Observable.just(sentence);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Sentence>applySchedulers());
    }

    @Override
    public Observable<List<Sentence>> getSentences(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        return bmobService.getSentencesRx(limit,skip)
                .flatMap(new Func1<Response<SentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<SentenceResult> sentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResultResponse.isSuccessful()){

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            SentenceResult sentenceResult = sentenceResultResponse.body();

                            List<Sentence> sentences = sentenceResult.getResults();
                            return Observable.just(sentences);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Sentence>>applySchedulers());
    }

    @Override
    public Observable<List<Sentence>> getSentences(String serachWord, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSearchSentenceRegex(serachWord);
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + regex);
        return bmobService.getSentencesRx(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<SentenceResult> sentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResultResponse.isSuccessful()){

                            SentenceResult sentenceResult = sentenceResultResponse.body();

                            List<Sentence> sentences = sentenceResult.getResults();
                            return Observable.just(sentences);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Sentence>>applySchedulers());
    }

    @Override
    public Observable<List<Sentence>> getSentencesRxBySentenceGroupId(String sentencegroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentencesRxBySentenceGroupId(sentencegroupId);
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + regex);
        return bmobService.getSentencesRxBySentenceGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<SentenceResult> sentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResultResponse.isSuccessful()){

                            SentenceResult sentenceResult = sentenceResultResponse.body();

                            List<Sentence> sentences = sentenceResult.getResults();
                            return Observable.just(sentences);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Sentence>>applySchedulers());
    }

    @Override
    public Observable<List<Sentence>> getSentencesRxBySentenceCollectGroupId(String sentencecollectgroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentencesRxBySentenceCollectGroupId(sentencecollectgroupId);
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + regex);
        return bmobService.getSentencesRxBySentenceGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<SentenceResult> sentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceResultResponse.isSuccessful()){

                            SentenceResult sentenceResult = sentenceResultResponse.body();

                            List<Sentence> sentences = sentenceResult.getResults();
                            return Observable.just(sentences);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Sentence>>applySchedulers());
    }

    @Override
    public Observable<Grammar> addGrammar(Grammar grammar) {

        WordGroup wordGroup = grammar.getWordGroup();
        wordGroup.setPointer();
        grammar.setWordGroup(wordGroup);

        SentenceGroup sentenceGroup = grammar.getSentenceGroup();
        sentenceGroup.setPointer();
        grammar.setSentenceGroup(sentenceGroup);

        User user = grammar.getUser();
        user.setPointer();

        grammar.setUser(user);

        return bmobService.addGrammarRx(grammar)
                .flatMap(new Func1<Response<Grammar>, Observable<Grammar>>() {
                    @Override
                    public Observable<Grammar> call(Response<Grammar> grammarResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(grammarResponse.isSuccessful()){

                            Grammar grammar = grammarResponse.body();

                            return Observable.just(grammar);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  grammarResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Grammar>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteGrammarById(String grammarId) {
        return bmobService.deleteGrammarRxById(grammarId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateGrammarRxById(Grammar grammar) {

        Grammar grammar1 = (Grammar) grammar.clone();
        grammar1.setObjectId(null);

        return bmobService.updateGrammarRxById(grammar.getObjectId(),grammar1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Grammar> getGrammarById(String grammarId) {
        return bmobService.getGrammarRxById(grammarId)
                .flatMap(new Func1<Response<Grammar>, Observable<Grammar>>() {
                    @Override
                    public Observable<Grammar> call(Response<Grammar> grammarResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(grammarResponse.isSuccessful()){

                            Grammar grammar = grammarResponse.body();
                            return Observable.just(grammar);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  grammarResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Grammar>applySchedulers());
    }

    @Override
    public Observable<List<Grammar>> getGrammars() {
        return bmobService.getGrammarsRx()
                .flatMap(new Func1<Response<GrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<GrammarResult> grammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(grammarResultResponse.isSuccessful()){
                            GrammarResult bmobGrammarResult = grammarResultResponse.body();

                            List<Grammar> grammars = bmobGrammarResult.getResults();

                            return Observable.just(grammars);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  grammarResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Grammar>>applySchedulers());
    }

    @Override
    public Observable<List<Grammar>> getGrammars(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        return bmobService.getGrammarsRx(limit,skip)
                .flatMap(new Func1<Response<GrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<GrammarResult> bmobGrammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResultResponse.isSuccessful()){
                            GrammarResult bmobGrammarResult = bmobGrammarResultResponse.body();

                            List<Grammar> grammars = bmobGrammarResult.getResults();
                            return Observable.just(grammars);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobGrammarResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Grammar>>applySchedulers());
    }

    @Override
    public Observable<List<Grammar>> getGrammars(String serachWord, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSearchGrammarRegex(serachWord);
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + regex);
        return bmobService.getGrammarsRx(regex,limit,skip)
                .flatMap(new Func1<Response<GrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<GrammarResult> grammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(grammarResultResponse.isSuccessful()){
                            GrammarResult bmobGrammarResult = grammarResultResponse.body();

                            List<Grammar> grammars = bmobGrammarResult.getResults();

                            return Observable.just(grammars);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  grammarResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Grammar>>applySchedulers());
    }

    @Override
    public Observable<Tractate> addTractate(File tractate) {
        return null;
    }

    @Override
    public Observable<Tractate> addTractate(Tractate tractate) {

        return bmobService.addTractate(tractate)
                .flatMap(new Func1<Response<Tractate>, Observable<Tractate>>() {
                    @Override
                    public Observable<Tractate> call(Response<Tractate> bmobTractateResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResponse.isSuccessful()){

                            Tractate tractate = bmobTractateResponse.body();

                            return Observable.just(tractate);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Tractate>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateRxById(String tractateId) {
        return bmobService.deleteTractateRxById(tractateId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractates(List<Tractate> tractates) {
        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < tractates.size(); i ++){
            BatchRequest.BatchRequestChild<Sentence> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/Tractate/" + tractates.get(i).getObjectId());
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateTractateRxById(Tractate tractate) {

        String objectId = tractate.getObjectId();
        tractate.setObjectId(null);
        return bmobService.updateTractateRxById(objectId,tractate).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Tractate> getTractateRxById(String tractateId) {
        return bmobService.getTractateRxById(tractateId)
                .flatMap(new Func1<Response<Tractate>, Observable<Tractate>>() {
                    @Override
                    public Observable<Tractate> call(Response<Tractate> bmobBmobTractateResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobTractateResponse.isSuccessful()){

                            Tractate tractate = bmobBmobTractateResponse.body();

                            return Observable.just(tractate);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobBmobTractateResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Tractate>applySchedulers());
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateTypeId(String tractateTypeId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getTractateRxByTractateTypeId(tractateTypeId);

        return bmobService.getTractatesRx(regex,limit,skip)
                .flatMap(new Func1<Response<TractateResult>, Observable<List<Tractate>>>() {
                    @Override
                    public Observable<List<Tractate>> call(Response<TractateResult> bmobTractateResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResultResponse.isSuccessful()){
                            TractateResult bmobTractateResult = bmobTractateResultResponse.body();

                            List<Tractate> tractates = bmobTractateResult.getResults();

                            return Observable.just(tractates);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Tractate>>applySchedulers());
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateGroupId(String tractateGroupId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("tractateGroupId",tractateGroupId);

        return bmobService.getTractatesRx(regex,limit,skip)
                .flatMap(new Func1<Response<TractateResult>, Observable<List<Tractate>>>() {
                    @Override
                    public Observable<List<Tractate>> call(Response<TractateResult> bmobTractateResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResultResponse.isSuccessful()){
                            TractateResult bmobTractateResult = bmobTractateResultResponse.body();

                            List<Tractate> tractates = bmobTractateResult.getResults();

                            return Observable.just(tractates);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Tractate>>applySchedulers());
    }

    @Override
    public Observable<List<Tractate>> getTractatesRx(String searchword, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSearchTractateRegex(searchword);

        return bmobService.getTractatesRx(regex,limit,skip)
                .flatMap(new Func1<Response<TractateResult>, Observable<List<Tractate>>>() {
                    @Override
                    public Observable<List<Tractate>> call(Response<TractateResult> bmobTractateResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResultResponse.isSuccessful()){
                            TractateResult bmobTractateResult = bmobTractateResultResponse.body();

                            List<Tractate> tractates = bmobTractateResult.getResults();

                            return Observable.just(tractates);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<Tractate>>applySchedulers());
    }

    @Override
    public Observable<WordGroup> addWordGroup(WordGroup wordGroup) {

        User user = wordGroup.getUser();
        user.setPointer();
        wordGroup.setUser(user);

        return bmobService.addWordGroup(wordGroup)
                .flatMap(new Func1<Response<WordGroup>, Observable<WordGroup>>() {
                    @Override
                    public Observable<WordGroup> call(Response<WordGroup> wordGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordGroupResponse.isSuccessful()){

                            WordGroup wordGroup = wordGroupResponse.body();

                            return Observable.just(wordGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                bmobRequestException = new BmobRequestException(bmobDefaultError);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<WordGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWordGroupRxById(final String wordGroupId) {

        //先查询是否有单词在这个词单里面
        String regex = searchUtil.getBmobEquals("wordGroup",wordGroupId);
        return bmobService.getWordCollectRxByWordGroupId(regex,5,0)
                .flatMap(new Func1<Response<WordCollectResult>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<WordCollectResult> wordCollectResultResponse) {
                        if(wordCollectResultResponse.isSuccessful()){
                            WordCollectResult wordCollectResult = wordCollectResultResponse.body();
                            List<WordCollect> wordCollects = wordCollectResult.getResults();
                            if(wordCollects.size() > 0){
                                BmobRequestException bmobRequestException = new BmobRequestException("删除失败，请先删除当前分组里面的单词");
                                return Observable.error(bmobRequestException);
                            }
                            return bmobService.deleteWordGroupRxById(wordGroupId);
                        }else{
                            try {
                                Gson gson = new GsonBuilder().create();
                                String errjson =  wordCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                BmobRequestException bmobRequestException = new BmobRequestException(errjson);
                                return Observable.error(bmobRequestException);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return Observable.error(e);
                            }
                        }
                    }
                }).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                bmobRequestException = new BmobRequestException(bmobDefaultError);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateWordGroupRxById(WordGroup wordGroup) {

        String objectId = wordGroup.getObjectId();
        WordGroup wordGroup1 = (WordGroup) wordGroup.clone();
        wordGroup1.setObjectId(null);

        return bmobService.updateWordGroupRxById(objectId,wordGroup1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<WordGroup> getWordGroupRxById(String wordGroupId) {
        return bmobService.getWordGroupRxById(wordGroupId)
                .flatMap(new Func1<Response<WordGroup>, Observable<WordGroup>>() {
                    @Override
                    public Observable<WordGroup> call(Response<WordGroup> wordGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordGroupResponse.isSuccessful()){

                            WordGroup wordGroup = wordGroupResponse.body();
                            return Observable.just(wordGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<WordGroup>applySchedulers());
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("user",userId);

        return bmobService.getWordGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<WordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<WordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            WordGroupResult wordGroupResult = bmobWordGroupResultResponse.body();

                            List<WordGroup> wordGroups = wordGroupResult.getResults();
                            return Observable.just(wordGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroup>>applySchedulers());
    }

    @Override
    public Observable<List<WordGroup>> getCollectWordGroupRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getCollectWordGroupRxByuserId(userId);

        return bmobService.getCollectWordGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<WordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<WordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            WordGroupResult wordGroupResult = bmobWordGroupResultResponse.body();

                            List<WordGroup> wordGroups = wordGroupResult.getResults();
                            return Observable.just(wordGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroup>>applySchedulers());
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId) {
        String regex = searchUtil.getBmobEquals("user",userId);

        return bmobService.getWordGroupRxByUserId(regex)
                .flatMap(new Func1<Response<WordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<WordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            WordGroupResult wordGroupResult = bmobWordGroupResultResponse.body();

                            List<WordGroup> wordGroups = wordGroupResult.getResults();
                            return Observable.just(wordGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroup>>applySchedulers());
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenRx(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getWordGroupsByOpenRx();

        return bmobService.getWordGroupsByOpenRx(regex,limit,skip)
                .flatMap(new Func1<Response<WordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<WordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            WordGroupResult wordGroupResult = bmobWordGroupResultResponse.body();

                            List<WordGroup> wordGroups = wordGroupResult.getResults();

                            return Observable.just(wordGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroup>>applySchedulers());
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenAndNotCollectRx(String userId,int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getWordgroupsOpenAndNotCollect(userId);

        return bmobService.getWordGroupsByOpenAndNotCollectRx(regex,limit,skip)
                .flatMap(new Func1<Response<WordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<WordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            WordGroupResult wordGroupResult = bmobWordGroupResultResponse.body();

                            List<WordGroup> wordGroups = wordGroupResult.getResults();

                            return Observable.just(wordGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroup>>applySchedulers());
    }

    @Override
    public Observable<WordGroupCollect> addWordGroupCollect(WordGroupCollect wordGroupCollect) {

        User user = wordGroupCollect.getUser();
        user.setPointer();
        wordGroupCollect.setUser(user);
        WordGroup wordGroup = wordGroupCollect.getWordGroup();
        wordGroup.setPointer();
        wordGroupCollect.setWordGroup(wordGroup);
        return bmobService.addWordGroupCollect(wordGroupCollect)
                .flatMap(new Func1<Response<WordGroupCollect>, Observable<WordGroupCollect>>() {
                    @Override
                    public Observable<WordGroupCollect> call(Response<WordGroupCollect> wordGroupCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordGroupCollectResponse.isSuccessful()){

                            WordGroupCollect wordGroupCollect =  wordGroupCollectResponse.body();
                            return Observable.just(wordGroupCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordGroupCollectResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<WordGroupCollect>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxById(String wordGroupCollectId) {
        return bmobService.deleteWordGroupCollectRxById(wordGroupCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxByuserIdAndwordGroupId(String userId, String wordGroupId) {
        return this.getWordGroupCollectRxByUserIdAndwordGroupId(userId,wordGroupId)
                .flatMap(new Func1<List<WordGroupCollect>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<WordGroupCollect> wordGroupCollects) {
                        if(wordGroupCollects != null && wordGroupCollects.size() > 0){
                            return deleteWordGroupCollectRxById(wordGroupCollects.get(0).getObjectId());
                        }
                        return Observable.error(new BmobRequestException());
                    }
                });
    }


    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("user",userId);

        return bmobService.getWordGroupCollectRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<WordGroupCollectResult>, Observable<List<WordGroupCollect>>>() {
                    @Override
                    public Observable<List<WordGroupCollect>> call(Response<WordGroupCollectResult> bmobWordGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupCollectResultResponse.isSuccessful()){
                            WordGroupCollectResult bmobWordGroupCollectResult = bmobWordGroupCollectResultResponse.body();

                            List<WordGroupCollect> wordGroupCollects = bmobWordGroupCollectResult.getResults();

                            return Observable.just(wordGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroupCollect>>applySchedulers());
    }

    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserIdAndwordGroupId(String userId, String wordGroupId) {

        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","wordgroupId"},new String[]{userId,wordGroupId});

        return bmobService.getWordGroupCollectRxByUserIdAndwordGroupId(regex)
                .flatMap(new Func1<Response<WordGroupCollectResult>, Observable<List<WordGroupCollect>>>() {
                    @Override
                    public Observable<List<WordGroupCollect>> call(Response<WordGroupCollectResult> bmobWordGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupCollectResultResponse.isSuccessful()){
                            WordGroupCollectResult bmobWordGroupCollectResult = bmobWordGroupCollectResultResponse.body();

                            List<WordGroupCollect> wordGroupCollects = bmobWordGroupCollectResult.getResults();

                            return Observable.just(wordGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordGroupCollect>>applySchedulers());
    }

    @Override
    public Observable<SentenceGroup> addSentenceGroup(SentenceGroup sentenceGroup) {

        sentenceGroup.getUser().setPointer();
        return bmobService.addSentenceGroup(sentenceGroup)
                .flatMap(new Func1<Response<SentenceGroup>, Observable<SentenceGroup>>() {
                    @Override
                    public Observable<SentenceGroup> call(Response<SentenceGroup> bmobSentenceGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupResponse.isSuccessful()){

                            SentenceGroup sentenceGroup = bmobSentenceGroupResponse.body();

                            return Observable.just(sentenceGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupRxById(final String sentenceGroupId) {
        //先查询是否有句子在这个分组里面
        String regex = searchUtil.getSentencesRxBySentenceGroupId(sentenceGroupId);
        return bmobService.getSentencesRxBySentenceGroupId(regex,5,0)
                .flatMap(new Func1<Response<SentenceResult>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<SentenceResult> sentenceResultResponse) {
                        if(sentenceResultResponse.isSuccessful()){
                            SentenceResult sentenceResult = sentenceResultResponse.body();
                            List<Sentence> sentences = sentenceResult.getResults();
                            if(sentences.size() > 0){
                              BmobRequestException bmobRequestException = new BmobRequestException("删除失败，请先删除当前分组里面的句子");
                              return Observable.error(bmobRequestException);
                            }
                            return bmobService.deleteSentenceGroupRxById(sentenceGroupId);
                        }else{
                            try {
                                Gson gson = new GsonBuilder().create();
                                String errjson =  sentenceResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                BmobRequestException bmobRequestException = new BmobRequestException(errjson);
                                return Observable.error(bmobRequestException);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return Observable.error(e);
                            }
                        }
                    }
                }).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupAndSentences(final String sentenceGroupId, List<Sentence> sentences) {
        return deleteSentences(sentences).onErrorReturn(new Func1<Throwable, Boolean>() {
            @Override
            public Boolean call(Throwable throwable) {
                if(throwable instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) throwable;
                    List<BatchResult> list = (List<BatchResult>) bmobRequestException.getObject();
                    for(BatchResult batchResult : list){
                        if(batchResult.getError() != null){
                            return false;
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }
        }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if(aBoolean){
                    return deleteSentenceGroupRxById(sentenceGroupId);
                }else{
                    return Observable.just(false);
                }
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateSentenceGroupRxById(SentenceGroup sentenceGroup) {

        String objectId = sentenceGroup.getObjectId();
        SentenceGroup sentenceGroup1 = (SentenceGroup) sentenceGroup.clone();
        sentenceGroup1.setObjectId(null);
        return bmobService.updateSentenceGroupRxById(objectId,sentenceGroup1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        if(createuser == RemoteCode.COMMON.Common_UNIQUE){
                            bmobRequestException = new BmobRequestException("分组名称已经存在了");
                        }else{
                            bmobRequestException = new BmobRequestException(createuser.getMessage());
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<SentenceGroup> getSentenceGroupRxById(String sentenceGroupId) {
        return bmobService.getSentenceGroupRxById(sentenceGroupId)
                .flatMap(new Func1<Response<SentenceGroup>, Observable<SentenceGroup>>() {
                    @Override
                    public Observable<SentenceGroup> call(Response<SentenceGroup> bmobBmobSentenceGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobSentenceGroupResponse.isSuccessful()){

                            SentenceGroup sentenceGroup = bmobBmobSentenceGroupResponse.body();

                            return Observable.just(sentenceGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobBmobSentenceGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceGroup>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId) {
        String regex = searchUtil.getSentenceGroupRxByUserId(userId);

        return bmobService.getSentenceGroupRxByUserId(regex)
                .flatMap(new Func1<Response<SentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<SentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            SentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<SentenceGroup> sentenceGroups = bmobSentenceGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                                bmobRequestException.setObject(bmobDefaultError);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroup>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId,boolean create, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentenceGroupRxByUserId(userId);

        return bmobService.getSentenceGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<SentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            SentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<SentenceGroup> sentenceGroups = bmobSentenceGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroup>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroup>> getCollectSentenceGroupRxByUserId(String userId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getCollectSentenceGroupRxByUserId(userId);

        return bmobService.getSentenceGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<SentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            SentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<SentenceGroup> sentenceGroups = bmobSentenceGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroup>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenRx(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentenceGroupsByOpenRx();

        return bmobService.getSentenceGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<SentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            SentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<SentenceGroup> sentenceGroups = bmobSentenceGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroup>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentenceGroupsOpenAndNotCollect(userId);

        return bmobService.getSentenceGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<SentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            SentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<SentenceGroup> sentenceGroups = bmobSentenceGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroup>>applySchedulers());
    }

    @Override
    public Observable<SentenceCollectGroup> addSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup) {

        User user = sentenceCollectGroup.getUser();
        user.setPointer();
        sentenceCollectGroup.setUser(user);

        return bmobService.addSentenceCollectGroup(sentenceCollectGroup)
                .flatMap(new Func1<Response<SentenceCollectGroup>, Observable<SentenceCollectGroup>>() {
                    @Override
                    public Observable<SentenceCollectGroup> call(Response<SentenceCollectGroup> sentenceCollectGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectGroupResponse.isSuccessful()){

                            SentenceCollectGroup sentenceCollectGroup = sentenceCollectGroupResponse.body();

                            return Observable.just(sentenceCollectGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceCollectGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectGroupRxById(final String sentencecollectGroupId) {

        String regex = searchUtil.getBmobEquals("sentenceCollectGroup",sentencecollectGroupId);
        //先查询是否有句子在这个分组里面
        return bmobService.getSentenceCollectRxBySentenceCollectGroupId(regex,5,0)
                .flatMap(new Func1<Response<SentenceCollectResult>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<SentenceCollectResult> sentenceCollectResultResponse) {
                        if(sentenceCollectResultResponse.isSuccessful()){
                            SentenceCollectResult sentenceCollectResult = sentenceCollectResultResponse.body();
                            List<SentenceCollect> sentenceCollects = sentenceCollectResult.getResults();
                            if(sentenceCollects.size() > 0){
                                BmobRequestException bmobRequestException = new BmobRequestException("删除失败，请先删除当前分组里面的句子");
                                return Observable.error(bmobRequestException);
                            }
                            return bmobService.deleteSentenceCollectGroupRxById(sentencecollectGroupId);
                        }else{
                            BmobRequestException bmobRequestException = new BmobRequestException("删除失败,请稍后再试");
                            return Observable.error(bmobRequestException);
                        }
                    }
                }).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateSentenceCollectGroupRxById(SentenceCollectGroup sentenceCollectGroup) {

        String objectId = sentenceCollectGroup.getObjectId();
        SentenceCollectGroup sentenceCollectGroup1 = (SentenceCollectGroup) sentenceCollectGroup.clone();
        sentenceCollectGroup1.setObjectId(null);

        return bmobService.updateSentenceCollectGroupRxById(objectId,sentenceCollectGroup1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<SentenceCollectGroup> getSentenceCollectGroupRxById(String sentencecollectgroupId) {

        return bmobService.getSentenceCollectGroupRxById(sentencecollectgroupId)
                .flatMap(new Func1<Response<SentenceCollectGroup>, Observable<SentenceCollectGroup>>() {
                    @Override
                    public Observable<SentenceCollectGroup> call(Response<SentenceCollectGroup> sentenceCollectGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectGroupResponse.isSuccessful()){

                            SentenceCollectGroup sentenceCollectGroup = sentenceCollectGroupResponse.body();

                            return Observable.just(sentenceCollectGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceCollectGroup>applySchedulers());
    }

    @Override
    public Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId) {

        String regex = searchUtil.getSentenceCollectGroupRxByUserId(userId);

        return bmobService.getSentenceCollectGroupRxByUserId(regex)
                .flatMap(new Func1<Response<SentenceCollectGroupResult>, Observable<List<SentenceCollectGroup>>>() {
                    @Override
                    public Observable<List<SentenceCollectGroup>> call(Response<SentenceCollectGroupResult> sentenceCollectGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectGroupResultResponse.isSuccessful()){
                            SentenceCollectGroupResult sentenceCollectGroupResult = sentenceCollectGroupResultResponse.body();

                            List<SentenceCollectGroup> sentenceGroups = sentenceCollectGroupResult.getResults();

                            return Observable.just(sentenceGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceCollectGroup>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getSentenceCollectGroupRxByUserId(userId);

        return bmobService.getSentenceCollectGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceCollectGroupResult>, Observable<List<SentenceCollectGroup>>>() {
                    @Override
                    public Observable<List<SentenceCollectGroup>> call(Response<SentenceCollectGroupResult> sentenceCollectGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectGroupResultResponse.isSuccessful()){
                            SentenceCollectGroupResult sentenceCollectGroupResult = sentenceCollectGroupResultResponse.body();

                            List<SentenceCollectGroup> sentenceCollectGroups = sentenceCollectGroupResult.getResults();

                            return Observable.just(sentenceCollectGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceCollectGroup>>applySchedulers());
    }

    //标准
    @Override
    public Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect) {

        sentenceGroupCollect.getUser().setPointer();
        sentenceGroupCollect.getSentenceGroup().setPointer();
        return bmobService.addSentenceGroupCollect(sentenceGroupCollect)
                .flatMap(new Func1<Response<SentenceGroupCollect>, Observable<SentenceGroupCollect>>() {
                    @Override
                    public Observable<SentenceGroupCollect> call(Response<SentenceGroupCollect> bmobSentenceGroupCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupCollectResponse.isSuccessful()){

                            SentenceGroupCollect sentenceGroupCollect = bmobSentenceGroupCollectResponse.body();

                            return Observable.just(sentenceGroupCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceGroupCollectResponse.errorBody().string();
                                Log.d(TAG,"addSentenceGroupCollect:" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                if(createuser == RemoteCode.COMMON.Common_UNIQUE){
                                    bmobRequestException = new BmobRequestException("已经收藏过当前句单了");
                                }else{
                                    bmobRequestException = new BmobRequestException(createuser.getMessage());
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceGroupCollect>applySchedulers());
    }

    @Override
    public Observable<SentenceGroupCollect> addSentenceGroupCollectByNotSelf(final SentenceGroupCollect sentenceGroupCollect) {

        sentenceGroupCollect.getUser().setPointer();
        sentenceGroupCollect.getSentenceGroup().setPointer();
        //查询这个句子分组是不是自己创建的
        return getSentenceGroupRxById(sentenceGroupCollect.getSentenceGroup().getObjectId())
                .flatMap(new Func1<SentenceGroup, Observable<SentenceGroupCollect>>() {
                    @Override
                    public Observable<SentenceGroupCollect> call(SentenceGroup sentenceGroup) {

                        if(sentenceGroup.getUser().getObjectId().equals(sentenceGroupCollect.getUser().getObjectId())){
                            BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.Common_COLLECTSENTENCEGROUPNOTSELF.getMessage());

                            return Observable.error(bmobRequestException);
                        }else{
                            return addSentenceGroupCollect(sentenceGroupCollect);
                        }

                    }
                });
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxById(String sentenceGroupCollectId) {
        return bmobService.deleteSentenceGroupCollectRxById(sentenceGroupCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(String userId, String sentencegroupId) {
        return this.getSentenceGroupCollectRxByUserIdAndsentencegroupId(userId,sentencegroupId)
                .flatMap(new Func1<List<SentenceGroupCollect>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<SentenceGroupCollect> sentenceGroupCollects) {
                        if(sentenceGroupCollects != null && sentenceGroupCollects.size() > 0){
                            return deleteSentenceGroupCollectRxById(sentenceGroupCollects.get(0).getObjectId());
                        }
                        return Observable.error(new BmobRequestException());
                    }
                });
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("user",userId);

        return bmobService.getSentenceGroupCollectRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceGroupCollectResult>, Observable<List<SentenceGroupCollect>>>() {
                    @Override
                    public Observable<List<SentenceGroupCollect>> call(Response<SentenceGroupCollectResult> bmobSentenceGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupCollectResultResponse.isSuccessful()){
                            SentenceGroupCollectResult bmobSentenceGroupCollectResult = bmobSentenceGroupCollectResultResponse.body();

                            List<SentenceGroupCollect> sentenceGroupCollects = bmobSentenceGroupCollectResult.getResults();

                            return Observable.just(sentenceGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroupCollect>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserIdAndsentencegroupId(String userId, String sentencegroupId) {

        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","sentencegroupId"},new String[]{userId,sentencegroupId});

        return bmobService.getSentenceGroupCollectRxByUserIdAndsentenceGroupId(regex)
                .flatMap(new Func1<Response<SentenceGroupCollectResult>, Observable<List<SentenceGroupCollect>>>() {
                    @Override
                    public Observable<List<SentenceGroupCollect>> call(Response<SentenceGroupCollectResult> bmobSentenceGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupCollectResultResponse.isSuccessful()){
                            SentenceGroupCollectResult bmobSentenceGroupCollectResult = bmobSentenceGroupCollectResultResponse.body();

                            List<SentenceGroupCollect> sentenceGroupCollects = bmobSentenceGroupCollectResult.getResults();

                            return Observable.just(sentenceGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceGroupCollect>>applySchedulers());
    }

    @Override
    public Observable<TractateGroup> addTractateGroup(TractateGroup tractateGroup) {

        User user = tractateGroup.getUserId();
        user.setPointer();
        tractateGroup.setUserId(user);

        return bmobService.addTractateGroup(tractateGroup)
                .flatMap(new Func1<Response<TractateGroup>, Observable<TractateGroup>>() {
                    @Override
                    public Observable<TractateGroup> call(Response<TractateGroup> bmobTractateGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResponse.isSuccessful()){

                            TractateGroup tractateGroup = bmobTractateGroupResponse.body();

                            return Observable.just(tractateGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateGroupRxById(final String tractateGroupId) {

        //先查询是否有句子在这个分组里面
        String regex = searchUtil.getBmobEquals("tractateGroupId",tractateGroupId);
        return bmobService.getTractateRxByTractateGroupId(regex,5,0)
                .flatMap(new Func1<Response<TractateResult>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<TractateResult> tractateResultResponse) {
                        if(tractateResultResponse.isSuccessful()){
                            TractateResult tractateResult = tractateResultResponse.body();
                            List<Tractate> tractates = tractateResult.getResults();
                            if(tractates.size() > 0){
                                BmobRequestException bmobRequestException = new BmobRequestException("删除失败，请先删除当前分组里面的文章");
                                return Observable.error(bmobRequestException);
                            }
                            return bmobService.deleteTractateGroupRxById(tractateGroupId);
                        }else{
                            try {
                                Gson gson = new GsonBuilder().create();
                                String errjson =  tractateResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                BmobRequestException bmobRequestException = new BmobRequestException(errjson);
                                return Observable.error(bmobRequestException);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return Observable.error(e);
                            }
                        }
                    }
                }).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());

    }

    @Override
    public Observable<Boolean> updateTractateGroupRxById(TractateGroup tractateGroup) {

        String objectId = tractateGroup.getObjectId();
        tractateGroup.setObjectId(null);
        return bmobService.updateTractateGroupRxById(objectId,tractateGroup).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<TractateGroup> getTractateGroupRxById(String tractateGroupId) {
        return bmobService.getTractateGroupRxById(tractateGroupId)
                .flatMap(new Func1<Response<TractateGroup>, Observable<TractateGroup>>() {
                    @Override
                    public Observable<TractateGroup> call(Response<TractateGroup> bmobBmobTractateGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobTractateGroupResponse.isSuccessful()){

                            TractateGroup tractateGroup = bmobBmobTractateGroupResponse.body();

                            return Observable.just(tractateGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobBmobTractateGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateGroup>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getTractateGroupsRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<TractateGroupResult> bmobTractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResultResponse.isSuccessful()){
                            TractateGroupResult bmobSentenceGroupCollectResult = bmobTractateGroupResultResponse.body();

                            List<TractateGroup> tractateGroups = bmobSentenceGroupCollectResult.getResults();

                            return Observable.just(tractateGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroup>>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId) {
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getTractateGroupsRxByUserId(regex)
                .flatMap(new Func1<Response<TractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<TractateGroupResult> bmobTractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResultResponse.isSuccessful()){
                            TractateGroupResult bmobSentenceGroupCollectResult = bmobTractateGroupResultResponse.body();

                            List<TractateGroup> tractateGroups = bmobSentenceGroupCollectResult.getResults();

                            return Observable.just(tractateGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroup>>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroup>> getCollectTractateGroupRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getCollectTractateGroupRxByUserId(userId);

        return bmobService.getTractateGroupsRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<TractateGroupResult> bmobTractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResultResponse.isSuccessful()){
                            TractateGroupResult bmobSentenceGroupCollectResult = bmobTractateGroupResultResponse.body();

                            List<TractateGroup> tractateGroups = bmobSentenceGroupCollectResult.getResults();

                            return Observable.just(tractateGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroup>>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getTractatesOpenAndNotCollect(userId);

        return bmobService.getTractateGroupsByOpenAndNotCollectRx(regex,limit,skip)
                .flatMap(new Func1<Response<TractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<TractateGroupResult> tractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateGroupResultResponse.isSuccessful()){
                            TractateGroupResult tractateGroupResult = tractateGroupResultResponse.body();

                            List<TractateGroup> tractateGroups = tractateGroupResult.getResults();

                            return Observable.just(tractateGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroup>>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsByOpenRx(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getTractateGroupsByOpenRx();

        return bmobService.getTractateGroupsByOpenRx(regex,limit,skip)
                .flatMap(new Func1<Response<TractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<TractateGroupResult> tractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateGroupResultResponse.isSuccessful()){
                            TractateGroupResult tractateGroupResult = tractateGroupResultResponse.body();

                            List<TractateGroup> tractateGroups = tractateGroupResult.getResults();

                            return Observable.just(tractateGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroup>>applySchedulers());
    }

    @Override
    public Observable<TractateCollectGroup> addTractateCollectGroup(TractateCollectGroup tractateCollectGroup) {
        User user = tractateCollectGroup.getUserId();
        user.setPointer();
        tractateCollectGroup.setUserId(user);

        return bmobService.addTractateCollectGroup(tractateCollectGroup)
                .flatMap(new Func1<Response<TractateCollectGroup>, Observable<TractateCollectGroup>>() {
                    @Override
                    public Observable<TractateCollectGroup> call(Response<TractateCollectGroup> tractateCollectGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateCollectGroupResponse.isSuccessful()){

                            TractateCollectGroup tractateCollectGroup = tractateCollectGroupResponse.body();

                            return Observable.just(tractateCollectGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateCollectGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateCollectGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateCollectGroupRxById(final String tractateCollectGroupId) {

        String regex = searchUtil.getBmobEquals("tractateCollectgId",tractateCollectGroupId);
        //先查询是否有句子在这个分组里面
        return bmobService.getTractateCollectRxByTractateCollectGroupId(regex,5,0)
                .flatMap(new Func1<Response<TractateCollectResult>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<TractateCollectResult> tractateCollectResultResponse) {
                        if(tractateCollectResultResponse.isSuccessful()){
                            TractateCollectResult tractateCollectResult = tractateCollectResultResponse.body();
                            List<TractateCollect> tractateCollects = tractateCollectResult.getResults();
                            if(tractateCollects.size() > 0){
                                BmobRequestException bmobRequestException = new BmobRequestException("删除失败，请先删除当前分组里面的文章");
                                return Observable.error(bmobRequestException);
                            }
                            return bmobService.deleteTractateCollectGroupRxById(tractateCollectGroupId);
                        }else{
                            BmobRequestException bmobRequestException = new BmobRequestException("删除失败,请稍后再试");
                            return Observable.error(bmobRequestException);
                        }
                    }
                }).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateTractateCollectGroupRxById(TractateCollectGroup tractateCollectGroup) {

        String objectId = tractateCollectGroup.getObjectId();
        TractateCollectGroup tractateCollectGroup1 = (TractateCollectGroup) tractateCollectGroup.clone();
        tractateCollectGroup1.setObjectId(null);

        return bmobService.updateTractateCollectGroupRxById(objectId,tractateCollectGroup1).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(responseBodyResponse.isSuccessful()){
                    return Observable.just(true);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  responseBodyResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<TractateCollectGroup> getTractateCollectGroupRxById(String tractateCollectGroupId) {

        return bmobService.getTractateCollectGroupRxById(tractateCollectGroupId)
                .flatMap(new Func1<Response<TractateCollectGroup>, Observable<TractateCollectGroup>>() {
                    @Override
                    public Observable<TractateCollectGroup> call(Response<TractateCollectGroup> tractateCollectGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateCollectGroupResponse.isSuccessful()){

                            TractateCollectGroup tractateCollectGroup = tractateCollectGroupResponse.body();

                            return Observable.just(tractateCollectGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateCollectGroupResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateCollectGroup>applySchedulers());
    }

    @Override
    public Observable<List<TractateCollectGroup>> getTractateCollectGroupRxByUserId(String userId) {

        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getTractateCollectGroupRxByUserId(regex)
                .flatMap(new Func1<Response<TractateCollectGroupResult>, Observable<List<TractateCollectGroup>>>() {
                    @Override
                    public Observable<List<TractateCollectGroup>> call(Response<TractateCollectGroupResult> tractateCollectGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateCollectGroupResultResponse.isSuccessful()){
                            TractateCollectGroupResult tractateCollectGroupResult = tractateCollectGroupResultResponse.body();

                            List<TractateCollectGroup> tractateCollectGroups = tractateCollectGroupResult.getResults();

                            return Observable.just(tractateCollectGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateCollectGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateCollectGroup>>applySchedulers());
    }

    @Override
    public Observable<List<TractateCollectGroup>> getTractateCollectGroupRxByUserId(String userId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getTractateCollectGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateCollectGroupResult>, Observable<List<TractateCollectGroup>>>() {
                    @Override
                    public Observable<List<TractateCollectGroup>> call(Response<TractateCollectGroupResult> tractateCollectGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateCollectGroupResultResponse.isSuccessful()){
                            TractateCollectGroupResult tractateCollectGroupResult = tractateCollectGroupResultResponse.body();

                            List<TractateCollectGroup> tractateCollectGroups = tractateCollectGroupResult.getResults();

                            return Observable.just(tractateCollectGroups);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateCollectGroupResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateCollectGroup>>applySchedulers());
    }

    @Override
    public Observable<TractateGroupCollect> addTractateGroupCollect(TractateGroupCollect tractateGroupCollect) {

        return bmobService.addTractateGroupCollect(tractateGroupCollect)
                .flatMap(new Func1<Response<TractateGroupCollect>, Observable<TractateGroupCollect>>() {
                    @Override
                    public Observable<TractateGroupCollect> call(Response<TractateGroupCollect> tractateGroupCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateGroupCollectResponse.isSuccessful()){

                            TractateGroupCollect tractateGroupCollect = tractateGroupCollectResponse.body();

                            return Observable.just(tractateGroupCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateGroupCollectResponse.errorBody().string();
                                Log.d(TAG,"addTractateGroupCollect:" + errjson);
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateGroupCollect>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxById(String tractateGroupCollectId) {
        return bmobService.deleteTractateGroupCollectRxById(tractateGroupCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxByuserIdAndtractateGroupId(String userId, String tractateGroupId) {
        return this.getTractateGroupCollectRxByUserIdAndtractateGroupId(userId,tractateGroupId)
                .flatMap(new Func1<List<TractateGroupCollect>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<TractateGroupCollect> tractateGroupCollects) {
                        if(tractateGroupCollects != null && tractateGroupCollects.size() > 0){
                            return deleteTractateGroupCollectRxById(tractateGroupCollects.get(0).getObjectId());
                        }
                        return Observable.error(new BmobRequestException());
                    }
                });
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getTractateGroupCollectRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateGroupCollectResult>, Observable<List<TractateGroupCollect>>>() {
                    @Override
                    public Observable<List<TractateGroupCollect>> call(Response<TractateGroupCollectResult> tractateGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateGroupCollectResultResponse.isSuccessful()){
                            TractateGroupCollectResult tractateGroupCollectResult = tractateGroupCollectResultResponse.body();

                            List<TractateGroupCollect> tractateGroupCollects = tractateGroupCollectResult.getResults();

                            return Observable.just(tractateGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroupCollect>>applySchedulers());
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserIdAndtractateGroupId(String userId, String tractategroupId) {
        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","sentencegroupId"},new String[]{userId,tractategroupId});

        return bmobService.getTractateGroupCollectRxByUserIdAndsentenceGroupId(regex)
                .flatMap(new Func1<Response<TractateGroupCollectResult>, Observable<List<TractateGroupCollect>>>() {
                    @Override
                    public Observable<List<TractateGroupCollect>> call(Response<TractateGroupCollectResult> tractateGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(tractateGroupCollectResultResponse.isSuccessful()){
                            TractateGroupCollectResult tractateGroupCollectResult = tractateGroupCollectResultResponse.body();

                            List<TractateGroupCollect> tractateGroupCollects = tractateGroupCollectResult.getResults();

                            return Observable.just(tractateGroupCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  tractateGroupCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateGroupCollect>>applySchedulers());
    }


    @Override
    public Observable<WordCollect> addWordCollect(WordCollect wordCollect) {

        User user = wordCollect.getUser();
        user.setPointer();

        WordGroup wordGroup = wordCollect.getWordGroup();
        wordGroup.setPointer();

        wordCollect.setUser(user);
        wordCollect.setWordGroup(wordGroup);

        return bmobService.addWordCollect(wordCollect)
                .flatMap(new Func1<Response<WordCollect>, Observable<WordCollect>>() {
                    @Override
                    public Observable<WordCollect> call(Response<WordCollect> wordCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordCollectResponse.isSuccessful()){

                            WordCollect wordCollect = wordCollectResponse.body();

                            return Observable.just(wordCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordCollectResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<WordCollect>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWordCollectRxById(String wordCollectId) {
        return bmobService.deleteWordCollectRxById(wordCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteWordCollects(List<WordCollect> wordCollects) {

        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < wordCollects.size(); i ++){
            BatchRequest.BatchRequestChild<Sentence> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/WordCollect/" + wordCollects.get(i).getObjectId());
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByUserIdAndWordGroupId(String userId, String wordGroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","wordgroupId"},new String[]{userId,wordGroupId});
        return bmobService.getWordCollectRxByUserIdAndWordGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<WordCollectResult>, Observable<List<WordCollect>>>() {
                    @Override
                    public Observable<List<WordCollect>> call(Response<WordCollectResult> wordCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordCollectResultResponse.isSuccessful()){
                            WordCollectResult wordCollectResult = wordCollectResultResponse.body();

                            List<WordCollect> wordCollects = wordCollectResult.getResults();
                            return Observable.just(wordCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordCollect>>applySchedulers());
    }


    @Override
    public Observable<List<WordCollect>> getWordCollectRxByWordGroupId(String wordGroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("wordGroup",wordGroupId);
        return bmobService.getWordCollectRxByWordGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<WordCollectResult>, Observable<List<WordCollect>>>() {
                    @Override
                    public Observable<List<WordCollect>> call(Response<WordCollectResult> wordCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(wordCollectResultResponse.isSuccessful()){
                            WordCollectResult wordCollectResult = wordCollectResultResponse.body();

                            List<WordCollect> wordCollects = wordCollectResult.getResults();
                            return Observable.just(wordCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  wordCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<WordCollect>>applySchedulers());
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollect(SentenceCollect sentenceCollect) {

        sentenceCollect.getUser().setPointer();
        sentenceCollect.getSentence().setPointer();
        sentenceCollect.getSentenceCollectGroup().setPointer();
        return bmobService.addSentenceCollect(sentenceCollect)
                .flatMap(new Func1<Response<SentenceCollect>, Observable<SentenceCollect>>() {
                    @Override
                    public Observable<SentenceCollect> call(Response<SentenceCollect> bmobSentenceCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceCollectResponse.isSuccessful()){

                            SentenceCollect sentenceCollect = bmobSentenceCollectResponse.body();

                            return Observable.just(sentenceCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceCollectResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceCollect>applySchedulers());
    }


    @Override
    public Observable<Boolean> addSentenceCollects(List<SentenceCollect> sentenceCollects) {

        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < sentenceCollects.size(); i ++){
            SentenceCollect sentenceCollect = sentenceCollects.get(i);

            User user = sentenceCollect.getUser();
            user.setPointer();
            sentenceCollect.setUser(user);
            Sentence sentence = sentenceCollect.getSentence();
            sentence.setPointer();
            sentenceCollect.setSentence(sentence);

            SentenceCollectGroup sentenceCollectGroup = sentenceCollect.getSentenceCollectGroup();
            sentenceCollectGroup.setPointer();
            sentenceCollect.setSentenceCollectGroup(sentenceCollectGroup);

            BatchRequest.BatchRequestChild<SentenceCollect> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("POST");
            batchRequestChild.setPath("/1/classes/SentenceCollect");
            batchRequestChild.setBody(sentenceCollect);
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                return Observable.error(e);
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    private List<BatchResult> getBatchResult(Response<ResponseBody> responseBodyResponse) throws BmobRequestException {

        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
        Gson gson = new GsonBuilder().create();
        String successmessage = null;
        List<BatchResult> list = null;
        try {
            successmessage = responseBodyResponse.body().string();
            list = gson.fromJson(successmessage,new TypeToken<List<BatchResult>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BatchResult> error = new ArrayList<BatchResult>();
        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).getError() != null){
                error.add(list.get(i));
            }
        }
        if(error.size() > 0){
            bmobRequestException.setObject(list);
            throw bmobRequestException;
        }
        Log.d(TAG,successmessage);
        return list;
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollectByNotSelf(final SentenceCollect sentenceCollect) {

        //查询这个句子是不是自己创建的
        return getSentenceById(sentenceCollect.getSentence().getObjectId())
                .flatMap(new Func1<Sentence, Observable<SentenceCollect>>() {
                    @Override
                    public Observable<SentenceCollect> call(Sentence sentence) {

                        if(sentence.getUser().getObjectId().equals(sentenceCollect.getUser().getObjectId())){
                            BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.Common_COLLECTSENTENCENOTSELF.getMessage());

                            return Observable.error(bmobRequestException);
                        }else{
                            return addSentenceCollect(sentenceCollect);
                        }

                    }
                }).compose(RxUtil.<SentenceCollect>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectRxById(String wordCollectId) {
        return bmobService.deleteSentenceCollectRxById(wordCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceCollects(final List<SentenceCollect> sentenceCollects) {

        //获得需要需要取消收藏的SentenceCollect

        BatchRequest batchRequestget = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildrenget = new ArrayList<>();
        for(int i = 0; i < sentenceCollects.size(); i ++){
            BatchRequest.BatchRequestChild<SentenceCollect> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/SentenceCollect/" + sentenceCollects.get(i).getObjectId());
            batchRequestChildrenget.add(batchRequestChild);
        }
        batchRequestget.setRequests(batchRequestChildrenget);

        return bmobService.batchPost(batchRequestget)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());

    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceCollectGroupId(String userId, String sentenceGroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","sentencegroupId"},new String[]{userId,sentenceGroupId});
        return bmobService.getSentenceCollectRxByUserIdAndSentenceGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceCollectResult>, Observable<List<SentenceCollect>>>() {
                    @Override
                    public Observable<List<SentenceCollect>> call(Response<SentenceCollectResult> sentenceCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectResultResponse.isSuccessful()){
                            SentenceCollectResult bmobSentenceCollectResult = sentenceCollectResultResponse.body();

                            List<SentenceCollect> sentenceCollects = bmobSentenceCollectResult.getResults();

                            return Observable.just(sentenceCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceCollect>>applySchedulers());
    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxBySentenceCollectGroupId(String sentenceCollectGroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("sentenceCollectGroup",sentenceCollectGroupId);

        return bmobService.getSentenceCollectRxByUserIdAndSentenceGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<SentenceCollectResult>, Observable<List<SentenceCollect>>>() {
                    @Override
                    public Observable<List<SentenceCollect>> call(Response<SentenceCollectResult> sentenceCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(sentenceCollectResultResponse.isSuccessful()){
                            SentenceCollectResult bmobSentenceCollectResult = sentenceCollectResultResponse.body();

                            List<SentenceCollect> sentenceCollects = bmobSentenceCollectResult.getResults();

                            return Observable.just(sentenceCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  sentenceCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<SentenceCollect>>applySchedulers());
    }


    @Override
    public Observable<TractateCollect> addTractateCollect(TractateCollect tractateCollect) {

        return bmobService.addTractateCollect(tractateCollect)
                .flatMap(new Func1<Response<TractateCollect>, Observable<TractateCollect>>() {
                    @Override
                    public Observable<TractateCollect> call(Response<TractateCollect> bmobTractateCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResponse.isSuccessful()){

                            TractateCollect tractateCollect = bmobTractateCollectResponse.body();

                            return Observable.just(tractateCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateCollectResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<TractateCollect>applySchedulers());
    }

    @Override
    public Observable<Boolean> addTractateCollects(List<TractateCollect> tractateCollects) {

        BatchRequest batchRequest = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildren = new ArrayList<>();
        for(int i = 0; i < tractateCollects.size(); i ++){
            TractateCollect tractateCollect = tractateCollects.get(i);

            User user = tractateCollect.getUserId();
            user.setPointer();
            tractateCollect.setUserId(user);
            Tractate tractate = tractateCollect.getTractateId();
            tractate.setPointer();
            tractateCollect.setTractateId(tractate);

            TractateCollectGroup tractateCollectGroup = tractateCollect.getTractateCollectgId();
            tractateCollectGroup.setPointer();
            tractateCollect.setTractateCollectgId(tractateCollectGroup);

            BatchRequest.BatchRequestChild<TractateCollect> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("POST");
            batchRequestChild.setPath("/1/classes/TractateCollect");
            batchRequestChild.setBody(tractateCollect);
            batchRequestChildren.add(batchRequestChild);
        }
        batchRequest.setRequests(batchRequestChildren);

        return bmobService.batchPost(batchRequest)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                return Observable.error(e);
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateCollectRxById(String tractateCollectId) {
        return bmobService.deleteTractateCollectRxById(tractateCollectId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteTractateCollects(List<TractateCollect> tractateCollects) {

        //获得需要需要取消收藏的SentenceCollect

        BatchRequest batchRequestget = new BatchRequest();

        List<BatchRequest.BatchRequestChild> batchRequestChildrenget = new ArrayList<>();
        for(int i = 0; i < tractateCollects.size(); i ++){
            BatchRequest.BatchRequestChild<SentenceCollect> batchRequestChild = new BatchRequest.BatchRequestChild();
            batchRequestChild.setMethod("DELETE");
            batchRequestChild.setPath("/1/classes/TractateCollect/" + tractateCollects.get(i).getObjectId());
            batchRequestChildrenget.add(batchRequestChild);
        }
        batchRequestget.setRequests(batchRequestChildrenget);

        return bmobService.batchPost(batchRequestget)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        Gson gson = new GsonBuilder().create();
                        if(responseBodyResponse.isSuccessful()){
                            return Observable.just(true);
                        }else{
                            try {
                                String errjson =  responseBodyResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<Boolean>applySchedulers());


    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByUserIdAndTractateGroupId(String userId, String tractateGroupId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","tractateGroupId"},new String[]{userId,tractateGroupId});
        return bmobService.getTractateCollectRxByUserIdAndTractateGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateCollectResult>, Observable<List<TractateCollect>>>() {
                    @Override
                    public Observable<List<TractateCollect>> call(Response<TractateCollectResult> bmobTractateCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResultResponse.isSuccessful()){
                            TractateCollectResult bmobTractateCollectResult = bmobTractateCollectResultResponse.body();

                            List<TractateCollect> tractateCollects = bmobTractateCollectResult.getResults();

                            return Observable.just(tractateCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateCollect>>applySchedulers());
    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByTractateCollectGroupId(String tractateCollectGroupId, int page, int pageSize) {

        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("tractateCollectgId",tractateCollectGroupId);
        return bmobService.getTractateCollectRxByTractateCollectGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<TractateCollectResult>, Observable<List<TractateCollect>>>() {
                    @Override
                    public Observable<List<TractateCollect>> call(Response<TractateCollectResult> bmobTractateCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResultResponse.isSuccessful()){
                            TractateCollectResult bmobTractateCollectResult = bmobTractateCollectResultResponse.body();

                            List<TractateCollect> tractateCollects = bmobTractateCollectResult.getResults();

                            return Observable.just(tractateCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<List<TractateCollect>>applySchedulers());
    }

    @Override
    public Observable<UploadFile> uploadFile(final File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audio/mp3", file.getName(), requestFile);
        return bmobService.uploadFile(file.getName(),body)
                .flatMap(new Func1<Response<UploadFile>, Observable<UploadFile>>() {
                    @Override
                    public Observable<UploadFile> call(Response<UploadFile> bmobTractateCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResultResponse.isSuccessful()){
                            UploadFile uploadFile = bmobTractateCollectResultResponse.body();

                            return Observable.just(uploadFile);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobTractateCollectResultResponse.errorBody().string();
                                BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<UploadFile>applySchedulers());
    }
}
