package com.englishlearn.myapplication.testmain;

import android.util.Log;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-11-28.
 */

public class TestMainHelper {

    private static final String TAG = TestMainHelper.class.getSimpleName();
    @Inject
    Repository repository;
    public TestMainHelper() {
        MyApplication.instance.getAppComponent().inject(this);
    }

    public void updateWordSoundByIciba(final int page){
        int pagesize = 100;

            Log.d(TAG,"getWords():" + page);
            repository.getWordsRx(page,pagesize).subscribe(new Subscriber<List<Word>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<Word> words) {
                    updateWords(words);
                    if(words != null && words.size() == 100){
                        updateWordSoundByIciba(page + 1);
                    }
                }
            });
    }

    //升级单词组
    private void updateWords(List<Word> words) {

        Log.d(TAG,"updateWords:" + words.size());
        for(int i = 0; i < words.size(); i++){
            final Word word = words.get(i);
            repository.getWordRxByIciba(word.getName()).subscribe(new Subscriber<Word>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Word wordIciba) {
                    if(wordIciba != null && wordIciba.getName().equals(word.getAliasName())){
                        word.setAmerican_soundurl(wordIciba.getAmerican_soundurl());
                        word.setBritish_soundurl(wordIciba.getBritish_soundurl());
                        updateWord(word);
                    }else if(wordIciba != null){
                        word.setAmerican_soundurl("");
                        word.setBritish_soundurl("");
                        updateWord(word);
                    }
                }
            });

        }
    }

    private void updateWord(Word word){
        Log.d(TAG,"updateWord:" + word);
        repository.updateWordRxById(word).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG,"addWordBYIciba:" + aBoolean);
            }
        });
    }
}
