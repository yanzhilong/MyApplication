
package com.englishlearn.myapplication.data.source.remote;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUpdateUserRequest;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobUser;
import com.englishlearn.myapplication.data.source.remote.bmob.RemoteData;

import java.util.List;

import rx.Observable;

/**
 * 从网络操作数据
 */
public class RemoteDataSource implements RemoteData {

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
    public Observable<List<Sentence>> getSentencesRx() {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(int page, int pageSize) {
        return null;
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(String searchword, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Grammar> getGrammars() {
        return null;
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx() {
        return null;
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx(int page, int pageSize) {
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
    public Observable<Sentence> getSentenceRxById(String id) {
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
    public Observable<Boolean> addSentenceRx(Sentence sentence) {
        return null;
    }

    @Override
    public boolean addGrammar(Grammar grammar) {
        return false;
    }

    @Override
    public Observable<Boolean> addGrammarRx(Grammar grammar) {
        return null;
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
        return null;
    }

    @Override
    public Observable<Boolean> updateGrammarRx(Grammar grammar) {
        return null;
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
        return null;
    }

    @Override
    public boolean deleteGrammarById(String id) {
        return false;
    }

    @Override
    public Observable<Boolean> deleteGrammarRxById(String id) {
        return null;
    }

    @Override
    public BmobUser register(BmobUser bmobUser) {
        return null;
    }

    @Override
    public BmobUser login(BmobUser bmobUser) {
        return null;
    }

    @Override
    public BmobUser update(BmobUpdateUserRequest bmobUpdateUserRequest) {
        return null;
    }
}
