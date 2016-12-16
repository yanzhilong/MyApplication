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


    //词典相关
    public final static int DICTTYPE_MDX = 0;
    public final static int DICTTYPE_MDD = 1;

}
