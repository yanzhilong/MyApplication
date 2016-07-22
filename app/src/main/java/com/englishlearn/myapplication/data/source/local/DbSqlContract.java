package com.englishlearn.myapplication.data.source.local;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzl on 16-7-18.
 */
public class DbSqlContract {

    private static final String TEXT_TYPE = " TEXT";//数据类型

    private static final String BOOLEAN_TYPE = " INTEGER";//布尔值的数据类型

    private static final String COMMA_SEP = ",";

    public static final Map<Integer,List<String>> DBCREATEANDUPDATE;

    /**
     * 版本1
     * 说明：
     * 句子表
     * 词法表
     * 句子语法关系表
     */
    public static final List<String> V1;

    /**
     * 版本2
     * 说明：
     *
     */
    public static final List<String> V2;

    public static final String V1_CreateSentence =
            "CREATE TABLE " + PersistenceContract.SentenceEntry.TABLE_NAME + " (" +
                    PersistenceContract.SentenceEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SentenceEntry.COLUMN_NAME_TRANSLATION + TEXT_TYPE +
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
                    PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_SENTENCE_ID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_GRAMMAR_ID + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_SENTENCE_ID  + ") REFERENCES " +
                    PersistenceContract.SentenceEntry.TABLE_NAME + "(" + PersistenceContract.SentenceEntry.COLUMN_NAME_ENTRY_ID +")" + COMMA_SEP +
                    "FOREIGN KEY(" + PersistenceContract.SentenceGrammarContactsEntry.COLUMN_NAME_GRAMMAR_ID  + ") REFERENCES " +
                    PersistenceContract.GrammarEntry.TABLE_NAME + "(" + PersistenceContract.GrammarEntry.COLUMN_NAME_ENTRY_ID +")" +
                    " )";

    static{
        DBCREATEANDUPDATE = new LinkedHashMap<>();

        V1 = new ArrayList<>();
        V2 = new ArrayList<>();


        V1.add(V1_CreateSentence);
        V1.add(V1_CreateGrammar);
        V1.add(V1_CreateSentenceGrammar);


        DBCREATEANDUPDATE.put(1,V1);
        DBCREATEANDUPDATE.put(2,V2);

    }

    /**
     * 最新版本号
     */
    public static int getVersion(){
        Object[] versions = DBCREATEANDUPDATE.keySet().toArray();
        int version = (int) versions[versions.length - 1];
        return version;
    }

    /**
     * 初始创建语句
     * @return
     */
    public static List<String> getCreates(){
        List<String> list = new ArrayList<>();
        Object[] versions = DBCREATEANDUPDATE.keySet().toArray();
        if(versions.length > 0){
            list.addAll(DBCREATEANDUPDATE.get(versions[0]));
        }
        return list;
    }

    /**
     * 获取升级语句
     * @param oldVersion
     * @param newVersion
     * @return
     */
    public static List<String> getUpdates(int oldVersion, int newVersion){
        List<String> list = new ArrayList<>();
        Iterator<Map.Entry<Integer,List<String>>> iterator = DBCREATEANDUPDATE.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,List<String>> entry = iterator.next();
            if(entry.getKey() >= oldVersion){
                list.addAll(entry.getValue());
            }
        }
        return list;
    }
}
