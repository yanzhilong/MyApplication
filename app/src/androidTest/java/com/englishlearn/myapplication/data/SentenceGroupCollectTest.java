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

import java.io.IOException;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SentenceGroupCollectTest {

    private static final String TAG = SentenceGroupCollectTest.class.getSimpleName();
    private static final String USERID = "468c3f629d";
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


    //添加单词分组收藏
    @Test
    public void addSentenceGroupCollect() throws IOException {

        Log.d(TAG,"testSentenceGroupCollect_add");
        SentenceGroupCollect addSentenceGroupCollect = new SentenceGroupCollect();
        addSentenceGroupCollect.setUserId(Constant.userId0703);
        addSentenceGroupCollect.setSentencegroupId("5d0881d114");

        TestSubscriber<SentenceGroupCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentenceGroupCollect(addSentenceGroupCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<SentenceGroupCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null && list.size() > 0){
            SentenceGroupCollect sentenceGroupCollect = list.get(0);
            Log.d(TAG,"testSentenceGroupCollect_add_result:" + sentenceGroupCollect.toString());
        }
    }
}
