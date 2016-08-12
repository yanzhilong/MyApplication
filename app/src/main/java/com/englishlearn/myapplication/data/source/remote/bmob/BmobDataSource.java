package com.englishlearn.myapplication.data.source.remote.bmob;

import android.util.Log;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.service.BmobService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.BmobServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobDataSource implements DataSource {

    private static final String TAG = BmobDataSource.class.getSimpleName();



    private static BmobDataSource INSTANCE;
    private Map<RequestParam,Call> callMap;//存放请求列表
    private BmobService bmobService;//请求接口

    public static BmobDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BmobDataSource();
        }
        return INSTANCE;
    }

    public BmobDataSource(){
        bmobService = BmobServiceFactory.createBmobService();
        callMap = new HashMap<>();
    }

    public void cancelRequest(RequestParam requestParam){
        Call call = getInstance().callMap.get(requestParam);
        if(call != null){
            call.cancel();
        }
    }


    /*@Override
    public List<Sentence> getSentences() throws BmobException {
        BmobQuery<BmobSentence> bmobQuery = new BmobQuery<>();
        FindListenerBmobSentence findBmob = new FindListenerBmobSentence();
        bmobQuery.findObjects(findBmob);
        List<BmobSentence> list = null;
        try {
            list = findBmob.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<Sentence> sentences = new ArrayList<>();
        if(list == null){
            if(findBmob .getBmobException() != null){
                throw findBmob.getBmobException();
            }
        }else{
            for(BmobSentence bmobSentence : list){
                Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
                sentences.add(sentence);
            }
        }
        return sentences;
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return null;
    }

    @Override
    public List<Grammar> getGrammars() throws BmobException {
        BmobQuery<BmobGrammar> bmobQuery = new BmobQuery<>();
        FindListenerBmobGrammar findBmob = new FindListenerBmobGrammar();
        bmobQuery.findObjects(findBmob);
        List<BmobGrammar> list = null;
        try {
            list = findBmob.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<Grammar> grammars = new ArrayList<>();
        if(list == null){
            if(findBmob .getBmobException() != null){
                throw findBmob.getBmobException();
            }
        }else{
            for(BmobGrammar bmobGrammar : list){
                Grammar grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
                grammars.add(grammar);
            }
        }
        return grammars;
    }

    @Override
    public List<Grammar> getGrammars(String searchword) {
        return null;
    }

    @Override
    public Sentence getSentenceBySentenceId(String sentenceid) throws BmobException {
        return null;
    }

    @Override
    public Grammar getGrammarByGrammarId(String grammarid) throws BmobException {
        return null;
    }

    @Override
    public Sentence getSentenceById(String id) throws BmobException {

        BmobQuery<BmobSentence> bmobQuery = new BmobQuery<>();
        QueryListenerBmobSentence queryListenerBmobSentence = new QueryListenerBmobSentence();
        bmobQuery.getObject(id, queryListenerBmobSentence);
        BmobSentence bmobSentence = null;
        try {
            bmobSentence = queryListenerBmobSentence.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Sentence sentence = null;
        if(queryListenerBmobSentence .getBmobException() != null){
            throw queryListenerBmobSentence.getBmobException();
        }
        if(bmobSentence != null){
            sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
        }
        return sentence;
    }

    @Override
    public Grammar getGrammarById(String id) throws BmobException {

        BmobQuery<BmobGrammar> bmobQuery = new BmobQuery<>();
        QueryListenerBmobGrammar queryListenerBmobGrammar = new QueryListenerBmobGrammar();
        bmobQuery.getObject(id, queryListenerBmobGrammar);
        BmobGrammar bmobGrammar = null;
        try {
            bmobGrammar = queryListenerBmobGrammar.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Grammar grammar = null;
        if(queryListenerBmobGrammar .getBmobException() != null){
            throw queryListenerBmobGrammar.getBmobException();
        }
        if(bmobGrammar != null){
            grammar = new Grammar(bmobGrammar.getObjectId(),bmobGrammar.getGrammarid(),bmobGrammar.getName(),bmobGrammar.getContent());
        }
        return grammar;
    }*/

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
    public List<Sentence> getSentences(String searchword) {
        return null;
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
        callMap.put(RequestParam.GETSENTENCEID,responseBodyCall);
        BmobSentence bmobSentence = null;
        try {
            bmobSentence = responseBodyCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sentence sentence = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
        return sentence;

        /*final Sentence[] sentence = new Sentence[1];
        bmobService.getSentenceRxById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BmobSentence>() {



            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BmobSentence bmobSentence) {
                Log.d(TAG,bmobSentence.toString());
                sentence[0] = new Sentence(bmobSentence.getObjectId(),bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
            }
        });
        return sentence[0];*/
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
    public boolean updateSentence(Sentence sentence) {
        return false;
    }

    @Override
    public boolean updateGrammar(Grammar grammar) {
        return false;
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
    public boolean deleteGrammarById(String id) {
        return false;
    }


   /* @Override
    public boolean addGrammar(Grammar grammar) {

        BmobGrammar bmobGrammar = new BmobGrammar(grammar.getGrammarid(),grammar.getName(),grammar.getContent());
        SaveFuture saveFuture = new SaveFuture();
        bmobGrammar.save(saveFuture);
        String result = null;
        try {
            result = saveFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(saveFuture.getBmobException() != null){
            throw saveFuture.getBmobException();
        }
        return(result != null && saveFuture.getBmobException() == null);
    }

    @Override
    public boolean updateSentence(Sentence sentence) {

        BmobSentence bmobSentence = new BmobSentence(sentence.getSentenceid(),sentence.getContent(),sentence.getTranslation());
        bmobSentence.setObjectId(sentence.getId());
        UpdateListenerFuture updateListenerFuture = new UpdateListenerFuture();
        bmobSentence.update(updateListenerFuture);
        boolean result = false;
        try {
            result = updateListenerFuture.save();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (BmobException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    public boolean updateGrammar(Grammar grammar) {

        BmobGrammar bmobGrammar = new BmobGrammar(grammar.getGrammarid(),grammar.getName(),grammar.getContent());
        bmobGrammar.setObjectId(grammar.getId());
        UpdateListenerFuture updateListenerFuture = new UpdateListenerFuture();
        bmobGrammar.update(updateListenerFuture);
        boolean result = false;
        try {
            result = updateListenerFuture.save();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (BmobException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
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
        BmobSentence bmobSentence = new BmobSentence(null);
        bmobSentence.setObjectId(id);
        UpdateListenerFuture updateListenerFuture = new UpdateListenerFuture();
        bmobSentence.delete(updateListenerFuture);
        boolean result = false;
        try {
            result = updateListenerFuture.save();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (BmobException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    public boolean deleteGrammarById(String id) {
        BmobGrammar bmobGrammar = new BmobGrammar(null);
        bmobGrammar.setObjectId(id);
        UpdateListenerFuture updateListenerFuture = new UpdateListenerFuture();
        bmobGrammar.delete(updateListenerFuture);
        boolean result = false;
        try {
            result = updateListenerFuture.save();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (BmobException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }*/


}
