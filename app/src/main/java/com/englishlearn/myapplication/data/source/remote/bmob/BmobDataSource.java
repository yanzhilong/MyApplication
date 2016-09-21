package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.service.BmobService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;
import com.englishlearn.myapplication.util.SearchUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private Observable execute(final Observable observable){
        return Observable.just(true)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Boolean, Observable<?>>() {
            @Override
            public Observable<?> call(Boolean aBoolean) {
                return observable;
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public List<Sentence> getSentences() {
        Call<BmobSentenceResult> listCall = bmobService.getSentences();
        Response<BmobSentenceResult> response;
        List<Sentence> sentences = new ArrayList<>();
        try {
            response = listCall.execute();
            List<BmobSentence> tmp = response.body().getResults();
            if(tmp != null){
                for(BmobSentence bmobSentence : tmp){
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sentences;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx() {
        return bmobService.getSentencesRx()
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
                        return Observable.just(sentences)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    @Override
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
    public List<Sentence> getSentences(String searchword) {
        return null;
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
    public List<Grammar> getGrammars() {
        Call<BmobGrammarResult> listCall = bmobService.getGrammars();
        Response<BmobGrammarResult> response;
        List<Grammar> grammars = new ArrayList<>();
        try {
            response = listCall.execute();
            List<BmobGrammar> tmp = response.body().getResults();
            if(tmp != null){
                for(BmobGrammar bmobGrammar : tmp){
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grammars;
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
    public List<Grammar> getGrammars(String searchword) {
        return null;
    }

    @Override
    public Sentence getSentenceBySentenceId(String sentenceid) {
        return null;
    }

    @Override
    public Grammar getGrammarByGrammarId(String grammarid) {
        return null;
    }

    @Override
    public Sentence getSentenceById(String id) {

        Call<BmobSentence> responseBodyCall = bmobService.getSentenceById(id);
        BmobSentence bmobSentence = null;
        try {
            bmobSentence = responseBodyCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return sentence;
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
    public Grammar getGrammarById(String id) {
        Call<BmobGrammar> responseBodyCall = bmobService.getGrammarById(id);
        BmobGrammar bmobGrammar = null;
        try {
            bmobGrammar = responseBodyCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Grammar grammar = new Grammar();
        grammar.setId(bmobGrammar.getObjectId());
        grammar.setGrammarId(bmobGrammar.getGrammarId());
        grammar.setContent(bmobGrammar.getContent());
        grammar.setCreateDate(bmobGrammar.getCreateDate());
        grammar.setTitle(bmobGrammar.getTitle());
        grammar.setUserId(bmobGrammar.getUserId());
        grammar.setRemark(bmobGrammar.getRemark());
        return grammar;
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
    public boolean deleteAllSentences() {
        return false;
    }

    @Override
    public boolean deleteAllGrammars() {
        return false;
    }

    @Override
    public boolean addSentence(Sentence sentence) {

        BmobCreateSentenceRequest bmobCreateSentenceRequest = new BmobCreateSentenceRequest();

        bmobCreateSentenceRequest.setSentenceId(sentence.getSentenceId());
        bmobCreateSentenceRequest.setContent(sentence.getContent());
        bmobCreateSentenceRequest.setTranslation(sentence.getTranslation());
        bmobCreateSentenceRequest.setCreateDate(sentence.getCreateDate());
        bmobCreateSentenceRequest.setRemark(sentence.getRemark());
        bmobCreateSentenceRequest.setSource(sentence.getSource());
        bmobCreateSentenceRequest.setTractateId(sentence.getTractateId());
        bmobCreateSentenceRequest.setUserId(sentence.getUserId());
        Call<ResponseBody> responseBodyCall = bmobService.addSentence(bmobCreateSentenceRequest);
        boolean result = false;
        try {
            Response<ResponseBody> responseBodyResponse = responseBodyCall.execute();
            result = responseBodyResponse.isSuccessful();
            Log.d(TAG,"isSuccess:" + result);
            ResponseBody responseBody = responseBodyResponse.body();
            Log.d(TAG,"result:" + new String(responseBody.bytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
    public boolean addGrammar(Grammar grammar) {

        BmobCreateGrammarRequest bmobCreateGrammarRequest = new BmobCreateGrammarRequest();
        bmobCreateGrammarRequest.setGrammarId(grammar.getGrammarId());
        bmobCreateGrammarRequest.setContent(grammar.getContent());
        bmobCreateGrammarRequest.setCreateDate(grammar.getCreateDate());
        bmobCreateGrammarRequest.setTitle(grammar.getTitle());
        bmobCreateGrammarRequest.setUserId(grammar.getUserId());
        bmobCreateGrammarRequest.setRemark(grammar.getRemark());

        Call<ResponseBody> responseBodyCall = bmobService.addGrammar(bmobCreateGrammarRequest);
        boolean result = false;
        try {
            Response<ResponseBody> responseBodyResponse = responseBodyCall.execute();
            result = responseBodyResponse.isSuccessful();
            Log.d(TAG,"isSuccess:" + result);
            ResponseBody responseBody = responseBodyResponse.body();
            Log.d(TAG,"result:" + new String(responseBody.bytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
    public boolean updateSentence(Sentence sentence) {
        return false;
    }

    @Override
    public boolean updateGrammar(Grammar grammar) {
        return false;
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
    public boolean deleteSentence(String sentenceid) {
        return false;
    }

    @Override
    public boolean deleteGrammar(String grammarid) {
        return false;
    }

    @Override
    public boolean deleteSentenceById(String id) {
        return false;
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
    public boolean deleteGrammarById(String id) {
        return false;
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
    }

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

        return this.execute(bmobService.createUserRx(bmobCreateUserRequest).flatMap(new Func1<Response<BmobCreateUserResult>, Observable<BmobCreateUserResult>>() {
            @Override
            public Observable<BmobCreateUserResult> call(Response<BmobCreateUserResult> bmobCreateUserResultResponse) {
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
        }));
    }

    @Override
    public Observable<User> createOrLoginUserByPhoneRx(String phone, String smscode) {
        User user = new User();
        BmobCreateOrLoginUserByPhoneRequest bmobCreateOrLoginUserByPhoneRequest = new BmobCreateOrLoginUserByPhoneRequest();
        bmobCreateOrLoginUserByPhoneRequest.setMobilePhoneNumber(phone);
        bmobCreateOrLoginUserByPhoneRequest.setUserId(user.getUserId());
        bmobCreateOrLoginUserByPhoneRequest.setSmsCode(smscode);

        return this.execute(bmobService.createOrLoginUserByPhoneRx(bmobCreateOrLoginUserByPhoneRequest).flatMap(new Func1<Response<BmobUser>, Observable<User>>() {
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
        }));
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
        return this.execute(bmobService.getUserByIdRx(id)
                .flatMap(new Func1<BmobUser, Observable<User>>() {
            @Override
            public Observable<User> call(BmobUser bmobUser) {
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
            }
        }));
    }

    @Override
    public Observable<User> getUserByName(String name) {
        String namejson = searchUtil.getUserByNameRegex(name);
        return this.execute(bmobService.getUserByNameRx(namejson).flatMap(new Func1<BmobUserResult, Observable<?>>() {
            @Override
            public Observable<?> call(BmobUserResult bmobUserResult) {

                if(bmobUserResult == null){
                    return Observable.just(null);
                }
                List<BmobUser> list = bmobUserResult.getResults();
                if(list.size() == 0){
                    return Observable.just(null);
                }
                BmobUser bmobUser = list.get(0);
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
            }
        }));
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
        return this.execute(bmobService.loginRx(name,password)
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
                }));
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

        return this.execute(bmobService.updateUserRx(user.getSessionToken(),user.getId(),bmobCreateUserRequest)
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
        }));
    }

    @Override
    public Observable<Boolean> pwdResetByEmail(String email) {
        PasswordResetEmail passwordResetEmail = new PasswordResetEmail();
        passwordResetEmail.setEmail(email);
        return this.execute(bmobService.passwordResetByEmail(passwordResetEmail).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
        }));
    }

    @Override
    public Observable<Boolean> pwdResetByMobile(String smsCode, String newpwd) {

        PasswordResetMobile passwordResetMobile = new PasswordResetMobile();
        passwordResetMobile.setPassword(newpwd);
        return this.execute(bmobService.passwordResetByMobile(smsCode,passwordResetMobile).flatMap(new Func1<Response<ResponseBody>, Observable<?>>() {
            @Override
            public Observable<?> call(Response<ResponseBody> responseBodyResponse) {
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
        }));
    }

    @Override
    public Observable<Boolean> pwdResetByOldPwd(String sessionToken, String objectId, String oldPwd, String newPwd) {
        PasswordResetOldPwd passwordResetOldPwd = new PasswordResetOldPwd();
        passwordResetOldPwd.setOldPassword(oldPwd);
        passwordResetOldPwd.setNewPassword(newPwd);

        return this.execute(bmobService.passwordResetByOldPassword(sessionToken,objectId,passwordResetOldPwd)
        .flatMap(new Func1<Response<ResponseBody>, Observable<?>>() {
            @Override
            public Observable<?> call(Response<ResponseBody> responseBodyResponse) {
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
        }));
    }

    @Override
    public Observable<String> requestSmsCode(String phone) {
        RequestSmsCode requestSmsCode = new RequestSmsCode();
        requestSmsCode.setMobilePhoneNumber(phone);
        return this.execute(bmobService.requestSmsCode(requestSmsCode)
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
                }));
    }

    @Override
    public Observable<Boolean> emailVerify(String email) {

        EmailVerify emailVerify = new EmailVerify();
        emailVerify.setEmail(email);
        return this.execute(bmobService.emailVerify(emailVerify).flatMap(new Func1<Response<ResponseBody>, Observable<?>>() {
            @Override
            public Observable<?> call(Response<ResponseBody> responseBodyResponse) {
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
        }));
    }

    @Override
    public Observable<Boolean> smsCodeVerify(String smsCode,String mobile) {
        SmsCodeVerify smsCodeVerify = new SmsCodeVerify();
        smsCodeVerify.setMobilePhoneNumber(mobile);
        return this.execute(bmobService.smsCodeVerify(smsCode,smsCodeVerify).flatMap(new Func1<Response<ResponseBody>, Observable<Boolean>>() {
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
        }));
    }
}
