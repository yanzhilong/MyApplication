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
public class PhoneticsWordsTest {

    private static final String USERID = "468c3f629d";
    private static final String TAG = PhoneticsWordsTest.class.getSimpleName();
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


    /**
     * 添加音标单词分组
     */
    @Test
    public void addPhoneticsWords(){

        //查询所有音标
        Log.d(TAG,"testPhoneticsSymbols_all");
        TestSubscriber<List<PhoneticsSymbols>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsRx().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<PhoneticsSymbols>> listall = testSubscriber_getall.getOnNextEvents();
        List<PhoneticsSymbols> msSourceall = null;
        if(listall != null && listall.size() > 0){
            msSourceall = listall.get(0);
        }
        Log.d(TAG,"testPhoneticsSymbols_all_resultSize:" + msSourceall.size());
        Log.d(TAG,"testPhoneticsSymbols_all_result:" + msSourceall.toString());

        //获得所有的音标单词分组
        //获得管理用户的所有收藏分组
        Log.d(TAG,"testWordGroup_getAll");
        TestSubscriber<List<WordGroup>> testSubscriber_getWordGroups = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupRxByUserId(USERID,0,100).toBlocking().subscribe(testSubscriber_getWordGroups);
        testSubscriber_getWordGroups.assertNoErrors();
        List<List<WordGroup>> wordgroups = testSubscriber_getWordGroups.getOnNextEvents();
        List<WordGroup> wordGroups = null;
        if(listall != null && wordgroups.size() > 0){
            wordGroups = wordgroups.get(0);
        }
        Log.d(TAG,"testWordGroup_getAll_resultSize:" + wordGroups.size());
        Log.d(TAG,"testWordGroup_getAll_result:" + wordGroups.toString());

        //过滤分组是音标的
        for(WordGroup wordGroup:wordGroups) {
            if (!wordGroup.getName().contains("示例单词")) {
                continue;
            }
            for(PhoneticsSymbols phoneticsSymbols : msSourceall){
                if(wordGroup.getName().contains(phoneticsSymbols.getIpaname())){
                    //添加音标相关的单词分组
                    Log.d(TAG,"testPhoneticsWords_add");
                    PhoneticsWords addphoneticsSymbols = new PhoneticsWords();
                    addphoneticsSymbols.setWordgroupId(wordGroup.getObjectId());
                    addphoneticsSymbols.setPhoneticsSymbolsId(phoneticsSymbols.getObjectId());
                    TestSubscriber<PhoneticsWords> testSubscriber_add = new TestSubscriber<>();
                    mBmobRemoteData.addPhoneticsWords(addphoneticsSymbols).toBlocking().subscribe(testSubscriber_add);
                    testSubscriber_add.assertNoErrors();
                    Thread thread = testSubscriber_add.getLastSeenThread();
                    Log.d(TAG,"testPhoneticsWords_add_thread:" + thread.getName());
                    List<PhoneticsWords> list = testSubscriber_add.getOnNextEvents();
                    Assert.assertNotNull(list);
                    if(list == null || list.size() == 0){
                        return;
                    }
                    PhoneticsWords phoneticsSymbol = list.get(0);
                    Log.d(TAG,"testPhoneticsWords_add_result:" + phoneticsSymbol.toString());
                    break;
                }
            }
        }

        //
    }

}
