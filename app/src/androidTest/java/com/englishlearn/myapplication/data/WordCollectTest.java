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
public class WordCollectTest {

    private static final String TAG = WordCollectTest.class.getSimpleName();
    private static final String WORDID = "94a9fe8251";
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


    //添加单词收藏
    @Test
    public void addWordCollect(){

        //获得管理用户的所有收藏分组
        Log.d(TAG,"testWordGroup_getAll");
        TestSubscriber<List<WordGroup>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupRxByUserId(USERID,0,100).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<WordGroup>> listall = testSubscriber_getall.getOnNextEvents();
        List<WordGroup> wordGroups = null;
        if(listall != null && listall.size() > 0){
            wordGroups = listall.get(0);
        }
        Log.d(TAG,"testWordGroup_getAll_result:" + wordGroups.toString());

        //过滤分组是音标的
        for(WordGroup wordGroup:wordGroups){
            if(!wordGroup.getName().contains("示例单词")){
                continue;
            }

            WordCollect addWordCollect = new WordCollect();
            addWordCollect.setUserId(USERID);
            addWordCollect.setWordId(WORDID);
            addWordCollect.setWordgroupId(wordGroup.getObjectId());

            TestSubscriber<WordCollect> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addWordCollect(addWordCollect).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            List<WordCollect> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list == null || list.size() == 0){
                return;
            }
            WordCollect wordCollect = list.get(0);
            Log.d(TAG,"testWordCollect_add_result:" + wordCollect.toString());
        }
    }
}
