package com.englishlearn.myapplication.util;

import android.content.Context;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.AddTractate;
import com.englishlearn.myapplication.data.Tractate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzl on 16-7-26.
 */
public class AndroidUtils {

    private static final String TAG = AndroidUtils.class.getSimpleName();
    private static Context context;
    public static String englishregex = "[,.?;!]";//英文标点正则
    public static String chineseregex = "[，。？；！、]";//中文标点正则
    public static String SPLIT = "|";

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
     * 获取TextView某一行的内容
     * @param textView
     * @return
     */
    public static String getTextViewString(TextView textView){
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
            Log.d(TAG,"SUB:"+sub);
            result += sub + System.getProperty("line.separator");
        }

        Log.d(TAG,"result" + result);
        System.out.print(result);
        return result;
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
     * 检测文章合法性
     * @param resId
     * @return
     */
    public static AddTractate checkoutTractateByRaw(int resId){

        AddTractate addTractate = new AddTractate();
        Tractate tractate = new Tractate();

        StringBuffer checkoutResult = new StringBuffer();//检测报告
        StringBuffer tractateResult = new StringBuffer();//文章字符串

        List<String> english_paragraph = new ArrayList<>();//英文段落数
        List<String> chinese_paragraph = new ArrayList<>();//中文段落数

        StringBuffer english_stringBuffer = new StringBuffer();
        StringBuffer chinese_stringBuffer = new StringBuffer();

        boolean result = true;
        boolean isTranslation = false;

        String tractateStr = getRawResource(resId);
        String[] tractateArray = tractateStr.split(System.getProperty("line.separator"));

        String title = tractateArray[0];
        String remark = tractateArray[1];

        tractateResult.append("标题：" + title + System.getProperty("line.separator"));
        tractateResult.append("备注：" + remark + System.getProperty("line.separator"));

        //检测段落是否一样
        for (int i = 2; i < tractateArray.length; i++){

            String line = tractateArray[i];
            if(!line.startsWith("#") && !line.equals("")){

                if(!isTranslation && !line.startsWith("!!!")){
                    Log.d(TAG,"英文:"+line);
                    english_paragraph.add(line);
                    english_stringBuffer.append(line + System.getProperty("line.separator"));
                }else if(line.startsWith("!!!")){
                    //是译文
                    isTranslation = true;
                }else{
                    Log.d(TAG,"中文:"+line);
                    chinese_paragraph.add(line);
                    chinese_stringBuffer.append(line + System.getProperty("line.separator"));
                }
            }
        }

        //检测段落数量
        if(english_paragraph.size() != chinese_paragraph.size()){
            Log.e(TAG,"英文和译文行数不同");
            Log.e(TAG,"英文行数：" + english_paragraph.size());
            Log.e(TAG,"译文行数：" + chinese_paragraph.size());
            checkoutResult.append("英文和译文行数不同" + System.getProperty("line.separator"));
            checkoutResult.append("英文行数" + english_paragraph.size() + System.getProperty("line.separator"));
            checkoutResult.append("译文行数" + chinese_paragraph.size() + System.getProperty("line.separator"));
            result = false;
        }else {
            checkoutResult.append("英文和译文行数一致" + System.getProperty("line.separator"));
            Log.d(TAG,"英文和译文行数一致");
        }

        Pattern english = Pattern.compile(englishregex,Pattern.CASE_INSENSITIVE);
        Pattern chinese = Pattern.compile(chineseregex,Pattern.CASE_INSENSITIVE);

        for (int i = 0; i < english_paragraph.size() || i < chinese_paragraph.size(); i ++){

            //英文某一段落的标点
            String[] englishsentences = null;
            if( i <= english_paragraph.size() - 1){
                Log.d(TAG,"英文第" + i + "段:" + english_paragraph.get(i).toString());
                //检测中英文段落的标点是否是合法
                Matcher englishMatcher = chinese.matcher(english_paragraph.get(i).toString());
                boolean englishcheck = englishMatcher.find();
                Log.d(TAG,"英文第" + i + "段:" + (englishcheck ? "存在中文标点" : "不存在中文标点"));
                checkoutResult.append("英文第" + i + "段:" + (englishcheck ? "存在中文标点" : "不存在中文标点") + System.getProperty("line.separator"));

                //检测英文各个标点
                englishsentences = english.split(english_paragraph.get(i).toString());
                for (int j = 0; j < englishsentences.length; j ++){
                    tractateResult.append("英文第" + i + "段第" + j + "句:" + englishsentences[j] + System.getProperty("line.separator"));
                    Log.d(TAG,"英文第" + i + "段第" + j + "句:" + englishsentences[j]);
                }
            }

            //中文某一段落的标点
            String[] chinesesentences = null;
            if( i <= chinese_paragraph.size() - 1){
                Log.d(TAG,"中文第" + i + "段:" + chinese_paragraph.get(i).toString());
                //检测中英文段落的标点是否是合法
                Matcher chineseMatcher = english.matcher(chinese_paragraph.get(i).toString());
                boolean chinesecheck = chineseMatcher.find();
                Log.d(TAG,"中文第" + i + "段:" + (chinesecheck ? "存在英文标点" : "不存在英文标点"));
                checkoutResult.append("中文第" + i + "段:" + (chinesecheck ? "存在英文标点" : "不存在英文标点") + System.getProperty("line.separator"));

                //检测中文各个标点
                chinesesentences = chinese.split(chinese_paragraph.get(i).toString());
                for (int j = 0; j < chinesesentences.length; j ++){
                    tractateResult.append("中文第" + i + "段第" + j + "句:" + chinesesentences[j] + System.getProperty("line.separator"));
                    Log.d(TAG,"中文第" + i + "段第" + j + "句:" + chinesesentences[j]);
                }
            }

            //检测标点数量　
            if(englishsentences != null && chinesesentences != null && englishsentences.length == chinesesentences.length){
                checkoutResult.append("英文和译文第" + i + "行标点数相同" + System.getProperty("line.separator"));
            }else {
                checkoutResult.append("英文和译文第" + i + "行标点数不同" + System.getProperty("line.separator"));
                checkoutResult.append("英文第" + i + "行标点数：" + (englishsentences != null ? englishsentences.length : null) + System.getProperty("line.separator"));
                checkoutResult.append("译文第" + i + "行标点数：" + (chinesesentences != null ? chinesesentences.length : null) + System.getProperty("line.separator"));
                Log.e(TAG,"英文和译文第" + i + "行标点数不同");
                Log.e(TAG,"英文第" + i + "行标点数：" + (englishsentences != null ? englishsentences.length : null));
                Log.e(TAG,"译文第" + i + "行标点数：" + (chinesesentences != null ? chinesesentences.length : null));
                result = false;
            }
        }

        tractate.setTitle(title);
        tractate.setRemark(remark);
        tractate.setContent(english_stringBuffer.toString());
        tractate.setTranslation(chinese_stringBuffer.toString());
        addTractate.setCheckout(result);
        addTractate.setTractate(tractate);
        addTractate.setCheckout(result);
        addTractate.setCheckoutResult(checkoutResult.toString());
        addTractate.setTractateResult(tractateResult.toString());

        return addTractate;
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

        String SPLITTAG = "\\|";

        Log.d(TAG,"标题：" + englishtitle + SPLITTAG + chinesetitle + System.getProperty("line.separator"));
        Log.d(TAG,"备注：" + remark + System.getProperty("line.separator"));


        boolean isEnglish = false;
        //检测段落是否一样
        for (int i = 3; i < tractateArray.length; i++){
            if(i % 2 == 1){
                isEnglish = !isEnglish;
            }else{
                
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
            Log.d(TAG,"英文和译文行数一致" + System.getProperty("line.separator"));
            Log.d(TAG,"英文和译文行数一致");
        }

        for (int i = 0; i < english_paragraph.size() || i < chinese_paragraph.size(); i ++){

            int english_sentences = english_paragraph.get(i).split(SPLITTAG).length;
            int chinese_sentences = chinese_paragraph.get(i).split(SPLITTAG).length;

            //检测标点数量　
            if(english_sentences == chinese_sentences){
                Log.d(TAG,"英文和译文第" + i + "段句子数相同" + System.getProperty("line.separator"));
            }else {
                Log.d(TAG,"英文和译文第" + i + "段句子数不同" + System.getProperty("line.separator"));
                result = false;
            }
        }

        tractate.setTitle(englishtitle + SPLITTAG + chinesetitle);

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
        //各个段落
        String[] englishArray = english.split(System.getProperty("line.separator"));
        String[] chineseArray = chinese.split(System.getProperty("line.separator"));
        List<List<String>> englishList = new ArrayList<>();
        List<List<String>> chineseList = new ArrayList<>();
        //各个语句
        for(String s: englishArray){

            List<String> englishSentences = new ArrayList<>();
            Pattern englishPattern = Pattern.compile(englishregex,Pattern.CASE_INSENSITIVE);

            //英文某一段落的标点
            String[] englishsentences = null;
            //检测英文各个标点
            englishsentences = englishPattern.split(s);
            for (int j = 0; j < englishsentences.length; j ++){
                englishSentences.add(englishsentences[j]);
            }
            englishList.add(englishSentences);
        }

        //各个语句
        for(String s: chineseArray){

            List<String> chineseSentences = new ArrayList<>();
            Pattern chinesePattern = Pattern.compile(chineseregex,Pattern.CASE_INSENSITIVE);

            //英文某一段落的标点
            String[] chinesesentences = null;
            //检测英文各个标点
            chinesesentences = chinesePattern.split(s);
            for (int j = 0; j < chinesesentences.length; j ++){
                chineseSentences.add(chinesesentences[j]);
            }
            chineseList.add(chineseSentences);
        }

        lists.add(englishList);
        lists.add(chineseList);

        return lists;
    }

}
