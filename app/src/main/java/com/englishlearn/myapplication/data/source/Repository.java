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
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import java.util.List;

import rx.Observable;

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final RemoteData mRemoteDataSource;

    private final LocalData mLocalDataSource;

    private final RemoteData mBmobDataSource;



    boolean mCacheIsDirty = false;

    private Repository(RemoteData remoteDataSource,
                       LocalData localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        mBmobDataSource = BmobDataSource.getInstance();
    }

    public static Repository getInstance(RemoteData tasksRemoteDataSource,
                                         LocalData tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(int page, int pageSize) {
        return mBmobDataSource.getSentencesRx(page,pageSize);
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(String searchword, int page, int pageSize) {
        if(searchword == null || searchword.equals("")){
            return getSentencesRx(page,pageSize);
        }
        return mBmobDataSource.getSentencesRx(searchword,page,pageSize);
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
    public Observable<Sentence> getSentenceRxById(String id) {
        return mBmobDataSource.getSentenceRxById(id);
    }

    @Override
    public Observable<Grammar> getGrammarRxById(String id) {
        return mBmobDataSource.getGrammarRxById(id);
    }

    @Override
    public Observable<Boolean> addSentenceRx(Sentence sentence) {
        return mBmobDataSource.addSentenceRx(sentence);
    }

    @Override
    public Observable<Boolean> addGrammarRx(Grammar grammar) {
        return mBmobDataSource.addGrammarRx(grammar);
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
    public Observable<Boolean> deleteSentenceRxById(String id) {
        return mBmobDataSource.deleteSentenceRxById(id);
    }

    @Override
    public Observable<Boolean> deleteGrammarRxById(String id) {
        return mBmobDataSource.deleteGrammarRxById(id);
    }
}
