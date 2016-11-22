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
public class GrammarTest {

    private static final String TAG = GrammarTest.class.getSimpleName();
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
    public void addGrammar(){

        //添加　
        Log.d(TAG,"testGrammar_add");
        Grammar addgrammar = new Grammar();
        addgrammar.setTitle("不定冠词a an的使用");
        addgrammar.setContent("Hello World!");

        WordGroup wordGroup = new WordGroup();
        wordGroup.setObjectId("1e954e9964");

        SentenceGroup sentenceGroup = new SentenceGroup();
        sentenceGroup.setObjectId("462a751c58");

        User user = new User();
        user.setObjectId("9d7707245a");

        addgrammar.setWordGroup(wordGroup);
        addgrammar.setSentenceGroup(sentenceGroup);
        addgrammar.setUser(user);

        TestSubscriber<Grammar> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addGrammar(addgrammar).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Grammar> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Grammar grammar = list.get(0);
        Log.d(TAG,"testGrammar_add_result:" + grammar.toString());
    }
}
