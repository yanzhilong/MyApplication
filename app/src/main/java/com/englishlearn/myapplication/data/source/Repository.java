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
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final DataSource mRemoteDataSource;

    private final DataSource mLocalDataSource;

    private final BmobDataSource mBmobDataSource;

    boolean mCacheIsDirty = false;

    private Repository(DataSource remoteDataSource,
                       DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        mBmobDataSource = BmobDataSource.getInstance();
    }

    public static Repository getInstance(DataSource tasksRemoteDataSource,
                                         DataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }


    @Override
    public List<Sentence> getSentences() throws BmobException {
        return mBmobDataSource.getSentences();
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return mLocalDataSource.getSentences(searchword);
    }

    @Override
    public List<Grammar> getGrammars() throws BmobException {
        return mBmobDataSource.getGrammars();
    }

    @Override
    public List<Grammar> getGrammars(String searchword) {
        return mLocalDataSource.getGrammars(searchword);
    }

    @Override
    public Sentence getSentenceBySentenceId(String sentenceid) throws BmobException {
        return mLocalDataSource.getSentenceBySentenceId(sentenceid);
    }

    @Override
    public Grammar getGrammarByGrammarId(String grammarid) throws BmobException {
        return mLocalDataSource.getGrammarByGrammarId(grammarid);
    }

    @Override
    public Sentence getSentenceById(String id) throws BmobException {
        return mBmobDataSource.getSentenceById(id);
    }

    @Override
    public Grammar getGrammarById(String id) throws BmobException {
        return mBmobDataSource.getGrammarById(id);
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
    public boolean addSentence(final Sentence sentence) throws BmobException {
        return mBmobDataSource.addSentence(sentence);
    }

    @Override
    public boolean addGrammar(final Grammar grammar) throws BmobException {
        return mBmobDataSource.addGrammar(grammar);
    }

    @Override
    public boolean updateSentence(Sentence sentence) throws BmobException {
        return mBmobDataSource.updateSentence(sentence);
    }

    @Override
    public boolean updateGrammar(Grammar grammar) throws BmobException {
        return mBmobDataSource.updateGrammar(grammar);
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
        return mBmobDataSource.deleteSentenceById(id);
    }

    @Override
    public boolean deleteGrammarById(String id) throws BmobException {
        return mBmobDataSource.deleteGrammarById(id);
    }

}
