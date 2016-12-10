package com.englishlearn.myapplication.util;

import android.content.Context;
import android.os.Environment;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.englishlearn.myapplication.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yanzl on 16-7-26.
 */
public class AndroidUtils {

    private static final String TAG = AndroidUtils.class.getSimpleName();
    private static Context context;
    static String SPLITTAG = "\\|";
    private static BufferedWriter bufferedWriter;

    public static AndroidUtils newInstance(Context context) {
        return new AndroidUtils(context);
    }

    public AndroidUtils(Context context) {
        this.context = context;
    }


    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
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

    public static void appendString(String filePath,String string) throws IOException {

        try {
            String basePath = context.getFilesDir().getAbsolutePath();
            File file = new File(basePath + "/" + filePath);

            //写文件
            FileOutputStream lOutputStream = new FileOutputStream(file,true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(lOutputStream));
            bufferedWriter.append(string);
        }finally {
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        }
    }

    public static void appendStringExternal(String filePath, String string) throws IOException {

        try {

            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taoge";
            File file = new File(basePath + "/" + filePath);

            if(!file.exists()){
                file.createNewFile();
            }

            //写文件
            FileOutputStream lOutputStream = new FileOutputStream(file,true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(lOutputStream));
            bufferedWriter.append(string);
        }finally {
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        }
    }

    public static void writeFile(String filePath,String string) throws IOException {

        Log.d(TAG,"writeFile");
        try {

            String basePath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(basePath + "/" + filePath);

          /*  if(file.exists()){
                file.createNewFile();
            }*/

            //写文件
            FileOutputStream lOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(lOutputStream));
            bufferedWriter.write(string);
        }finally {
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        }
    }

    public static void deleFile(String filePath) throws IOException {

        String basePath = context.getFilesDir().getAbsolutePath();
        File file = new File(basePath + "/" + filePath);
        file.delete();

    }

    public static String getString(String filePath) throws IOException {

            String basePath = context.getFilesDir().getAbsolutePath();
            File file = new File(basePath + "/" + filePath);

        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = new FileInputStream(file);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null){
                sb.append(line + System.getProperty("line.separator"));
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从/data/data/<package name>/files目录/
     * @param targetName
     * @return
     * @throws FileNotFoundException
     */
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


    /**
     * 获取TextView每一行的内容
     * @param textView
     * @return
     */
    public static List<String> getTextViewStringByLine(TextView textView){

        List<String> list = new ArrayList<>();
        Layout layout = textView.getLayout();
        //总行数
        int line = textView.getLayout().getLineCount();
        //Log.d(TAG,"总行数：" + line);
        String result = "";
        String text = layout.getText().toString();
        for(int i = 0; i < line-1; i++){
            int start = layout.getLineStart(i);
            int end = layout.getLineEnd(i);
            String sub = text.substring(start, end);
            list.add(sub);
            //Log.d(TAG,"has " + System.getProperty("line.separator") +sub.contains(System.getProperty("line.separator")));
            //Log.d(TAG,"SUB:"+sub);
            result += sub + System.getProperty("line.separator");
        }

        //Log.d(TAG,"result" + result);
        //System.out.print(result);
        return list;
    }

    /**
     * 获取raw资源文件
     * @param resId
     * @return
     */
    public static String getStringByResource(int resId){

        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null){
                sb.append(line + System.getProperty("line.separator"));
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取raw资源文件
     * @return
     */
    public static String getStringByFilePath(String filepath) throws IOException {

        StringBuffer sb = new StringBuffer();

        InputStream inputStream = new FileInputStream(new File(filepath));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + System.getProperty("line.separator"));
        }
        bufferedReader.close();
        inputStream.close();
        return sb.toString();
    }




}
