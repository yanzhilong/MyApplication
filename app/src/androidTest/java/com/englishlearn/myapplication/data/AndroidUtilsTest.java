package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.englishlearn.myapplication.util.AndroidUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Created by yanzl on 16-10-26.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AndroidUtilsTest {
    private Context context;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void cleanUp() {
        //mLocalDataSource.deleteAllSentences();
    }

    @Test
    public void testPreConditions() {
    }

    @Test
    public void addFileTest(){

        String source = "hello";
        String fileName = "words";

        /*try {
            AndroidUtils.newInstance(context).deleFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            AndroidUtils.newInstance(context).appendString(fileName,source + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = "";
        try {
            result = AndroidUtils.newInstance(context).getString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(source,(result.split(System.getProperty("line.separator")))[0]);
    }
}
