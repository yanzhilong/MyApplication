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

    public static final List<String> V1TOV2;

    public static final String V1_CreateSentence =
            "CREATE TABLE " + SentencePersistenceContract.SentenceEntry.TABLE_NAME + " (" +
                    SentencePersistenceContract.SentenceEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    SentencePersistenceContract.SentenceEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.SentenceEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.SentenceEntry.COLUMN_NAME_TRANSLATE + TEXT_TYPE +
                    " )";

    public static final String V1_CreateGrammar =
            "CREATE TABLE " + SentencePersistenceContract.GrammarEntry.TABLE_NAME + " (" +
                    SentencePersistenceContract.GrammarEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    SentencePersistenceContract.GrammarEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.GrammarEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.GrammarEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +
                    " )";

    public static final String V1_CreateSentenceGrammar =
            "CREATE TABLE " + SentencePersistenceContract.SentenceGrammarContactsEntry.TABLE_NAME + " (" +
                    SentencePersistenceContract.SentenceGrammarContactsEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    SentencePersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_SENTENCE_ID + TEXT_TYPE + COMMA_SEP +
                    SentencePersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_GRAMMAR_ID + TEXT_TYPE +
                    " )";

    static{
        V1 = new ArrayList<>();
        V1TOV2 = new ArrayList<>();




        V1.add(V1_CreateSentence);
        V1.add(V1_CreateGrammar);
        V1.add(V1_CreateSentenceGrammar);
    }


}
