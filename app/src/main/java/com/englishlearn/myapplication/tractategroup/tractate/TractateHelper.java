package com.englishlearn.myapplication.tractategroup.tractate;

import android.text.TextPaint;

import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.tractategroup.addtractate.AddTractateHelper;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;


/**
 * Created by yanzl on 16-10-15.
 *
 */

public class TractateHelper {

    private TextPaint textPaint;//用于计算占用空间

    private float newLineLength;//换行符空间
    private float width;//TextView最大宽

    List<String> list;//当前显示的英文的每行
    int[] englishSents;//当前行的句首数
    float[] englishSentX;//当前行的句首位置

    private List<List<String>> english;//英语段落列表
    private List<List<String>> chinese;//译文段落列表

    private List<List<int[]>> sentenceIndexs;//当前文章对应的每个段落每句英文对应的下标，方便点击事件的时候显示当前句英文
    private List<int[]> currentSentenceIndex;//当前段落每句的前后下标
    private StringBuffer stringBuffer;

    public TractateHelper(List<List<String>> english, List<List<String>> chinese) {

        this.english = english;
        this.chinese = chinese;
    }

    public TractateHelper(List<List<String>> english, List<List<String>> chinese, List<String> list, float textViewMaxWidth, TextPaint textPaint) {
        this.english = english;
        this.chinese = chinese;
        this.textPaint = textPaint;
        newLineLength = textPaint.measureText(System.getProperty("line.separator"));
        width = textViewMaxWidth;
        this.list = list;

        sentenceIndexs = new ArrayList<>();
        currentSentenceIndex = new ArrayList<>();
    }


    public List<List<int[]>> getSentenceIndexs() {
        return sentenceIndexs;
    }







    /**
     * 返回只有英文的字符串
     * @return
     */
    public String getTractateByEnglishString(){

        sentenceIndexs = new ArrayList<>();
        currentSentenceIndex = new ArrayList<>();

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < english.size(); i++){

            List<String> paragraphenglish = english.get(i);
            List<String> paragraphchinese = chinese.get(i);
            for(int j = 0; j < paragraphenglish.size(); j++){
                stringBuffer.append(paragraphenglish.get(j));

                int[] currentSentenceArray = new int[2];

                //一个句子的下标开始
                int start = stringBuffer.toString().lastIndexOf(paragraphenglish.get(j));
                currentSentenceArray[0] = start;

                //一个句子的下标结束
                currentSentenceArray[1] = stringBuffer.toString().lastIndexOf(paragraphenglish.get(j)) + paragraphenglish.get(j).length();

                currentSentenceIndex.add(currentSentenceArray);
            }

            //下标保存
            List<int[]> currentSentenceIndexTmp = new ArrayList<>();
            currentSentenceIndexTmp.addAll(currentSentenceIndex);
            currentSentenceIndex.clear();
            sentenceIndexs.add(currentSentenceIndexTmp);

            stringBuffer.append(System.getProperty("line.separator"));
            stringBuffer.append(System.getProperty("line.separator"));

        }

        return stringBuffer.toString();
    }



    /**
     * 返回一段英文，一段中文的字符串
     * @return
     */
    public String getTractateByParagraphString(){

        sentenceIndexs = new ArrayList<>();
        currentSentenceIndex = new ArrayList<>();

        StringBuffer stringBuffer = new StringBuffer();

        for(int i = 0; i < english.size(); i++){

            List<String> paragraphenglish = english.get(i);
            List<String> paragraphchinese = chinese.get(i);
            for(int j = 0; j < paragraphenglish.size(); j++){
                stringBuffer.append(paragraphenglish.get(j));

                int[] currentSentenceArray = new int[2];

                //一个句子的下标开始
                int start = stringBuffer.toString().lastIndexOf(paragraphenglish.get(j));
                currentSentenceArray[0] = start;

                //一个句子的下标结束
                currentSentenceArray[1] = stringBuffer.toString().lastIndexOf(paragraphenglish.get(j)) + paragraphenglish.get(j).length();

                currentSentenceIndex.add(currentSentenceArray);
            }

            //下标保存
            List<int[]> currentSentenceIndexTmp = new ArrayList<>();
            currentSentenceIndexTmp.addAll(currentSentenceIndex);
            currentSentenceIndex.clear();
            sentenceIndexs.add(currentSentenceIndexTmp);

            stringBuffer.append(System.getProperty("line.separator"));
            stringBuffer.append(System.getProperty("line.separator"));

            for(int j = 0; j < paragraphchinese.size(); j++){
                stringBuffer.append(paragraphchinese.get(j));
            }

            stringBuffer.append(System.getProperty("line.separator"));
            stringBuffer.append(System.getProperty("line.separator"));
        }

        return stringBuffer.toString();
    }


    /**
     * 返回适用于当前TextView的上英文下中文的字符串
     * @return
     */
    public String getTractateString(){


        stringBuffer = new StringBuffer();

        EnglishManager englishManager = new EnglishManager();
        ChineseManager chineseManager = new ChineseManager();

        int paragraph = 0;
        for (int i = 0; i < list.size(); i++){

            String line = list.get(i);
            if(line.equals(System.getProperty("line.separator"))){
                //stringBuffer.append(System.getProperty("line.separator"));
                continue;
            }

            if(line.contains(System.getProperty("line.separator"))){
                stringBuffer.append(line);//已经到达最后了不加換行符了
            }else{
                stringBuffer.append(line + System.getProperty("line.separator"));//增加一行英文,加上换行符
            }
            englishManager.checkEnglishLine(paragraph,line);
            String ch = "";
            if(line.contains(System.getProperty("line.separator"))){
                //最后一行了，加上所有的剩下的中文
                ch = chineseManager.getChineseLise1(paragraph,englishSents,englishSentX,true);
            }else{
                ch = chineseManager.getChineseLise1(paragraph,englishSents,englishSentX,false);
            }

            stringBuffer.append(ch + System.getProperty("line.separator"));//增加一行中文

            if(line.contains(System.getProperty("line.separator"))){
                stringBuffer.append(System.getProperty("line.separator"));//最后加完中文再加一下换行符，空一行
            }

            if(line.contains(System.getProperty("line.separator"))){
                if(paragraph < english.size() - 1){
                    paragraph++;

                    //下标保存
                    List<int[]> currentSentenceIndexTmp = new ArrayList<>();
                    currentSentenceIndexTmp.addAll(currentSentenceIndex);
                    currentSentenceIndex.clear();
                    sentenceIndexs.add(currentSentenceIndexTmp);
                }else if(paragraph == english.size() - 1){
                    //下标保存
                    List<int[]> currentSentenceIndexTmp = new ArrayList<>();
                    currentSentenceIndexTmp.addAll(currentSentenceIndex);
                    currentSentenceIndex.clear();
                    sentenceIndexs.add(currentSentenceIndexTmp);
                    break;
                }else{
                    break;
                }
            }
        }

        return stringBuffer.toString();
    }

    class EnglishManager{

        private int currentIndex = 0;//当前的句子行
        private int englishPara = 0;//当前段落
        StringBuffer englishOther = new StringBuffer();//剩余英文

        List<Float> widths = new ArrayList<>();//保存当前行所有句子头的位置
        List<Integer> indexs = new ArrayList<>();//保存当前行所有句子的下标

        List<String> currentParaEnglish;//当前段落
        int[] currentSentenceArray  = new int[2];//当前句子
        private int lastIndex;//记录已经显示的最后一个下标

        /**
         * 1、放上次剩下的
         *  剩下的为空
         *      放当前句子
         *          放得下
         *              放下一句
         *                  放得下
         *                      ....
         *                  放不下
         *          放不下
         *  放得下
         *      放当前句子
         *          放得下
         *              ....
         *          放不下
         *  放不下
         * 2、
         * @param englishPara
         * @param line
         */
        public void checkEnglishLine(int englishPara,String line){

            if(this.englishPara != englishPara){
                this.englishPara = englishPara;
                currentIndex = 0;//恢复句子数
            }
            if(line == null || line.equals("")){
                return;
            }
            //当前段落
            currentParaEnglish = english.get(englishPara);

            if(englishOther.toString().equals("")){
                lastIndex = 0;//从0开始依次检测每个句子能不能放下
                checkoutEverySentence(line);
            }else if(line.contains(englishOther.toString())){
                //放得下，记录这个句子的最后一个字符的下标，依次检测每个句子
                lastIndex = line.indexOf(englishOther.toString()) + englishOther.toString().length() - 1;//已经显示的englishOther的最后一个字母的下标

                //一个句子的下标结束
                currentSentenceArray[1] = stringBuffer.toString().indexOf(englishOther.toString(),stringBuffer.toString().lastIndexOf(line)) + englishOther.length();
                saveCurrendSentence(currentSentenceArray);


                englishOther = new StringBuffer();//清空剩余句子
                if(lastIndex == line.length() - 1){
                    //放不下了
                    return;
                }
                checkoutEverySentence(line);
            }else{
                //上次剩余的句子放不下了
                lastIndex = 0;
                checkoutLageSentence(line,englishOther.toString());
                englishSents = new int[widths.size()];
                englishSentX = new float[widths.size()];
            }
        }

        /**
         *
         * 检测依次检测句子能不能放下
         */
        public void checkoutEverySentence(String line){

            for (; currentIndex < currentParaEnglish.size(); currentIndex++){

                String currentSent = currentParaEnglish.get(currentIndex);
                currentSent = cutSpan(currentSent);//取出前后空格
                if(currentSent == null || currentSent.equals("") || currentSent.length() == 0 ){
                    continue;
                }
                int index = 0;
                //从lastIndex那里开始找，会包括lastIndex这一項
                if((index = line.indexOf(currentSent.toCharArray()[0],lastIndex == 0 ? lastIndex : lastIndex + 1)) != -1){
                    //"asd".substring(0,2),as,0,1,a,0,0 0
                    //当前句子头部有显示，计算当前句头的位置值
                    float width = textPaint.measureText(line.substring(0,index));
                    widths.add(width);
                    indexs.add(currentIndex);

                    //一个句子的下标开始
                    int start = stringBuffer.toString().lastIndexOf(line) + index;
                    currentSentenceArray[0] = start;

                    if(currentSent.contains("he asked the youngest son")){
                        int ss = 0;
                        ss = ss + 1;
                        ss = ss + 2;
                    }

                    //判断当前句子有没有显示完成
                    int currentIn = 0;
                    if((currentIn = line.indexOf(currentSent,lastIndex == 0 ? lastIndex : lastIndex + 1)) != -1){
                        //当前句子显示完成了
                        lastIndex = currentIn + currentSent.length() - 1;
                        //一个句子的下标结束
                        currentSentenceArray[1] = stringBuffer.toString().indexOf(currentSent,stringBuffer.toString().lastIndexOf(line)) + currentSent.length();
                        saveCurrendSentence(currentSentenceArray);

                        continue;
                    }else {
                        //没有显示完,判断显示了多少
                        checkoutLageSentence(line,currentSent);
                        currentIndex++;//下次检测下一句
                        break;
                    }
                }else{
                    //没有显示下一句,或显示不下了
                    break;
                }
            }

            englishSents = new int[widths.size()];
            englishSentX = new float[widths.size()];
            if(widths.size() > 0){
                for (int i = 0; i < widths.size(); i++){
                    englishSents[i] = indexs.get(i);
                    englishSentX[i] = widths.get(i);
                }
            }
            widths.clear();
            indexs.clear();
        }


        private void saveCurrendSentence(int[] sentenceIndex){
            int[] currentSent = new int[2];
            currentSent[0] = sentenceIndex[0];
            currentSent[1] = sentenceIndex[1];
            currentSentenceIndex.add(currentSent);
        }


        /**
         * 这个句子，放不下了
         */
        public void checkoutLageSentence(String line,String currentOrOther){

            char[] senchars = currentOrOther.toCharArray();
            int j = 1;
            for(; j <= senchars.length; j++){
                if(line.indexOf(String.valueOf(senchars,0,j),lastIndex == 0 ? lastIndex : lastIndex + 1) == -1){
                    break;
                }
            }
            englishOther = new StringBuffer();
            //保存剩余的句子
            englishOther.append(cutSpan(currentOrOther.substring(j - 1,currentOrOther.length())));
        }
    }


    class ChineseManager{



        /**
         * 递归
         * 变量：剩下的中文
         * 变量：已经存放的中文尾部位置
         *
         * 循环放入每个句子的中文
         *  放上次剩下的中文
         *      全部放入
         *          比较已经放好的中文大小位置和待放的当前下标中文的位置
         *              已放中文位置超过待放位置
         *                  看是否能全部放入当前下标中文
         *                      全部放入
         *                          记录己放中文的位置
         *                              比较已经放好的中文大小位置和待放的当前下标中文的位置
         *                                  ......
         *                      不能全部放入
         *                          循环放入前下标对应中文
         *                          清空剩下中文变量
         *                          将放不下的中文放入剩下中文变量中
         *                          循环将剩下的下标中文全部放入到剩下中文变量中(家里可以放在单独方法中)
         *                          判断是否到达当前段落的最后一句
         *                              到达
         *                                  当前行加入換行符，循环放入剩余的中文最后一行不放換行符
         *              已放中文位置未超过待放位置
         *                  计算已放中文位置和待放的当前下标中文的位置距离，用空格存在变量中
         *                  看是否能全部放入 空格变量＋当前下标中文
         *                      全部放入
         *                          ...
         *                      不能全部放入
         *                          ...
         *      不能全部放入
         *          ......
         * 获得一行中文,或剩下的全部中文
         * @param englishPara
         * @param englishSents
         * @param englishSentX
         * @return
         */


        StringBuffer chineseOther = new StringBuffer();//剩余中文
        StringBuffer line = new StringBuffer();//当前行已经添加的中文
        int lastIndex = 0;//已经存放的中文尾部的位置
        float spanWidth = textPaint.measureText(" ");//一个空格的大小

        private String getChineseLise1(int englishPara,int[] englishSents,float[] englishSentX,boolean isLastLine){

            List<String> currentParaChin1 = chinese.get(englishPara);//当前段落
            line = new StringBuffer();
            int[] mEnglishSents;
            //放剩下的中文
            lastIndex = getLargeSentence(0,chineseOther.toString(),true);

            if(lastIndex == Integer.MAX_VALUE){
                mEnglishSents = englishSents;
                addAll(englishPara,mEnglishSents,isLastLine);
                return line.toString();
            }

            for(int p = 0; p < englishSents.length; p ++){

                lastIndex = getLargeSentence(englishSentX[p],currentParaChin1.get(englishSents[p]),true);

                if(lastIndex == Integer.MAX_VALUE){
                    if(p == englishSents.length - 1){

                    }else{
                        mEnglishSents = new int[englishSents.length - 1 - p];
                        for(int i = p,j =  0; i < englishSents.length; i++,j++){
                            mEnglishSents[j] = englishSents[j];
                        }
                        addAll(englishPara,mEnglishSents,isLastLine);
                    }
                }
            }
            return line.toString();
        }

        private void addAll(int englishPara,int[] englishSents,boolean isLastLine){

            List<String> currentParaChin1 = chinese.get(englishPara);//当前段落
            for(int p = 0; p < englishSents.length; p++){
                chineseOther.append(currentParaChin1.get(englishSents[p]));
            }
            //判断是不是最后一句了，是的話全部加入
            if(isLastLine){
                if(chineseOther.toString().length() > 0){
                    line.append(System.getProperty("line.separator"));
                    int lasIn = Integer.MAX_VALUE;
                    while (lasIn == Integer.MAX_VALUE){
                        lasIn = getLargeSentence(0,chineseOther.toString(),false);
                    }
                }
            }
        }


        /**
         * 递归
         * 变量：剩下的中文
         * 变量：已经存放的中文尾部位置
         *
         * 循环放入每个句子的中文
         *  放上次剩下的中文
         *      全部放入
         *          比较已经放好的中文大小位置和待放的当前下标中文的位置
         *              已放中文位置超过待放位置
         *                  看是否能全部放入当前下标中文
         *                      全部放入
         *                          记录己放中文的位置
         *                              比较已经放好的中文大小位置和待放的当前下标中文的位置
         *                                  ......
         *                      不能全部放入
         *                          循环放入前下标对应中文
         *                          清空剩下中文变量
         *                          将放不下的中文放入剩下中文变量中
         *                          循环将剩下的下标中文全部放入到剩下中文变量中(家里可以放在单独方法中)
         *                          判断是否到达当前段落的最后一句
         *                              到达
         *                                  当前行加入換行符，循环放入剩余的中文最后一行不放換行符
         *              已放中文位置未超过待放位置
         *                  计算已放中文位置和待放的当前下标中文的位置距离，用空格存在变量中
         *                  看是否能全部放入 空格变量＋当前下标中文
         *                      全部放入
         *                          ...
         *                      不能全部放入
         *                          ...
         *      不能全部放入
         *          ......
         * 获得一行中文,或剩下的全部中文
         * @return 返回放入后最后一个字符的下标
         */
        public int getLargeSentence(float targetWidth,String sentence,boolean isSingleLine){

            //计算已经存放的大小位置
            float addedWidth = textPaint.measureText(line.toString());

            if(!isSingleLine){
                addedWidth = 0f;
            }
            if(addedWidth >= targetWidth - spanWidth / 2){
                // 看是否能全部放入当前下标中文
                if(targetWidth + textPaint.measureText(sentence) > (width - newLineLength)){
                    //不能全部放入,循环放入前下标对应中文
                    char[] currentSentenceChars = sentence.toCharArray();
                    int j = 1;
                    for(; j <= currentSentenceChars.length; j++){
                        if(addedWidth + textPaint.measureText(String.valueOf(currentSentenceChars,0,j)) > (width - newLineLength)){
                            break;
                        }
                    }
                    j--;
                    line.append(String.valueOf(currentSentenceChars,0,j));
                    if(!isSingleLine){
                        line.append(System.getProperty("line.separator"));
                    }
                    chineseOther = new StringBuffer();
                    chineseOther.append(sentence.substring(j,sentence.length()));
                    return Integer.MAX_VALUE;
                }else{
                    chineseOther = new StringBuffer();
                    //全部放入
                    line.append(sentence);
                    return line.toString().length() > 0 ? line.toString().length() - 1 : 0;
                }
            }else{
                StringBuffer span = new StringBuffer();
                while (addedWidth + textPaint.measureText(span.toString()) < targetWidth - spanWidth / 2){
                    span.append(" ");
                }
                line.append(span.toString());
                if(!isSingleLine){
                    return getLargeSentence(targetWidth,chineseOther.toString(),false);
                }
                return getLargeSentence(targetWidth,sentence,true);
            }
        }
    }




    /**
     * 去除后面的空格
     * @param res
     * @return
     */
    public String cutAfterSpan(String res){
        char[] chars = res.toCharArray();
        int c = chars.length;
        while (Character.isWhitespace(chars[c-1])){
            c--;
        }
        String result = valueOf(chars,0,c);
        return result;
    }

    /**
     * 去除前面的空格
     * @param res
     * @return
     */
    public String cutBeforeSpan(String res){
        char[] chars1 = res.toCharArray();
        int c1 = 0;
        while (Character.isWhitespace(chars1[c1])){
            c1++;
        }
        String result = valueOf(chars1,c1,chars1.length - c1);
        return result;
    }

    /**
     * 去除前后英文
     * @param res
     * @return
     */
    public String cutSpan(String res){
        if(res.equals("")){
            return res;
        }
        res = cutAfterSpan(res);
        res = cutBeforeSpan(res);
        return res;
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

            String[] sentenceArray = s.split(AddTractateHelper.SPLITTAG);
            List<String> englishSentences = new ArrayList<>();

            for(String s1:sentenceArray){
                englishSentences.add(s1);
            }
            englishList.add(englishSentences);
        }

        //各个语句
        for(String s: chineseArray){

            String[] sentenceArray = s.split(AddTractateHelper.SPLITTAG);
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
