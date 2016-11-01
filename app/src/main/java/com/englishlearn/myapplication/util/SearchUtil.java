package com.englishlearn.myapplication.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.englishlearn.myapplication.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 根据音标id获得搜索关联的单词的json搜索语句
     * @param phoneticsId
     * @return
     */
    public String getWordJsonByPhoneticsId(String phoneticsId){

        Gson gson = new Gson();
        Map map1 = new HashMap();
        map1.put("phoneticsSymbolsId",phoneticsId);
        Map map2 = new HashMap();
        map2.put("className","PhoneticsWords");
        map2.put("where",map1);
        Map map3 = new HashMap();
        map3.put("query",map2);
        map3.put("key","wordgroupId");
        Map map4 = new HashMap();
        map4.put("$select",map3);
        Map map5 = new HashMap();
        map5.put("wordgroupId",map4);
        Map map6 = new HashMap();
        map6.put("className","WordCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","wordId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }


    /**
     * 根据分组id获得搜索组里面的单词的json搜索语句
     * @param wordgroupId
     * @return
     */
    public String getWordsRxByWordGroupId(String wordgroupId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("wordgroupId",wordgroupId);
        Map map6 = new HashMap();
        map6.put("className","WordCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","wordId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }



    /**
     * 根据分组id获得搜索组里面的句子的json搜索语句
     * @param sentencegroupId
     * @return
     */
    public String getSentencesRxBySentenceGroupId(String sentencegroupId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("sentencegroupId",sentencegroupId);
        Map map6 = new HashMap();
        map6.put("className","SentenceCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","sentenceId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }


    /**
     * 根据分组id获得搜索组里面的句子的json搜索语句
     * @param tractateGroupId
     * @return
     */
    public String getTractateRxByTractateGroupId(String tractateGroupId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("tractategroupId",tractateGroupId);
        Map map6 = new HashMap();
        map6.put("className","TractateCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","tractateId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }


    /**
     * 根据用户id获得搜索收藏的句子分组的json搜索语句
     * @param userId
     * @return
     */
    public String getCollectSentenceGroupRxByUserId(String userId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","SentenceGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","sentencegroupId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }


    /**
     * 根据用户id获得搜索收藏的单詞分组的json搜索语句
     * @param userId
     * @return
     */
    public String getCollectWordGroupRxByuserId(String userId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","WordGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","wordgroupId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }

    /**
     * 根据用户id获得搜索收藏的文章分组的json搜索语句
     * @param userId
     * @return
     */
    public String getCollectTractateGroupRxByUserId(String userId){

        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","TractateGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","tractategroupId");
        Map map8 = new HashMap();
        map8.put("$select",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);


        //String where = getWhereSubquery("objectId",getWhereSubquery("$select",getSelect("WordCollect",getWhereSubquery("wordgroupId",getSelect("PhoneticsWords",getWhere("phoneticsSymbolsId",phoneticsId),"wordgroupId")),"wordId")));

        String jsonStr = gson.toJson(map9);
        return jsonStr;
        //return where;
    }



    //获得未收藏的单詞分組
    public String getWordgroupsOpenAndNotCollect(String userId){

        //排除查询用户收藏的单词分组Id
        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","WordGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","wordgroupId");
        Map map8 = new HashMap();
        map8.put("$dontSelect",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);

        Map map10 = new HashMap();
        map10.put("open","true");

        List<Map<String,String>> list = new ArrayList<>();
        list.add(map10);
        list.add(map9);
        Map<String,List<Map<String,String>>> map11 = new HashMap();
        map11.put("$and",list);

        String jsonStr = gson.toJson(map11);
        return jsonStr;
    }

    //获得未收藏的文章分組
    public String getTractatesOpenAndNotCollect(String userId){

        //排除查询用户收藏的单词分组Id
        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","TractateGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","tractategroupId");
        Map map8 = new HashMap();
        map8.put("$dontSelect",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);

        Map map10 = new HashMap();
        map10.put("open","true");

        List<Map<String,String>> list = new ArrayList<>();
        list.add(map10);
        list.add(map9);
        Map<String,List<Map<String,String>>> map11 = new HashMap();
        map11.put("$and",list);

        String jsonStr = gson.toJson(map11);
        return jsonStr;
    }


    //获得未收藏的句子分組
    public String getSentenceGroupsOpenAndNotCollect(String userId){

        //排除查询用户收藏的单词分组Id
        Gson gson = new Gson();
        Map map5 = new HashMap();
        map5.put("userId",userId);
        Map map6 = new HashMap();
        map6.put("className","SentenceGroupCollect");
        map6.put("where",map5);
        Map map7 = new HashMap();
        map7.put("query",map6);
        map7.put("key","sentencegroupId");
        Map map8 = new HashMap();
        map8.put("$dontSelect",map7);
        Map map9 = new HashMap();
        map9.put("objectId",map8);

        Map map10 = new HashMap();
        map10.put("open","true");

        List<Map<String,String>> list = new ArrayList<>();
        list.add(map10);
        list.add(map9);
        Map<String,List<Map<String,String>>> map11 = new HashMap();
        map11.put("$and",list);

        String jsonStr = gson.toJson(map11);
        return jsonStr;
    }


    /**
     * key name
     * value yanzl
     * return {"name":"yanzl"}
     * @param key
     * @param value
     * @return
     */
    private String getWhere(String key,String value){

        String start = "{\"";
        String and = "\":\"";
        String end = "\"}";
        String result = start + key + and + value + end;
        return result;
    }



    /**
     * key name
     * value yanzl
     * return "name":"yanzl"
     * @param key
     * @param value
     * @return
     */
    private String getKeyValue(String key,String value){
        String start = "\"";
        String and = "\":\"";
        String end = "\"";
        return start + key + and + value + end;
    }

    /**
     * key name
     * value {..}
     * return "name":{..}
     * @param key
     * @param value
     * @return
     */
    private String getKeyAndValue(String key,String value){
        String start = "\"";
        String and = "\":\"";
        String end = "\"";
        return start + key + and + value + end;
    }

    /**
     * key name
     * value {....}
     * return {"name":{....}}
     * @param key
     * @param expression
     * @return
     */
    private String getWhereSubquery(String key,String expression){

        String start = "{";

        String end = "}";
        String result = start + getKeyAndValue(key,expression) + end;
        return result;
    }

    private String getSelect(String className,String where,String key){

        String selectStart = "{";
        String query = getKeyValue("query",getQuery(className,where));
        String and = ",";
        String keyvalue = getKeyValue("key",key);
        String selectEnd = "}";

        return selectStart + query + and + keyvalue + selectEnd;
    }

    /**
     * {
     *     className": "...",
     *     "where": ...
     * }
     * @param className
     * @param where
     * @return
     */
    private String getQuery(String className,String where){

        String queryStart = "{";
        String queryClassName = getKeyValue("className",className);
        String queryWhere = getKeyAndValue("where",where);
        String queryEnd = "}";
        return queryStart + queryClassName + queryWhere + queryEnd;
    }


    /**
     * 句子
     * 根据搜索的关键詞返回用于请求的正則表达式
     * @param searchWord
     * @return
     */
    public String getSearchSentenceRegex(String searchWord){
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
     * 语法
     * 根据搜索的关键詞返回用于请求的正則表达式
     * @param searchWord
     * @return
     */
    public String getSearchGrammarRegex(String searchWord){
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
    }


    /**
     * 文章
     * 根据搜索的关键詞返回用于请求的正則表达式
     * @param searchWord
     * @return
     */
    public String getSearchTractateRegex(String searchWord){
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
     * 根据名称和值返回搜索的匹配字条串
     * @param regexName
     * @param regexValue
     * @return
     */
    public String getBmobEquals(String regexName, String regexValue){
        String where = "{\""+regexName+"\":\""+ regexValue +"\"}";
        return where;
    }

    /**
     * 根据名称和值返回搜索的匹配字条串
     * @param regexName
     * @param regexValue
     * @return
     */
    public String getBmobEqualsByAnd(String[] regexName, String[] regexValue){
        if(regexName == null || regexValue == null || regexName.length < 2 || regexName.length != regexValue.length){
            throw new RuntimeException("array error");
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\"$and\":[");
        for(int i = 0; i < regexName.length; i++){
            stringBuffer.append("{\""+regexName[i]+"\""+":\""+regexValue[i]+"\"}");
            if(i < regexName.length - 1){
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("]}");
        return stringBuffer.toString();
    }


    /**
     * 根据用户名搜索用户信息
     * @param username
     * @return
     */
    public String getUserByNameRegex(String username){
        String where = "{\"username\":\""+username+"\"}";
        return where;
    }

    /**
     * 根据邮箱搜索用户信息
     * @param email
     * @return
     */
    public String getUserByMailRegex(String email){
        String where = "{\"email\":\""+email+"\"}";
        return where;
    }

    /**
     * 根据邮箱手机号搜索用户信息
     * @param mobilePhoneNumber
     * @return
     */
    public String getUserByPhoneRegex(String mobilePhoneNumber){
      String where = "{\"mobilePhoneNumber\":\""+mobilePhoneNumber+"\"}";
        return where;
    }

    /**
     * 根据搜索的关键詞及返回的数据返回Spannable,用于设置指定文字的颜色
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
