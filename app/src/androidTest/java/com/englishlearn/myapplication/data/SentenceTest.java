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
public class SentenceTest {

    private static final String TAG = SentenceTest.class.getSimpleName();
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


    //上传单读音，添加单词
    @Test
    public void addSentence() {

        for(int i = 0; i < 100; i++){
            //添加　
            Log.d(TAG,"testSentence_add");
            Sentence addsentence = new Sentence();
            addsentence.setContent("Hello World " + i);
            addsentence.setTranslation("你好，世界 " + i);
            addsentence.setUserId("0703");
            addsentence.setRemark("测试");

            TestSubscriber<Sentence> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addSentence(addsentence).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            List<Sentence> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list != null && list.size() >= 0){
                Sentence sentence = list.get(0);
                Log.d(TAG,"testSentence_add_result:" + sentence.toString());
            }
        }
    }
}
