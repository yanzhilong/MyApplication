package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.service.BmobService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;

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
public class BmobDataSource implements DataSource {

    private static final String TAG = BmobDataSource.class.getSimpleName();



    private static BmobDataSource INSTANCE;
    private BmobService bmobService;//请求接口

    public static BmobDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BmobDataSource();
        }
        return INSTANCE;
    }

    public BmobDataSource(){
        bmobService = ServiceFactory.getInstance().createBmobService();
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
                    Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
                            Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
                            Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
        Log.d(TAG,"getSentencesRx:limit=" + limit + "skip=" + skip + "regex:" + searchword);
        return bmobService.getSentencesRx(searchword,limit,skip)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BmobSentenceResult, Observable<List<Sentence>>>() {
                    @Override
                    public Observable<List<Sentence>> call(BmobSentenceResult bmobSentenceResult) {
                        List<BmobSentence> list = bmobSentenceResult.getResults();
                        List<Sentence> sentences = new ArrayList<>();
                        for(BmobSentence bmobSentence : list){
                            Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
                    Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
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
                            Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
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
                            Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
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
        Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
                        Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
        Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());

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
                        Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
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

        Call<ResponseBody> responseBodyCall = bmobService.addSentence(sentence);
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
        return bmobService.addSentenceRx(sentence)
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
        Call<ResponseBody> responseBodyCall = bmobService.addGrammar(grammar);
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
        return bmobService.addGrammarRx(grammar)
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
        return bmobService.updateSentencRxById(sentence.getId(),sentence)
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
        return bmobService.updateGrammarRxById(grammar.getId(),grammar)
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
}
