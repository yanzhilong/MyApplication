package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WordGroupTest {

    private static final String TAG = WordGroupTest.class.getSimpleName();
    private static final String USERID_189 = "468c3f629d";
    private static final String USERID_159 = "943a8a40ed";
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
    public void addPhoneticsWordGroup() throws IOException {

        //读取音标
        List<PhoneticsSymbols> phoneticsSymbolses = new ArrayList<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.phoneticssymbols);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean isvowel = false;

        String line;

        while ((line = bufferedReader.readLine()) != null) {

            if (!line.startsWith("#") && !line.equals("")) {
                if (line.contains("p")) {
                    isvowel = true;
                }
                PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();
                //判断是否有美英
                String[] names = line.split("\\s+");
                String ipaname;
                String kkname;
                if (names.length > 1) {
                    ipaname = names[0];
                    kkname = names[1];
                } else {
                    ipaname = line;
                    kkname = line;
                }
                phoneticsSymbols.setIpaname(ipaname);
                phoneticsSymbols.setKkname(kkname);
                phoneticsSymbols.setVowel(isvowel);
                phoneticsSymbolses.add(phoneticsSymbols);

            }
        }
        Log.d(TAG, phoneticsSymbolses.size() + "");
        Log.d(TAG, phoneticsSymbolses.toString());

        for (PhoneticsSymbols phoneticsSymbols : phoneticsSymbolses) {
            //添加　
            Log.d(TAG, "testWordGroup_add");
            WordGroup addwordGroup = new WordGroup();
            addwordGroup.setName(phoneticsSymbols.getIpaname() + " or " + phoneticsSymbols.getKkname() + "示例单词");
            addwordGroup.setOpen(false);
            User user = new User();
            user.setObjectId("9d7707245a");
            addwordGroup.setUser(user);

            TestSubscriber<WordGroup> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addWordGroup(addwordGroup).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            List<WordGroup> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);
            if (list == null || list.size() == 0) {
                return;
            }
            WordGroup wordGroup = list.get(0);
            Log.d(TAG, "testWordGroup_add_result:" + wordGroup.toString());
        }

    }

    //添加音标单词分组
    @Test
    public void addWordGroup() {

        //添加　
        Log.d(TAG, "testWordGroup_add");
        WordGroup addwordGroup = new WordGroup();
        addwordGroup.setName("新概念英语第一册(1-10)");
        addwordGroup.setOpen(false);
        User user = new User();
        user.setObjectId("9d7707245a");
        addwordGroup.setUser(user);

        TestSubscriber<WordGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordGroup(addwordGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if (list == null || list.size() == 0) {
            return;
        }
        WordGroup wordGroup = list.get(0);
        Log.d(TAG, "testWordGroup_add_result:" + wordGroup.toString());

    }

    //添加语法关联单词分组
    @Test
    public void addGrammarWordGroup() {

        //添加　
        Log.d(TAG, "testWordGroup_add");
        WordGroup addwordGroup = new WordGroup();
        addwordGroup.setName("语法相关单词");
        addwordGroup.setOpen(false);
        User user = new User();
        user.setObjectId("9d7707245a");
        addwordGroup.setUser(user);

        TestSubscriber<WordGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordGroup(addwordGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if (list == null || list.size() == 0) {
            return;
        }
        WordGroup wordGroup = list.get(0);
        Log.d(TAG, "testWordGroup_add_result:" + wordGroup.toString());

    }
}
