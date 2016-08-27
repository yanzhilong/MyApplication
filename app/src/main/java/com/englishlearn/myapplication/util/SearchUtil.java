package com.englishlearn.myapplication.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-8-27.
 */
public class SearchUtil {

    private static SearchUtil INSTANCE;

    // Prevent direct instantiation.
    private SearchUtil(){
    }

    public static SearchUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchUtil();
        }
        return INSTANCE;
    }

    public String getSearchRegex(String searchWord){
        List<Word> list = getWords(searchWord);
        String where = "{\"content\":{\"$regex\":\"%s\"}}";
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
    };

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
