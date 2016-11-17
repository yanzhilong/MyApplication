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
public class SentenceGroupTest {

    private static final String TAG = SentenceGroupTest.class.getSimpleName();
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

    //添加音标单词分组
    @Test
    public void addWordGroup() {

        for(int i = 0; i < 5; i++){

            //添加　
            Log.d(TAG,"testSentenceGroup_add");
            SentenceGroup addSentenceGroup = new SentenceGroup();
            addSentenceGroup.setName("测试" + i);
            addSentenceGroup.setOpen(i % 2 == 0 ? true : false);
            addSentenceGroup.setUser(null);

            TestSubscriber<SentenceGroup> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addSentenceGroup(addSentenceGroup).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            List<SentenceGroup> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);

            if(list != null && list.size() > 0){
                SentenceGroup sentenceGroup = list.get(0);
                Log.d(TAG,"testSentenceGroup_add_result:" + sentenceGroup.toString());
            }
        }
    }
}
