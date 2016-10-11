package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.Constant;
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
public class SentenceCollectTest {

    private static final String TAG = SentenceCollectTest.class.getSimpleName();
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


    //添加单词收藏
    @Test
    public void addWordCollect(){

        //添加　
        Log.d(TAG,"testSentenceCollect_add");
        SentenceCollect addSentenceCollect = new SentenceCollect();
        addSentenceCollect.setUserId(Constant.userId0703);
        addSentenceCollect.setSentencegroupId("5325311d62");
        addSentenceCollect.setSentenceId("aa53588477");

        TestSubscriber<SentenceCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentenceCollect(addSentenceCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<SentenceCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null || list.size() > 0){
            SentenceCollect sentenceCollect = list.get(0);
            Log.d(TAG,"testSentenceCollect_add_result:" + sentenceCollect.toString());
        }
    }
}
