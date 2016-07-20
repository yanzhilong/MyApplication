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

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class PersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PersistenceContract() {}

    /**
     * 句子表
     */
    public static abstract class SentenceEntry implements BaseColumns {
        public static final String TABLE_NAME = "sentence";
        public static final String COLUMN_NAME_ENTRY_ID = "sentenceid";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TRANSLATION = "translate";
    }

    /**
     * 语法表
     */
    public static abstract class GrammarEntry implements BaseColumns {
        public static final String TABLE_NAME = "grammar";
        public static final String COLUMN_NAME_ENTRY_ID = "grammarid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CONTENT = "content";
    }

    /**
     * 句子语法关系表
     */
    public static abstract class SentenceGrammarContactsEntry implements BaseColumns {
        public static final String TABLE_NAME = "sentencegrammar";
        public static final String COLUMN_NAME_SENTENCE_ID = "sentenceid";
        public static final String COLUMN_NAME_GRAMMAR_ID = "grammarid";
    }
}
