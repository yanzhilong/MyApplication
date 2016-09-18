/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.englishlearn.myapplication.data.source;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.local.LocalData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.RemoteData;
import com.englishlearn.myapplication.util.SearchUtil;

import java.util.List;

import rx.Observable;

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final RemoteData mRemoteDataSource;

    private final LocalData mLocalDataSource;

    private final BmobDataSource mBmobDataSource;

    private SearchUtil searchUtil;

    boolean mCacheIsDirty = false;

    private Repository(RemoteData remoteDataSource,
                       LocalData localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        mBmobDataSource = BmobDataSource.getInstance();
        searchUtil = SearchUtil.getInstance();
    }

    public static Repository getInstance(RemoteData tasksRemoteDataSource,
                                         LocalData tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }


    @Override
    public List<Sentence> getSentences(){
        return mBmobDataSource.getSentences();
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx() {
        return mBmobDataSource.getSentencesRx();
    }

    private int i = 1;
    @Override
    public Observable<List<Sentence>> getSentencesRx(int page, int pageSize) {
        return mBmobDataSource.getSentencesRx(page,pageSize);
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return mLocalDataSource.getSentences(searchword);
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(String searchword, int page, int pageSize) {
        if(searchword == null || searchword.equals("")){
            return getSentencesRx(page,pageSize);
        }
        String regex = searchUtil.getSearchSentenceRegex(searchword);
        return mBmobDataSource.getSentencesRx(regex,page,pageSize);
    }

    @Override
    public List<Grammar> getGrammars(){
        return mBmobDataSource.getGrammars();
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx() {
        return mBmobDataSource.getGrammarsRx();
    }

    @Override
    public Observable<List<Grammar>> getGrammarsRx(int page, int pageSize) {
        return mBmobDataSource.getGrammarsRx(page,pageSize);
    }

    @Override
    public List<Grammar> getGrammars(String searchword) {
        return mLocalDataSource.getGrammars(searchword);
    }

    @Override
    public Sentence getSentenceBySentenceId(String sentenceid){
        return mLocalDataSource.getSentenceBySentenceId(sentenceid);
    }

    @Override
    public Grammar getGrammarByGrammarId(String grammarid){
        return mLocalDataSource.getGrammarByGrammarId(grammarid);
    }

    @Override
    public Sentence getSentenceById(String id){
        return mBmobDataSource.getSentenceById(id);
    }

    @Override
    public Observable<Sentence> getSentenceRxById(String id) {
        return mBmobDataSource.getSentenceRxById(id);
    }

    @Override
    public Grammar getGrammarById(String id){
        return mBmobDataSource.getGrammarById(id);
    }

    @Override
    public Observable<Grammar> getGrammarRxById(String id) {
        return mBmobDataSource.getGrammarRxById(id);
    }

    @Override
    public boolean deleteAllSentences() {
        mLocalDataSource.deleteAllSentences();
        return false;
    }

    @Override
    public boolean deleteAllGrammars() {
        mLocalDataSource.deleteAllGrammars();
        return false;
    }

    @Override
    public boolean addSentence(final Sentence sentence){
        return mBmobDataSource.addSentence(sentence);
    }

    @Override
    public Observable<Boolean> addSentenceRx(Sentence sentence) {
        return mBmobDataSource.addSentenceRx(sentence);
    }

    @Override
    public boolean addGrammar(final Grammar grammar){
        return mBmobDataSource.addGrammar(grammar);
    }

    @Override
    public Observable<Boolean> addGrammarRx(Grammar grammar) {
        return mBmobDataSource.addGrammarRx(grammar);
    }

    @Override
    public boolean updateSentence(Sentence sentence){
        return mBmobDataSource.updateSentence(sentence);
    }

    @Override
    public boolean updateGrammar(Grammar grammar){
        return mBmobDataSource.updateGrammar(grammar);
    }

    @Override
    public Observable<Boolean> updateSentenceRx(Sentence sentence) {
        return mBmobDataSource.updateSentenceRx(sentence);
    }

    @Override
    public Observable<Boolean> updateGrammarRx(Grammar grammar) {
        return mBmobDataSource.updateGrammarRx(grammar);
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
    public boolean deleteSentenceById(String id){
        return mBmobDataSource.deleteSentenceById(id);
    }

    @Override
    public Observable<Boolean> deleteSentenceRxById(String id) {
        return mBmobDataSource.deleteSentenceRxById(id);
    }

    @Override
    public boolean deleteGrammarById(String id){
        return mBmobDataSource.deleteGrammarById(id);
    }

    @Override
    public Observable<Boolean> deleteGrammarRxById(String id) {
        return mBmobDataSource.deleteGrammarRxById(id);
    }
}
