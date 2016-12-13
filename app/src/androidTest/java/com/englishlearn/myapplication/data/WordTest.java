package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.core.MdictManager;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.local.LocalDataSource;
import com.englishlearn.myapplication.data.source.preferences.SharedPreferencesDataSource;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.RemoteDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.util.AndroidFileUtils;
import com.englishlearn.myapplication.util.AndroidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WordTest {

    private static final String TAG = WordTest.class.getSimpleName();
    private RemoteData mBmobRemoteData;
    private Repository mRepository;
    private Context context;

    @Before
    public void setup() {
        mBmobRemoteData = BmobDataSource.getInstance();
        mRepository = Repository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context), SharedPreferencesDataSource.getInstance(context));
        context = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void cleanUp() {
        //mLocalDataSource.deleteAllSentences();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mBmobRemoteData);
    }


    @Test
    public void addSoundFile(){


        try {
            AndroidUtils.newInstance(context).appendStringExternal("word/addsounderror.txt","16" + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //获得单詞json
        String wordjson = "";
        //wordjson =  AndroidUtils.newInstance(context).getStringByResource(R.raw.words);

        Gson gson = new Gson();
        List<Word> words = gson.fromJson(wordjson,new TypeToken<List<Word>>() {
        }.getType());
        for(int i = 0; i < 2; i++){
            Word word = words.get(i);
            TestSubscriber<Word> testSubscriber_getWordByName = new TestSubscriber<>();
            mBmobRemoteData.getWordRxByName(word.getName()).subscribe(testSubscriber_getWordByName);

            testSubscriber_getWordByName.assertNoErrors();

            List<Word> wordresults = testSubscriber_getWordByName.getOnNextEvents();
            Word wordresult = null;
            if(wordresults != null && wordresults.size() > 0){
                wordresult = wordresults.get(0);
            }
            if(wordresult != null){

                File file = getSoundFile(wordresult.getName());


                if(file.exists()){
                    TestSubscriber<BmobFile> testSubscriber_deleteById = new TestSubscriber<>();
                    mBmobRemoteData.uploadFile(file,"audio/mp3").toBlocking().subscribe(testSubscriber_deleteById);
                    testSubscriber_deleteById.assertNoErrors();

                    List<BmobFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
                    BmobFile uploadFile = null;
                    if(uploadFiles != null && uploadFiles.size() > 0){
                        uploadFile = uploadFiles.get(0);
                    }
                    if(uploadFile != null){
                        wordresult.setAmerican_soundurl(uploadFile.getUrl());
                        wordresult.setBritish_soundurl(uploadFile.getUrl());

                        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
                        mBmobRemoteData.updateWordRxById(wordresult).toBlocking().subscribe(testSubscriber_update);
                        testSubscriber_update.assertNoErrors();
                        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
                        if(listupdate != null && listupdate.size() > 0){
                            Log.d(TAG,"testWord_update"+"success");
                        }
                    }else{
                        try {
                            AndroidUtils.newInstance(context).appendStringExternal("word/addsounderror.txt",word.getName() + System.getProperty("line.separator"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    try {
                        AndroidUtils.newInstance(context).appendStringExternal("word/addsounderror.txt",word.getName() + System.getProperty("line.separator"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    private Word getWordByIciba(String wordName){
        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mRepository.getWordRxByIciba(wordName).toBlocking().subscribe(testSubscriber_add);
        //testSubscriber_add.assertNoErrors();
        List<Word> list = testSubscriber_add.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            for(int i = 0; i < throwables.size(); i++){
                Log.d(TAG,"addWordBYYouDao_error:" + wordName +":"+ throwables.get(i).getMessage() + "");
            }
        }
        if(list == null || list.size() == 0){
            return null;
        }
        return list.get(0);
    }




    //上传单读音，添加单词
    @Test
    public void addWord() {

        //上传读音
        try {
            AndroidUtils.newInstance(context).addFile(R.raw.hello,"hello.mp3",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File("/data/data/com.englishlearn.myapplication/files/hello.mp3");
        if (!file.exists()) {
            return;
        }

        TestSubscriber<BmobFile> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.uploadFile(file,"audio/mp3").toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();

        List<BmobFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
        BmobFile uploadFile = null;
        if(uploadFiles != null && uploadFiles.size() > 0){
            uploadFile = uploadFiles.get(0);
        }
        if(uploadFile == null){
            return;
        }

        for(int i = 0; i < 10; i++){

            Word word = new Word();
            word.setName("Hello" + i);
            word.setTranslate("你好" + i);
            word.setBritish_phonogram("[hə'ləʊ; he-]");
            word.setBritish_soundurl("https://www.baidu.com/british_word"+ i +".mp3");
            word.setBritish_soundurl(uploadFile.getUrl());
            word.setAmerican_phonogram("[hɛˈlo, hə-]");
            word.setAmerican_soundurl("https://www.baidu.com/american_word"+ i +".mp3");
            word.setAmerican_soundurl(uploadFile.getUrl());
            word.setTranslate("哈罗"+i);
            word.setCorrelation("[ 复数 hellos或helloes 过去式 helloed 过去分词 helloed 现在分词 helloing ]");
            word.setRemark("备注" + i);

            TestSubscriber<Word> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.addWord(word).toBlocking().subscribe(testSubscriber);
            testSubscriber.assertNoErrors();
            List<Word> list = testSubscriber.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list == null || list.size() == 0){
                return;
            }
            Word wordresult = list.get(0);
            Log.d(TAG,"testWord_add_result:" + wordresult.toString());
        }

    }

    /**
     * 测试根据音标id获取单词列表
     */
    @Test
    public void getWordsRxByPhoneticsIdTest(){

        TestSubscriber<List<Word>> testSubscriber = new TestSubscriber<>();
        mBmobRemoteData.getWordsRxByPhoneticsId("554224d6f9").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        List<List<Word>> wordslist = testSubscriber.getOnNextEvents();
        List<Word> words = wordslist.get(0);
        Log.d(TAG,"getWordsRxByPhoneticsIdTest():" + words);
    }


    public File getSoundFile(String wordName){

        String dir = String.valueOf(wordName.toLowerCase().toCharArray()[0]);
        File file = AndroidFileUtils.newInstance(context).getFile("voice/" + dir + "/" +wordName + ".mp3");


        return file;
    }


    /**
     * 从列表添加单詞
     */
    @Test
    public void addWordsByRaw(){

        //获得单詞json
        String wordjson = "";
        //wordjson =  AndroidUtils.newInstance(context).getStringByResource(R.raw.words);

        Gson gson = new Gson();
        List<Word> words = gson.fromJson(wordjson,new TypeToken<List<Word>>() {
        }.getType());
        for(int i = 0; i < words.size(); i++){
            Word word = words.get(i);
            addWord(word);
        }
    }




    /**
     * 从列表添加单詞
     */
    @Test
    public void addWordsByRawDicts(){

        Gson gson = new Gson();
        //获得单詞json
        String wordstr1 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.dicts);
        String wordstr2 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google20k);
        String wordstr3 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);


        String[] wordstr2s = wordstr2.split(System.getProperty("line.separator"));
        String[] wordstr3s = wordstr3.split(System.getProperty("line.separator"));

        List<String> words1 = gson.fromJson(wordstr1,new TypeToken<List<String>>() {}.getType());
        List<String> words2 = Arrays.asList(wordstr2s);
        List<String> words3 = Arrays.asList(wordstr3s);

        List<String> words = new ArrayList<>();
        words.addAll(words1);
        words.addAll(words2);
        words.addAll(words3);

        HashSet<String> wordsSet = new HashSet<String>(words);

        Iterator<String> iterator = wordsSet.iterator();
        while (iterator.hasNext()){
            String wordName = iterator.next();
            addWordsByYouDao(wordName);
        }
        /*String[] wordsArray = (String[]) wordsSet.toArray();
        for(int i = 0; i < words1.size(); i++){
            if(!words.contains(words1.get(i))){
                words.add(words1.get(i));
            }
        }

        for(int i = 0; i < wordstr2s.length; i++){
            if(!words.contains(wordstr2s[i])){
                words.add(wordstr2s[i]);
            }
        }

        for(int i = 0; i < wordstr3s.length; i++){
            if(!words.contains(wordstr3s[i])){
                words.add(wordstr3s[i]);
            }
        }


        for(int i = 0; i < words.size(); i++){
            String name = words.get(i);
            addWordsByYouDao(name);
        }*/
    }


    @Test
    public void addWordsByRawDicts1(){

        Gson gson = new Gson();
        //获得单詞json
        String wordstr1 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.dicts);
        String wordstr2 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google20k);
        String wordstr3 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);


        String[] wordstr2s = wordstr2.split(System.getProperty("line.separator"));
        String[] wordstr3s = wordstr3.split(System.getProperty("line.separator"));

        List<String> words1 = gson.fromJson(wordstr1,new TypeToken<List<String>>() {}.getType());
        List<String> words2 = Arrays.asList(wordstr2s);
        List<String> words3 = Arrays.asList(wordstr3s);

        List<String> words = new ArrayList<>();
        words.addAll(words1);
        words.addAll(words2);
        words.addAll(words3);

        for(int i = 0; i < words1.size(); i++){
            if(!words.contains(words1.get(i))){
                words.add(words1.get(i));
            }
        }

        for(int i = 0; i < wordstr2s.length; i++){
            if(!words.contains(wordstr2s[i])){
                words.add(wordstr2s[i]);
            }
        }

        for(int i = 0; i < wordstr3s.length; i++){
            if(!words.contains(wordstr3s[i])){
                words.add(wordstr3s[i]);
            }
        }


        for(int i = 0; i < words.size(); i++){
            String name = words.get(i);
            //addWordsByYouDao(name);
        }
    }

    /**
     * 从列表添加单詞
     */

    public void addWordsByRawgoogle(){

        //获得单詞json
        String wordjson = "";

        wordjson =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);

        String[] words = wordjson.split(System.getProperty("line.separator"));
        for(int i = 0; i < words.length; i++){
            String word = words[i];
            addWordsByYouDao(word);
            Log.d(TAG,words[i]);
        }
    }

    /**
     * 从列表添加单詞
     */

    public void addWordsByRawgoogle1(){

        //获得单詞json
        String wordjson = "";

        wordjson =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google20k);

        String[] words = wordjson.split(System.getProperty("line.separator"));
        for(int i = 0; i < words.length; i++){
            String word = words[i];
            addWordsByYouDao(word);
            Log.d(TAG,words[i]);
        }
    }

    /**
     * 从列表添加单詞
     */
    @Test
    public void addWordsByYouDao(){

        String wordName = "Pardon";
            TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.getWordRxByYouDao(wordName).toBlocking().subscribe(testSubscriber_add);
            //testSubscriber_add.assertNoErrors();
            List<Word> list = testSubscriber_add.getOnNextEvents();
            if(list == null || list.size() == 0){
                return;
            }
            Word word = list.get(0);
            if(!word.getTranslate().trim().equals("")){
                if(word.getName().equals(wordName)){
                    addWord(word);
                }else {
                    Word wordnew = (Word) word.clone();
                    wordnew.setName(wordName);
                    addWord(word);
                    addWord(wordnew);
                }
            }
            Log.d(TAG,"testGrammar_add_result:" + word.toString());
            //addWord(word);
    }

    public void addWordsByYouDao(String wordName){

        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.getWordRxByYouDao(wordName).toBlocking().subscribe(testSubscriber_add);
        //testSubscriber_add.assertNoErrors();
        List<Word> list = testSubscriber_add.getOnNextEvents();
        if(list == null || list.size() == 0){
            return;
        }
        Word word = list.get(0);
        if(!word.getTranslate().trim().equals("")){
            if(word.getName().equals(wordName)){
                addWord(word);
            }else {
                Word wordnew = (Word) word.clone();
                wordnew.setName(wordName);
                addWord(word);
                addWord(wordnew);
            }
        }
        Log.d(TAG,"testGrammar_add_result:" + word.toString());
        //addWord(word);
    }

    /**
     * 从列表添加单詞
     */
    @Test
    public void getWordByBaiDu(){

        String wordName = "savers";
        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mRepository.getWordRxByIciba(wordName).toBlocking().subscribe(testSubscriber_add);
        //testSubscriber_add.assertNoErrors();
        List<Word> list = testSubscriber_add.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            for(int i = 0; i < throwables.size(); i++){
                Log.d(TAG,"addWordBYYouDao_error:" + throwables.get(i).getMessage());
            }
        }
        if(list == null || list.size() == 0){
            return;
        }
        for(int i = 0; i < list.size(); i++){
            Word word = list.get(i);
            Log.d(TAG,"addWordBYYouDao_result:" + word);
        }
    }

    public void addWord(Word word){

        TestSubscriber<Word> testSubscriber = new TestSubscriber<>();
        mBmobRemoteData.addWord(word).toBlocking().subscribe(testSubscriber);
        //testSubscriber.assertNoErrors();
        List<Word> list = testSubscriber.getOnNextEvents();
        //Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            try {
                AndroidUtils.newInstance(context).appendString("words",word + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Word wordresult = list.get(0);
        Log.d(TAG,"testWord_add_result:" + wordresult.toString());
    }

    @Test
    public void getWords(){


        List<Word> words = getWordsByBmob();
        Log.d(TAG,"getWordsRxByPhoneticsIdTest():" + words);
    }

    //得到words并创建词典源文件
    @Test
    public void createWordsMDict(){

        List<Word> words = getWordsByBmob();
        StringBuffer dictstring = new StringBuffer();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < words.size(); i++){
            Log.d(TAG,"words:" + i);
            Word word = words.get(i);
            dictstring.append(word.getName() + System.getProperty("line.separator"));
            Gson gson = new Gson();
            String wordjson = gson.toJson(word);
            dictstring.append(wordjson + System.getProperty("line.separator"));
            dictstring.append("</>" + System.getProperty("line.separator"));
            if(i == 0){
                Log.d(TAG,"writeFile:" + i);
                try {
                    AndroidUtils.newInstance(context).writeFile("mdxsource.txt",dictstring.toString());
                    dictstring = new StringBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(i % 50 == 0){
                try {
                    Log.d(TAG,"appendStringExternal:" + i);
                    AndroidUtils.newInstance(context).appendStringExternal("mdxsource.txt",dictstring.toString());
                    dictstring = new StringBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            AndroidUtils.newInstance(context).appendStringExternal("mdxsource.txt",dictstring.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
    }


    public List<Word> getWordsByBmob(){

        int page = 0;
        int pagesize = 100;

        List<Word> words = new ArrayList<>();
        List<Word> wordtemp = new ArrayList<>();

        do{
            Log.d(TAG,"getWords():" + page);
            TestSubscriber<List<Word>> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.getWordsRx(page,pagesize).toBlocking().subscribe(testSubscriber);
            testSubscriber.assertNoErrors();
            List<List<Word>> wordslist = testSubscriber.getOnNextEvents();

            List<Throwable> throwables = testSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                for(int i = 0; i < throwables.size(); i++){
                    Log.d(TAG,"addWordBYYouDao_error:" + throwables.get(i).getMessage());
                }
            }
            if(wordslist == null || wordslist.size() == 0){
                page++;
                continue;
            }else{
                page++;
            }
            wordtemp = wordslist.get(0);
            Log.d(TAG,"getWords():" +page+ wordtemp.size());
            words.addAll(wordtemp);
        }while (wordtemp.size() == 100);


        //Log.d(TAG,"getWordsRxByPhoneticsIdTest():" + words);
        return words;
    }


    public List<WordCollect> getWordsByWordGroupId(String wordGroupId){

        int page = 0;
        int pagesize = 100;

        List<WordCollect> words = new ArrayList<>();
        List<WordCollect> wordtemp = new ArrayList<>();

        do{
            Log.d(TAG,"getWords():" + page);
            TestSubscriber<List<WordCollect>> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.getWordCollectRxByWordGroupId(wordGroupId,page,pagesize).toBlocking().subscribe(testSubscriber);
            List<List<WordCollect>> wordslist = testSubscriber.getOnNextEvents();

            List<Throwable> throwables = testSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                for(int i = 0; i < throwables.size(); i++){
                    Log.d(TAG,"getWordsByWordGroupId_error:" + throwables.get(i).getMessage());
                }
                continue;
            }
            if(wordslist == null || wordslist.size() == 0){
                page++;
                continue;
            }else{
                page++;
            }
            wordtemp = wordslist.get(0);
            Log.d(TAG,"getWordsByWordGroupId():" + wordtemp.size());
            words.addAll(wordtemp);
        }while (wordtemp.size() == 100);


        //Log.d(TAG,"getWordsRxByPhoneticsIdTest():" + words);
        return words;
    }

    @Test
    public void deletWords(){

        int page = 0;
        int pagesize = 50;

        List<Word> wordtemp = new ArrayList<>();

        do{
            Log.d(TAG,"getWords():" + page);
            TestSubscriber<List<Word>> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.getWordsRx(page,pagesize).toBlocking().subscribe(testSubscriber);
            testSubscriber.assertNoErrors();
            List<List<Word>> wordslist = testSubscriber.getOnNextEvents();

            if(wordslist == null || wordslist.size() == 0){
                page++;
                continue;
            }else{
                page++;
            }
            wordtemp = wordslist.get(0);
            Log.d(TAG,"getWords():" +page+ wordtemp.size());
            deleteWords(wordtemp);
        }while (wordtemp.size() == 50 && page <= 140);

    }



    public void deleteWords(List<Word> words){
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        mRepository.deleteWords(words).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        List<Boolean> wordslist = testSubscriber.getOnNextEvents();
        if(wordslist != null && wordslist.size() > 0){
            Log.d(TAG,"deleteWords:" + wordslist.get(0));
        }
    }

    @Test
    public void getWordsWavData(){


        List<WordCollect> wordCollects = getWordsByWordGroupId("6cc72c0845");
        String dir = "";
        for (int i = 0; i < wordCollects.size(); i++){

            MDict mDict = MdictManager.newInstance(context).getMDict(wordCollects.get(i).getName());
            if(mDict != null){
                String newdir = "tmp" + i/3000;
                if(!newdir.equals(dir)){
                    dir = newdir;
                    Log.d(TAG,"i:" + i + dir);
                }
                boolean result = mDict.saveWaveData(dir);
                //Log.d(TAG,"i:" + wordCollects.get(i).getName() + (result ? "保存成功" : "保存失败"));
                boolean resultuk = mDict.saveUKWaveData("tmp" + i/3000);
                //Log.d(TAG,"i:" + wordCollects.get(i).getName()+"_UK" + (resultuk ? "保存成功" : "保存失败"));
            }
        }
    }

    @Test
    public void getWordsnotAdd(){

        String wordstr3 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);
        String[] wordstr3s = wordstr3.split(System.getProperty("line.separator"));

        for (int i = 0; i < wordstr3s.length; i++){

            TestSubscriber<Word> wordTestSubscriber = new TestSubscriber<>();
            mRepository.getWordRxByName(wordstr3s[i]).toBlocking().subscribe(wordTestSubscriber);
            List<Word> wordslist = wordTestSubscriber.getOnNextEvents();
            List<Throwable> throwables = wordTestSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                Throwable throwable = throwables.get(0);
                if(throwable instanceof BmobRequestException){
                    mRepository.addWordByHtml(wordstr3s[i]);
                }else{

                }
                Log.d(TAG,"throwable:" + throwable.getMessage());
            }

            if(wordslist != null && wordslist.size() > 0){
                Word word = wordslist.get(0);
                Log.d(TAG,"word:" + word);
            }
        }
    }


    //查看10000个单词里面哪些是没有查询到的
    @Test
    public void checkWords() throws IOException {

        String wordstr3 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);
        String[] wordstr3s = wordstr3.split(System.getProperty("line.separator"));

        AndroidUtils.newInstance(context).appendStringExternal("googleNotAdd","000" + System.getProperty("line.separator"));
        for (int i = 0; i < wordstr3s.length; i++){

            TestSubscriber<Word> wordTestSubscriber = new TestSubscriber<>();
            mRepository.getWordRxByName(wordstr3s[i]).toBlocking().subscribe(wordTestSubscriber);
            List<Word> wordslist = wordTestSubscriber.getOnNextEvents();
            List<Throwable> throwables = wordTestSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                try {
                    AndroidUtils.newInstance(context).appendStringExternal("googleNotAdd", wordstr3s[i] + System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wordslist != null && wordslist.size() > 0){
                Word word = wordslist.get(0);
                //Log.d(TAG,"word:" + word);
            }
        }
    }

    @Test
    public void addWordsToWordGroup(){

        String wordstr3 =  AndroidUtils.newInstance(context).getStringByResource(R.raw.google10000english);
        String[] wordstr3s = wordstr3.split(System.getProperty("line.separator"));

        for (int i = 0; i < wordstr3s.length; i++){

            TestSubscriber<Word> wordTestSubscriber = new TestSubscriber<>();
            mRepository.getWordRxByName(wordstr3s[i]).toBlocking().subscribe(wordTestSubscriber);
            List<Word> wordslist = wordTestSubscriber.getOnNextEvents();
            List<Throwable> throwables = wordTestSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                continue;
            }
            if(wordslist != null && wordslist.size() > 0){
                Word word = wordslist.get(0);
                addWOrdCOllect(word.getName());
                //Log.d(TAG,"word:" + word);
            }
        }



    }

    private void addWOrdCOllect(String wordName){
        //添加　
        WordCollect wordCollect = new WordCollect();
        wordCollect.setName(wordName);

        User user = new User();
        user.setObjectId("9d7707245a");

        wordCollect.setUser(user);

        WordGroup wordGroup = new WordGroup();
        wordGroup.setObjectId("6cc72c0845");

        wordCollect.setWordGroup(wordGroup);

        TestSubscriber<WordCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordCollect(wordCollect).toBlocking().subscribe(testSubscriber_add);
        List<WordCollect> list = testSubscriber_add.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            try {
                AndroidUtils.newInstance(context).appendStringExternal("addWordCollectFail.txt", wordName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(list != null && list.size() > 0){
            //WordCollect wordCollect1 = list.get(0);
            Log.d(TAG,"成功");
        }else{
            try {
                AndroidUtils.newInstance(context).appendStringExternal("addWordCollectFail.txt", wordName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
