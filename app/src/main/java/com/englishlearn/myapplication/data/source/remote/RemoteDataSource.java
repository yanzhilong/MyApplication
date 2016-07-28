
package com.englishlearn.myapplication.data.source.remote;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;

import java.util.List;

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
    public Sentence getSentenceById(String sentenceid) {
        return null;
    }

    @Override
    public Grammar getGrammarById(String grammarid) {
        return null;
    }

    @Override
    public void deleteAllSentences() {

    }

    @Override
    public void deleteAllGrammars() {

    }

    @Override
    public void addSentence(Sentence sentence) {

    }

    @Override
    public void addGrammar(Grammar grammar) {

    }

    @Override
    public void deleteSentence(String sid) {
    }

    @Override
    public void deleteGrammar(String gid) {
    }
}
