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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        AddTractateHelper addTractateHelper = new AddTractateHelper(context);

        File file = new File("/storage/emulated/0/AAA/new/");
        File[] files = file.listFiles();
        List<Tractate> tractates = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < files.length; i++){
            try {
                Tractate tractate = addTractateHelper.getTractateByFilePath(files[i].getPath());
                //设置排序
                String title = tractate.getTitle();
                String sortregex = "\\d+";//查找连续的数字
                Pattern cntitlepattern = Pattern.compile(sortregex);
                Matcher cntitlematcher = cntitlepattern.matcher(title);
                int sort = Integer.MAX_VALUE;
                if(cntitlematcher.find()){
                    sort = Integer.valueOf(cntitlematcher.group());
                }
                tractate.setSort(sort);
                tractates.add(tractate);
            } catch (TractateLegalException e) {
                stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch (IOException e) {
                stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch(Exception e){
                stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                e.printStackTrace();
            }
        }
        if(stringBuffer.toString().equals("")){
            addTractate(tractates);
        }else{
            Log.d(TAG,stringBuffer.toString());
        }
    }

    @Test
    public void addTractates() throws IOException {



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

    /*
   * 增加文章
   * @param tractate
   */
    private void addTractate(final List<Tractate> tractates) {

        final StringBuffer stringBufferupdateerror = new StringBuffer();
        final StringBuffer stringBuffergrouperror = new StringBuffer();

        for(int i = 0; i < tractates.size(); i++){

            Tractate addtractate = tractates.get(i);
            addtractate.setUserId("943a8a40ed");
            addtractate.setTractatetypeId("880d538e1d");
            addtractate.setOpen("true");
            TestSubscriber<Tractate> tractateTestSubscriber = new TestSubscriber<>();
            mBmobRemoteData.addTractate(addtractate).toBlocking().subscribe(tractateTestSubscriber);
            List<Tractate> list = tractateTestSubscriber.getOnNextEvents();
            if(list != null || list.size() >= 0){

                Tractate tractate = list.get(0);
                Log.d(TAG,"addtractate:" + tractate.toString());

                //添加分组
                TractateCollect addtractateCollect = new TractateCollect();
                addtractateCollect.setUserId("943a8a40ed");
                addtractateCollect.setTractateId(tractate.getObjectId());
                addtractateCollect.setTractategroupId("8c754205c4");

                TestSubscriber<TractateCollect> tractateCollectTestSubscriber = new TestSubscriber<>();
                mBmobRemoteData.addTractateCollect(addtractateCollect).toBlocking().subscribe(tractateCollectTestSubscriber);
                List<TractateCollect> tractateCollects = tractateCollectTestSubscriber.getOnNextEvents();
                if(tractateCollects != null || tractateCollects.size() >= 0){
                    TractateCollect tractateCollect = tractateCollects.get(0);
                    Log.d(TAG,"addtractatecollect:" + tractateCollect.toString());
                }else{
                    stringBuffergrouperror.append(addtractate.getTitle() + "上传失败" + System.getProperty("line.separator"));
                }
            }else {
                stringBufferupdateerror.append(addtractate.getTitle() + "上传失败" + System.getProperty("line.separator"));
            }
        }
        Log.d(TAG,"addtractate:" + stringBufferupdateerror.toString());
        Log.d(TAG,"addtractate:" + stringBuffergrouperror.toString());
    }

    @Test
    public void getTractateRxByTractateTypeIdTest(){
        TestSubscriber<List<Tractate>> tractateCollectTestSubscriber = new TestSubscriber<>();
        mBmobRemoteData.getTractateRxByTractateTypeId("880d538e1d",0,100).toBlocking().subscribe(tractateCollectTestSubscriber);
        List<List<Tractate>> tractateCollects = tractateCollectTestSubscriber.getOnNextEvents();
        if(tractateCollects != null || tractateCollects.size() >= 0){
            List<Tractate> tractates = tractateCollects.get(0);
            Log.d(TAG,"getTractateRxByTractateTypeIdTest:" + tractates.size());
        }else{
            Log.d(TAG,"getTractateRxByTractateTypeIdTest:null");
        }

    }
}
