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
public class TractateGroupTest {

    private static final String TAG = TractateGroupTest.class.getSimpleName();
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
    public void addTractateGroup() {

        //添加　
        Log.d(TAG,"testTractateGroup_add");
        TractateGroup addTractateGroup = new TractateGroup();
        addTractateGroup.setName("新概念英语第一册");
        addTractateGroup.setUserId(null);

        TestSubscriber<TractateGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateGroup(addTractateGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null || list.size() > 0){
            TractateGroup tractateGroup = list.get(0);
            Log.d(TAG,"testTractateGroup_add_result:" + tractateGroup.toString());
        }

    }

    @Test
    public void updateTractateGroup(){

        //根据Id获取信息来源　
        Log.d(TAG,"testTractateGroup_byId");
        TestSubscriber<TractateGroup> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getTractateGroupRxById("8c754205c4").toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<TractateGroup> listbyid = testSubscriber_getById.getOnNextEvents();
        TractateGroup tractateGroup1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            tractateGroup1ById = listbyid.get(0);
        }
        Log.d(TAG,"testTractateGroup_byId_result:" + tractateGroup1ById);
        //修改
        tractateGroup1ById.setOpen(true);
        Log.d(TAG,"testTractateGroup_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateTractateGroupRxById(tractateGroup1ById).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testTractateGroup_update"+"success");
        }

    }

}
