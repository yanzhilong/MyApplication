package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.content.Context;
import android.text.TextPaint;
import android.widget.TextView;

import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.util.List;


/**
 * Created by yanzl on 16-10-15.
 *
 */

public class TractateHelper {

    private Context context;
    private Tractate tractate;//文章
    private TextView textView;//显示文章的TextView
    private TextPaint textPaint;//用于计算占用空间
    private float newLineLength;//换行符空间
    private float width;//TextView最大宽
    List<String> list;//当前显示的英文的每行


    CurrentEnglishIndex currentEnglishIndex;
    CurrentChineseIndex currentChineseIndex;
    private List<List<List<String>>> tractateList;

    public TractateHelper(Context context, Tractate tractate, TextView textView, TextPaint textPaint) {
        this.context = context;
        this.tractate = tractate;
        this.textView = textView;
        this.textPaint = textPaint;
        newLineLength = textPaint.measureText(System.getProperty("line.separator"));
        width = textView.getWidth();
        list = AndroidUtils.newInstance(context).getTextViewStringByLine(textView);
    }



    /**
     * 返回适用于当前TextView的上英文下中文的字符串
     * @return
     */
    public String getTractateString(){

        int englishPara = 0;//英文段落数
        int englishSent = 0;//英文句子数
        int chinesePara = 0;//中文段落数
        int chineseSent = 0;//中文句子数

        boolean isEngLineEnd = false;//当前英文句子检测结束
        boolean isChinLineEnd = false;//当前中文句子添加不下了
        int currentIndex = 0;//记录当前句子检测的起点
        String alreadAddChin = "";//当前行已经加入的中文
        String alreadAddEng = "";//当前行已检测过的英文
        String lastAddEng = "";//最后加的一句英文

        String currentLineEngAlread = "";//当前行已经添加过的


        boolean isEngParaEnd = false;//段落是否结束
        boolean isEngSentEnd = true;//句子是否结束,默认结束
        boolean isChiParaEnd = false;//段落是否结束
        boolean isChiSentEnd = true;//句子是否结束,默认结束
        String englishother = "";//英文上次剩余的
        String chineseother = "";//中文上次剩余的

        //1. 将英文和中文的内容分解成List
        //获得英文和中文的段落和句子List
        tractateList = AndroidUtils.splitTractate(tractate);
        //英文段落
        final List<List<String>> englishParagraph = tractateList.get(0);
        final List<List<String>> chineseParagraph = tractateList.get(1);

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++){
            isEngLineEnd = false;//检测下一条句子
            isChinLineEnd = false;//可以继续放中文了
            alreadAddChin = "";//清除已经添加的行
            currentIndex = 0;
            //判断当前行包含的段落数，句子数
            String line = list.get(i);
            stringBuffer.append(line + System.getProperty("line.separator"));//增加一行英文
            //stringBuffer.append(chineseother);//增加上一行剩余的中文
            //取出当前段落
            List<String> currentPara = englishParagraph.get(englishPara);

            //判断当前行检测结束
            while (!isEngLineEnd){

                if(isEngSentEnd){
                    //得到当前句子
                    String currentSent = currentPara.get(englishSent);
                    //当前句子去除尾部空格
                    char[] chars = currentSent.toCharArray();
                    int c = chars.length;
                    while (Character.isWhitespace(chars[c-1])){
                        c--;
                    }
                    String currentSentNew = String.valueOf(chars,0,c);
                    //去除当前句子前面空格
                    char[] chars1 = currentSentNew.toCharArray();
                    int c1 = 0;
                    while (Character.isWhitespace(chars1[c1])){
                        c1++;
                    }
                    String currentSentNew1 = String.valueOf(chars1,c1,chars1.length - c1);

                    //当前剩余字符串
                    String otherLine = line.substring(currentIndex,line.length());
                    //判断当前句子是否显示完全
                    if(otherLine.contains(currentSentNew1)){
                        lastAddEng = currentSentNew1;
                        alreadAddEng += currentSentNew;//已经检测过的英文存起来

                        //当前句子显示完成
                        //判断当前句子的第一个字符位置

                        englishSent++;//英文句子数+1
                        if(isChinLineEnd){
                            //如果中文已经不能显示了
                            continue;
                        }
                        int index = line.indexOf(currentSentNew1);//句子第一个字符位置

                        currentIndex += currentSentNew1.length();//下次从这句的下一个字符开始检测
                        //计算位置
                        String currentSenLine = line.substring(0,index);
                        float len = textPaint.measureText(currentSenLine);//从头到第一个字符的位置
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(chineseother);
                        //看是否上一次剩余的句子能不能完全放入
                        if(lenother >= width - newLineLength){
                            //将上一次的中文放入部分，剩余的留到下一次放
                            char[] charChinOther = chineseother.toCharArray();
                            int o = 0;
                            for(; o < charChinOther.length; o++){
                                if(textPaint.measureText(String.valueOf(charChinOther,0,o)) > (width - newLineLength)){
                                    o--;
                                    break;
                                }
                            }
                            //中文放入
                            stringBuffer.append(String.valueOf(charChinOther,0,o));//增加上一行剩余的中文
                            //剩余的存到字符串中
                            chineseother = chineseother.substring(o,chineseother.length());
                            isChinLineEnd  = true;//中文放不下了
                            continue;
                        }else{
                            alreadAddChin = alreadAddChin + chineseother;
                            chineseother = "";//清除上一行
                            stringBuffer.append(alreadAddChin);//增加上一行剩余的中文
                        }
                        //获得当前中文的句子
                        //取出当前段落
                        List<String> currentParaChin = chineseParagraph.get(chinesePara);
                        String currentSentChin = currentParaChin.get(chineseSent);
                        //在当前句子前面加上适当的空格，直到到达新句子的位置　
                        String add = "";
                        while (lenother + textPaint.measureText(add) < len){
                            add += " ";
                        }
                        char[] chin = currentSentChin.toCharArray();
                        int j = 0;
                        boolean isbreak = false;
                        String addchin = "";
                        for(; j < chin.length; j++){
                            if(lenother + textPaint.measureText(add + String.valueOf(chin,0,j)) > (width - newLineLength)){
                                isbreak = true;
                                break;
                            }
                        }
                        j--;
                        if(isbreak){
                            //保存剩余的内容
                            chineseother = String.valueOf(chin,j,chin.length - j);
                            addchin = add + String.valueOf(chin,0,j);
                            isChinLineEnd  = true;
                            stringBuffer.append(System.getProperty("line.separator"));
                        }else{
                            addchin = add + currentSentChin;
                            chineseSent++;//当前中文下标+1
                            //查看下一句英文


                        }
                        alreadAddChin = alreadAddChin + addchin;
                        stringBuffer.append(addchin);
                        //
                    }else{
                        //当前句子不能显示完成
                        isEngLineEnd = true;//当前行的英文句子检测结束了

                        if(isChinLineEnd){
                            //如果中文已经不能显示了
                            continue;
                        }
                        //搜索看能不能看到下一个句子

                        int lastAlread = line.indexOf(lastAddEng,(alreadAddEng.length() - lastAddEng.length())) + lastAddEng.length();
                        //放入中文
                        int index = line.indexOf(currentSentNew1.toCharArray()[0],lastAlread);//句子第一个字符位置

                        if(index == -1){
                            isEngSentEnd = true;
                            //已经没有放下一个句子了
                            //直接检测下一句英文
                            break;
                        }else{
                            //保存剩余的英文；
                            //currentSentNew1.
                            //englishother =
                            String otherEnline = line.substring(index,line.length());
                            otherEnline = cutSpan(otherEnline);//去除空格
                            //保存剩余的英文
                            englishother = currentSentNew1.substring(currentSentNew1.indexOf(otherEnline) + otherEnline.length(),currentSentNew1.length());

                            isEngSentEnd = false;
                        }
                        //计算位置
                        String currentSenLine = line.substring(0,index);
                        float len = textPaint.measureText(currentSenLine);//从头到第一个字符的位置
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(alreadAddChin);
                        //获得当前中文的句子
                        //取出当前段落
                        List<String> currentParaChin = chineseParagraph.get(chinesePara);
                        String currentSentChin = currentParaChin.get(chineseSent);
                        //在当前句子前面加上适当的空格，直到到达新句子的位置　
                        String add = "";
                        while (lenother + textPaint.measureText(add) < len){
                            add += " ";
                        }
                        char[] chin = currentSentChin.toCharArray();
                        int j = 0;
                        boolean isbreak = false;
                        String addchin = "";
                        for(; j < chin.length; j++){
                            if(lenother + textPaint.measureText(add + String.valueOf(chin,0,j)) > (width - newLineLength)){
                                isbreak = true;
                                break;
                            }
                        }
                        j--;
                        if(isbreak){
                            //保存剩余的内容
                            chineseother = String.valueOf(chin,j,chin.length - j);
                            addchin = add + String.valueOf(chin,0,j);
                            isChinLineEnd  = true;
                        }else{
                            addchin = add + currentSentChin;
                            //查看下一句英文
                        }
                        alreadAddChin = alreadAddChin + addchin;
                        stringBuffer.append(addchin);
                        stringBuffer.append(System.getProperty("line.separator"));
                    }
                }else{

                    if(isChinLineEnd){
                        //如果中文已经不能显示了
                        continue;
                    }

                    if(!chineseother.equals("")){
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(chineseother);
                        //看是否上一次剩余的句子能不能完全放入
                        if(lenother >= width - newLineLength){
                            //将上一次的中文放入部分，剩余的留到下一次放
                            char[] charChinOther = chineseother.toCharArray();
                            int o = 0;
                            for(; o < charChinOther.length; o++){
                                if(textPaint.measureText(String.valueOf(charChinOther,0,o)) > (width - newLineLength)){
                                    o--;
                                    break;
                                }
                            }
                            //中文放入
                            stringBuffer.append(String.valueOf(charChinOther,0,o));//增加上一行剩余的中文
                            //剩余的存到字符串中
                            chineseother = chineseother.substring(o,chineseother.length());
                            isChinLineEnd  = true;//中文放不下了
                            continue;
                        }else{
                            alreadAddChin = alreadAddChin + chineseother;
                            chineseother = "";//清除上一行
                            stringBuffer.append(alreadAddChin);//增加上一行剩余的中文
                            chineseSent++;//当前中文下标+1
                        }
                    }
                    //上次英文没有显示完成
                    //查看家次有没有显示完成
                    if(line.contains(cutSpan(englishother))){
                        //显示完成了
                        //显示剩余的中文
                        englishSent++;//英文句子数+1


                    }else{
                        //这次也没有显示完成
                        //显示剩余的中文


                    }
                }
            }



        }

        return stringBuffer.toString();
    }


    /**
     * 返回适用于当前TextView的上英文下中文的字符串
     * @return
     */
    public String getTractate1String(){

        int englishPara = 0;//英文当前段落
        int englishSent = 0;//英文句子数
        int chinesePara = 0;//中文段落数
        int chineseSent = 0;//中文句子数

        boolean isEngLineEnd = false;//当前英文句子检测结束
        boolean isChinLineEnd = false;//当前中文句子添加不下了
        int currentIndex = 0;//记录当前句子检测的起点
        String alreadAddChin = "";//当前行已经加入的中文
        String alreadAddEng = "";//当前行已检测过的英文
        String lastAddEng = "";//最后加的一句英文

        String currentLineEngAlread = "";//当前行已经添加过的


        boolean isEngParaEnd = false;//段落是否结束
        boolean isEngSentEnd = true;//句子是否结束,默认结束
        boolean isChiParaEnd = false;//段落是否结束
        boolean isChiSentEnd = true;//句子是否结束,默认结束
        String englishother = "";//英文上次剩余的
        String chineseother = "";//中文上次剩余的

        //1. 将英文和中文的内容分解成List
        //获得英文和中文的段落和句子List
        tractateList = AndroidUtils.splitTractate(tractate);
        //英文段落
        final List<List<String>> englishParagraph = tractateList.get(0);
        final List<List<String>> chineseParagraph = tractateList.get(1);

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++){
            isEngLineEnd = false;//检测下一条句子
            isChinLineEnd = false;//可以继续放中文了
            alreadAddChin = "";//清除已经添加的行
            currentIndex = 0;
            //判断当前行包含的段落数，句子数
            String line = list.get(i);
            stringBuffer.append(line + System.getProperty("line.separator"));//增加一行英文
            //stringBuffer.append(chineseother);//增加上一行剩余的中文
            //取出当前段落
            List<String> currentPara = englishParagraph.get(englishPara);

            //判断当前行检测结束
            while (!isEngLineEnd){

                if(isEngSentEnd){
                    //得到当前句子
                    String currentSent = currentPara.get(englishSent);
                    //当前句子去除尾部空格
                    char[] chars = currentSent.toCharArray();
                    int c = chars.length;
                    while (Character.isWhitespace(chars[c-1])){
                        c--;
                    }
                    String currentSentNew = String.valueOf(chars,0,c);
                    //去除当前句子前面空格
                    char[] chars1 = currentSentNew.toCharArray();
                    int c1 = 0;
                    while (Character.isWhitespace(chars1[c1])){
                        c1++;
                    }
                    String currentSentNew1 = String.valueOf(chars1,c1,chars1.length - c1);

                    //当前剩余字符串
                    String otherLine = line.substring(currentIndex,line.length());
                    //判断当前句子是否显示完全
                    if(otherLine.contains(currentSentNew1)){
                        lastAddEng = currentSentNew1;
                        alreadAddEng += currentSentNew;//已经检测过的英文存起来

                        //当前句子显示完成
                        //判断当前句子的第一个字符位置

                        englishSent++;//英文句子数+1
                        if(isChinLineEnd){
                            //如果中文已经不能显示了
                            continue;
                        }
                        int index = line.indexOf(currentSentNew1);//句子第一个字符位置

                        currentIndex += currentSentNew1.length();//下次从这句的下一个字符开始检测
                        //计算位置
                        String currentSenLine = line.substring(0,index);
                        float len = textPaint.measureText(currentSenLine);//从头到第一个字符的位置
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(chineseother);
                        //看是否上一次剩余的句子能不能完全放入
                        if(lenother >= width - newLineLength){
                            //将上一次的中文放入部分，剩余的留到下一次放
                            char[] charChinOther = chineseother.toCharArray();
                            int o = 0;
                            for(; o < charChinOther.length; o++){
                                if(textPaint.measureText(String.valueOf(charChinOther,0,o)) > (width - newLineLength)){
                                    o--;
                                    break;
                                }
                            }
                            //中文放入
                            stringBuffer.append(String.valueOf(charChinOther,0,o));//增加上一行剩余的中文
                            //剩余的存到字符串中
                            chineseother = chineseother.substring(o,chineseother.length());
                            isChinLineEnd  = true;//中文放不下了
                            continue;
                        }else{
                            alreadAddChin = alreadAddChin + chineseother;
                            chineseother = "";//清除上一行
                            stringBuffer.append(alreadAddChin);//增加上一行剩余的中文
                        }
                        //获得当前中文的句子
                        //取出当前段落
                        List<String> currentParaChin = chineseParagraph.get(chinesePara);
                        String currentSentChin = currentParaChin.get(chineseSent);
                        //在当前句子前面加上适当的空格，直到到达新句子的位置　
                        String add = "";
                        while (lenother + textPaint.measureText(add) < len){
                            add += " ";
                        }
                        char[] chin = currentSentChin.toCharArray();
                        int j = 0;
                        boolean isbreak = false;
                        String addchin = "";
                        for(; j < chin.length; j++){
                            if(lenother + textPaint.measureText(add + String.valueOf(chin,0,j)) > (width - newLineLength)){
                                isbreak = true;
                                break;
                            }
                        }
                        j--;
                        if(isbreak){
                            //保存剩余的内容
                            chineseother = String.valueOf(chin,j,chin.length - j);
                            addchin = add + String.valueOf(chin,0,j);
                            isChinLineEnd  = true;
                            stringBuffer.append(System.getProperty("line.separator"));
                        }else{
                            addchin = add + currentSentChin;
                            chineseSent++;//当前中文下标+1
                            //查看下一句英文


                        }
                        alreadAddChin = alreadAddChin + addchin;
                        stringBuffer.append(addchin);
                        //
                    }else{
                        //当前句子不能显示完成
                        isEngLineEnd = true;//当前行的英文句子检测结束了

                        if(isChinLineEnd){
                            //如果中文已经不能显示了
                            continue;
                        }
                        //搜索看能不能看到下一个句子

                        int lastAlread = line.indexOf(lastAddEng,(alreadAddEng.length() - lastAddEng.length())) + lastAddEng.length();
                        //放入中文
                        int index = line.indexOf(currentSentNew1.toCharArray()[0],lastAlread);//句子第一个字符位置

                        if(index == -1){
                            isEngSentEnd = true;
                            //已经没有放下一个句子了
                            //直接检测下一句英文
                            break;
                        }else{
                            //保存剩余的英文；
                            //currentSentNew1.
                            //englishother =
                            String otherEnline = line.substring(index,line.length());
                            otherEnline = cutSpan(otherEnline);//去除空格
                            //保存剩余的英文
                            englishother = currentSentNew1.substring(currentSentNew1.indexOf(otherEnline) + otherEnline.length(),currentSentNew1.length());

                            isEngSentEnd = false;
                        }
                        //计算位置
                        String currentSenLine = line.substring(0,index);
                        float len = textPaint.measureText(currentSenLine);//从头到第一个字符的位置
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(alreadAddChin);
                        //获得当前中文的句子
                        //取出当前段落
                        List<String> currentParaChin = chineseParagraph.get(chinesePara);
                        String currentSentChin = currentParaChin.get(chineseSent);
                        //在当前句子前面加上适当的空格，直到到达新句子的位置　
                        String add = "";
                        while (lenother + textPaint.measureText(add) < len){
                            add += " ";
                        }
                        char[] chin = currentSentChin.toCharArray();
                        int j = 0;
                        boolean isbreak = false;
                        String addchin = "";
                        for(; j < chin.length; j++){
                            if(lenother + textPaint.measureText(add + String.valueOf(chin,0,j)) > (width - newLineLength)){
                                isbreak = true;
                                break;
                            }
                        }
                        j--;
                        if(isbreak){
                            //保存剩余的内容
                            chineseother = String.valueOf(chin,j,chin.length - j);
                            addchin = add + String.valueOf(chin,0,j);
                            isChinLineEnd  = true;
                        }else{
                            addchin = add + currentSentChin;
                            //查看下一句英文
                        }
                        alreadAddChin = alreadAddChin + addchin;
                        stringBuffer.append(addchin);
                        stringBuffer.append(System.getProperty("line.separator"));
                    }
                }else{

                    if(isChinLineEnd){
                        //如果中文已经不能显示了
                        continue;
                    }

                    if(!chineseother.equals("")){
                        //计算上次剩余的字符串位置
                        float lenother = textPaint.measureText(chineseother);
                        //看是否上一次剩余的句子能不能完全放入
                        if(lenother >= width - newLineLength){
                            //将上一次的中文放入部分，剩余的留到下一次放
                            char[] charChinOther = chineseother.toCharArray();
                            int o = 0;
                            for(; o < charChinOther.length; o++){
                                if(textPaint.measureText(String.valueOf(charChinOther,0,o)) > (width - newLineLength)){
                                    o--;
                                    break;
                                }
                            }
                            //中文放入
                            stringBuffer.append(String.valueOf(charChinOther,0,o));//增加上一行剩余的中文
                            //剩余的存到字符串中
                            chineseother = chineseother.substring(o,chineseother.length());
                            isChinLineEnd  = true;//中文放不下了
                            continue;
                        }else{
                            alreadAddChin = alreadAddChin + chineseother;
                            chineseother = "";//清除上一行
                            stringBuffer.append(alreadAddChin);//增加上一行剩余的中文
                            chineseSent++;//当前中文下标+1
                        }
                    }
                    //上次英文没有显示完成
                    //查看家次有没有显示完成
                    if(line.contains(cutSpan(englishother))){
                        //显示完成了
                        //显示剩余的中文
                        englishSent++;//英文句子数+1


                    }else{
                        //这次也没有显示完成
                        //显示剩余的中文


                    }
                }
            }



        }

        return stringBuffer.toString();
    }

    class EnglishManager{
        final List<List<String>> englishParagraph = tractateList.get(0);//英文
        StringBuffer englishOther = new StringBuffer();//剩余英文
        int currentLine = 0;
        int[] englishSents;//当前行的句首数
        int[] englishSentX;//当前行的句首位置
        List<String> currentParaChin;//当前段落
        public String checkEnglishLine(int englishPara,String line){
            currentParaChin = englishParagraph.get(englishPara);
            //取出当前行
            String currentSentence = currentParaChin.get(currentLine);
            //判断剩余句子有没有显示完成
            if(line.contains(englishOther.toString())){
                //剩余句子显示完成,循环加入新句子
                

            }
        }

    }


    class ChineseManager{

        final List<List<String>> chineseParagraph = tractateList.get(1);//中文
        boolean lineFull = false;
        StringBuffer chineseOther = new StringBuffer();//剩余中文
        StringBuffer line = new StringBuffer();//当前行已经添加的中文

        /**
         * 获得一行中文
         * @param englishPara
         * @param englishSents
         * @param englishSentX
         * @return
         */
        private String getChineseLise(int englishPara,int[] englishSents,int[] englishSentX){

            List<String> currentParaChin = chineseParagraph.get(englishPara);//当前段落
            //判断剩余句子能不能放下
            float lenother = textPaint.measureText(chineseOther.toString());
            //看是否上一次剩余的句子能不能完全放入
            if(lenother >= width - newLineLength){
                //将上一次的中文放入部分，剩余的留到下一次放
                char[] charChinOther = chineseOther.toString().toCharArray();
                int o = 0;
                for(; o < charChinOther.length; o++){
                    if(textPaint.measureText(String.valueOf(charChinOther,0,o)) > (width - newLineLength)){
                        o--;
                        break;
                    }
                }
                //中文放入部分
                line.append(String.valueOf(charChinOther,0,o));//增加上一行剩余的中文
                //剩余的存到字符串中
                chineseOther = new StringBuffer(chineseOther.toString().substring(o,chineseOther.toString().length()));
                lineFull = true;//中文放不下了
            }else{
                line.append(chineseOther);
                chineseOther = new StringBuffer();
                lineFull = false;
            }

            while (!lineFull){
                for(int i = 0; i < englishSents.length; i++){
                    if(lineFull){
                        //剩下的全部加入到chineseOther中
                        int sentenceIndex = englishSents[i];
                        chineseOther.append(currentParaChin.get(sentenceIndex));
                        continue;
                    }
                    //获得当前中文的句子
                    int sentenceIndex = englishSents[i];
                    String currentSentence =  currentParaChin.get(sentenceIndex);


                    //计算从头到要放入的第一个句子的位置
                    float len = englishSentX[i];
                    //计算已经存入的中文位置
                    float linefloat = textPaint.measureText(line.toString());

                    //在当前句子前面加上适当的空格，直到到达新句子的位置　
                    String add = "";
                    while (lenother + textPaint.measureText(add) < len){
                        add += " ";
                    }
                    //放入当前句子
                    char[] currentSentenceChars = currentSentence.toCharArray();
                    int j = 0;
                    boolean isbreak = false;
                    String currentSentenceAdd = "";//当前句子能加入的
                    for(; j < currentSentenceChars.length; j++){
                        if(lenother + textPaint.measureText(add + String.valueOf(currentSentenceChars,0,j)) > (width - newLineLength)){
                            isbreak = true;
                            break;
                        }
                    }
                    if(isbreak){
                        //保存剩余的内容
                        chineseOther.append(String.valueOf(currentSentenceChars,j,currentSentenceChars.length - j));
                        currentSentenceAdd = add + String.valueOf(currentSentenceChars,0,j);

                        lineFull = true;
                    }else{
                        currentSentenceAdd = add + currentSentence;
                    }
                    line.append(currentSentenceAdd);
                }
            }
            line.append(System.getProperty("line.separator"));
            //判断是否是最后一句
            if(englishSents.length > 0 && currentParaChin.size() - 1 == englishSents[englishSents.length - 1]){
                //所有剩余的句子加入
            }
            return line.toString();
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
        String result = String.valueOf(chars,0,c);
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
        String result = String.valueOf(chars1,c1,chars1.length - c1);
        return result;
    }

    /**
     * 去除前后英文
     * @param res
     * @return
     */
    public String cutSpan(String res){
        res = cutAfterSpan(res);
        res = cutBeforeSpan(res);
        return res;
    }



    List<String> eng;//英文当前行的
    /**
     * 获取一行英文
     * @return
     */
    public String getEnglishLine(){

        if(currentEnglishIndex == null){
            currentEnglishIndex = new CurrentEnglishIndex();
        }
        //取当前段落剩余的内容
        
        return null;
    }

    /**
     * 读取一行，返回数组0为读取的，并加了"\n"，1为剩余的
     * @return
     */
    private String[] readLine(String string){

        int start = 0,i = 0,end = 0;
        int lastIndex = 0;
        char[] chars = string.toCharArray();
        String result = "";
        String other = "";//剩余的
        while(textPaint.measureText(result) <= (width - newLineLength) && i < chars.length){
            lastIndex = i;
            if(!Character.isLetter(chars[i])){
                i++;
            }else{
                while (Character.isLetter(chars[i])){
                    i++;
                }
            }
            result = string.substring(start,i);
        }
        result = string.substring(0,lastIndex);
        if(textPaint.measureText(result) > (width - newLineLength)){
            other = string.substring(lastIndex,string.length());
        }
        return new String[]{result,other};
    }


    /**
     * 获取一行中文
     * @return
     */
    public String getChineseLine(){


        return null;
    }

    //标识上面英文的当前位置
    private class CurrentEnglishIndex{
        private boolean isParagraphEnd;//段落结束
        private boolean isEnd;//全部结束
        private int currentParagrap;//当前段落
        private List<int[]> currentSentence;//当前句子和当前句子的头部位置
        private String currentSentenceOther;//当前句子剩余部分

        public boolean isParagraphEnd() {
            return isParagraphEnd;
        }

        public void setParagraphEnd(boolean paragraphEnd) {
            isParagraphEnd = paragraphEnd;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public int getCurrentParagrap() {
            return currentParagrap;
        }

        public void setCurrentParagrap(int currentParagrap) {
            this.currentParagrap = currentParagrap;
        }

        public List<int[]> getCurrentSentence() {
            return currentSentence;
        }

        public void setCurrentSentence(List<int[]> currentSentence) {
            this.currentSentence = currentSentence;
        }

        public String getCurrentSentenceOther() {
            return currentSentenceOther;
        }

        public void setCurrentSentenceOther(String currentSentenceOther) {
            this.currentSentenceOther = currentSentenceOther;
        }
    }

    //标识下面中文的当前位置
    private class CurrentChineseIndex{
        private boolean isParagraphEnd;//段落结束
        private boolean isEnd;//全部结束
        private int currentParagrap;//当前段落
        private int currentSentence;//当前句子
        private String currentSentenceOther;//当前句子剩余部分

        public boolean isParagraphEnd() {
            return isParagraphEnd;
        }

        public void setParagraphEnd(boolean paragraphEnd) {
            isParagraphEnd = paragraphEnd;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public int getCurrentParagrap() {
            return currentParagrap;
        }

        public void setCurrentParagrap(int currentParagrap) {
            this.currentParagrap = currentParagrap;
        }

        public int getCurrentSentence() {
            return currentSentence;
        }

        public void setCurrentSentence(int currentSentence) {
            this.currentSentence = currentSentence;
        }

        public String getCurrentSentenceOther() {
            return currentSentenceOther;
        }

        public void setCurrentSentenceOther(String currentSentenceOther) {
            this.currentSentenceOther = currentSentenceOther;
        }
    }
}
