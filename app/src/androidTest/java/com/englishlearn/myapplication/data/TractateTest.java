package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.tractategroup.addtractate.AddTractateHelper;
import com.englishlearn.myapplication.tractategroup.addtractate.TractateLegalException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TractateTest {

    private static final String TAG = TractateTest.class.getSimpleName();
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
     * 添加双语文章
     * 第一行：英文标题
     * 第二行：中文标题
     * 第三行：备注
     * 一段英文
     * 空一行
     * 一段中文
     * 空一行
     * 一段英文
     * ....
     * @throws IOException
     */
    @Test
    public void addTractate() throws IOException {


    }

    @Test
    public void getTractate(){

        AddTractateHelper addTractateHelper = new AddTractateHelper(context);
        Tractate tractate = null;
        try {
            tractate = addTractateHelper.getTractateByRaw(R.raw.abundleofsticks);
        } catch (TractateLegalException e) {
            e.printStackTrace();
        }
        Log.d(TAG,tractate.toString());
    }
}
