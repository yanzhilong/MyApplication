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

package com.englishlearn.myapplication.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

import rx.Observable;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalDataSource implements LocalData {

    private static LocalDataSource INSTANCE;

    private DbHelper mDbHelper;

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull Context context) {
        mDbHelper = new DbHelper(context);
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRx(String searchword, int page, int pageSize) {
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
    public Observable<Sentence> getSentenceRxById(String id) {
        return null;
    }

    @Override
    public Observable<Grammar> getGrammarRxById(String id) {
        return null;
    }

    @Override
    public Observable<Boolean> addSentenceRx(Sentence sentence) {
        return null;
    }

    @Override
    public Observable<Boolean> addGrammarRx(Grammar grammar) {
        return null;
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
    public Observable<Boolean> deleteSentenceRxById(String id) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteGrammarRxById(String id) {
        return null;
    }
}
