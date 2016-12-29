package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.config.ApplicationConfig;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhoneticsSymbolsVoiceTest {

    private static final String USERID = "468c3f629d";
    private static final String TAG = PhoneticsSymbolsVoiceTest.class.getSimpleName();
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
    public void testAddPhoneticsSymbolsVoice() throws IOException {

        List<PhoneticsSymbols> phoneticsSymbolses = getPhoneticsSymbolses();
        Log.d(TAG, phoneticsSymbolses.size() + "");

        File file = new File(ApplicationConfig.EXTERNALBASE + File.separator + "pbnew1");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file1 = files[i];

            String name = file1.getName();
            boolean isPb = name.contains("_");//是否是音标
            String namepb = name.substring(0, isPb ? name.indexOf("_") : name.indexOf("."));
            String wodName = name.substring(isPb ? name.indexOf("_") + 1 : 0, name.indexOf("."));

            PhoneticsVoice phoneticsVoice = new PhoneticsVoice();
            phoneticsVoice.setSymbols(isPb);
            phoneticsVoice.setName(wodName);
            phoneticsVoice.setRemark("people");
            for (PhoneticsSymbols phoneticsSymbols : phoneticsSymbolses) {

                if (phoneticsSymbols.getAlias().equals(namepb)) {
                    phoneticsVoice.setPhoneticsSymbols(phoneticsSymbols);
                }
            }
            Log.d(TAG, phoneticsVoice.toString());
            BmobFile bmobFile = uploadFile(file1);
            if(bmobFile == null){
                Log.d(TAG,"testAddPhoneticsSymbolsVoice:uploadFileFail" + file1.getName());
                continue;
            }
            phoneticsVoice.setFile(bmobFile);
            PhoneticsVoice phoneticsVoice1 = addPhoneticesVoice(phoneticsVoice);
            if(phoneticsVoice1 == null){
                Log.d(TAG,"testAddPhoneticsSymbolsVoice:addPhoneticsSymbolsVoiceFail" + phoneticsVoice.getName());
            }

        }
    }


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    private BmobFile uploadFile(File file) {

        if (file.exists()) {
            TestSubscriber<BmobFile> testSubscriber_deleteById = new TestSubscriber<>();
            mBmobRemoteData.uploadFile(file, "audio/mp3").toBlocking().subscribe(testSubscriber_deleteById);
            //testSubscriber_deleteById.assertNoErrors();
            List<Throwable> throwables = testSubscriber_deleteById.getOnErrorEvents();
            List<BmobFile> uploadFiles = testSubscriber_deleteById.getOnNextEvents();
            BmobFile uploadFile = null;
            if (uploadFiles != null && uploadFiles.size() > 0) {
                uploadFile = uploadFiles.get(0);
                return uploadFile;
            }
            Log.d(TAG,"testAddPhoneticsSymbolsVoice:reUploadFile:" + file.getName());
            return uploadFile(file);
        }else{
            Log.d(TAG,"uploadFileFail" + "file empty");
            return null;
        }
    }


    /**
     * 获得全部音标
     *
     * @return
     */
    private List<PhoneticsSymbols> getPhoneticsSymbolses() {

        TestSubscriber<List<PhoneticsSymbols>> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsRx().toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<List<PhoneticsSymbols>> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<PhoneticsSymbols> p = list.get(0);
        Log.d(TAG, "getPhoneticsSymbols_result:" + p.toString());
        return p;
    }


    private PhoneticsVoice addPhoneticesVoice(PhoneticsVoice phoneticsVoice) {
        TestSubscriber<PhoneticsVoice> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.addPhoneticsSymbolsVoice(phoneticsVoice).toBlocking().subscribe(testSubscriber_getall);
        //testSubscriber_getall.assertNoErrors();
        List<Throwable> throwables = testSubscriber_getall.getOnErrorEvents();
        List<PhoneticsVoice> listall = testSubscriber_getall.getOnNextEvents();
        PhoneticsVoice msSourceall = null;
        if (listall != null && listall.size() > 0) {
            msSourceall = listall.get(0);
            return msSourceall;
        }
        if(throwables.size() > 0){
            Log.d(TAG,"testAddPhoneticsSymbolsVoice:reAddPhone:" + phoneticsVoice.getName());
            return addPhoneticesVoice(phoneticsVoice);
        }
        return null;
    }


    /**
     * 添加音标单词分组
     */
    @Test
    public void addPhoneticsVoice() {

        PhoneticsVoice phoneticsSymbolsVoice = new PhoneticsVoice();
        phoneticsSymbolsVoice.setName("");
        BmobFile bmobFile = new BmobFile();
        bmobFile.setUrl("123");
        bmobFile.setCdn("123");
        bmobFile.setFilename("123");

        phoneticsSymbolsVoice.setFile(bmobFile);
        PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();
        phoneticsSymbols.setObjectId("123456");

        phoneticsSymbolsVoice.setPhoneticsSymbols(phoneticsSymbols);
        TestSubscriber<PhoneticsVoice> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.addPhoneticsSymbolsVoice(phoneticsSymbolsVoice).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<PhoneticsVoice> listall = testSubscriber_getall.getOnNextEvents();
        PhoneticsVoice msSourceall = null;
        if (listall != null && listall.size() > 0) {
            msSourceall = listall.get(0);
        }
        Log.d(TAG, "testPhoneticsSymbols_all_result:" + msSourceall.toString());
    }

    @Test
    public void updatePhoneticsVoice() {

        PhoneticsVoice phoneticsSymbolsVoice = new PhoneticsVoice();
        phoneticsSymbolsVoice.setName("newName");
        phoneticsSymbolsVoice.setObjectId("929b96c407");
        PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();
        phoneticsSymbols.setObjectId("123456");

        phoneticsSymbolsVoice.setPhoneticsSymbols(phoneticsSymbols);
        TestSubscriber<Boolean> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.updatePhoneticsSymbolsVoiceRxById(phoneticsSymbolsVoice).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_getall.getOnNextEvents();
        if (listdeleteById != null && listdeleteById.size() > 0) {
            Log.d(TAG, "testPhoneticsWords_deleteById" + "success");
        }
    }

    @Test
    public void getPhoneticsVoice() {

        Log.d(TAG, "testPhoneticsWords_byId");
        TestSubscriber<PhoneticsVoice> testSubscriber_getById1 = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsVoiceRxById("929b96c407").toBlocking().subscribe(testSubscriber_getById1);
        testSubscriber_getById1.assertNoErrors();
        List<PhoneticsVoice> listbyid1 = testSubscriber_getById1.getOnNextEvents();
        PhoneticsVoice mPhoneticsSymbolsById1 = listbyid1.get(0);
        Log.d(TAG, "testPhoneticsWords_byId_result:" + mPhoneticsSymbolsById1.toString());

    }



    @Test
    public void getPhoneticsSymbols() {

        TestSubscriber<List<PhoneticsVoice>> testSubscriber_getById1 = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsVoicesRx("123456").toBlocking().subscribe(testSubscriber_getById1);
        testSubscriber_getById1.assertNoErrors();
        List<List<PhoneticsVoice>> listbyid1 = testSubscriber_getById1.getOnNextEvents();
        List<PhoneticsVoice> mPhoneticsSymbolsById1 = listbyid1.get(0);
        Log.d(TAG, "testPhoneticsWords_byId_result:" + mPhoneticsSymbolsById1.toString());

    }


    @Test
    public void deletePhoneticsVoice() {

        Log.d(TAG, "testPhoneticsWords_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deletePhoneticsSymbolsVoiceById("6f4939ce67").toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if (listdeleteById != null && listdeleteById.size() > 0) {
            Log.d(TAG, "testPhoneticsWords_deleteById" + "success");
        }
    }


    @Test
    public void realName() {

        File file = new File(ApplicationConfig.EXTERNALBASE + File.separator + "pbnew");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File[] files1 = files[i].listFiles();
            for (int j = 0; j < files1.length; j++) {
                File file1 = files1[j];
                String name = file1.getName().substring(0, (file1.getName().lastIndexOf(".") == -1 ? file1.getName().length() : file1.getName().lastIndexOf(".")));
                if (name.equals(files[i].getName())) {
                    Log.d(TAG, "音标：" + name);
                } else {
                    Log.d(TAG, "单词：" + name);
                    realName(files[i].getName(), file1);
                }
            }
        }
    }


    private void realName(String basename, File file) {
        String rootPath = file.getParent();
        System.out.println("根路径是：" + rootPath);
        File newFile = new File(rootPath + File.separator + basename + "_" + file.getName());
        file.renameTo(newFile);
    }
}
