package com.englishlearn.myapplication.core;

import android.app.DownloadManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.config.ApplicationConfig;
import com.englishlearn.myapplication.data.MDict;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.observer.DownloadUtilObserver;
import com.englishlearn.myapplication.util.RxUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import cn.mdict.mdx.DictEntry;
import cn.mdict.mdx.DictPref;
import cn.mdict.mdx.MDictApp;
import cn.mdict.mdx.MdxDictBase;
import cn.mdict.mdx.MdxEngine;
import cn.mdict.mdx.MdxUtils;
import rx.Subscriber;

/**
 * Created by yanzl on 16-10-11.
 */
public class MdictManager {

    private static final String TAG = MdictManager.class.getSimpleName();
    private static MdictManager mdictManager;
    private Context context;
    private MDictApp theApp;
    private MdxDictBase mainDict;
    private HashMap<String, MDict> dictMap;//保存词典
    private HashMap<String, DictEntry> dictEntryMap;//保存词典
    private TextToSpeech ttsEngine = null;//tts播放引擎

    @Inject
    Repository repository;

    public MdictManager(Context context) {
        MyApplication.instance.getAppComponent().inject(this);
        this.context = context;
        dictMap = new HashMap<>();
        dictEntryMap = new HashMap<>();
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
        if (mdictManager == null) {
            mdictManager = new MdictManager(context);
        }
        return mdictManager;
    }

    /**
     * 初始化词典
     */
    public void initMdict() {

        rx.Observable
                .create(new rx.Observable.OnSubscribe<Boolean>() {

                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        refreshMdict();
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                })
                .compose(RxUtil.<Boolean>applySchedulers())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });

    }

    /**
     * 刷新词典
     */
    private void refreshMdict() {

        MdxEngine.refresh();//刷新初始化
        theApp = MDictApp.getInstance();
        theApp.setupAppEnv(context);
        List<DictPref> dictPrefs = getDictPrefsEntry();
        if (dictPrefs.size() > 0) {
            //默认只有一个词典
            mainDict = new MdxDictBase();
            MdxEngine.openDictById(dictPrefs.get(0).getDictId(), mainDict);
            long current  = System.currentTimeMillis();
            Log.d(TAG,"Init Mdict:" + current);
            for (int i = 0; i < mainDict.getEntryCount(); i++) {
                DictEntry dictEntry = new DictEntry(i, "", mainDict.getDictPref().getDictId());
                mainDict.getHeadword(dictEntry);
                dictEntryMap.put(dictEntry.getHeadword(),dictEntry);
                //Log.d(TAG,"Init Mdict:" + i + dictEntry.getHeadword());
            }
            long last  = System.currentTimeMillis();
            Log.d(TAG,"Init Mdict:" + last);
            Log.d(TAG,"Init Mdict:" + (last - current) / 1000);
        }
    }

    //获得词典Mdict
    private MDict getMdict(DictEntry dictEntry) {

        Word word = null;
        MDict mDict = null;
        String translate = MdxUtils.getTranslate(mainDict, dictEntry);
        Gson gson = new Gson();
        try {
            word = gson.fromJson(translate, Word.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDict = new MDict();
        mDict.setWord(word);
        mDict.setUseTTS(true);
        mDict.setDictEntry(dictEntry);
        mDict.setMdxDictBase(mainDict);
        mDict.setTtsEngine(ttsEngine);
        return mDict;
    }


    /**
     * 获得一个单词的解释
     *
     * @param name
     */
    public MDict getMDict(String name) {

        DictEntry dictEntry = dictEntryMap.get(name);
        if(dictEntry != null){
            return getMdict(dictEntry);
        }
        return null;
    }

    //获得词典名称
    private List<DictPref> getDictPrefsEntry() {

        List<DictPref> dictPrefs = new ArrayList<>();
        //得到当前目录
        DictPref dictPref = MdxEngine.getLibMgr().getRootDictPref();
        int count = dictPref.getChildCount();//当前词典的数量
        for (int i = 0; i < count; i++) {
            DictPref item = dictPref.getChildDictPrefAtIndex(i);//得到某一个子项
            dictPrefs.add(item);
        }
        return dictPrefs;
    }

    //获得词典名称
    public void addDownloadDictObserver() {

        DownloadUtilObserver.newInstance().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                List<DownloadStatus> downloadStatusList = (List<DownloadStatus>) data;
                for(DownloadStatus downloadStatus : downloadStatusList){

                    if(downloadStatus.getStatus() != DownloadManager.STATUS_SUCCESSFUL){
                        break;
                    }
                    String filePath = downloadStatus.getFileUri().getPath();
                    if(filePath.contains(ApplicationConfig.INSIDEMDXNAME)){
                        initMdict();
                        DownloadUtilObserver.newInstance().deleteObserver(this);
                    }
                }
            }
        });
    }
}
