package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

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
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.service.BmobService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;
import com.englishlearn.myapplication.util.RxUtil;
import com.englishlearn.myapplication.util.SearchUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    private BmobService bmobService;//请求接口

    private SearchUtil searchUtil;

    public static BmobDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BmobDataSource();
        }
        return INSTANCE;
    }

    public BmobDataSource(){
        bmobService = ServiceFactory.getInstance().createBmobService();
        searchUtil = SearchUtil.getInstance();
    }

    public BmobDataSource(BmobService bmobService) {
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
                }).compose(RxUtil.<Word>applySchedulers());
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
                                return Observable.error(new NullPointerException());
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
    public Observable<Boolean> updateSentenceById(Sentence sentence) {

        String sentenceId = sentence.getObjectId();
        Sentence sentence1 = (Sentence) sentence.clone();
        sentence1.setObjectId(null);

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
    public Observable<Grammar> addGrammar(Grammar grammar) {

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
        String regex = searchUtil.getTractateRxByTractateGroupId(tractateGroupId);

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
    public Observable<Boolean> deleteWordGroupRxById(String wordGroupId) {
        return bmobService.deleteWordGroupRxById(wordGroupId)
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
        String regex = searchUtil.getBmobEquals("userId",userId);

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
        String regex = searchUtil.getBmobEquals("userId",userId);

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
        String regex = searchUtil.getBmobEquals("open","true");

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
        String regex = searchUtil.getBmobEquals("userId",userId);

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
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceGroup>applySchedulers());
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupRxById(String sentenceGroupId) {
        return bmobService.deleteSentenceGroupRxById(sentenceGroupId)
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
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId,boolean create) {
        String regex = searchUtil.getSentenceGroupRxByUserId(userId,create);

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
        String regex = searchUtil.getSentenceGroupRxByUserId(userId,create);

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
        String regex = searchUtil.getBmobEquals("open","true");

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
    public Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect) {

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
                                bmobRequestException = new BmobRequestException(createuser.getMessage());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        return Observable.error(bmobRequestException);
                    }
                }).compose(RxUtil.<SentenceGroupCollect>applySchedulers());
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
        String regex = searchUtil.getBmobEquals("userId",userId);

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
    public Observable<Boolean> deleteTractateGroupRxById(String tractateGroupId) {
        return bmobService.deleteTractateGroupRxById(tractateGroupId)
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
        String regex = searchUtil.getBmobEquals("wordGroupId",wordGroupId);
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
    public Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceGroupId(String userId, String sentenceGroupId, int page, int pageSize) {
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
