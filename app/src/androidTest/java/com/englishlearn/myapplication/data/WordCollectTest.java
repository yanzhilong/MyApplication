package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
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
    public void addWordCollect() {

        //添加　
        WordCollect wordCollect = new WordCollect();
        wordCollect.setName("hello");

        User user = new User();
        user.setObjectId("9d7707245a");

        wordCollect.setUser(user);

        WordGroup wordGroup = new WordGroup();
        wordGroup.setObjectId("6cc72c0845");

        wordCollect.setWordGroup(wordGroup);

        TestSubscriber<WordCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordCollect(wordCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordCollect> list = testSubscriber_add.getOnNextEvents();
        if(list != null || list.size() > 0){
            WordCollect wordCollect1 = list.get(0);
            Log.d(TAG,"成功");
        }
    }

}
