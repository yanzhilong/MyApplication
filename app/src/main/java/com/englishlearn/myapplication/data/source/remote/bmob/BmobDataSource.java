package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.future.FindListenerBmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.future.FindListenerBmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.future.QueryListenerBmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.future.QueryListenerBmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.future.SaveFuture;
import com.englishlearn.myapplication.data.source.remote.bmob.future.UpdateListenerFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by yanzl on 16-8-1.
 */
public class BmobDataSource implements DataSource {
    private static final String TAG = BmobDataSource.class.getSimpleName();

    private static BmobDataSource INSTANCE;

    public static BmobDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BmobDataSource();
        }
        return INSTANCE;
    }

    @Override
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
    public boolean addSentence(Sentence sentence) throws BmobException {
        BmobSentence bmobSentence = new BmobSentence(sentence.getSentenceid(),sentence.getContent(),sentence.getTranslation());
        SaveFuture saveFuture = new SaveFuture();
        bmobSentence.save(saveFuture);
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
    public boolean addGrammar(Grammar grammar) throws BmobException {

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
    public boolean updateSentence(Sentence sentence) throws BmobException {

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
    public boolean updateGrammar(Grammar grammar) throws BmobException {

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
    public boolean deleteSentenceById(String id) throws BmobException {
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
    public boolean deleteGrammarById(String id) throws BmobException {
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
    }


}
