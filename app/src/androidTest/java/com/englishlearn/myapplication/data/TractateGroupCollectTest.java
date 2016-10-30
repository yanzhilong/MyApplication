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
public class TractateGroupCollectTest {

    private static final String TAG = TractateGroupCollectTest.class.getSimpleName();
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
    public void addTractateGroupCollect() throws IOException {

        Log.d(TAG,"testSentenceGroupCollect_add");
        TractateGroupCollect addtractateGroupCollect = new TractateGroupCollect();
        addtractateGroupCollect.setUserId(Constant.userId3838);
        addtractateGroupCollect.setTractategroupId("8c754205c4");

        TestSubscriber<TractateGroupCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateGroupCollect(addtractateGroupCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateGroupCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null && list.size() > 0){
            TractateGroupCollect tractateGroupCollect = list.get(0);
            Log.d(TAG,"testTractateGroupCollect_add_result:" + tractateGroupCollect.toString());
        }
    }
}
