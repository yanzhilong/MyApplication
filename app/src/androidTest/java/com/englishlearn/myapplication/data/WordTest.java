package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;
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
    private Context context;

    @Before
    public void setup() {
        mBmobRemoteData = BmobDataSource.getInstance();
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
            AndroidUtils.newInstance(context).appendString１("word/addsounderror.txt","16" + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //获得单詞json
        String wordjson = "";
        wordjson =  AndroidUtils.newInstance(context).getRawResource(R.raw.words);

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
                    TestSubscriber<UploadFile> testSubscriber_deleteById = new TestSubscriber<>();
                    mBmobRemoteData.uploadFile(file).toBlocking().subscribe(testSubscriber_deleteById);
                    testSubscriber_deleteById.assertNoErrors();

                    List<UploadFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
                    UploadFile uploadFile = null;
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
                            AndroidUtils.newInstance(context).appendString１("word/addsounderror.txt",word.getName() + System.getProperty("line.separator"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    try {
                        AndroidUtils.newInstance(context).appendString１("word/addsounderror.txt",word.getName() + System.getProperty("line.separator"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

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

        TestSubscriber<UploadFile> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.uploadFile(file).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();

        List<UploadFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
        UploadFile uploadFile = null;
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
        wordjson =  AndroidUtils.newInstance(context).getRawResource(R.raw.words);

        Gson gson = new Gson();
        List<Word> words = gson.fromJson(wordjson,new TypeToken<List<Word>>() {
        }.getType());
        for(int i = 0; i < words.size(); i++){
            Word word = words.get(i);
            addWord(word);
        }
    }

    public void addWord(Word word){

        TestSubscriber<Word> testSubscriber = new TestSubscriber<>();
        mBmobRemoteData.addWord(word).toBlocking().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        List<Word> list = testSubscriber.getOnNextEvents();
        Assert.assertNotNull(list);
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
}
