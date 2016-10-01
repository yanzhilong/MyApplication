package com.englishlearn.myapplication.util;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yanzl on 16-7-26.
 */
public class AndroidUtils {

    private static Context context;

    public static AndroidUtils newInstance(Context context) {
        return new AndroidUtils(context);
    }

    public AndroidUtils(Context context) {
        this.context = context;
    }

    /**
     * dp转换成px
     * @param context
     * @param dipValue
     * @return
     */
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    /**
     * 增加raw资源文件到/data/data/<package name>/files目录/
     * @param ressourceId
     * @param targetName
     */
    public static void addFile(int ressourceId, String targetName,boolean isOverwrite) throws IOException {

        String basePath = context.getFilesDir().getAbsolutePath();
        File file = new File(basePath + "/" + targetName);
        if(isOverwrite && file.exists()){
            throw new IOException("文件已经存在");
        }
        //写文件
        FileOutputStream lOutputStream = context.openFileOutput (targetName, 0);
        InputStream lInputStream = context.getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while (( readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff,0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }


    public static InputStream loadFile(String targetName) throws FileNotFoundException {

        String basePath = context.getFilesDir().getAbsolutePath();
        File file = new File(basePath + "/" + targetName);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        //写文件
        InputStream lInputStream = context.openFileInput(targetName);
        return lInputStream;
    }

}
