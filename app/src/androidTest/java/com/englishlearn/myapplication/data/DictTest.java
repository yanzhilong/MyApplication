package com.englishlearn.myapplication.data;

import android.content.Context;
import android.content.res.AssetManager;
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

import java.io.File;
import java.util.List;

import cn.mdict.utils.IOUtil;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DictTest {

    private static final String TAG = DictTest.class.getSimpleName();
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
     * 增加词典
     */
    @Test
    public void addDict(){

        Dict dictAdd = new Dict();
        dictAdd.setName("涛哥词典");
        dictAdd.setContent("包含涛哥所有的在线单词");
        dictAdd.setRemark("remark");
        dictAdd.setSize("9.2M");
        dictAdd.setUrl("http://");
        TestSubscriber<Dict> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addDict(dictAdd).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Dict> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list != null && list.size() >= 0){
            Dict dict = list.get(0);
            Log.d(TAG,"addDict_result:" + dict.toString());
        }

    }

    @Test
    public void copyFileTest(){
        String mdictHome = context.getFilesDir().getAbsolutePath() + "/mdict";
        String docfolder = mdictHome + "/doc";
        File file = new File(docfolder);
        AssetManager assets = context.getAssets();
        if(file.exists() && file.isDirectory()){
            IOUtil.copyAssetToFile(assets, "niu.mdx", true, docfolder, null);
            String[] files = file.list();
            for (String s : files){
                Log.d(TAG,s);
            }
        }
    }

}
