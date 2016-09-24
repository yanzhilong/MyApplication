package com.englishlearn.myapplication.data;

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
 * Created by yanzl on 16-9-22.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BmobDataSourceTest {

    private static final String TAG = BmobDataSourceTest.class.getSimpleName();
    private RemoteData mBmobRemoteData;

    @Before
    public void setup() {
        mBmobRemoteData = BmobDataSource.getInstance();
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
    public void testMsSource() {
        //添加　
        Log.d(TAG,"testMsSource_add");
        MsSource addMsSource = new MsSource();
        addMsSource.setName("88890");
        TestSubscriber<MsSource> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addMssource(addMsSource).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        Thread thread = testSubscriber_add.getLastSeenThread();
        Log.d(TAG,"testMsSource_add_thread:" + thread.getName());
        List<MsSource> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        MsSource msSource = list.get(0);
        Log.d(TAG,"testMsSource_add_result:" + msSource.toString());
        //修改
        msSource.setName("8890");
        Log.d(TAG,"testMsSource_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateMssourceRxById(msSource).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testMsSource_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testMsSource_byId");
        TestSubscriber<MsSource> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getMssourceRxById(msSource.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<MsSource> listbyid = testSubscriber_getById.getOnNextEvents();
        MsSource msSourceById = null;
        if(listbyid != null && listbyid.size() > 0){
            msSourceById = listbyid.get(0);
        }
        Log.d(TAG,"testMsSource_byId_result:" + msSourceById.toString());

        //获取所有信息来源　
        Log.d(TAG,"testMsSource_all");
        TestSubscriber<List<MsSource>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getMssourcesRx().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getById.assertNoErrors();
        List<List<MsSource>> listall = testSubscriber_getall.getOnNextEvents();
        List<MsSource> msSourceall = null;
        if(listall != null && listall.size() > 0){
            msSourceall = listall.get(0);
        }
        Log.d(TAG,"testMsSource_all_resultSize:" + msSourceall.size());
        Log.d(TAG,"testMsSource_all_result:" + msSourceall.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testMsSource_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteMssourceById(msSource.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testMsSource_deleteById"+"success");
        }
    }

    @Test
    public void testTractateType() {
        //添加　
        Log.d(TAG,"testTractateType_add");
        TractateType addTractateType = new TractateType();
        addTractateType.setName("88890");
        TestSubscriber<TractateType> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateType(addTractateType).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateType> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        TractateType tractateType = list.get(0);
        Log.d(TAG,"testTractateType_add_result:" + tractateType.toString());
        //修改
        tractateType.setName("8890");
        Log.d(TAG,"testTractateType_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateTractateTypeRxById(tractateType).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testTractateType_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testTractateType_byId");
        TestSubscriber<TractateType> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getTractateTypeRxById(tractateType.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<TractateType> listbyid = testSubscriber_getById.getOnNextEvents();
        TractateType tractateTypeById = null;
        if(listbyid != null && listbyid.size() > 0){
            tractateTypeById = listbyid.get(0);
        }
        Log.d(TAG,"testTractateType_byId_result:" + tractateTypeById.toString());

        //获取所有信息来源　
        Log.d(TAG,"testTractateType_all");
        TestSubscriber<List<TractateType>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getTractateTypesRx().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getById.assertNoErrors();
        List<List<TractateType>> listall = testSubscriber_getall.getOnNextEvents();
        List<TractateType> tractateTypeall = null;
        if(listall != null && listall.size() > 0){
            tractateTypeall = listall.get(0);
        }
        Log.d(TAG,"testTractateType_all_resultSize:" + tractateTypeall.size());
        Log.d(TAG,"testTractateType_all_result:" + tractateTypeall.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testTractateType_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteTractateTypeById(tractateType.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }
    }

    @Test
    public void testWord() {
        //添加　
        Log.d(TAG,"ttestWord_add");
        Word addword = new Word();
        addword.setName("hello");
        addword.setAmerican_soundurl("http:");
        addword.setRemark("remark");
        addword.setTranslate("你好");
        addword.setAmerican_phonogram("American_phonogram");

        TestSubscriber<Word> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWord(addword).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Word> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Word word = list.get(0);
        Log.d(TAG,"testWord_add_result:" + word.toString());
        //修改
        word.setName("hello_new");
        Log.d(TAG,"testWord_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateWordRxById(word).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testWord_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testWord_byId");
        TestSubscriber<Word> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getWordRxById(word.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<Word> listbyid = testSubscriber_getById.getOnNextEvents();
        Word word1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            word1ById = listbyid.get(0);
        }
        Log.d(TAG,"testWord_byId_result:" + word1ById);

        //获取所有信息来源　
        Log.d(TAG,"testWord_byName");
        TestSubscriber<Word> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getWordRxByName(word.getName()).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<Word> listall = testSubscriber_getall.getOnNextEvents();
        Word wordByName = null;
        if(listall != null && listall.size() > 0){
            wordByName = listall.get(0);
        }
        Log.d(TAG,"testWord_byName_result:" + wordByName.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testWord_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteWordById(word.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }
    }
}
