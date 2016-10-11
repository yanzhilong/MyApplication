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
public class TractateTypeTest {

    private static final String TAG = TractateTypeTest.class.getSimpleName();
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
    public void addTractateType() {

        String[] tractatetypes = {"会话短文","精彩故事","电影台词","书本精摘"};
        for(int i = 0; i < tractatetypes.length; i++){
            Log.d(TAG,"testTractateType_add");
            TractateType addTractateType = new TractateType();
            addTractateType.setName(tractatetypes[i]);
            TestSubscriber<TractateType> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addTractateType(addTractateType).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            List<TractateType> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list != null || list.size() >= 0){

                TractateType tractateType = list.get(0);
                Log.d(TAG,"testTractateType_add_result:" + tractateType.toString());
            }

        }
    }
}
