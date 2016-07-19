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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import static com.englishlearn.myapplication.data.source.local.PersistenceContract.SentenceEntry;
import static com.englishlearn.myapplication.data.source.local.PersistenceContract.GrammarEntry;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.source.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalDataSource implements DataSource {

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
    public List<Sentence> getSentences() {
        List<Sentence> sentences = new ArrayList();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_NAME_ENTRY_ID,
                SentenceEntry.COLUMN_NAME_CONTENT,
                SentenceEntry.COLUMN_NAME_TRANSLATE,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mId = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_ENTRY_ID));
                String content = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_CONTENT));
                String translate =
                        c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_TRANSLATE));

                Sentence sentence = new Sentence(mId,content,translate,null);
                sentences.add(sentence);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return sentences;
    }

    @Override
    public List<Sentence> getSentences(String searchword) {
        List<Sentence> sentences = new ArrayList();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_NAME_ENTRY_ID,
                SentenceEntry.COLUMN_NAME_CONTENT,
                SentenceEntry.COLUMN_NAME_TRANSLATE,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, SentenceEntry.COLUMN_NAME_CONTENT + "LIKE '%?%'", new String[]{searchword}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(SentenceEntry._ID));
                String mId = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_ENTRY_ID));
                String content = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_CONTENT));
                String translate =
                        c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_TRANSLATE));

                Sentence sentence = new Sentence(mId,content,translate,null);
                sentences.add(sentence);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return sentences;
    }

    @Override
    public List<Grammar> getGrammars() {

        List<Grammar> grammars = new ArrayList();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GrammarEntry.COLUMN_NAME_ENTRY_ID,
                GrammarEntry.COLUMN_NAME_NAME,
                GrammarEntry.COLUMN_NAME_CONTENT,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mid = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_ENTRY_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_NAME));
                String content =
                        c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_CONTENT));

                Grammar grammar = new Grammar(mid,content,name);
                grammars.add(grammar);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return grammars;
    }

    @Override
    public List<Grammar> getGrammars(String searchword) {
        List<Grammar> grammars = new ArrayList<Grammar>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GrammarEntry.COLUMN_NAME_ENTRY_ID,
                GrammarEntry.COLUMN_NAME_NAME,
                GrammarEntry.COLUMN_NAME_CONTENT,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, GrammarEntry.COLUMN_NAME_NAME + "LIKE '%?%'", new String[]{searchword}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mid = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_ENTRY_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_NAME));
                String content =
                        c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_CONTENT));

                Grammar grammar = new Grammar(mid,content,name);
                grammars.add(grammar);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return grammars;
    }

    @Override
    public void addSentence(Sentence sentence) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_NAME_ENTRY_ID, sentence.getmId());
        values.put(SentenceEntry.COLUMN_NAME_CONTENT, sentence.getContent());
        values.put(SentenceEntry.COLUMN_NAME_TRANSLATE, sentence.getTranslate());

        db.insert(SentenceEntry.TABLE_NAME,null,values);

        db.close();
    }

    @Override
    public void addGrammar(Grammar grammar) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(GrammarEntry.COLUMN_NAME_ENTRY_ID, grammar.getmId());
        values.put(GrammarEntry.COLUMN_NAME_NAME, grammar.getName());
        values.put(GrammarEntry.COLUMN_NAME_CONTENT, grammar.getContent());

        db.insert(SentenceEntry.TABLE_NAME,null,values);

        db.close();
    }

    @Override
    public void deleteSentence(String sid) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = SentenceEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {sid};

        db.delete(SentenceEntry.TABLE_NAME,selection,selectionArgs);

        db.close();
    }

    @Override
    public void deleteGrammar(String gid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = GrammarEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {gid};

        db.delete(GrammarEntry.TABLE_NAME,selection,selectionArgs);

        db.close();
    }
}
