package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.local.LocalDataSource;
import com.englishlearn.myapplication.data.source.preferences.SharedPreferencesDataSource;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.RemoteDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WordUpdateTest {

    private static final String TAG = WordUpdateTest.class.getSimpleName();
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
    public void updteWords(){

        int page = 0;
        int pagesize = 100;

        List<Word> words = new ArrayList<>();

        do{
            Log.d(TAG,"getWords():" + page);
            TestSubscriber<List<Word>> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.getWordsRx(page,pagesize).toBlocking().subscribe(testSubscriber);
            testSubscriber.assertNoErrors();
            List<List<Word>> wordslist = testSubscriber.getOnNextEvents();

            List<Throwable> throwables = testSubscriber.getOnErrorEvents();
            if(throwables != null && throwables.size() > 0){
                for(int i = 0; i < throwables.size(); i++){
                    Log.d(TAG,"getWords_error:" + throwables.get(i).getMessage());
                }
            }
            if(wordslist == null || wordslist.size() == 0){
                page++;
                continue;
            }else{
                page++;
            }
            words = wordslist.get(0);
            Log.d(TAG,"getWords():" +page+":"+ words.size());
            updateWords(words);
        }while (words.size() == 100);
    }

    private void updateWords(List<Word> words) {

        for(int i = 0; i < words.size(); i++){

            Word word = words.get(i);


            if(word == null){
                continue;
            }

            if(word.getBritish_soundurl() != null && word.getBritish_soundurl().contains("res-tts") || word.getAmerican_soundurl() != null && word.getAmerican_soundurl().contains("res-tts")){

                if(word.getBritish_soundurl().contains("res-tts")){
                    word.setBritish_soundurl("");
                }
                if(word.getAmerican_soundurl().contains("res-tts")){
                    word.setAmerican_soundurl("");
                }
                updateWord(word);
            }else if(word.getBritish_soundurl() != null && !word.getBritish_soundurl().equals("") || word.getAmerican_soundurl() != null && !word.getAmerican_soundurl().equals("")){
                continue;
            }

            Word wordIciba = getWordByIciba(word.getName());

            if(wordIciba == null){
                continue;
            }

            if(wordIciba.getBritish_soundurl() != null && !wordIciba.getBritish_soundurl().equals("") || wordIciba.getAmerican_soundurl() != null && !wordIciba.getAmerican_soundurl().equals("")){
                if(word.getAliasName().toLowerCase().equals(wordIciba.getName().toLowerCase())){
                    word.setBritish_soundurl(wordIciba.getBritish_soundurl());
                    word.setAmerican_soundurl(wordIciba.getAmerican_soundurl());
                    updateWord(word);
                }
            }
        }
    }

    private void updateWord(Word word) {

        Log.d(TAG,"updateWord:" + word.getName());
        TestSubscriber<Boolean> testSubscriber_add = new TestSubscriber<>();
        mRepository.updateWordRxById(word).toBlocking().subscribe(testSubscriber_add);
        List<Boolean> list = testSubscriber_add.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            for(int i = 0; i < throwables.size(); i++){
                Log.d(TAG,"updateWordRxById_error:" + throwables.get(i).getMessage());
            }
        }
        if(list == null || list.size() == 0){
            return;
        }
        Log.d(TAG,"updateWordRxById_result:" + list.get(0));
    }


    public Word getWordByIciba(String wordName){

        Log.d(TAG,"getWordByIciba:" + wordName);
        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mRepository.getWordRxByIciba(wordName).toBlocking().subscribe(testSubscriber_add);
        List<Word> list = testSubscriber_add.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            for(int i = 0; i < throwables.size(); i++){
                Log.d(TAG,"getWordByIciba_error:" + throwables.get(i).getMessage());
            }
        }
        if(list == null || list.size() == 0){
            return null;
        }
        Word word = list.get(0);
        return word;
    }

}
