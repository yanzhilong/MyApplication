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

import cn.bmob.v3.exception.BmobException;


/**
 * 数据接口
 */
public interface

DataSource {

    List<Sentence> getSentences() throws BmobException;

    /**
     * 根据指定的搜索词搜索相关的句子
     * @param searchword
     * @return
     */
    List<Sentence> getSentences(String searchword);

    List<Grammar> getGrammars() throws BmobException;

    List<Grammar> getGrammars(String searchword);

    Sentence getSentenceBySentenceId(String sentenceid) throws BmobException;

    Grammar getGrammarByGrammarId(String grammarid) throws BmobException;

    Sentence getSentenceById(String id) throws BmobException;

    Grammar getGrammarById(String id) throws BmobException;

    boolean deleteAllSentences();

    boolean deleteAllGrammars();

    boolean addSentence(Sentence sentence) throws BmobException;

    boolean addGrammar(Grammar grammar) throws BmobException;

    boolean updateSentence(Sentence sentence) throws BmobException;

    boolean updateGrammar(Grammar grammar) throws BmobException;

    boolean deleteSentence(String sid);

    boolean deleteGrammar(String gid);

}