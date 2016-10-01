package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.MsSource;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateGroup;
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
import java.util.ArrayList;
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
    public Observable<BmobCreateUserResult> register(final User user) {
        BmobCreateUserRequest bmobCreateUserRequest = new BmobCreateUserRequest();
        bmobCreateUserRequest.setUserId(user.getUserId());
        bmobCreateUserRequest.setUsername(user.getUsername());
        bmobCreateUserRequest.setPassword(user.getPassword());
        bmobCreateUserRequest.setSex(user.getSex());
        bmobCreateUserRequest.setMobilePhoneNumber(user.getMobilePhoneNumber());
        bmobCreateUserRequest.setEmail(user.getEmail());
        bmobCreateUserRequest.setNickname(user.getNickname());
        bmobCreateUserRequest.setIconurl(user.getIconurl());
        bmobCreateUserRequest.setBirthday(user.getBirthday());
        bmobCreateUserRequest.setCreateDate(user.getCreateDate());

        return bmobService.createUserRx(bmobCreateUserRequest).flatMap(new Func1<Response<BmobCreateUserResult>, Observable<BmobCreateUserResult>>() {
            @Override
            public Observable<BmobCreateUserResult> call(Response<BmobCreateUserResult> bmobCreateUserResultResponse) {
                Log.d(TAG,"register->call" + Thread.currentThread().getName());
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.CREATEUSER.getDefauleError().getMessage());
                if(bmobCreateUserResultResponse.isSuccessful()){
                    BmobCreateUserResult bmobCreateUserResult = bmobCreateUserResultResponse.body();
                    return Observable.just(bmobCreateUserResult);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  bmobCreateUserResultResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.CREATEUSER createuser = RemoteCode.CREATEUSER.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<BmobCreateUserResult>applySchedulers());
    }

    @Override
    public Observable<User> createOrLoginUserByPhoneRx(String phone, String smscode) {
        User user = new User();
        BmobCreateOrLoginUserByPhoneRequest bmobCreateOrLoginUserByPhoneRequest = new BmobCreateOrLoginUserByPhoneRequest();
        bmobCreateOrLoginUserByPhoneRequest.setMobilePhoneNumber(phone);
        bmobCreateOrLoginUserByPhoneRequest.setUserId(user.getUserId());
        bmobCreateOrLoginUserByPhoneRequest.setSmsCode(smscode);

        return bmobService.createOrLoginUserByPhoneRx(bmobCreateOrLoginUserByPhoneRequest).flatMap(new Func1<Response<BmobUser>, Observable<User>>() {
            @Override
            public Observable<User> call(Response<BmobUser> bmobUserResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.CREATEUSER.getDefauleError().getMessage());
                if(bmobUserResponse.isSuccessful()){
                    BmobUser bmobUser = bmobUserResponse.body();
                    User user = new User();
                    user.setId(bmobUser.getObjectId());
                    user.setUserId(bmobUser.getUserId());
                    user.setUsername(bmobUser.getUsername());
                    user.setSessionToken(bmobUser.getSessionToken());
                    user.setMobilePhoneNumber(bmobUser.getMobilePhoneNumber());
                    user.setMobilePhoneVerified(bmobUser.getMobilePhoneVerified());
                    user.setEmail(bmobUser.getEmail());
                    user.setEmailVerified(bmobUser.getEmailVerified());
                    user.setNickname(bmobUser.getNickname());
                    user.setSex(bmobUser.getSex());
                    user.setIconurl(bmobUser.getIconurl());
                    user.setBirthday(bmobUser.getBirthday());
                    user.setCreateDate(bmobUser.getCreateDate());
                    return Observable.just(user);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  bmobUserResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobUser>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<BmobUser> bmobUserResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobUserResponse.isSuccessful()){
                            BmobUser bmobUser = bmobUserResponse.body();
                            User user = new User();
                            user.setId(bmobUser.getObjectId());
                            user.setUserId(bmobUser.getUserId());
                            user.setUsername(bmobUser.getUsername());
                            user.setSessionToken(bmobUser.getSessionToken());
                            user.setMobilePhoneNumber(bmobUser.getMobilePhoneNumber());
                            user.setMobilePhoneVerified(bmobUser.getMobilePhoneVerified());
                            user.setEmail(bmobUser.getEmail());
                            user.setEmailVerified(bmobUser.getEmailVerified());
                            user.setNickname(bmobUser.getNickname());
                            user.setSex(bmobUser.getSex());
                            user.setIconurl(bmobUser.getIconurl());
                            user.setBirthday(bmobUser.getBirthday());
                            user.setCreateDate(bmobUser.getCreateDate());
                            return Observable.just(user);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobUserResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobUserResult>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<BmobUserResult> bmobUserResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobUserResultResponse.isSuccessful()){
                            BmobUserResult bmobUserResult = bmobUserResultResponse.body();
                            List<BmobUser> bmobUsers = bmobUserResult.getResults();
                            if(bmobUsers != null && bmobUsers.size() > 0){
                                BmobUser bmobUser = bmobUsers.get(0);
                                
                                User user = new User();
                                user.setId(bmobUser.getObjectId());
                                user.setUserId(bmobUser.getUserId());
                                user.setUsername(bmobUser.getUsername());
                                user.setSessionToken(bmobUser.getSessionToken());
                                user.setMobilePhoneNumber(bmobUser.getMobilePhoneNumber());
                                user.setMobilePhoneVerified(bmobUser.getMobilePhoneVerified());
                                user.setEmail(bmobUser.getEmail());
                                user.setEmailVerified(bmobUser.getEmailVerified());
                                user.setNickname(bmobUser.getNickname());
                                user.setSex(bmobUser.getSex());
                                user.setIconurl(bmobUser.getIconurl());
                                user.setBirthday(bmobUser.getBirthday());
                                user.setCreateDate(bmobUser.getCreateDate());
                                return Observable.just(user);
                            }else{
                                return Observable.error(new NullPointerException());
                            }
                            
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobUserResultResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobUser>, Observable<User>>() {
                    @Override
                    public Observable<User> call(Response<BmobUser> bmobUserResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                        if(bmobUserResponse.isSuccessful()){
                            BmobUser bmobUser = bmobUserResponse.body();
                            User user = new User();
                            user.setId(bmobUser.getObjectId());
                            user.setUserId(bmobUser.getUserId());
                            user.setUsername(bmobUser.getUsername());
                            user.setSessionToken(bmobUser.getSessionToken());
                            user.setMobilePhoneNumber(bmobUser.getMobilePhoneNumber());
                            user.setMobilePhoneVerified(bmobUser.getMobilePhoneVerified());
                            user.setEmail(bmobUser.getEmail());
                            user.setEmailVerified(bmobUser.getEmailVerified());
                            user.setNickname(bmobUser.getNickname());
                            user.setSex(bmobUser.getSex());
                            user.setIconurl(bmobUser.getIconurl());
                            user.setBirthday(bmobUser.getBirthday());
                            user.setCreateDate(bmobUser.getCreateDate());
                            return Observable.just(user);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobUserResponse.errorBody().string();
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

        BmobCreateUserRequest bmobCreateUserRequest = new BmobCreateUserRequest();
        bmobCreateUserRequest.setUserId(user.getUserId());
        bmobCreateUserRequest.setUsername(user.getUsername());
        bmobCreateUserRequest.setPassword(user.getPassword());
        bmobCreateUserRequest.setSex(user.getSex());
        bmobCreateUserRequest.setMobilePhoneNumber(user.getMobilePhoneNumber());
        bmobCreateUserRequest.setEmail(user.getEmail());
        bmobCreateUserRequest.setNickname(user.getNickname());
        bmobCreateUserRequest.setIconurl(user.getIconurl());
        bmobCreateUserRequest.setBirthday(user.getBirthday());
        bmobCreateUserRequest.setCreateDate(user.getCreateDate());

        return bmobService.updateUserRx(user.getSessionToken(),user.getId(),bmobCreateUserRequest)
        .flatMap(new Func1<Response<BmobUser>, Observable<User>>() {
            @Override
            public Observable<User> call(Response<BmobUser> bmobUserResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.PASSWORDRESET.getDefauleError().getMessage());
                if(bmobUserResponse.isSuccessful()){
                    BmobUser bmobUser = bmobUserResponse.body();
                    User user = new User();
                    user.setId(bmobUser.getObjectId());
                    user.setUserId(bmobUser.getUserId());
                    user.setUsername(bmobUser.getUsername());
                    user.setSessionToken(bmobUser.getSessionToken());
                    user.setMobilePhoneNumber(bmobUser.getMobilePhoneNumber());
                    user.setMobilePhoneVerified(bmobUser.getMobilePhoneVerified());
                    user.setEmail(bmobUser.getEmail());
                    user.setEmailVerified(bmobUser.getEmailVerified());
                    user.setNickname(bmobUser.getNickname());
                    user.setSex(bmobUser.getSex());
                    user.setIconurl(bmobUser.getIconurl());
                    user.setBirthday(bmobUser.getBirthday());
                    user.setCreateDate(bmobUser.getCreateDate());
                    return Observable.just(user);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  bmobUserResponse.errorBody().string();
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
        final BmobCreatePhoneticsSymbolsRequest bmobCreatePhoneticsSymbolsRequest = new BmobCreatePhoneticsSymbolsRequest();

        bmobCreatePhoneticsSymbolsRequest.setPhoneticsSymbolsId(phoneticsSymbols.getPhoneticsSymbolsId());
        bmobCreatePhoneticsSymbolsRequest.setIpaname(phoneticsSymbols.getIpaname());
        bmobCreatePhoneticsSymbolsRequest.setKkname(phoneticsSymbols.getKkname());
        bmobCreatePhoneticsSymbolsRequest.setSoundurl(phoneticsSymbols.getSoundurl());
        bmobCreatePhoneticsSymbolsRequest.setVideourl(phoneticsSymbols.getVideourl());
        bmobCreatePhoneticsSymbolsRequest.setContent(phoneticsSymbols.getContent());
        bmobCreatePhoneticsSymbolsRequest.setIsvowel(phoneticsSymbols.getIsvowel());

        return bmobService.addPhoneticsSymbols(bmobCreatePhoneticsSymbolsRequest)
                .flatMap(new Func1<Response<BmobPhoneticsSymbols>, Observable<PhoneticsSymbols>>() {
                    @Override
                    public Observable<PhoneticsSymbols> call(Response<BmobPhoneticsSymbols> bmobPhoneticsSymbolsResponse) {
                        Log.d(TAG,"addPhoneticsSymbols->call" + Thread.currentThread().getName());
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsSymbolsResponse.isSuccessful()){

                            BmobPhoneticsSymbols bmobPhoneticsSymbols = bmobPhoneticsSymbolsResponse.body();
                            PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();

                            phoneticsSymbols.setId(bmobPhoneticsSymbols.getObjectId());
                            phoneticsSymbols.setIpaname(bmobPhoneticsSymbols.getIpaname());
                            phoneticsSymbols.setKkname(bmobPhoneticsSymbols.getKkname());
                            phoneticsSymbols.setSoundurl(bmobPhoneticsSymbols.getSoundurl());
                            phoneticsSymbols.setVideourl(bmobPhoneticsSymbols.getVideourl());
                            phoneticsSymbols.setContent(bmobPhoneticsSymbols.getContent());
                            phoneticsSymbols.setIsvowel(bmobPhoneticsSymbols.getIsvowel());

                            return Observable.just(phoneticsSymbols);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsSymbolsResponse.errorBody().string();
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

        final BmobCreatePhoneticsSymbolsRequest bmobCreatePhoneticsSymbolsRequest = new BmobCreatePhoneticsSymbolsRequest();
        bmobCreatePhoneticsSymbolsRequest.setPhoneticsSymbolsId(phoneticsSymbols.getPhoneticsSymbolsId());
        bmobCreatePhoneticsSymbolsRequest.setIpaname(phoneticsSymbols.getIpaname());
        bmobCreatePhoneticsSymbolsRequest.setKkname(phoneticsSymbols.getKkname());
        bmobCreatePhoneticsSymbolsRequest.setSoundurl(phoneticsSymbols.getSoundurl());
        bmobCreatePhoneticsSymbolsRequest.setVideourl(phoneticsSymbols.getVideourl());
        bmobCreatePhoneticsSymbolsRequest.setContent(phoneticsSymbols.getContent());
        bmobCreatePhoneticsSymbolsRequest.setIsvowel(phoneticsSymbols.getIsvowel());

        return bmobService.updatePhoneticsSymbolsRxById(phoneticsSymbols.getId(),bmobCreatePhoneticsSymbolsRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobPhoneticsSymbols>, Observable<PhoneticsSymbols>>() {
                    @Override
                    public Observable<PhoneticsSymbols> call(Response<BmobPhoneticsSymbols> bmobPhoneticsSymbolsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsSymbolsResponse.isSuccessful()){

                            BmobPhoneticsSymbols bmobPhoneticsSymbols = bmobPhoneticsSymbolsResponse.body();
                            PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();

                            phoneticsSymbols.setId(bmobPhoneticsSymbols.getObjectId());
                            phoneticsSymbols.setIpaname(bmobPhoneticsSymbols.getIpaname());
                            phoneticsSymbols.setKkname(bmobPhoneticsSymbols.getKkname());
                            phoneticsSymbols.setSoundurl(bmobPhoneticsSymbols.getSoundurl());
                            phoneticsSymbols.setVideourl(bmobPhoneticsSymbols.getVideourl());
                            phoneticsSymbols.setContent(bmobPhoneticsSymbols.getContent());
                            phoneticsSymbols.setIsvowel(bmobPhoneticsSymbols.getIsvowel());

                            return Observable.just(phoneticsSymbols);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsSymbolsResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobPhoneticsSymbolsResult>, Observable<List<PhoneticsSymbols>>>() {
                    @Override
                    public Observable<List<PhoneticsSymbols>> call(Response<BmobPhoneticsSymbolsResult> bmobPhoneticsSymbolsResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsSymbolsResultResponse.isSuccessful()){
                            BmobPhoneticsSymbolsResult bmobPhoneticsSymbolsResult = bmobPhoneticsSymbolsResultResponse.body();

                            List<BmobPhoneticsSymbols> bmobPhoneticsSymbolses = bmobPhoneticsSymbolsResult.getResults();
                            List<PhoneticsSymbols> phoneticsSymbolses = new ArrayList<>();

                            for(BmobPhoneticsSymbols bmobPhoneticsSymbols:bmobPhoneticsSymbolses){
                                PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();

                                phoneticsSymbols.setId(bmobPhoneticsSymbols.getObjectId());
                                phoneticsSymbols.setIpaname(bmobPhoneticsSymbols.getIpaname());
                                phoneticsSymbols.setKkname(bmobPhoneticsSymbols.getKkname());
                                phoneticsSymbols.setSoundurl(bmobPhoneticsSymbols.getSoundurl());
                                phoneticsSymbols.setVideourl(bmobPhoneticsSymbols.getVideourl());
                                phoneticsSymbols.setContent(bmobPhoneticsSymbols.getContent());
                                phoneticsSymbols.setIsvowel(bmobPhoneticsSymbols.getIsvowel());
                                phoneticsSymbolses.add(phoneticsSymbols);
                            }
                            return Observable.just(phoneticsSymbolses);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsSymbolsResultResponse.errorBody().string();
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
        final BmobCreatePhoneticsWordsRequest bmobCreatePhoneticsWordsRequest = new BmobCreatePhoneticsWordsRequest();

        bmobCreatePhoneticsWordsRequest.setPhoneticswordsId(phoneticsWords.getPhoneticswordsId());
        bmobCreatePhoneticsWordsRequest.setPhoneticsSymbolsId(phoneticsWords.getPhoneticsSymbolsId());
        bmobCreatePhoneticsWordsRequest.setWordgroupId(phoneticsWords.getWordgroupId());

        return bmobService.addPhoneticsWords(bmobCreatePhoneticsWordsRequest)
                .flatMap(new Func1<Response<BmobPhoneticsWords>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<BmobPhoneticsWords> bmobPhoneticsWordsResponse) {
                        Log.d(TAG,"addPhoneticsSymbols->call" + Thread.currentThread().getName());
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsWordsResponse.isSuccessful()){

                            BmobPhoneticsWords bmobPhoneticsWords = bmobPhoneticsWordsResponse.body();
                            PhoneticsWords phoneticsWords = new PhoneticsWords();

                            phoneticsWords.setId(bmobPhoneticsWords.getObjectId());
                            phoneticsWords.setPhoneticswordsId(bmobPhoneticsWords.getPhoneticswordsId());
                            phoneticsWords.setPhoneticsSymbolsId(bmobPhoneticsWords.getPhoneticsSymbolsId());
                            phoneticsWords.setWordgroupId(bmobPhoneticsWords.getWordgroupId());

                            return Observable.just(phoneticsWords);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsWordsResponse.errorBody().string();
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

        final BmobCreatePhoneticsWordsRequest bmobCreatePhoneticsWordsRequest = new BmobCreatePhoneticsWordsRequest();

        bmobCreatePhoneticsWordsRequest.setPhoneticswordsId(phoneticsWords.getPhoneticswordsId());
        bmobCreatePhoneticsWordsRequest.setPhoneticsSymbolsId(phoneticsWords.getPhoneticsSymbolsId());
        bmobCreatePhoneticsWordsRequest.setWordgroupId(phoneticsWords.getWordgroupId());

        return bmobService.updatePhoneticsWordsRxById(phoneticsWords.getId(),bmobCreatePhoneticsWordsRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobPhoneticsWords>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<BmobPhoneticsWords> bmobPhoneticsWordsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsWordsResponse.isSuccessful()){

                            BmobPhoneticsWords bmobPhoneticsWords = bmobPhoneticsWordsResponse.body();
                            PhoneticsWords phoneticsWords = new PhoneticsWords();

                            phoneticsWords.setId(bmobPhoneticsWords.getObjectId());
                            phoneticsWords.setPhoneticswordsId(bmobPhoneticsWords.getPhoneticswordsId());
                            phoneticsWords.setPhoneticsSymbolsId(bmobPhoneticsWords.getPhoneticsSymbolsId());
                            phoneticsWords.setWordgroupId(bmobPhoneticsWords.getWordgroupId());

                            return Observable.just(phoneticsWords);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsWordsResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobPhoneticsWords>, Observable<PhoneticsWords>>() {
                    @Override
                    public Observable<PhoneticsWords> call(Response<BmobPhoneticsWords> bmobPhoneticsWordsResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsWordsResponse.isSuccessful()){

                            BmobPhoneticsWords bmobPhoneticsWords = bmobPhoneticsWordsResponse.body();
                            PhoneticsWords phoneticsWords = new PhoneticsWords();

                            phoneticsWords.setId(bmobPhoneticsWords.getObjectId());
                            phoneticsWords.setPhoneticswordsId(bmobPhoneticsWords.getPhoneticswordsId());
                            phoneticsWords.setPhoneticsSymbolsId(bmobPhoneticsWords.getPhoneticsSymbolsId());
                            phoneticsWords.setWordgroupId(bmobPhoneticsWords.getWordgroupId());

                            return Observable.just(phoneticsWords);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsWordsResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobPhoneticsWordsResult>, Observable<List<PhoneticsWords>>>() {
                    @Override
                    public Observable<List<PhoneticsWords>> call(Response<BmobPhoneticsWordsResult> bmobPhoneticsSymbolsResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobPhoneticsSymbolsResultResponse.isSuccessful()){
                            BmobPhoneticsWordsResult bmobPhoneticsWordsResult = bmobPhoneticsSymbolsResultResponse.body();

                            List<BmobPhoneticsWords> bmobPhoneticsWordses = bmobPhoneticsWordsResult.getResults();
                            List<PhoneticsWords> phoneticsWordses = new ArrayList<>();

                            for(BmobPhoneticsWords bmobPhoneticsWords:bmobPhoneticsWordses){
                                PhoneticsWords phoneticsWords = new PhoneticsWords();

                                phoneticsWords.setId(bmobPhoneticsWords.getObjectId());
                                phoneticsWords.setPhoneticswordsId(bmobPhoneticsWords.getPhoneticswordsId());
                                phoneticsWords.setPhoneticsSymbolsId(bmobPhoneticsWords.getPhoneticsSymbolsId());
                                phoneticsWords.setWordgroupId(bmobPhoneticsWords.getWordgroupId());

                                phoneticsWordses.add(phoneticsWords);
                            }
                            return Observable.just(phoneticsWordses);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobPhoneticsSymbolsResultResponse.errorBody().string();
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
    public Observable<MsSource> addMssource(MsSource msSource) {

        final BmobCreateMsSourceRequest bmobCreateMsSourceRequest = new BmobCreateMsSourceRequest();
        bmobCreateMsSourceRequest.setName(msSource.getName());
        bmobCreateMsSourceRequest.setMsSourceId(msSource.getMsSourceId());

        return bmobService.addMssource(bmobCreateMsSourceRequest)
                .flatMap(new Func1<Response<BmobMsSource>, Observable<MsSource>>() {
                    @Override
                    public Observable<MsSource> call(Response<BmobMsSource> bmobMsSourceResponse) {
                        Log.d(TAG,"addMssource->call" + Thread.currentThread().getName());
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobMsSourceResponse.isSuccessful()){

                            BmobMsSource bmobMsSource = bmobMsSourceResponse.body();
                            MsSource msSource = new MsSource();
                            msSource.setMsSourceId(bmobMsSource.getMsSourceId());
                            msSource.setId(bmobMsSource.getObjectId());
                            msSource.setName(bmobMsSource.getName());

                            return Observable.just(msSource);
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
                }).compose(RxUtil.<MsSource>applySchedulers());

    }

    @Override
    public Observable<Boolean> deleteMssourceById(String msSourceId) {
        return bmobService.deleteMssourceById(msSourceId)
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
    public Observable<Boolean> updateMssourceRxById(MsSource msSource) {

        BmobCreateMsSourceRequest bmobCreateMsSourceRequest = new BmobCreateMsSourceRequest();
        bmobCreateMsSourceRequest.setName(msSource.getName());
        bmobCreateMsSourceRequest.setMsSourceId(msSource.getMsSourceId());

        return bmobService.updateMssourceRxById(msSource.getId(),bmobCreateMsSourceRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
    public Observable<MsSource> getMssourceRxById(String msSourceId) {
        return bmobService.getMssourceRxById(msSourceId)
        .flatMap(new Func1<Response<BmobMsSource>, Observable<MsSource>>() {
            @Override
            public Observable<MsSource> call(Response<BmobMsSource> bmobMsSourceResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(bmobMsSourceResponse.isSuccessful()){

                    BmobMsSource bmobMsSource = bmobMsSourceResponse.body();
                    MsSource msSource = new MsSource();
                    msSource.setMsSourceId(bmobMsSource.getMsSourceId());
                    msSource.setId(bmobMsSource.getObjectId());
                    msSource.setName(bmobMsSource.getName());

                    return Observable.just(msSource);
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
        }).compose(RxUtil.<MsSource>applySchedulers());
    }

    @Override
    public Observable<List<MsSource>> getMssourcesRx() {
        return bmobService.getMssourceRx()
        .flatMap(new Func1<Response<BmobMsSourceResult>, Observable<List<MsSource>>>() {
            @Override
            public Observable<List<MsSource>> call(Response<BmobMsSourceResult> bmobMsSourceResultResponse) {
                BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                if(bmobMsSourceResultResponse.isSuccessful()){
                    BmobMsSourceResult bmobMsSourceResult = bmobMsSourceResultResponse.body();

                    List<BmobMsSource> bmobMsSources = bmobMsSourceResult.getResults();
                    List<MsSource> msSources = new ArrayList<MsSource>();

                    for(BmobMsSource bmobMsSource:bmobMsSources){
                        MsSource msSource = new MsSource();
                        msSource.setId(bmobMsSource.getObjectId());
                        msSource.setMsSourceId(bmobMsSource.getMsSourceId());
                        msSource.setName(bmobMsSource.getName());
                        msSources.add(msSource);
                    }
                    return Observable.just(msSources);
                }else{
                    Gson gson = new GsonBuilder().create();
                    try {
                        String errjson =  bmobMsSourceResultResponse.errorBody().string();
                        BmobDefaultError bmobDefaultError = gson.fromJson(errjson,BmobDefaultError.class);
                        RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                        bmobRequestException = new BmobRequestException(createuser.getMessage());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return Observable.error(bmobRequestException);
            }
        }).compose(RxUtil.<List<MsSource>>applySchedulers());
    }

    @Override
    public Observable<TractateType> addTractateType(TractateType tractateType) {
        final BmobCreateTractateTypeRequest bmobCreateTractateTypeRequest = new BmobCreateTractateTypeRequest();
        bmobCreateTractateTypeRequest.setName(tractateType.getName());
        bmobCreateTractateTypeRequest.setTractatetypeId(tractateType.getTractatetypeId());
        return bmobService.addTractateType(bmobCreateTractateTypeRequest)
                .flatMap(new Func1<Response<BmobTractateType>, Observable<TractateType>>() {
                    @Override
                    public Observable<TractateType> call(Response<BmobTractateType> bmobMsSourceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobMsSourceResponse.isSuccessful()){

                            BmobTractateType bmobTractateType = bmobMsSourceResponse.body();
                            TractateType tractateType = new TractateType();
                            tractateType.setTractatetypeId(bmobTractateType.getTractatetypeId());
                            tractateType.setId(bmobTractateType.getObjectId());
                            tractateType.setName(bmobTractateType.getName());

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
        final BmobCreateTractateTypeRequest bmobCreateTractateTypeRequest = new BmobCreateTractateTypeRequest();
        bmobCreateTractateTypeRequest.setName(tractateType.getName());
        bmobCreateTractateTypeRequest.setTractatetypeId(tractateType.getTractatetypeId());

        return bmobService.updateTractateTypeRxById(tractateType.getId(),bmobCreateTractateTypeRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobTractateType>, Observable<TractateType>>() {
                    @Override
                    public Observable<TractateType> call(Response<BmobTractateType> bmobTractateTypeResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateTypeResponse.isSuccessful()){

                            BmobTractateType bmobTractateType = bmobTractateTypeResponse.body();
                            TractateType tractateType = new TractateType();
                            tractateType.setTractatetypeId(bmobTractateType.getTractatetypeId());
                            tractateType.setId(bmobTractateType.getObjectId());
                            tractateType.setName(bmobTractateType.getName());

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
                .flatMap(new Func1<Response<BmobTractateTypeResult>, Observable<List<TractateType>>>() {
                    @Override
                    public Observable<List<TractateType>> call(Response<BmobTractateTypeResult> bmobTractateTypeResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateTypeResultResponse.isSuccessful()){
                            BmobTractateTypeResult bmobTractateTypeResult = bmobTractateTypeResultResponse.body();

                            List<BmobTractateType> bmobTractateTypes = bmobTractateTypeResult.getResults();
                            List<TractateType> tractateTypes = new ArrayList<>();

                            for(BmobTractateType bmobTractateType:bmobTractateTypes){
                                TractateType tractateType = new TractateType();
                                tractateType.setTractatetypeId(bmobTractateType.getTractatetypeId());
                                tractateType.setId(bmobTractateType.getObjectId());
                                tractateType.setName(bmobTractateType.getName());
                                tractateTypes.add(tractateType);
                            }
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
        final BmobCreateWordRequest bmobCreateWordRequest = new BmobCreateWordRequest();

        bmobCreateWordRequest.setWordId(word.getWordId());
        bmobCreateWordRequest.setName(word.getName());
        bmobCreateWordRequest.setBritish_phonogram(word.getBritish_phonogram());
        bmobCreateWordRequest.setBritish_soundurl(word.getBritish_soundurl());
        bmobCreateWordRequest.setAmerican_phonogram(word.getAmerican_phonogram());
        bmobCreateWordRequest.setAmerican_soundurl(word.getAmerican_soundurl());
        bmobCreateWordRequest.setTranslate(word.getTranslate());
        bmobCreateWordRequest.setCorrelation(word.getCorrelation());
        bmobCreateWordRequest.setRemark(word.getRemark());

        return bmobService.addWord(bmobCreateWordRequest)
                .flatMap(new Func1<Response<BmobWord>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<BmobWord> bmobWordResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordResponse.isSuccessful()){

                            BmobWord bmobWord = bmobWordResponse.body();
                            Word word = new Word();
                            word.setId(bmobWord.getObjectId());
                            word.setWordId(bmobWord.getWordId());
                            word.setName(bmobWord.getName());
                            word.setBritish_phonogram(bmobWord.getBritish_phonogram());
                            word.setBritish_soundurl(bmobWord.getBritish_soundurl());
                            word.setAmerican_phonogram(bmobWord.getAmerican_phonogram());
                            word.setAmerican_soundurl(bmobWord.getAmerican_soundurl());
                            word.setTranslate(bmobWord.getTranslate());
                            word.setCorrelation(bmobWord.getCorrelation());
                            word.setRemark(bmobWord.getRemark());
                            return Observable.just(word);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordResponse.errorBody().string();
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
        final BmobCreateWordRequest bmobCreateWordRequest = new BmobCreateWordRequest();

        bmobCreateWordRequest.setWordId(word.getWordId());
        bmobCreateWordRequest.setName(word.getName());
        bmobCreateWordRequest.setBritish_phonogram(word.getBritish_phonogram());
        bmobCreateWordRequest.setBritish_soundurl(word.getBritish_soundurl());
        bmobCreateWordRequest.setAmerican_phonogram(word.getAmerican_phonogram());
        bmobCreateWordRequest.setAmerican_soundurl(word.getAmerican_soundurl());
        bmobCreateWordRequest.setTranslate(word.getTranslate());
        bmobCreateWordRequest.setCorrelation(word.getCorrelation());
        bmobCreateWordRequest.setRemark(word.getRemark());

        return bmobService.updateWordRxById(word.getId(),bmobCreateWordRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
    public Observable<Word> getWordRxById(String wordId) {
        return bmobService.getWordRxById(wordId)
                .flatMap(new Func1<Response<BmobWord>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<BmobWord> bmobBmobWordResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobWordResponse.isSuccessful()){

                            BmobWord bmobWord = bmobBmobWordResponse.body();
                            Word word = new Word();
                            word.setId(bmobWord.getObjectId());
                            word.setWordId(bmobWord.getWordId());
                            word.setName(bmobWord.getName());
                            word.setBritish_phonogram(bmobWord.getBritish_phonogram());
                            word.setBritish_soundurl(bmobWord.getBritish_soundurl());
                            word.setAmerican_phonogram(bmobWord.getAmerican_phonogram());
                            word.setAmerican_soundurl(bmobWord.getAmerican_soundurl());
                            word.setTranslate(bmobWord.getTranslate());
                            word.setCorrelation(bmobWord.getCorrelation());
                            word.setRemark(bmobWord.getRemark());
                            return Observable.just(word);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobBmobWordResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobWordResult>, Observable<Word>>() {
                    @Override
                    public Observable<Word> call(Response<BmobWordResult> bmobWordResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordResultResponse.isSuccessful()){
                            BmobWordResult bmobWordResult = bmobWordResultResponse.body();
                            List<BmobWord> bmobWords = bmobWordResult.getResults();
                            if(bmobWords != null && bmobWords.size() > 0){
                                BmobWord bmobWord  = bmobWords.get(0);

                                Word word = new Word();
                                word.setId(bmobWord.getObjectId());
                                word.setWordId(bmobWord.getWordId());
                                word.setName(bmobWord.getName());
                                word.setBritish_phonogram(bmobWord.getBritish_phonogram());
                                word.setBritish_soundurl(bmobWord.getBritish_soundurl());
                                word.setAmerican_phonogram(bmobWord.getAmerican_phonogram());
                                word.setAmerican_soundurl(bmobWord.getAmerican_soundurl());
                                word.setTranslate(bmobWord.getTranslate());
                                word.setCorrelation(bmobWord.getCorrelation());
                                word.setRemark(bmobWord.getRemark());
                                return Observable.just(word);
                            }else{
                                return Observable.error(new NullPointerException());
                            }

                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordResultResponse.errorBody().string();
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
    public Observable<Sentence> addSentence(Sentence sentence) {
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
                .flatMap(new Func1<Response<BmobSentence>, Observable<Sentence>>() {
                    @Override
                    public Observable<Sentence> call(Response<BmobSentence> bmobSentenceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceResponse.isSuccessful()){

                            BmobSentence bmobSentence = bmobSentenceResponse.body();
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
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceResponse.errorBody().string();
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
        BmobCreateSentenceRequest bmobCreateSentenceRequest = new BmobCreateSentenceRequest();

        bmobCreateSentenceRequest.setSentenceId(sentence.getSentenceId());
        bmobCreateSentenceRequest.setContent(sentence.getContent());
        bmobCreateSentenceRequest.setTranslation(sentence.getTranslation());
        bmobCreateSentenceRequest.setCreateDate(sentence.getCreateDate());
        bmobCreateSentenceRequest.setRemark(sentence.getRemark());
        bmobCreateSentenceRequest.setSource(sentence.getSource());
        bmobCreateSentenceRequest.setTractateId(sentence.getTractateId());
        bmobCreateSentenceRequest.setUserId(sentence.getUserId());

        return bmobService.updateSentencRxById(sentence.getId(),bmobCreateSentenceRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobSentence>, Observable<Sentence>>() {
                    @Override
                    public Observable<Sentence> call(Response<BmobSentence> bmobSentenceResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceResponse.isSuccessful()){

                            BmobSentence bmobSentence = bmobSentenceResponse.body();
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
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobSentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<BmobSentenceResult> bmobSentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceResultResponse.isSuccessful()){
                            BmobSentenceResult bmobSentenceResult = bmobSentenceResultResponse.body();

                            List<BmobSentence> bmobSentences = bmobSentenceResult.getResults();
                            List<Sentence> sentences = new ArrayList<>();

                            for(BmobSentence bmobSentence:bmobSentences){

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
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceResultResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobSentenceResult>, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(Response<BmobSentenceResult> bmobSentenceResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceResultResponse.isSuccessful()){
                            BmobSentenceResult bmobSentenceResult = bmobSentenceResultResponse.body();

                            List<BmobSentence> bmobSentences = bmobSentenceResult.getResults();
                            List<Sentence> sentences = new ArrayList<>();

                            for(BmobSentence bmobSentence:bmobSentences){

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
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceResultResponse.errorBody().string();
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

        BmobCreateGrammarRequest bmobCreateGrammarRequest = new BmobCreateGrammarRequest();
        bmobCreateGrammarRequest.setGrammarId(grammar.getGrammarId());
        bmobCreateGrammarRequest.setContent(grammar.getContent());
        bmobCreateGrammarRequest.setCreateDate(grammar.getCreateDate());
        bmobCreateGrammarRequest.setTitle(grammar.getTitle());
        bmobCreateGrammarRequest.setUserId(grammar.getUserId());
        bmobCreateGrammarRequest.setRemark(grammar.getRemark());

        return bmobService.addGrammarRx(bmobCreateGrammarRequest)
                .flatMap(new Func1<Response<BmobGrammar>, Observable<Grammar>>() {
                    @Override
                    public Observable<Grammar> call(Response<BmobGrammar> bmobGrammarResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResponse.isSuccessful()){

                            BmobGrammar bmobGrammar = bmobGrammarResponse.body();
                            Grammar grammar = new Grammar();
                            grammar.setId(bmobGrammar.getObjectId());
                            grammar.setGrammarId(bmobGrammar.getGrammarId());
                            grammar.setContent(bmobGrammar.getContent());
                            grammar.setCreateDate(bmobGrammar.getCreateDate());
                            grammar.setTitle(bmobGrammar.getTitle());
                            grammar.setUserId(bmobGrammar.getUserId());
                            grammar.setRemark(bmobGrammar.getRemark());

                            return Observable.just(grammar);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobGrammarResponse.errorBody().string();
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
        BmobCreateGrammarRequest bmobCreateGrammarRequest = new BmobCreateGrammarRequest();
        bmobCreateGrammarRequest.setGrammarId(grammar.getGrammarId());
        bmobCreateGrammarRequest.setContent(grammar.getContent());
        bmobCreateGrammarRequest.setCreateDate(grammar.getCreateDate());
        bmobCreateGrammarRequest.setTitle(grammar.getTitle());
        bmobCreateGrammarRequest.setUserId(grammar.getUserId());
        bmobCreateGrammarRequest.setRemark(grammar.getRemark());

        return bmobService.updateGrammarRxById(grammar.getId(),bmobCreateGrammarRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobGrammar>, Observable<Grammar>>() {
                    @Override
                    public Observable<Grammar> call(Response<BmobGrammar> bmobGrammarResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResponse.isSuccessful()){

                            BmobGrammar bmobGrammar = bmobGrammarResponse.body();
                            Grammar grammar = new Grammar();
                            grammar.setId(bmobGrammar.getObjectId());
                            grammar.setGrammarId(bmobGrammar.getGrammarId());
                            grammar.setContent(bmobGrammar.getContent());
                            grammar.setCreateDate(bmobGrammar.getCreateDate());
                            grammar.setTitle(bmobGrammar.getTitle());
                            grammar.setUserId(bmobGrammar.getUserId());
                            grammar.setRemark(bmobGrammar.getRemark());
                            return Observable.just(grammar);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobGrammarResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobGrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<BmobGrammarResult> bmobGrammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResultResponse.isSuccessful()){
                            BmobGrammarResult bmobGrammarResult = bmobGrammarResultResponse.body();

                            List<BmobGrammar> bmobGrammars = bmobGrammarResult.getResults();
                            List<Grammar> grammars = new ArrayList<>();

                            for(BmobGrammar bmobGrammar:bmobGrammars){

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
    public Observable<List<Grammar>> getGrammars(int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        return bmobService.getGrammarsRx(limit,skip)
                .flatMap(new Func1<Response<BmobGrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<BmobGrammarResult> bmobGrammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResultResponse.isSuccessful()){
                            BmobGrammarResult bmobGrammarResult = bmobGrammarResultResponse.body();

                            List<BmobGrammar> bmobGrammars = bmobGrammarResult.getResults();
                            List<Grammar> grammars = new ArrayList<>();

                            for(BmobGrammar bmobGrammar:bmobGrammars){

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
                .flatMap(new Func1<Response<BmobGrammarResult>, Observable<List<Grammar>>>() {
                    @Override
                    public Observable<List<Grammar>> call(Response<BmobGrammarResult> bmobGrammarResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobGrammarResultResponse.isSuccessful()){
                            BmobGrammarResult bmobGrammarResult = bmobGrammarResultResponse.body();

                            List<BmobGrammar> bmobGrammars = bmobGrammarResult.getResults();
                            List<Grammar> grammars = new ArrayList<>();

                            for(BmobGrammar bmobGrammar:bmobGrammars){

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
    public Observable<Tractate> addTractate(Tractate tractate) {
        final BmobCreateTractateRequest bmobCreateTractateRequest = new BmobCreateTractateRequest();

        bmobCreateTractateRequest.setUserId(tractate.getUserId());
        bmobCreateTractateRequest.setSource(tractate.getSource());
        bmobCreateTractateRequest.setTractateId(tractate.getTractateId());
        bmobCreateTractateRequest.setTractatetypeId(tractate.getTractatetypeId());
        bmobCreateTractateRequest.setRemark(tractate.getRemark());
        bmobCreateTractateRequest.setContent(tractate.getContent());
        bmobCreateTractateRequest.setCreateDate(tractate.getCreateDate());
        bmobCreateTractateRequest.setTitle(tractate.getTitle());
        bmobCreateTractateRequest.setTranslation(tractate.getTranslation());

        return bmobService.addTractate(bmobCreateTractateRequest)
                .flatMap(new Func1<Response<BmobTractate>, Observable<Tractate>>() {
                    @Override
                    public Observable<Tractate> call(Response<BmobTractate> bmobTractateResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResponse.isSuccessful()){

                            BmobTractate bmobTractate = bmobTractateResponse.body();
                            Tractate tractate = new Tractate();
                            tractate.setId(bmobTractate.getObjectId());
                            tractate.setUserId(bmobTractate.getUserId());
                            tractate.setSource(bmobTractate.getSource());
                            tractate.setTractateId(bmobTractate.getTractateId());
                            tractate.setTractatetypeId(bmobTractate.getTractatetypeId());
                            tractate.setRemark(bmobTractate.getRemark());
                            tractate.setContent(bmobTractate.getContent());
                            tractate.setCreateDate(bmobTractate.getCreateDate());
                            tractate.setTitle(bmobTractate.getTitle());
                            tractate.setTranslation(bmobTractate.getTranslation());
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
        final BmobCreateTractateRequest bmobCreateTractateRequest = new BmobCreateTractateRequest();

        bmobCreateTractateRequest.setUserId(tractate.getUserId());
        bmobCreateTractateRequest.setSource(tractate.getSource());
        bmobCreateTractateRequest.setTractateId(tractate.getTractateId());
        bmobCreateTractateRequest.setTractatetypeId(tractate.getTractatetypeId());
        bmobCreateTractateRequest.setRemark(tractate.getRemark());
        bmobCreateTractateRequest.setContent(tractate.getContent());
        bmobCreateTractateRequest.setCreateDate(tractate.getCreateDate());
        bmobCreateTractateRequest.setTitle(tractate.getTitle());
        bmobCreateTractateRequest.setTranslation(tractate.getTranslation());

        return bmobService.updateTractateRxById(tractate.getId(),bmobCreateTractateRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobTractate>, Observable<Tractate>>() {
                    @Override
                    public Observable<Tractate> call(Response<BmobTractate> bmobBmobTractateResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobTractateResponse.isSuccessful()){

                            BmobTractate bmobTractate = bmobBmobTractateResponse.body();
                            Tractate tractate = new Tractate();
                            tractate.setId(bmobTractate.getObjectId());
                            tractate.setUserId(bmobTractate.getUserId());
                            tractate.setSource(bmobTractate.getSource());
                            tractate.setTractateId(bmobTractate.getTractateId());
                            tractate.setTractatetypeId(bmobTractate.getTractatetypeId());
                            tractate.setRemark(bmobTractate.getRemark());
                            tractate.setContent(bmobTractate.getContent());
                            tractate.setCreateDate(bmobTractate.getCreateDate());
                            tractate.setTitle(bmobTractate.getTitle());
                            tractate.setTranslation(bmobTractate.getTranslation());
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
        String regex = searchUtil.getBmobEquals("tractateId",tractateTypeId);

        return bmobService.getTractatesRx(regex,limit,skip)
                .flatMap(new Func1<Response<BmobTractateResult>, Observable<List<Tractate>>>() {
                    @Override
                    public Observable<List<Tractate>> call(Response<BmobTractateResult> bmobTractateResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResultResponse.isSuccessful()){
                            BmobTractateResult bmobTractateResult = bmobTractateResultResponse.body();

                            List<BmobTractate> bmobTractates = bmobTractateResult.getResults();
                            List<Tractate> tractates = new ArrayList<>();

                            for(BmobTractate bmobTractate:bmobTractates){

                                Tractate tractate = new Tractate();
                                tractate.setId(bmobTractate.getObjectId());
                                tractate.setUserId(bmobTractate.getUserId());
                                tractate.setSource(bmobTractate.getSource());
                                tractate.setTractateId(bmobTractate.getTractateId());
                                tractate.setTractatetypeId(bmobTractate.getTractatetypeId());
                                tractate.setRemark(bmobTractate.getRemark());
                                tractate.setContent(bmobTractate.getContent());
                                tractate.setCreateDate(bmobTractate.getCreateDate());
                                tractate.setTitle(bmobTractate.getTitle());
                                tractate.setTranslation(bmobTractate.getTranslation());
                                tractates.add(tractate);
                            }
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
                .flatMap(new Func1<Response<BmobTractateResult>, Observable<List<Tractate>>>() {
                    @Override
                    public Observable<List<Tractate>> call(Response<BmobTractateResult> bmobTractateResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateResultResponse.isSuccessful()){
                            BmobTractateResult bmobTractateResult = bmobTractateResultResponse.body();

                            List<BmobTractate> bmobTractates = bmobTractateResult.getResults();
                            List<Tractate> tractates = new ArrayList<>();

                            for(BmobTractate bmobTractate:bmobTractates){

                                Tractate tractate = new Tractate();
                                tractate.setId(bmobTractate.getObjectId());
                                tractate.setUserId(bmobTractate.getUserId());
                                tractate.setSource(bmobTractate.getSource());
                                tractate.setTractateId(bmobTractate.getTractateId());
                                tractate.setTractatetypeId(bmobTractate.getTractatetypeId());
                                tractate.setRemark(bmobTractate.getRemark());
                                tractate.setContent(bmobTractate.getContent());
                                tractate.setCreateDate(bmobTractate.getCreateDate());
                                tractate.setTitle(bmobTractate.getTitle());
                                tractate.setTranslation(bmobTractate.getTranslation());
                                tractates.add(tractate);
                            }
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
        final BmobCreateWordGroupRequest bmobCreateWordGroupRequest = new BmobCreateWordGroupRequest();
        bmobCreateWordGroupRequest.setName(wordGroup.getName());
        bmobCreateWordGroupRequest.setOpen(wordGroup.getOpen());
        bmobCreateWordGroupRequest.setUserId(wordGroup.getUserId());
        bmobCreateWordGroupRequest.setWordgroupId(wordGroup.getWordgroupId());

        return bmobService.addWordGroup(bmobCreateWordGroupRequest)
                .flatMap(new Func1<Response<BmobWordGroup>, Observable<WordGroup>>() {
                    @Override
                    public Observable<WordGroup> call(Response<BmobWordGroup> bmobWordGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResponse.isSuccessful()){

                            BmobWordGroup bmobWordGroup = bmobWordGroupResponse.body();
                            WordGroup wordGroup = new WordGroup();

                            wordGroup.setId(bmobWordGroup.getObjectId());
                            wordGroup.setName(bmobWordGroup.getName());
                            wordGroup.setOpen(bmobWordGroup.getOpen());
                            wordGroup.setUserId(bmobWordGroup.getUserId());
                            wordGroup.setWordgroupId(bmobWordGroup.getWordgroupId());
                            return Observable.just(wordGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupResponse.errorBody().string();
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
    public Observable<Boolean> updateWordGroupRxById(WordGroup wordGroup) {
        final BmobCreateWordGroupRequest bmobCreateWordGroupRequest = new BmobCreateWordGroupRequest();
        bmobCreateWordGroupRequest.setName(wordGroup.getName());
        bmobCreateWordGroupRequest.setOpen(wordGroup.getOpen());
        bmobCreateWordGroupRequest.setUserId(wordGroup.getUserId());
        bmobCreateWordGroupRequest.setWordgroupId(wordGroup.getWordgroupId());

        return bmobService.updateWordGroupRxById(wordGroup.getId(),bmobCreateWordGroupRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobWordGroup>, Observable<WordGroup>>() {
                    @Override
                    public Observable<WordGroup> call(Response<BmobWordGroup> bmobBmobWordGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobWordGroupResponse.isSuccessful()){

                            BmobWordGroup bmobWordGroup = bmobBmobWordGroupResponse.body();
                            WordGroup wordGroup = new WordGroup();

                            wordGroup.setId(bmobWordGroup.getObjectId());
                            wordGroup.setName(bmobWordGroup.getName());
                            wordGroup.setOpen(bmobWordGroup.getOpen());
                            wordGroup.setUserId(bmobWordGroup.getUserId());
                            wordGroup.setWordgroupId(bmobWordGroup.getWordgroupId());
                            return Observable.just(wordGroup);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobBmobWordGroupResponse.errorBody().string();
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
                .flatMap(new Func1<Response<BmobWordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<BmobWordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            BmobWordGroupResult bmobWordGroupResult = bmobWordGroupResultResponse.body();

                            List<BmobWordGroup> bmobWordGroups = bmobWordGroupResult.getResults();
                            List<WordGroup> wordGroups = new ArrayList<>();

                            for(BmobWordGroup bmobWordGroup:bmobWordGroups){

                                WordGroup wordGroup = new WordGroup();

                                wordGroup.setId(bmobWordGroup.getObjectId());
                                wordGroup.setName(bmobWordGroup.getName());
                                wordGroup.setOpen(bmobWordGroup.getOpen());
                                wordGroup.setUserId(bmobWordGroup.getUserId());
                                wordGroup.setWordgroupId(bmobWordGroup.getWordgroupId());
                                wordGroups.add(wordGroup);
                            }
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
                .flatMap(new Func1<Response<BmobWordGroupResult>, Observable<List<WordGroup>>>() {
                    @Override
                    public Observable<List<WordGroup>> call(Response<BmobWordGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            BmobWordGroupResult bmobWordGroupResult = bmobWordGroupResultResponse.body();

                            List<BmobWordGroup> bmobWordGroups = bmobWordGroupResult.getResults();
                            List<WordGroup> wordGroups = new ArrayList<>();

                            for(BmobWordGroup bmobWordGroup:bmobWordGroups){

                                WordGroup wordGroup = new WordGroup();

                                wordGroup.setId(bmobWordGroup.getObjectId());
                                wordGroup.setName(bmobWordGroup.getName());
                                wordGroup.setOpen(bmobWordGroup.getOpen());
                                wordGroup.setUserId(bmobWordGroup.getUserId());
                                wordGroup.setWordgroupId(bmobWordGroup.getWordgroupId());
                                wordGroups.add(wordGroup);
                            }
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

        final BmobCreateWordGroupCollectRequest wordGroupCollectRequest = new BmobCreateWordGroupCollectRequest();

        wordGroupCollectRequest.setWordGroupcollectId(wordGroupCollect.getWordGroupcollectId());
        wordGroupCollectRequest.setWordgroupId(wordGroupCollect.getWordgroupId());
        wordGroupCollectRequest.setUserId(wordGroupCollect.getUserId());

        return bmobService.addWordGroupCollect(wordGroupCollectRequest)
                .flatMap(new Func1<Response<BmobWordGroupCollect>, Observable<WordGroupCollect>>() {
                    @Override
                    public Observable<WordGroupCollect> call(Response<BmobWordGroupCollect> bmobWordGroupCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupCollectResponse.isSuccessful()){

                            BmobWordGroupCollect bmobWordGroupCollect = bmobWordGroupCollectResponse.body();
                            WordGroupCollect wordGroupCollect =  new WordGroupCollect();
                            wordGroupCollect.setId(bmobWordGroupCollect.getObjectId());
                            wordGroupCollect.setUserId(bmobWordGroupCollect.getUserId());
                            wordGroupCollect.setWordGroupcollectId(bmobWordGroupCollect.getWordGroupcollectId());
                            wordGroupCollect.setWordgroupId(bmobWordGroupCollect.getWordgroupId());
                            return Observable.just(wordGroupCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordGroupCollectResponse.errorBody().string();
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
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getWordGroupCollectRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<BmobWordGroupCollectResult>, Observable<List<WordGroupCollect>>>() {
                    @Override
                    public Observable<List<WordGroupCollect>> call(Response<BmobWordGroupCollectResult> bmobWordGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupCollectResultResponse.isSuccessful()){
                            BmobWordGroupCollectResult bmobWordGroupCollectResult = bmobWordGroupCollectResultResponse.body();

                            List<BmobWordGroupCollect> bmobWordGroups = bmobWordGroupCollectResult.getResults();
                            List<WordGroupCollect> wordGroupCollects = new ArrayList<>();

                            for(BmobWordGroupCollect bmobWordGroupCollect:bmobWordGroups){

                                WordGroupCollect wordGroupCollect =  new WordGroupCollect();
                                wordGroupCollect.setId(bmobWordGroupCollect.getObjectId());
                                wordGroupCollect.setUserId(bmobWordGroupCollect.getUserId());
                                wordGroupCollect.setWordGroupcollectId(bmobWordGroupCollect.getWordGroupcollectId());
                                wordGroupCollect.setWordgroupId(bmobWordGroupCollect.getWordgroupId());
                                wordGroupCollects.add(wordGroupCollect);
                            }
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
        final BmobCreateSentenceGroupRequest bmobCreateSentenceGroupRequest = new BmobCreateSentenceGroupRequest();

        bmobCreateSentenceGroupRequest.setSentencegroupId(sentenceGroup.getSentencegroupId());
        bmobCreateSentenceGroupRequest.setUserId(sentenceGroup.getUserId());
        bmobCreateSentenceGroupRequest.setOpen(sentenceGroup.getOpen());
        bmobCreateSentenceGroupRequest.setName(sentenceGroup.getName());

        return bmobService.addSentenceGroup(bmobCreateSentenceGroupRequest)
                .flatMap(new Func1<Response<BmobSentenceGroup>, Observable<SentenceGroup>>() {
                    @Override
                    public Observable<SentenceGroup> call(Response<BmobSentenceGroup> bmobSentenceGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupResponse.isSuccessful()){

                            BmobSentenceGroup bmobSentenceGroup = bmobSentenceGroupResponse.body();
                            SentenceGroup sentenceGroup = new SentenceGroup();
                            sentenceGroup.setId(bmobSentenceGroup.getObjectId());
                            sentenceGroup.setSentencegroupId(bmobSentenceGroup.getSentencegroupId());
                            sentenceGroup.setName(bmobSentenceGroup.getName());
                            sentenceGroup.setOpen(bmobSentenceGroup.getOpen());
                            sentenceGroup.setUserId(bmobSentenceGroup.getUserId());

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
        final BmobCreateSentenceGroupRequest bmobCreateSentenceGroupRequest = new BmobCreateSentenceGroupRequest();

        bmobCreateSentenceGroupRequest.setSentencegroupId(sentenceGroup.getSentencegroupId());
        bmobCreateSentenceGroupRequest.setUserId(sentenceGroup.getUserId());
        bmobCreateSentenceGroupRequest.setOpen(sentenceGroup.getOpen());
        bmobCreateSentenceGroupRequest.setName(sentenceGroup.getName());

        return bmobService.updateSentenceGroupRxById(sentenceGroup.getId(),bmobCreateSentenceGroupRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobSentenceGroup>, Observable<SentenceGroup>>() {
                    @Override
                    public Observable<SentenceGroup> call(Response<BmobSentenceGroup> bmobBmobSentenceGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobSentenceGroupResponse.isSuccessful()){

                            BmobSentenceGroup bmobSentenceGroup = bmobBmobSentenceGroupResponse.body();
                            SentenceGroup sentenceGroup = new SentenceGroup();
                            sentenceGroup.setId(bmobSentenceGroup.getObjectId());
                            sentenceGroup.setSentencegroupId(bmobSentenceGroup.getSentencegroupId());
                            sentenceGroup.setName(bmobSentenceGroup.getName());
                            sentenceGroup.setOpen(bmobSentenceGroup.getOpen());
                            sentenceGroup.setUserId(bmobSentenceGroup.getUserId());

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
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getSentenceGroupRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<BmobSentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<BmobSentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            BmobSentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<BmobSentenceGroup> bmobSentenceGroups = bmobSentenceGroupResult.getResults();
                            List<SentenceGroup> sentenceGroups = new ArrayList<>();

                            for(BmobSentenceGroup bmobSentenceGroup:bmobSentenceGroups){

                                SentenceGroup sentenceGroup = new SentenceGroup();
                                sentenceGroup.setId(bmobSentenceGroup.getObjectId());
                                sentenceGroup.setSentencegroupId(bmobSentenceGroup.getSentencegroupId());
                                sentenceGroup.setName(bmobSentenceGroup.getName());
                                sentenceGroup.setOpen(bmobSentenceGroup.getOpen());
                                sentenceGroup.setUserId(bmobSentenceGroup.getUserId());
                                sentenceGroups.add(sentenceGroup);
                            }
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
                .flatMap(new Func1<Response<BmobSentenceGroupResult>, Observable<List<SentenceGroup>>>() {
                    @Override
                    public Observable<List<SentenceGroup>> call(Response<BmobSentenceGroupResult> bmobWordGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordGroupResultResponse.isSuccessful()){
                            BmobSentenceGroupResult bmobSentenceGroupResult = bmobWordGroupResultResponse.body();

                            List<BmobSentenceGroup> bmobSentenceGroups = bmobSentenceGroupResult.getResults();
                            List<SentenceGroup> sentenceGroups = new ArrayList<>();

                            for(BmobSentenceGroup bmobSentenceGroup:bmobSentenceGroups){

                                SentenceGroup sentenceGroup = new SentenceGroup();
                                sentenceGroup.setId(bmobSentenceGroup.getObjectId());
                                sentenceGroup.setSentencegroupId(bmobSentenceGroup.getSentencegroupId());
                                sentenceGroup.setName(bmobSentenceGroup.getName());
                                sentenceGroup.setOpen(bmobSentenceGroup.getOpen());
                                sentenceGroup.setUserId(bmobSentenceGroup.getUserId());
                                sentenceGroups.add(sentenceGroup);
                            }
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
        final BmobCreateSentenceGroupCollectRequest bmobCreateSentenceGroupCollectRequest = new  BmobCreateSentenceGroupCollectRequest();

        bmobCreateSentenceGroupCollectRequest.setSentenceGroupcollId(sentenceGroupCollect.getSentenceGroupCollectId());
        bmobCreateSentenceGroupCollectRequest.setSentencegroupId(sentenceGroupCollect.getSentencegroupId());
        bmobCreateSentenceGroupCollectRequest.setUserId(sentenceGroupCollect.getUserId());

        return bmobService.addSentenceGroupCollect(bmobCreateSentenceGroupCollectRequest)
                .flatMap(new Func1<Response<BmobSentenceGroupCollect>, Observable<SentenceGroupCollect>>() {
                    @Override
                    public Observable<SentenceGroupCollect> call(Response<BmobSentenceGroupCollect> bmobSentenceGroupCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupCollectResponse.isSuccessful()){

                            BmobSentenceGroupCollect bmobSentenceGroupCollect = bmobSentenceGroupCollectResponse.body();
                            SentenceGroupCollect sentenceGroupCollect = new SentenceGroupCollect();

                            sentenceGroupCollect.setId(bmobSentenceGroupCollect.getObjectId());
                            sentenceGroupCollect.setUserId(bmobSentenceGroupCollect.getUserId());
                            sentenceGroupCollect.setSentenceGroupCollectId(bmobSentenceGroupCollect.getSentenceGroupcollId());
                            sentenceGroupCollect.setSentencegroupId(bmobSentenceGroupCollect.getSentencegroupId());

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
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserId(String userId, int page, int pageSize) {
        if(page < 0){
            throw new RuntimeException("The page shoule don't be above 0");
        }

        final int limit = pageSize;
        final int skip = (page) * pageSize;
        String regex = searchUtil.getBmobEquals("userId",userId);

        return bmobService.getSentenceGroupCollectRxByUserId(regex,limit,skip)
                .flatMap(new Func1<Response<BmobSentenceGroupCollectResult>, Observable<List<SentenceGroupCollect>>>() {
                    @Override
                    public Observable<List<SentenceGroupCollect>> call(Response<BmobSentenceGroupCollectResult> bmobSentenceGroupCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceGroupCollectResultResponse.isSuccessful()){
                            BmobSentenceGroupCollectResult bmobSentenceGroupCollectResult = bmobSentenceGroupCollectResultResponse.body();

                            List<BmobSentenceGroupCollect> bmobSentenceGroupCollects = bmobSentenceGroupCollectResult.getResults();
                            List<SentenceGroupCollect> sentenceGroupCollects = new ArrayList<>();

                            for(BmobSentenceGroupCollect bmobSentenceGroupCollect:bmobSentenceGroupCollects){

                                SentenceGroupCollect sentenceGroupCollect = new SentenceGroupCollect();

                                sentenceGroupCollect.setId(bmobSentenceGroupCollect.getObjectId());
                                sentenceGroupCollect.setUserId(bmobSentenceGroupCollect.getUserId());
                                sentenceGroupCollect.setSentenceGroupCollectId(bmobSentenceGroupCollect.getSentenceGroupcollId());
                                sentenceGroupCollect.setSentencegroupId(bmobSentenceGroupCollect.getSentencegroupId());
                                sentenceGroupCollects.add(sentenceGroupCollect);
                            }
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
        final BmobCreateTractateGroupRequest bmobCreateTractateGroupRequest = new  BmobCreateTractateGroupRequest();

        bmobCreateTractateGroupRequest.setTractategroupId(tractateGroup.getTractategroupId());
        bmobCreateTractateGroupRequest.setUserId(tractateGroup.getUserId());
        bmobCreateTractateGroupRequest.setName(tractateGroup.getName());

        return bmobService.addTractateGroup(bmobCreateTractateGroupRequest)
                .flatMap(new Func1<Response<BmobTractateGroup>, Observable<TractateGroup>>() {
                    @Override
                    public Observable<TractateGroup> call(Response<BmobTractateGroup> bmobTractateGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResponse.isSuccessful()){

                            BmobTractateGroup bmobTractateGroup = bmobTractateGroupResponse.body();
                            TractateGroup tractateGroup = new TractateGroup();

                            tractateGroup.setId(bmobTractateGroup.getObjectId());
                            tractateGroup.setTractategroupId(bmobTractateGroup.getTractategroupId());
                            tractateGroup.setUserId(bmobTractateGroup.getUserId());
                            tractateGroup.setName(bmobTractateGroup.getName());

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
        final BmobCreateTractateGroupRequest bmobCreateTractateGroupRequest = new  BmobCreateTractateGroupRequest();

        bmobCreateTractateGroupRequest.setTractategroupId(tractateGroup.getTractategroupId());
        bmobCreateTractateGroupRequest.setUserId(tractateGroup.getUserId());
        bmobCreateTractateGroupRequest.setName(tractateGroup.getName());


        return bmobService.updateTractateGroupRxById(tractateGroup.getId(),bmobCreateTractateGroupRequest).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
                .flatMap(new Func1<Response<BmobTractateGroup>, Observable<TractateGroup>>() {
                    @Override
                    public Observable<TractateGroup> call(Response<BmobTractateGroup> bmobBmobTractateGroupResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobBmobTractateGroupResponse.isSuccessful()){

                            BmobTractateGroup bmobTractateGroup = bmobBmobTractateGroupResponse.body();
                            TractateGroup tractateGroup = new TractateGroup();

                            tractateGroup.setId(bmobTractateGroup.getObjectId());
                            tractateGroup.setTractategroupId(bmobTractateGroup.getTractategroupId());
                            tractateGroup.setUserId(bmobTractateGroup.getUserId());
                            tractateGroup.setName(bmobTractateGroup.getName());

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
                .flatMap(new Func1<Response<BmobTractateGroupResult>, Observable<List<TractateGroup>>>() {
                    @Override
                    public Observable<List<TractateGroup>> call(Response<BmobTractateGroupResult> bmobTractateGroupResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateGroupResultResponse.isSuccessful()){
                            BmobTractateGroupResult bmobSentenceGroupCollectResult = bmobTractateGroupResultResponse.body();

                            List<BmobTractateGroup> bmobTractateGroups = bmobSentenceGroupCollectResult.getResults();
                            List<TractateGroup> tractateGroups = new ArrayList<>();

                            for(BmobTractateGroup bmobTractateGroup:bmobTractateGroups){

                                TractateGroup tractateGroup = new TractateGroup();

                                tractateGroup.setId(bmobTractateGroup.getObjectId());
                                tractateGroup.setTractategroupId(bmobTractateGroup.getTractategroupId());
                                tractateGroup.setUserId(bmobTractateGroup.getUserId());
                                tractateGroup.setName(bmobTractateGroup.getName());
                                tractateGroups.add(tractateGroup);
                            }
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
    public Observable<WordCollect> addWordCollect(WordCollect wordCollect) {
        final BmobCreateWordCollectRequest bmobCreateWordCollectRequest = new  BmobCreateWordCollectRequest();

        bmobCreateWordCollectRequest.setWordcollectId(wordCollect.getWordcollectId());
        bmobCreateWordCollectRequest.setCreateDate(wordCollect.getCreateDate());
        bmobCreateWordCollectRequest.setUserId(wordCollect.getUserId());
        bmobCreateWordCollectRequest.setWordGroupId(wordCollect.getWordgroupId());
        bmobCreateWordCollectRequest.setWordId(wordCollect.getWordId());

        return bmobService.addWordCollect(bmobCreateWordCollectRequest)
                .flatMap(new Func1<Response<BmobWordCollect>, Observable<WordCollect>>() {
                    @Override
                    public Observable<WordCollect> call(Response<BmobWordCollect> bmobWordCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordCollectResponse.isSuccessful()){

                            BmobWordCollect bmobWordCollect = bmobWordCollectResponse.body();
                            WordCollect wordCollect = new WordCollect();

                            wordCollect.setId(bmobWordCollect.getObjectId());
                            wordCollect.setWordcollectId(bmobWordCollect.getWordcollectId());
                            wordCollect.setCreateDate(bmobWordCollect.getCreateDate());
                            wordCollect.setUserId(bmobWordCollect.getUserId());
                            wordCollect.setWordgroupId(bmobWordCollect.getWordgroupId());
                            wordCollect.setWordId(bmobWordCollect.getWordId());

                            return Observable.just(wordCollect);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordCollectResponse.errorBody().string();
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
        String regex = searchUtil.getBmobEqualsByAnd(new String[]{"userId","wordGroupId"},new String[]{userId,wordGroupId});
        return bmobService.getWordCollectRxByUserIdAndWordGroupId(regex,limit,skip)
                .flatMap(new Func1<Response<BmobWordCollectResult>, Observable<List<WordCollect>>>() {
                    @Override
                    public Observable<List<WordCollect>> call(Response<BmobWordCollectResult> bmobWordCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobWordCollectResultResponse.isSuccessful()){
                            BmobWordCollectResult bmobWordCollectResult = bmobWordCollectResultResponse.body();

                            List<BmobWordCollect> bmobWordCollects = bmobWordCollectResult.getResults();
                            List<WordCollect> wordCollects = new ArrayList<>();

                            for(BmobWordCollect bmobWordCollect:bmobWordCollects){

                                WordCollect wordCollect = new WordCollect();

                                wordCollect.setId(bmobWordCollect.getObjectId());
                                wordCollect.setWordcollectId(bmobWordCollect.getWordcollectId());
                                wordCollect.setCreateDate(bmobWordCollect.getCreateDate());
                                wordCollect.setUserId(bmobWordCollect.getUserId());
                                wordCollect.setWordgroupId(bmobWordCollect.getWordgroupId());
                                wordCollect.setWordId(bmobWordCollect.getWordId());
                                wordCollects.add(wordCollect);
                            }
                            return Observable.just(wordCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobWordCollectResultResponse.errorBody().string();
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
        final BmobCreateSentenceCollectRequest bmobCreateSentenceCollectRequest = new  BmobCreateSentenceCollectRequest();

        bmobCreateSentenceCollectRequest.setSentenceCollectId(sentenceCollect.getSentenceCollectId());
        bmobCreateSentenceCollectRequest.setUserId(sentenceCollect.getUserId());
        bmobCreateSentenceCollectRequest.setCreateDate(sentenceCollect.getCreateDate());
        bmobCreateSentenceCollectRequest.setSentencegroupId(sentenceCollect.getSentencegroupId());
        bmobCreateSentenceCollectRequest.setSentenceId(sentenceCollect.getSentenceId());

        return bmobService.addSentenceCollect(bmobCreateSentenceCollectRequest)
                .flatMap(new Func1<Response<BmobSentenceCollect>, Observable<SentenceCollect>>() {
                    @Override
                    public Observable<SentenceCollect> call(Response<BmobSentenceCollect> bmobSentenceCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceCollectResponse.isSuccessful()){

                            BmobSentenceCollect bmobSentenceCollect = bmobSentenceCollectResponse.body();
                            SentenceCollect sentenceCollect = new SentenceCollect();

                            sentenceCollect.setId(bmobSentenceCollect.getObjectId());
                            sentenceCollect.setSentenceCollectId(bmobSentenceCollect.getSentenceCollectId());
                            sentenceCollect.setUserId(bmobSentenceCollect.getUserId());
                            sentenceCollect.setCreateDate(bmobSentenceCollect.getCreateDate());
                            sentenceCollect.setSentencegroupId(bmobSentenceCollect.getSentencegroupId());
                            sentenceCollect.setSentenceId(bmobSentenceCollect.getSentenceId());

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
                .flatMap(new Func1<Response<BmobSentenceCollectResult>, Observable<List<SentenceCollect>>>() {
                    @Override
                    public Observable<List<SentenceCollect>> call(Response<BmobSentenceCollectResult> bmobSentenceCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobSentenceCollectResultResponse.isSuccessful()){
                            BmobSentenceCollectResult bmobSentenceCollectResult = bmobSentenceCollectResultResponse.body();

                            List<BmobSentenceCollect> bmobSentenceCollects = bmobSentenceCollectResult.getResults();
                            List<SentenceCollect> sentenceCollects = new ArrayList<>();

                            for(BmobSentenceCollect bmobSentenceCollect:bmobSentenceCollects){

                                WordCollect wordCollect = new WordCollect();

                                SentenceCollect sentenceCollect = new SentenceCollect();

                                sentenceCollect.setId(bmobSentenceCollect.getObjectId());
                                sentenceCollect.setSentenceCollectId(bmobSentenceCollect.getSentenceCollectId());
                                sentenceCollect.setUserId(bmobSentenceCollect.getUserId());
                                sentenceCollect.setCreateDate(bmobSentenceCollect.getCreateDate());
                                sentenceCollect.setSentencegroupId(bmobSentenceCollect.getSentencegroupId());
                                sentenceCollect.setSentenceId(bmobSentenceCollect.getSentenceId());
                                sentenceCollects.add(sentenceCollect);
                            }
                            return Observable.just(sentenceCollects);
                        }else{
                            Gson gson = new GsonBuilder().create();
                            try {
                                String errjson =  bmobSentenceCollectResultResponse.errorBody().string();
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
        final BmobCreateTractateCollectRequest bmobCreateTractateCollectRequest = new  BmobCreateTractateCollectRequest();

        bmobCreateTractateCollectRequest.setTractatecollectId(tractateCollect.getTractatecollectId());
        bmobCreateTractateCollectRequest.setUserId(tractateCollect.getUserId());
        bmobCreateTractateCollectRequest.setCreateDate(tractateCollect.getCreateDate());
        bmobCreateTractateCollectRequest.setTractategroupId(tractateCollect.getTractategroupId());
        bmobCreateTractateCollectRequest.setTractateId(tractateCollect.getTractateId());

        return bmobService.addTractateCollect(bmobCreateTractateCollectRequest)
                .flatMap(new Func1<Response<BmobTractateCollect>, Observable<TractateCollect>>() {
                    @Override
                    public Observable<TractateCollect> call(Response<BmobTractateCollect> bmobTractateCollectResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResponse.isSuccessful()){

                            BmobTractateCollect bmobTractateCollect = bmobTractateCollectResponse.body();
                            TractateCollect tractateCollect = new TractateCollect();

                            tractateCollect.setId(bmobTractateCollect.getObjectId());
                            tractateCollect.setTractatecollectId(bmobTractateCollect.getTractatecollectId());
                            tractateCollect.setUserId(bmobTractateCollect.getUserId());
                            tractateCollect.setCreateDate(bmobTractateCollect.getCreateDate());
                            tractateCollect.setTractategroupId(bmobTractateCollect.getTractategroupId());
                            tractateCollect.setTractateId(bmobTractateCollect.getTractateId());

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
                .flatMap(new Func1<Response<BmobTractateCollectResult>, Observable<List<TractateCollect>>>() {
                    @Override
                    public Observable<List<TractateCollect>> call(Response<BmobTractateCollectResult> bmobTractateCollectResultResponse) {
                        BmobRequestException bmobRequestException = new BmobRequestException(RemoteCode.COMMON.getDefauleError().getMessage());
                        if(bmobTractateCollectResultResponse.isSuccessful()){
                            BmobTractateCollectResult bmobTractateCollectResult = bmobTractateCollectResultResponse.body();

                            List<BmobTractateCollect> bmobTractateCollects = bmobTractateCollectResult.getResults();
                            List<TractateCollect> tractateCollects = new ArrayList<>();

                            for(BmobTractateCollect bmobTractateCollect:bmobTractateCollects){

                                TractateCollect tractateCollect = new TractateCollect();

                                tractateCollect.setId(bmobTractateCollect.getObjectId());
                                tractateCollect.setTractatecollectId(bmobTractateCollect.getTractatecollectId());
                                tractateCollect.setUserId(bmobTractateCollect.getUserId());
                                tractateCollect.setCreateDate(bmobTractateCollect.getCreateDate());
                                tractateCollect.setTractategroupId(bmobTractateCollect.getTractategroupId());
                                tractateCollect.setTractateId(bmobTractateCollect.getTractateId());
                                tractateCollects.add(tractateCollect);
                            }
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
    public Observable<Boolean> uploadFile(final File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audio/mp3", file.getName(), requestFile);
        return bmobService.uploadFile(file.getName(),body)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        if(responseBodyResponse.isSuccessful()) {
                            return Observable.just(true);
                        }else{
                            return Observable.just(false);
                        }
                    }
                });
    }
}
