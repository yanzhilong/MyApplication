package com.englishlearn.myapplication.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
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
import java.util.ArrayList;
import java.util.List;

import cn.mdict.mdx.DictEntry;
import cn.mdict.mdx.DictPref;
import cn.mdict.mdx.MDictApp;
import cn.mdict.mdx.MdxDictBase;
import cn.mdict.mdx.MdxEngine;
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
    public void addAndUpdateDict(){

        //String mdictHome = context.getFilesDir().getAbsolutePath() + "AAA/zhazhi/mdict";
        String mdictHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AAA/zhazhi/sound377.mdd";
        File file = new File(mdictHome);
       /* AssetManager assets = context.getAssets();
        if(file.exists() && file.isDirectory()){
            IOUtil.copyAssetToFile(assets, "taoge.mdx", true, mdictHome, null);
            String[] files = file.list();
            for (String s : files){
                Log.d(TAG,s);
            }
        }

        File file1 = new File(file.getAbsolutePath()+ "/taoge.mdx");
        //上传读音
*/
        TestSubscriber<BmobFile> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.uploadFile(file,"application/octet-stream").toBlocking().subscribe(testSubscriber_deleteById);
        //testSubscriber_deleteById.assertNoErrors();

        List<BmobFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
        List<Throwable> throwables = testSubscriber_deleteById.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            Throwable throwable = throwables.get(0);
            Log.d(TAG,"throwable:" + throwable.getMessage());
        }

        BmobFile uploadFile = null;
        if(uploadFiles != null && uploadFiles.size() > 0){
            uploadFile = uploadFiles.get(0);
        }
        if(uploadFile == null){
            return;
        }
        uploadFile.setPointer();





        Dict dictAdd = new Dict();
        dictAdd.setName("离线语音");
        dictAdd.setContent("非常全且清晰的离线语音");
        dictAdd.setRemark("remark");
        dictAdd.setSize("395M");
        dictAdd.setVersion(1);
        dictAdd.setFile(uploadFile);
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
            IOUtil.copyAssetToFile(assets, "taoge.mdx", true, docfolder, null);
           // IOUtil.copyAssetToFile(assets, "33html.mdx", true, docfolder, null);
            String[] files = file.list();
            for (String s : files){
                Log.d(TAG,s);
            }
        }
    }


    @Test
    public void getDicts(){
        MDictApp theApp = MDictApp.getInstance();
        MdxDictBase mainDict;

        theApp.setupAppEnv(context);
        List<DictPref> dictPrefs = new ArrayList<>();
        //得到当前目录
        DictPref dictPref = MdxEngine.getLibMgr().getRootDictPref();
        int count = dictPref.getChildCount();//当前词典的数量
        for(int i = 0; i < count; i++){
            DictPref item = dictPref.getChildDictPrefAtIndex(i);//得到某一个子项
            dictPrefs.add(item);
        }
        if(dictPrefs.size() > 0){
            //默认只有一个词典
            mainDict = new MdxDictBase();
            MdxEngine.openDictById(dictPrefs.get(0).getDictId(), mainDict);
            for (int i = 0; i < mainDict.getEntryCount(); i++){
                DictEntry dictEntry = new DictEntry(i,"",mainDict.getDictPref().getDictId());
                mainDict.getHeadword(dictEntry);
            }
        }
    }

}
