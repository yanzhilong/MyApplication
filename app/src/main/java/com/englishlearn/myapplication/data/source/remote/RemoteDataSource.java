
package com.englishlearn.myapplication.data.source.remote;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.RequestParam;

import java.util.List;

import rx.Observable;

/**
 * 从网络操作数据
 */
public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RemoteDataSource() {}

    @Override
    public List<Sentence> getSentences() {
        return null;
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return null;
    }

    @Override
    public List<Grammar> getGrammars() {
        return null;
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
        return null;
    }

    @Override
    public Grammar getGrammarById(String id) {
        return null;
    }

    @Override
    public Observable<Grammar> getGrammarRxById(String id) {
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
    public boolean addSentence(Sentence sentence) {
        return false;
    }

    @Override
    public boolean addGrammar(Grammar grammar) {
        return false;
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

    @Override
    public void cancelRequest(RequestParam requestParam) {

    }


}
