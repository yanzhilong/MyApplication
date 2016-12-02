package com.englishlearn.myapplication.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.mdict.mdx.DictPref;
import cn.mdict.mdx.MDictApp;
import cn.mdict.mdx.MdxDictBase;
import cn.mdict.mdx.MdxEngine;

/**
 * Created by yanzl on 16-10-11.
 */
public class MdictManager {

    private static MdictManager mdictManager;
    private Context context;
    private MDictApp theApp;
    List<DictPref> dictPrefs;

    public MdictManager(Context context) {
        this.context = context;
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
        dictPrefs = getDictPrefsEntry();
        if(dictPrefs.size() > 0){
            MdxDictBase mainDict=new MdxDictBase();
            MdxEngine.openDictById(dictPrefs.get(0).getDictId(),mainDict);
            int count = mainDict.getEntryCount();
        }

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
