package com.englishlearn.myapplication.util;

import android.content.Context;
import android.os.Environment;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;

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

    public static void appendString１(String filePath,String string) throws IOException {

        try {

            String basePath = Environment.getExternalStorageDirectory().getPath();
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
        Log.d(TAG,"总行数：" + line);
        String result = "";
        String text = layout.getText().toString();
        for(int i = 0; i < line-1; i++){
            int start = layout.getLineStart(i);
            int end = layout.getLineEnd(i);
            String sub = text.substring(start, end);
            list.add(sub);
            Log.d(TAG,"has " + System.getProperty("line.separator") +sub.contains(System.getProperty("line.separator")));
            Log.d(TAG,"SUB:"+sub);
            result += sub + System.getProperty("line.separator");
        }

        Log.d(TAG,"result" + result);
        System.out.print(result);
        return list;
    }

    /**
     * 获取raw资源文件
     * @param resId
     * @return
     */
    public static String getRawResource(int resId){

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
    public static String getRawResource(String filepath){

        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = new FileInputStream(new File(filepath));

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
     * 检测文章合法性
     * @param resId
     * @return
     */
    public static Tractate getTractateByRaw(int resId){

        Tractate tractate = new Tractate();

        List<String> english_paragraph = new ArrayList<>();//英文段落数
        List<String> chinese_paragraph = new ArrayList<>();//中文段落数

        StringBuffer english_stringBuffer = new StringBuffer();
        StringBuffer chinese_stringBuffer = new StringBuffer();

        boolean result = true;

        String tractateStr = getRawResource(resId);
        String[] tractateArray = tractateStr.split(System.getProperty("line.separator"));

        String englishtitle = tractateArray[0];
        String chinesetitle = tractateArray[1];
        String remark = tractateArray[2];

        Log.d(TAG,"标题：" + englishtitle + SPLITTAG + chinesetitle + System.getProperty("line.separator"));
        Log.d(TAG,"备注：" + remark + System.getProperty("line.separator"));

        boolean isEnglish = false;
        //检测段落是否一样
        for (int i = 3; i < tractateArray.length; i++){
            if(i % 2 == 1){
                isEnglish = !isEnglish;
            }else{
                if(isEnglish){
                    english_stringBuffer.append(System.getProperty("line.separator"));
                }else{
                    chinese_stringBuffer.append(System.getProperty("line.separator"));
                }
                continue;
            }
            String line = tractateArray[i];

            if(isEnglish){
                Log.d(TAG,"英文:"+line);
                english_paragraph.add(line);
                english_stringBuffer.append(line + System.getProperty("line.separator"));
            }else{
                Log.d(TAG,"中文:"+line);
                chinese_paragraph.add(line);
                chinese_stringBuffer.append(line + System.getProperty("line.separator"));
            }
        }

        //检测段落数量
        if(english_paragraph.size() != chinese_paragraph.size()){
            Log.e(TAG,"英文和译文段落不同");
            Log.e(TAG,"英文段落：" + english_paragraph.size());
            Log.e(TAG,"译文段落：" + chinese_paragraph.size());
            result = false;
        }else {
            Log.d(TAG,"英文和译文段落数一致" + System.getProperty("line.separator"));
            Log.d(TAG,"英文和译文段落数一致");
        }

        for (int i = 0; i < english_paragraph.size() || i < chinese_paragraph.size(); i ++){

            int english_sentences = english_paragraph.get(i).split(SPLITTAG).length;
            int chinese_sentences = chinese_paragraph.get(i).split(SPLITTAG).length;

            //检测标点数量　
            if(english_sentences == chinese_sentences){
                Log.d(TAG,"英文和译文第" + i + "段句子数相同，都为：" + english_sentences + System.getProperty("line.separator"));
            }else {
                Log.d(TAG,"英文和译文第" + i + "段句子数不同，英文：" + english_sentences + "译文：" + chinese_sentences + System.getProperty("line.separator"));
                result = false;
            }
        }
        Log.d(TAG,"检测结果:" + result);
        tractate.setTitle(englishtitle + SPLITTAG + chinesetitle);
        tractate.setContent(english_stringBuffer.toString());
        tractate.setTranslation(chinese_stringBuffer.toString());
        return tractate;
    }


    /**
     * 检测文章合法性
     * @return
     */
    public static Tractate getTractateByfilePath(String filepath){

        Tractate tractate = new Tractate();

        List<String> english_paragraph = new ArrayList<>();//英文段落数
        List<String> chinese_paragraph = new ArrayList<>();//中文段落数

        StringBuffer english_stringBuffer = new StringBuffer();
        StringBuffer chinese_stringBuffer = new StringBuffer();

        boolean result = true;

        String tractateStr = getRawResource(filepath);
        String[] tractateArray = tractateStr.split(System.getProperty("line.separator"));

        String englishtitle = tractateArray[0];
        String chinesetitle = tractateArray[1];
        String remark = tractateArray[2];

        Log.d(TAG,"标题：" + englishtitle + SPLITTAG + chinesetitle + System.getProperty("line.separator"));
        Log.d(TAG,"备注：" + remark + System.getProperty("line.separator"));

        boolean isEnglish = false;
        //检测段落是否一样
        for (int i = 3; i < tractateArray.length; i++){
            if(i % 2 == 1){
                isEnglish = !isEnglish;
            }else{
                if(isEnglish){
                    english_stringBuffer.append(System.getProperty("line.separator"));
                }else{
                    chinese_stringBuffer.append(System.getProperty("line.separator"));
                }
                continue;
            }
            String line = tractateArray[i];

            if(isEnglish){
                Log.d(TAG,"英文:"+line);
                english_paragraph.add(line);
                english_stringBuffer.append(line + System.getProperty("line.separator"));
            }else{
                Log.d(TAG,"中文:"+line);
                chinese_paragraph.add(line);
                chinese_stringBuffer.append(line + System.getProperty("line.separator"));
            }
        }

        //检测段落数量
        if(english_paragraph.size() != chinese_paragraph.size()){
            Log.e(TAG,"英文和译文段落不同");
            Log.e(TAG,"英文段落：" + english_paragraph.size());
            Log.e(TAG,"译文段落：" + chinese_paragraph.size());
            result = false;
        }else {
            Log.d(TAG,"英文和译文段落数一致" + System.getProperty("line.separator"));
            Log.d(TAG,"英文和译文段落数一致");
        }

        for (int i = 0; i < english_paragraph.size() || i < chinese_paragraph.size(); i ++){

            int english_sentences = english_paragraph.get(i).split(SPLITTAG).length;
            int chinese_sentences = chinese_paragraph.get(i).split(SPLITTAG).length;

            //检测标点数量　
            if(english_sentences == chinese_sentences){
                Log.d(TAG,"英文和译文第" + i + "段句子数相同，都为：" + english_sentences + System.getProperty("line.separator"));
            }else {
                Log.d(TAG,"英文和译文第" + i + "段句子数不同，英文：" + english_sentences + "译文：" + chinese_sentences + System.getProperty("line.separator"));
                result = false;
            }
        }
        Log.d(TAG,"检测结果:" + result);
        tractate.setTitle(englishtitle + SPLITTAG + chinesetitle);
        tractate.setContent(english_stringBuffer.toString());
        tractate.setTranslation(chinese_stringBuffer.toString());
        return tractate;
    }

    /**
     * 将Tractate分解成英文和中文各个段落的各个句子的List(英文在前面，中文在后面)
     * @param tractate
     * @return
     */
    public static List<List<List<String>>> splitTractate(Tractate tractate){

        List<List<List<String>>> lists = new ArrayList<>();

        //英文和中文組
        String english = tractate.getContent();
        String chinese = tractate.getTranslation();

        String[] englishArray = cutSpanLine(english).split(System.getProperty("line.separator"));
        String[] chineseArray = cutSpanLine(chinese).split(System.getProperty("line.separator"));
        List<List<String>> englishList = new ArrayList<>();
        List<List<String>> chineseList = new ArrayList<>();
        //各个段落
        for(String s: englishArray){

            String[] sentenceArray = s.split(SPLITTAG);
            List<String> englishSentences = new ArrayList<>();

            for(String s1:sentenceArray){
                englishSentences.add(s1);
            }
            englishList.add(englishSentences);
        }

        //各个语句
        for(String s: chineseArray){

            String[] sentenceArray = s.split(SPLITTAG);
            List<String> chineseSentences = new ArrayList<>();

            for(String s1:sentenceArray){
                chineseSentences.add(s1);
            }
            chineseList.add(chineseSentences);
        }
        lists.add(englishList);
        lists.add(chineseList);
        return lists;
    }

    //去除空行
    public static String cutSpanLine(String res){
        String[] strings = res.split(System.getProperty("line.separator"));
        StringBuffer stringBuffer = new StringBuffer();
        for (String s:strings){
            if(s.equals("")){
                continue;
            }
            stringBuffer.append(s + System.getProperty("line.separator"));
        }
        return stringBuffer.toString();
    }
}
