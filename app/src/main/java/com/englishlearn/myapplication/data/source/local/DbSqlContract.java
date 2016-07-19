package com.englishlearn.myapplication.data.source.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-7-18.
 */
public class DbSqlContract {

    private static final String TEXT_TYPE = " TEXT";//数据类型

    private static final String BOOLEAN_TYPE = " INTEGER";//布尔值的数据类型

    private static final String COMMA_SEP = ",";

    public static final List<String> V1;

    public static final List<String> V2;

    public static final String V1_CreateSentence =
            "CREATE TABLE " + PersistenceContract.SentenceEntry.TABLE_NAME + " (" +
                    PersistenceContract.SentenceEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_TRANSLATE + TEXT_TYPE +
                    " )";

    public static final String V1_CreateGrammar =
            "CREATE TABLE " + PersistenceContract.GrammarEntry.TABLE_NAME + " (" +
                    PersistenceContract.GrammarEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.GrammarEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.GrammarEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.GrammarEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +
                    " )";

    public static final String V1_CreateSentenceGrammar =
            "CREATE TABLE " + PersistenceContract.SentenceGrammarContactsEntry.TABLE_NAME + " (" +
                    PersistenceContract.SentenceGrammarContactsEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    "FOREIGN KEY(" + PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_SENTENCE_ID  + ") REFERENCES " +
                    PersistenceContract.SentenceEntry.TABLE_NAME + "(" + PersistenceContract.SentenceEntry.COLUMN_NAME_ENTRY_ID +")" + COMMA_SEP +
                    "FOREIGN KEY(" + PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_GRAMMAR_ID  + ") REFERENCES " +
                    PersistenceContract.GrammarEntry.TABLE_NAME + "(" + PersistenceContract.GrammarEntry.COLUMN_NAME_ENTRY_ID +")" + COMMA_SEP +
                    " )";

    static{
        V1 = new ArrayList<>();
        V2 = new ArrayList<>();




        V1.add(V1_CreateSentence);
        V1.add(V1_CreateGrammar);
        V1.add(V1_CreateSentenceGrammar);
    }


}
