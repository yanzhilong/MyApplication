package com.englishlearn.myapplication.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by yanzl on 16-12-14.
 */

public class ApplicationConfig {

    public final static String FILEBASENAME = "taoge";//文件夹
    public final static String EXTERNALBASE = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILEBASENAME;//外置目录



    //词典相关
    public final static int DICTTYPE_MDX = 0;
    public final static int DICTTYPE_MDD = 1;

}
