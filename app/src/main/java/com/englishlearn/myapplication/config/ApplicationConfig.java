package com.englishlearn.myapplication.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by yanzl on 16-12-14.
 */

public class ApplicationConfig {

    public final static String FILEBASENAME = "taoge";//文件夹
    public final static String FILEMDXSUBPATH = "doc";//文件夹
    public final static String EXTERNALBASE = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILEBASENAME;//外置目录
    public final static String INSIDEMDXPATH = File.separator + FILEBASENAME + File.separator + FILEMDXSUBPATH;//内部的词典目录
    public final static String INSIDEBASEPATH = File.separator + FILEBASENAME;//内部的词典目录
    public final static String INSIDEMDXNAME = "taoge.mdx";//内部的词典名称
    public final static String EXTERNALMDDNAME = "sound";//读音名称

    public final static String MDDUK = "_UK";//内部的词典名称
    public final static int DOWNLOADREFRESH = 1000;//下载刷新间隔


    //词典相关
    public final static int DICTTYPE_MDX = 0;
    public final static int DICTTYPE_MDD = 1;


    public final static String PSBASENAME = "psbase";//音标基础名称，读音文件在此基础上加上词组下标
    //音标常量 20个以后是辅音，数组里面两个值分别是#国际英标(IPA) 美英(KK)
    public final static String[][] phoneticsSymbolsArray = {
            {"iː","i"},0
            {"ɪ","ɪ"},1
            {"e","ɛ"},2
            {"æ","æ"},3
            {"ʌ","ʌ"},4
            {"ɜː","ɝ"},5
            {"ə","ə"},6
            {"uː","u"},7
            {"ʊ","ʊ"},8
            {"ɔː","ɔ"},9
            {"ɒ","ɑ"},10
            {"ɑː","ɑ"},11
            {"eɪ","e"},12
            {"aɪ","aɪ"},13
            {"ɒɪ","ɔɪ"},14
            {"əʊ","o"},15
            {"aʊ","aʊ"},16
            {"ɪə","ɪr"},17
            {"eə","ɛr"},18
            {"ʊə","ʊr"},19
            {"p",""},20
            {"t",""},21
            {"k",""},22
            {"b",""},23
            {"d",""},24
            {"g",""},25
            {"f",""},26
            {"s",""},27
            {"ʃ",""},28
            {"θ",""},29
            {"h",""},30
            {"v",""},31
            {"z",""},32
            {"ʒ",""},33
            {"ð",""},34
            {"tʃ",""},35
            {"tr",""},36
            {"ts",""},37
            {"dʒ",""},38
            {"dr",""},39
            {"dz",""},40
            {"m",""},41
            {"n",""},42
            {"ŋ",""},43
            {"l",""},44
            {"r",""},45
            {"w",""},46
            {"j",""},47

    };
}
