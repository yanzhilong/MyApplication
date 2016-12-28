package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.englishlearn.myapplication.config.ApplicationConfig;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BmobFileTest {

    private static final String TAG = BmobFileTest.class.getSimpleName();
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
    public void uploadFile(){
        File file = new File(ApplicationConfig.EXTERNALBASE + File.separator + "psbase0.mp3");
        if(file.exists()){
            TestSubscriber<BmobFile> testSubscriber_deleteById = new TestSubscriber<>();
            //mBmobRemoteData.uploadFile(file,"audio/mp3").toBlocking().subscribe(testSubscriber_deleteById);
            mBmobRemoteData.uploadFile(file,"audio/mp3").toBlocking().subscribe(testSubscriber_deleteById);
            testSubscriber_deleteById.assertNoErrors();

            List<BmobFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
            BmobFile uploadFile = null;
            if(uploadFiles != null && uploadFiles.size() > 0){
                uploadFile = uploadFiles.get(0);
            }
        }
    }
}
