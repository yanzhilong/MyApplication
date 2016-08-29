package com.englishlearn.myapplication.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.englishlearn.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzl on 16-8-27.
 */
public class SearchUtil {

    private static SearchUtil INSTANCE;

    private String regEx = "[\u4e00-\u9fa5]";
    private Pattern pat = null;

    {
        pat = Pattern.compile(regEx);
    }

    // Prevent direct instantiation.
    private SearchUtil(){
    }

    public static SearchUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchUtil();
        }
        return INSTANCE;
    }

    /**
     * 根据搜索的关键詞返回用于请求的正則表达式
     * @param searchWord
     * @return
     */
    public String getSearchRegex(String searchWord){
        List<Word> list = getWords(searchWord);
        String where = "";
        if(isContainsChinese(searchWord)){
            where = "{\"translation\":{\"$regex\":\"%s\"}}";
        }else{
            where = "{\"content\":{\"$regex\":\"%s\"}}";
        }
        StringBuffer regex = new StringBuffer();
        for(int i = 0; i < list.size(); i++){
            Word word = list.get(i);
            regex.append(word.getName());
            if(word.isWord()){
                regex.append("\\s+");//一个或多个空格
            }else{
                regex.append(".*");//零个或多个字符
            }
        }
        where = String.format(where,regex);
        return where;
    }


    /**
     * 根据搜索的关键詞及返回的数据返回Spannable
     * @param searchWord
     * @param result
     * @return
     */
    public Spannable getSpannable(String searchWord,String result,Context context){
        List<Word> list = getWords(searchWord);
        Spannable spannable = new SpannableString(result);
        result = result.toLowerCase();
        for(Word word:list){
            int startIndex = -1;
            int newStartIndex = startIndex;
            do{
                startIndex = newStartIndex;
                if(startIndex + word.getName().length() < result.length()){
                    newStartIndex = result.indexOf(word.getName().toLowerCase(),startIndex + 1);
                    if(newStartIndex != -1){
                        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.search_matching)), newStartIndex,newStartIndex + word.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else{
                        break;
                    }
                }else {
                    break;
                }
            }while (newStartIndex != startIndex);
        }
        return spannable;
        //spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public boolean isContainsChinese(String str)
    {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()){
        flg = true;
    }
        return flg;
    }

    private List<Word> getWords(String searchWord){
        String result = "";
        //判断searchword是几个单詞
        String regex = ",|，|\\s+";
        String[] searchs = searchWord.split(regex);
        List<Word> list = new ArrayList<>();
        for(String str:searchs){
            Word word = new Word();
            word.setName(str);
            word.setWord(isWord(str));
            list.add(word);
        }
        return list;
    }



    private boolean isWord(String word){

        return false;
    }

    static class Word{

        private String name;//单詞名称
        private boolean isWord;//是否是整个的单詞

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isWord() {
            return isWord;
        }

        public void setWord(boolean word) {
            isWord = word;
        }
    }
}
