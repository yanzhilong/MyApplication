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

import rx.Observable;


/**
 * 数据接口
 */
public interface

DataSource {

    List<Sentence> getSentences() ;

    Observable<List<Sentence>> getSentencesRx() ;

    Observable<List<Sentence>> getSentencesRx(int page,int pageSize) ;

    List<Sentence> getSentences(String searchword);

    List<Grammar> getGrammars();

    Observable<List<Grammar>> getGrammarsRx();

    Observable<List<Grammar>> getGrammarsRx(int page,int pageSize);

    List<Grammar> getGrammars(String searchword);

    Sentence getSentenceBySentenceId(String sentenceid);

    Grammar getGrammarByGrammarId(String grammarid);

    Sentence getSentenceById(String id);

    Observable<Sentence> getSentenceRxById(String id);

    Grammar getGrammarById(String id);

    Observable<Grammar> getGrammarRxById(String id);

    boolean deleteAllSentences();

    boolean deleteAllGrammars();

    boolean addSentence(Sentence sentence);

    Observable<Boolean> addSentenceRx(Sentence sentence);

    boolean addGrammar(Grammar grammar);

    Observable<Boolean> addGrammarRx(Grammar grammar);

    boolean updateSentence(Sentence sentence);

    Observable<Boolean> updateSentenceRx(Sentence sentence);

    boolean updateGrammar(Grammar grammar);

    Observable<Boolean> updateGrammarRx(Grammar grammar);

    boolean deleteSentence(String sentenceid);

    boolean deleteGrammar(String grammarid);

    boolean deleteSentenceById(String id);

    Observable<Boolean> deleteSentenceRxById(String id);

    boolean deleteGrammarById(String id);

    Observable<Boolean> deleteGrammarRxById(String id);

}