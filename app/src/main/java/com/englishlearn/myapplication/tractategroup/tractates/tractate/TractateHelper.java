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

    CurrentEnglishIndex currentEnglishIndex;
    CurrentChineseIndex currentChineseIndex;

    public TractateHelper(Context context, Tractate tractate, TextView textView, TextPaint textPaint) {
        this.context = context;
        this.tractate = tractate;
        this.textView = textView;
        this.textPaint = textPaint;
        newLineLength = textPaint.measureText(System.getProperty("line.separator"));
        width = textView.getWidth();
    }

    /**
     * 返回适用于当前TextView的上英文下中文的字符串
     * @return
     */
    public String getTractateString(){

        //1. 将英文和中文的内容分解成List
        //获得英文和中文的段落和句子List
        List<List<List<String>>> tractateList = AndroidUtils.splitTractate(tractate);
        //英文段落
        final List<List<String>> englishParagraph = tractateList.get(0);
        final List<List<String>> chineseParagraph = tractateList.get(1);


        return null;
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
