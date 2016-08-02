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

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;

import java.util.ArrayList;
import java.util.List;

import static com.englishlearn.myapplication.data.source.local.PersistenceContract.GrammarEntry;
import static com.englishlearn.myapplication.data.source.local.PersistenceContract.SentenceEntry;

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
    public List<Sentence> getSentences() {
        List<Sentence> sentences = new ArrayList();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_NAME_ENTRY_ID,
                SentenceEntry.COLUMN_NAME_CONTENT,
                SentenceEntry.COLUMN_NAME_TRANSLATION,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mId = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_ENTRY_ID));
                String content = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_CONTENT));
                String translate =
                        c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_TRANSLATION));

                List<Grammar> list = new ArrayList<>();
                /*for(int i = 0; i < 7; i++){
                    Grammar grammar = new Grammar();
                    grammar.setName("进行时" + i);
                    list.add(grammar);
                }*/

                Sentence sentence = new Sentence(mId,content,translate,list);
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
                SentenceEntry.COLUMN_NAME_TRANSLATION,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, SentenceEntry.COLUMN_NAME_CONTENT + " LIKE ?", new String[]{"%" + searchword + "%"}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(SentenceEntry._ID));
                String mId = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_ENTRY_ID));
                String content = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_CONTENT));
                String translate =
                        c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_TRANSLATION));

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
                GrammarEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mid = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_ENTRY_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_NAME));
                String content =
                        c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_CONTENT));

                Grammar grammar = new Grammar(mid,name,content);
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
        List<Grammar> grammars = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GrammarEntry.COLUMN_NAME_ENTRY_ID,
                GrammarEntry.COLUMN_NAME_NAME,
                GrammarEntry.COLUMN_NAME_CONTENT,
        };

        Cursor c = db.query(
                GrammarEntry.TABLE_NAME, projection, GrammarEntry.COLUMN_NAME_NAME + " LIKE ?", new String[]{"%" + searchword + "%"}, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String mid = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_ENTRY_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_NAME));
                String content =
                        c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_CONTENT));

                Grammar grammar = new Grammar(mid,name,content);
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
    public Sentence getSentenceById(String sentenceid) {

        Sentence sentence = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_NAME_ENTRY_ID,
                SentenceEntry.COLUMN_NAME_CONTENT,
                SentenceEntry.COLUMN_NAME_TRANSLATION,
        };

        Cursor c = db.query(
                SentenceEntry.TABLE_NAME, projection, SentenceEntry.COLUMN_NAME_ENTRY_ID + " = ?", new String[]{sentenceid}, null, null, null);

        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                String mId = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_ENTRY_ID));
                String content = c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_CONTENT));
                String translate =
                        c.getString(c.getColumnIndexOrThrow(SentenceEntry.COLUMN_NAME_TRANSLATION));

                sentence = new Sentence(mId,content,translate,null);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return sentence;
    }

    @Override
    public Grammar getGrammarById(String grammarid) {
        Grammar grammar = null;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                GrammarEntry.COLUMN_NAME_ENTRY_ID,
                GrammarEntry.COLUMN_NAME_NAME,
                GrammarEntry.COLUMN_NAME_CONTENT,
        };

        Cursor c = db.query(
                GrammarEntry.TABLE_NAME, projection, GrammarEntry.COLUMN_NAME_ENTRY_ID + " = ?", new String[]{grammarid}, null, null, null);

        if (c != null && c.getCount() > 0) {
            if (c.moveToFirst()) {
                String mid = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_ENTRY_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_NAME));
                String content =
                        c.getString(c.getColumnIndexOrThrow(GrammarEntry.COLUMN_NAME_CONTENT));

                grammar = new Grammar(mid,name,content);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();
        return grammar;
    }

    @Override
    public boolean deleteAllSentences() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int result = db.delete(SentenceEntry.TABLE_NAME,null,null);

        db.close();

        return(result > 0);
    }

    @Override
    public boolean deleteAllGrammars() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int result = db.delete(GrammarEntry.TABLE_NAME,null,null);

        db.close();

        return(result > 0);
    }

    @Override
    public boolean addSentence(Sentence sentence) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_NAME_ENTRY_ID, sentence.getmId());
        values.put(SentenceEntry.COLUMN_NAME_CONTENT, sentence.getContent());
        values.put(SentenceEntry.COLUMN_NAME_TRANSLATION, sentence.getTranslation());

        long result = db.insert(SentenceEntry.TABLE_NAME,null,values);

        db.close();

        return(result != -1);
    }

    @Override
    public boolean addGrammar(Grammar grammar) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(GrammarEntry.COLUMN_NAME_ENTRY_ID, grammar.getmId());
        values.put(GrammarEntry.COLUMN_NAME_NAME, grammar.getName());
        values.put(GrammarEntry.COLUMN_NAME_CONTENT, grammar.getContent());

        long result = db.insert(GrammarEntry.TABLE_NAME,null,values);

        db.close();

        return(result != -1);
    }

    @Override
    public boolean updateSentence(Sentence sentence) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_NAME_ENTRY_ID, sentence.getmId());
        values.put(SentenceEntry.COLUMN_NAME_CONTENT, sentence.getContent());
        values.put(SentenceEntry.COLUMN_NAME_TRANSLATION, sentence.getTranslation());

        String selection = SentenceEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {sentence.getmId()};

        int result = db.update(SentenceEntry.TABLE_NAME,values,selection,selectionArgs);

        db.close();

        return(result > 0);
    }

    @Override
    public boolean updateGrammar(Grammar grammar) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(GrammarEntry.COLUMN_NAME_ENTRY_ID, grammar.getmId());
        values.put(GrammarEntry.COLUMN_NAME_NAME, grammar.getName());
        values.put(GrammarEntry.COLUMN_NAME_CONTENT, grammar.getContent());

        String selection = GrammarEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {grammar.getmId()};

        int result = db.update(GrammarEntry.TABLE_NAME,values,selection,selectionArgs);

        db.close();

        return(result > 0);
    }

    @Override
    public boolean deleteSentence(String sid) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = SentenceEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {sid};

        int result = db.delete(SentenceEntry.TABLE_NAME,selection,selectionArgs);

        db.close();

        return(result > 0);
    }

    @Override
    public boolean deleteGrammar(String gid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = GrammarEntry.COLUMN_NAME_ENTRY_ID +" = ?";
        String[] selectionArgs = {gid};

        int result = db.delete(GrammarEntry.TABLE_NAME,selection,selectionArgs);

        db.close();

        return(result > 0);
    }
}
