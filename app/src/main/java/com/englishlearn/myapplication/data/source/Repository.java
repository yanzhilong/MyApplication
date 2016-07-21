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

import java.util.List;

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final DataSource mRemoteDataSource;

    private final DataSource mLocalDataSource;

    boolean mCacheIsDirty = false;

    private Repository(DataSource remoteDataSource,
                       DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(DataSource tasksRemoteDataSource,
                                         DataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }


    @Override
    public List<Sentence> getSentences() {
        return mLocalDataSource.getSentences();
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        return mLocalDataSource.getSentences(searchword);
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
    public void addSentence(Sentence sentence) {
        mLocalDataSource.addSentence(sentence);
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
