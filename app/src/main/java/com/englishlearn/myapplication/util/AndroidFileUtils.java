package com.englishlearn.myapplication.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by yanzl on 16-10-26.
 */
public class AndroidFileUtils {

    private Context context;
    String basePath;
    File externalStorage;
    private static AndroidFileUtils androidFileUtils;

    public AndroidFileUtils(Context context) {
        this.context = context;
        basePath = context.getFilesDir().getAbsolutePath();
        externalStorage = Environment.getExternalStorageDirectory();
    }

    public static synchronized AndroidFileUtils newInstance(Context context) {

        if(androidFileUtils == null){
            androidFileUtils = new AndroidFileUtils(context);
        }
        return androidFileUtils;
    }

    /**
     * 根据文件或文件平夹返回File
     * @param filePath
     * @return
     */
    public File getFile(String filePath){
        File file = new File(externalStorage + "/" + filePath);
        return file;
    }

    /**
     * 根据文件或文件平夹返回File
     * @param filePath
     * @return
     */
    public File getExternalFile(String filePath){
        File file = new File(basePath + "/" + filePath);
        return file;
    }


    /**
     * 将bytes转成kb,mb,gb
     * @param bytes
     * @param si
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
