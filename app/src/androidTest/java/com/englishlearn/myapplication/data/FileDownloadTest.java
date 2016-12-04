package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FileDownloadTest {

    private static final String TAG = FileDownloadTest.class.getSimpleName();
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
    public void downFileTest(){

        /*String mdictHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mdict";
        mdictHome += File.separator + "mddd.mdx";
        String downUrl = "http://bmob-cdn-6604.b0.upaiyun.com/2016/12/04/a21b68c5409f4a3a806aef3f6831d05c.mdx";
        TestSubscriber<DownloadStatus> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.downloadFile(downUrl,mdictHome).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<DownloadStatus> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        for(DownloadStatus downloadStatus:list){
            Log.d(TAG,"downFIle Currentsize:" + downloadStatus.getCurrentsizestr() + " Size:" + downloadStatus.getSizeStr() + "Percent:" + downloadStatus.getPercent());
        }*/
    }
}
