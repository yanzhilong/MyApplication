package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GetWordHtmlTest {

    private static final String TAG = GetWordHtmlTest.class.getSimpleName();
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
    public void getWordByHtmlTest(){
        String wordName = "hello";
        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.getWordRxByYouDao(wordName).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Word> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Word word = list.get(0);
        Log.d(TAG,"testGrammar_add_result:" + word.toString());
    }
}
