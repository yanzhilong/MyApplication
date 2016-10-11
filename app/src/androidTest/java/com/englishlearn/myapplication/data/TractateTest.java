package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.Constant;
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


    @Test
    public void addTractate() throws IOException {

        InputStream inputStream = context.getResources().openRawResource(R.raw.newconcept_one_lesson1);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        List<String> contentlist = new ArrayList<>();
        List<String> translationlist = new ArrayList<>();


        boolean isTranslation = false;//是否是译文

        String title = bufferedReader.readLine();
        String remark = bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null){

            Log.d(TAG,line);
            if(!line.startsWith("#") && !line.equals("")){

                if(isTranslation){
                    translationlist.add(line);


                }else if(line.startsWith("!!!")){
                    //是译文
                    isTranslation = true;
                }else{
                    contentlist.add(line);

                }
            }
        }
        bufferedReader.close();
        inputStream.close();
        if(contentlist.size() != translationlist.size()){
            Log.d(TAG,"英文和译文行数不同");
            return;
        }

        StringBuffer content = new StringBuffer();
        StringBuffer translation = new StringBuffer();

        for(String c:contentlist){
            content.append(c);
            content.append("\r\n");
        }

        for(String t:translationlist){
            translation.append(t);
            translation.append("\r\n");
        }
        //添加　
        Log.d(TAG,"testTractate_add");
        Tractate addtractate = new Tractate();
        addtractate.setTractatetypeId("880d538e1d");
        addtractate.setTitle(title);
        addtractate.setRemark(remark);
        addtractate.setUserId(Constant.userId0703);
        addtractate.setContent(content.toString());
        addtractate.setTranslation(translation.toString());

        TestSubscriber<Tractate> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractate(addtractate).toBlocking().subscribe(testSubscriber_add);
        List<Throwable> throwables = testSubscriber_add.getOnErrorEvents();
        if(throwables.size() > 0){
            Log.d(TAG,throwables.get(0).getMessage());
        }
        testSubscriber_add.assertNoErrors();
        List<Tractate> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null || list.size() > 0){
            Tractate tractate = list.get(0);
            Log.d(TAG,"testTractate_add_result:" + tractate.toString());
        }
    }
}
