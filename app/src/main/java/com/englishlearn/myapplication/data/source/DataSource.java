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

import android.support.annotation.NonNull;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;


/**
 * 数据接口
 */
public interface

DataSource {

    List<Sentence> getSentences();

    /**
     * 根据指定的搜索词搜索相关的句子
     * @param searchword
     * @return
     */
    List<Sentence> getSentences(String searchword);

    List<Grammar> getGrammars();

    List<Grammar> getGrammars(String searchword);

    void addSentence(Sentence sentence);

    void addGrammar(Grammar grammar);

    void deleteSentence(String sid);

    void deleteGrammar(String gid);

}