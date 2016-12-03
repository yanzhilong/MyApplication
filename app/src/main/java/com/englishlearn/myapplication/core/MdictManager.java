package com.englishlearn.myapplication.core;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.englishlearn.myapplication.data.MDict;
import com.englishlearn.myapplication.data.Word;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.mdict.mdx.DictEntry;
import cn.mdict.mdx.DictPref;
import cn.mdict.mdx.MDictApp;
import cn.mdict.mdx.MdxDictBase;
import cn.mdict.mdx.MdxEngine;
import cn.mdict.mdx.MdxUtils;

/**
 * Created by yanzl on 16-10-11.
 */
public class MdictManager {

    private static MdictManager mdictManager;
    private Context context;
    private MDictApp theApp;
    private MdxDictBase mainDict;
    private List<DictEntry> dictEntries;
    private TextToSpeech ttsEngine = null;//tts播放引擎

    public MdictManager(Context context) {
        this.context = context;
        dictEntries = new ArrayList<>();
        ttsEngine = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    if (ttsEngine != null) {
                        ttsEngine.shutdown();
                        ttsEngine = null;
                    }
                } else {
                    ttsEngine.setLanguage(new Locale("en_GB"));
                }
            }
        });
    }

    public static synchronized MdictManager newInstance(Context context) {
        if(mdictManager == null){
            mdictManager = new MdictManager(context);
        }
        return mdictManager;
    }

    /**
     * 初始化词典
     */
    public void initMdict(){

        theApp = MDictApp.getInstance();
        theApp.setupAppEnv(context);
        refreshMdict();
    }



    /**
     * 刷新词典
     */
    public void refreshMdict(){
        List<DictPref> dictPrefs = getDictPrefsEntry();
        if(dictPrefs.size() > 0){
            //默认只有一个词典
            mainDict = new MdxDictBase();
            MdxEngine.openDictById(dictPrefs.get(0).getDictId(), mainDict);
            for (int i = 0; i < mainDict.getEntryCount(); i++){
                DictEntry dictEntry = new DictEntry(i,"",mainDict.getDictPref().getDictId());
                mainDict.getHeadword(dictEntry);
                dictEntries.add(dictEntry);
            }
        }
    }

    /**
     * 获得一个单词的解释
     * @param name
     */
    public MDict getMDict(String name){

        Word word = null;
        MDict mDict = null;
        for(DictEntry dictEntry : dictEntries){
            if(dictEntry.getHeadword().toLowerCase().equals(name.toLowerCase())){
                String translate = MdxUtils.getTranslate(mainDict,dictEntry);
                Gson gson = new Gson();
                try {
                    word = gson.fromJson(translate,Word.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
                mDict = new MDict();
                mDict.setWord(word);
                mDict.setUseTTS(true);
                mDict.setDictEntry(dictEntry);
                mDict.setMdxDictBase(mainDict);
                mDict.setTtsEngine(ttsEngine);
                break;
            }
        }
        return mDict;
    }

    //获得词典名称
    private List<DictPref> getDictPrefsEntry(){

        List<DictPref> dictPrefs = new ArrayList<>();
        //得到当前目录
        DictPref dictPref = MdxEngine.getLibMgr().getRootDictPref();
        int count = dictPref.getChildCount();//当前词典的数量
        for(int i = 0; i < count; i++){
            DictPref item = dictPref.getChildDictPrefAtIndex(i);//得到某一个子项
            dictPrefs.add(item);
        }
        return dictPrefs;
    }
}
