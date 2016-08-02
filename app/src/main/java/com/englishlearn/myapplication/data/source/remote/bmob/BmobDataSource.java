package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.future.FindBmobGrammar;
import com.englishlearn.myapplication.data.source.remote.bmob.future.FindBmobSentence;
import com.englishlearn.myapplication.data.source.remote.bmob.future.SaveFuture;

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
        FindBmobSentence findBmob = new FindBmobSentence();
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
                Sentence sentence = new Sentence(bmobSentence.getSentenceid(),bmobSentence.getContent(),bmobSentence.getTranslation(),null);
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
        FindBmobGrammar findBmob = new FindBmobGrammar();
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
                Grammar grammar = new Grammar(bmobGrammar.getName(),bmobGrammar.getContent());
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
    public Sentence getSentenceById(String sentenceid) {
        return null;
    }

    @Override
    public Grammar getGrammarById(String grammarid) {
        return null;
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
        BmobSentence bmobSentence = new BmobSentence(sentence.getmId(),sentence.getContent(),sentence.getTranslation());
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

        BmobGrammar bmobGrammar = new BmobGrammar(grammar.getmId(),grammar.getName(),grammar.getContent());
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
        return false;
    }

    @Override
    public boolean updateGrammar(Grammar grammar) {
        return false;
    }

    @Override
    public boolean deleteSentence(String sid) {
        return false;
    }

    @Override
    public boolean deleteGrammar(String gid) {
        return false;
    }

}
