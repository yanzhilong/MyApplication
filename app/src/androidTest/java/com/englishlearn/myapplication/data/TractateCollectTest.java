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
public class TractateCollectTest {

    private static final String TAG = TractateCollectTest.class.getSimpleName();
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
    public void addTractateCollect() {

        //添加　
        Log.d(TAG,"testTractateCollect_add");
        TractateCollect addTractateCollect = new TractateCollect();
        User user = new User();
        user.setObjectId("9d7707245a");
        user.setPointer();
        addTractateCollect.setUserId(user);
        Tractate tractate = new Tractate();
        tractate.setObjectId("b5d8b92847");
        tractate.setPointer();
        addTractateCollect.setTractateId(tractate);

        TractateCollectGroup tractateCollectGroup = new TractateCollectGroup();
        tractateCollectGroup.setObjectId("a34e8da68d");
        tractateCollectGroup.setPointer();
        addTractateCollect.setTractateCollectgId(tractateCollectGroup);


        TestSubscriber<TractateCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateCollect(addTractateCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null || list.size() > 0){
            TractateCollect tractateCollect = list.get(0);
            Log.d(TAG,"testTractateCollect_add_result:" + tractateCollect.toString());
        }


    }

}
