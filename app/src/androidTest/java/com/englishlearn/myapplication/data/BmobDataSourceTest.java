package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
     * 音标数据库初始化,增加音标
     */
    @Test
    public void testAddPhoneticsSymbols() throws IOException {

        List<PhoneticsSymbols> phoneticsSymbolses = new ArrayList<>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.phoneticssymbols);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean isvowel = false;

        String line;

        while ((line = bufferedReader.readLine()) != null){

            if(!line.startsWith("#") && !line.equals("")){
                if(line.contains("p")){
                    isvowel = true;
                }
                PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();
                //判断是否有美英
                String[] names = line.split("\\s+");
                String ipaname;
                String kkname;
                if(names.length > 1){
                    ipaname = names[0];
                    kkname = names[1];
                }else {
                    ipaname = line;
                    kkname = line;
                }
                phoneticsSymbols.setIpaname(ipaname);
                phoneticsSymbols.setKkname(kkname);
                phoneticsSymbols.setIsvowel(isvowel ? 1 : 0);
                phoneticsSymbolses.add(phoneticsSymbols);

            }
        }
        Log.d(TAG,phoneticsSymbolses.size()+"");
        Log.d(TAG,phoneticsSymbolses.toString());
        //48个音标
        for(PhoneticsSymbols phoneticsSymbols : phoneticsSymbolses){
            TestSubscriber<PhoneticsSymbols> testSubscriber_add = new TestSubscriber<>();
            mBmobRemoteData.addPhoneticsSymbols(phoneticsSymbols).toBlocking().subscribe(testSubscriber_add);
            testSubscriber_add.assertNoErrors();
            Thread thread = testSubscriber_add.getLastSeenThread();
            Log.d(TAG,"testPhoneticsSymbols_add_thread:" + thread.getName());
            List<PhoneticsSymbols> list = testSubscriber_add.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list == null || list.size() == 0){
                return;
            }
            PhoneticsSymbols p = list.get(0);
            Log.d(TAG,"testPhoneticsSymbols_add_result:" + p.toString());
        }
    }

    @Test
    public void testPhoneticsWords() {
        //添加　
        Log.d(TAG,"testPhoneticsWords_add");
        PhoneticsWords addphoneticsSymbols = new PhoneticsWords();
        addphoneticsSymbols.setWordgroupId("88890");
        addphoneticsSymbols.setPhoneticsSymbolsId("123");
        TestSubscriber<PhoneticsWords> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addPhoneticsWords(addphoneticsSymbols).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        Thread thread = testSubscriber_add.getLastSeenThread();
        Log.d(TAG,"testPhoneticsWords_add_thread:" + thread.getName());
        List<PhoneticsWords> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        PhoneticsWords phoneticsSymbols = list.get(0);
        Log.d(TAG,"testPhoneticsWords_add_result:" + phoneticsSymbols.toString());
        //修改
        phoneticsSymbols.setWordgroupId("8890new");
        Log.d(TAG,"testPhoneticsWords_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updatePhoneticsWordsRxById(phoneticsSymbols).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testPhoneticsWords_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testPhoneticsWords_byId");
        TestSubscriber<PhoneticsWords> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsWordsRxById(phoneticsSymbols.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<PhoneticsWords> listbyid = testSubscriber_getById.getOnNextEvents();
        PhoneticsWords mPhoneticsSymbolsById = null;
        if(listbyid != null && listbyid.size() > 0){
            mPhoneticsSymbolsById = listbyid.get(0);
        }
        Log.d(TAG,"testPhoneticsWords_byId_result:" + mPhoneticsSymbolsById.toString());

        //根据Id获取信息来源　
        Log.d(TAG,"testPhoneticsWords_byId");
        TestSubscriber<PhoneticsWords> testSubscriber_getById1 = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsWordsRxByPhoneticsId(mPhoneticsSymbolsById.getPhoneticsSymbolsId()).toBlocking().subscribe(testSubscriber_getById1);
        testSubscriber_getById.assertNoErrors();
        List<PhoneticsWords> listbyid1 = testSubscriber_getById.getOnNextEvents();
        PhoneticsWords mPhoneticsSymbolsById1 = null;
        if(listbyid != null && listbyid.size() > 0){
            mPhoneticsSymbolsById1 = listbyid1.get(0);
        }
        Log.d(TAG,"testPhoneticsWords_byId_result:" + mPhoneticsSymbolsById1.toString());



        //获取所有信息来源　
        Log.d(TAG,"testPhoneticsWords_all");
        TestSubscriber<List<PhoneticsWords>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsWordsRx().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getById.assertNoErrors();
        List<List<PhoneticsWords>> listall = testSubscriber_getall.getOnNextEvents();
        List<PhoneticsWords> msSourceall = null;
        if(listall != null && listall.size() > 0){
            msSourceall = listall.get(0);
        }
        Log.d(TAG,"testPhoneticsWords_all_resultSize:" + msSourceall.size());
        Log.d(TAG,"testPhoneticsWords_all_result:" + msSourceall.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testPhoneticsWords_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deletePhoneticsWordsById(phoneticsSymbols.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testPhoneticsWords_deleteById"+"success");
        }
    }


    @Test
    public void testPhoneticsSymbols() {
        //添加　
        Log.d(TAG,"testPhoneticsSymbols_add");
        PhoneticsSymbols addphoneticsSymbols = new PhoneticsSymbols();
        addphoneticsSymbols.setIpaname("88890");
        TestSubscriber<PhoneticsSymbols> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addPhoneticsSymbols(addphoneticsSymbols).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        Thread thread = testSubscriber_add.getLastSeenThread();
        Log.d(TAG,"testPhoneticsSymbols_add_thread:" + thread.getName());
        List<PhoneticsSymbols> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        PhoneticsSymbols phoneticsSymbols = list.get(0);
        Log.d(TAG,"testPhoneticsSymbols_add_result:" + phoneticsSymbols.toString());
        //修改
        phoneticsSymbols.setIpaname("8890new");
        Log.d(TAG,"testPhoneticsSymbols_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updatePhoneticsSymbolsRxById(phoneticsSymbols).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testPhoneticsSymbols_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testPhoneticsSymbols_byId");
        TestSubscriber<PhoneticsSymbols> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsRxById(phoneticsSymbols.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<PhoneticsSymbols> listbyid = testSubscriber_getById.getOnNextEvents();
        PhoneticsSymbols mPhoneticsSymbolsById = null;
        if(listbyid != null && listbyid.size() > 0){
            mPhoneticsSymbolsById = listbyid.get(0);
        }
        Log.d(TAG,"testPhoneticsSymbols_byId_result:" + mPhoneticsSymbolsById.toString());

        //获取所有信息来源　
        Log.d(TAG,"testPhoneticsSymbols_all");
        TestSubscriber<List<PhoneticsSymbols>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getPhoneticsSymbolsRx().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getById.assertNoErrors();
        List<List<PhoneticsSymbols>> listall = testSubscriber_getall.getOnNextEvents();
        List<PhoneticsSymbols> msSourceall = null;
        if(listall != null && listall.size() > 0){
            msSourceall = listall.get(0);
        }
        Log.d(TAG,"testPhoneticsSymbols_all_resultSize:" + msSourceall.size());
        Log.d(TAG,"testPhoneticsSymbols_all_result:" + msSourceall.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testPhoneticsSymbols_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deletePhoneticsSymbolsById(phoneticsSymbols.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testPhoneticsSymbols_deleteById"+"success");
        }
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

    //添加单词
    @Test
    public void addWord() {

        for(int i = 0; i < 10; i++){
            Word word = new Word();
            word.setName("Hello" + i);
            word.setTranslate("你好" + i);
            word.setBritish_phonogram("[hə'ləʊ; he-]");
            word.setBritish_soundurl("https://www.baidu.com/british_word"+ i +".mp3");
            word.setAmerican_phonogram("[hɛˈlo, hə-]");
            word.setBritish_soundurl("https://www.baidu.com/american_word"+ i +".mp3");
            word.setTranslate("哈罗"+i);
            word.setCorrelation("[ 复数 hellos或helloes 过去式 helloed 过去分词 helloed 现在分词 helloing ]");
            word.setRemark("备注" + i);

            TestSubscriber<Word> testSubscriber = new TestSubscriber<>();
            mBmobRemoteData.addWord(word).toBlocking().subscribe(testSubscriber);
            testSubscriber.assertNoErrors();
            List<Word> list = testSubscriber.getOnNextEvents();
            Assert.assertNotNull(list);
            if(list == null || list.size() == 0){
                return;
            }
            Word wordresult = list.get(0);
            Log.d(TAG,"testWord_add_result:" + wordresult.toString());
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
        mBmobRemoteData.getWordRxById(word.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
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
        mBmobRemoteData.deleteWordById(word.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }
    }


    @Test
    public void testSentence() {
        //添加　
        Log.d(TAG,"testSentence_add");
        Sentence addsentence = new Sentence();
        addsentence.setContent("Hello World!");
        addsentence.setTranslation("你好，世界!");

        TestSubscriber<Sentence> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentence(addsentence).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Sentence> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Sentence sentence = list.get(0);
        Log.d(TAG,"testSentence_add_result:" + sentence.toString());
        //修改
        sentence.setContent("hello word new");
        sentence.setTranslation("你好，新世界");
        Log.d(TAG,"testSentence_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateSentenceById(sentence).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testSentence_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testSentence_byId");
        TestSubscriber<Sentence> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getSentenceById(sentence.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<Sentence> listbyid = testSubscriber_getById.getOnNextEvents();
        Sentence sentence1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            sentence1ById = listbyid.get(0);
        }
        Log.d(TAG,"testSentence_byId_result:" + sentence1ById);

        //获取所有信息来源　
        Log.d(TAG,"testSentence_getAll");
        TestSubscriber<List<Sentence>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getSentences(0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<Sentence>> listall = testSubscriber_getall.getOnNextEvents();
        List<Sentence> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testSentence_getAll_result:" + sentences.toString());

        //获取所有信息来源　
        Log.d(TAG,"testSentence_getSearch");
        TestSubscriber<List<Sentence>> testSubscriber_getsearch = new TestSubscriber<>();
        mBmobRemoteData.getSentences("Hello",0,10).toBlocking().subscribe(testSubscriber_getsearch);
        testSubscriber_getsearch.assertNoErrors();
        List<List<Sentence>> onNextEvents = testSubscriber_getsearch.getOnNextEvents();
        List<Sentence> sentencelist = null;
        if(listall != null && listall.size() > 0){
            sentencelist = onNextEvents.get(0);
        }
        Log.d(TAG,"testSentence_getSearch_result:" + sentencelist.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testSentence_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteSentenceById(sentence.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }

    }

    @Test
    public void testGrammar() {
        //添加　
        Log.d(TAG,"testGrammar_add");
        Grammar addgrammar = new Grammar();
        addgrammar.setContent("Hello World!");

        TestSubscriber<Grammar> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addGrammar(addgrammar).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Grammar> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Grammar grammar = list.get(0);
        Log.d(TAG,"testGrammar_add_result:" + grammar.toString());
        //修改
        grammar.setContent("hello word new");
        Log.d(TAG,"testGrammar_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateGrammarRxById(grammar).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testGrammar_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testGrammar_byId");
        TestSubscriber<Grammar> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getGrammarById(grammar.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<Grammar> listbyid = testSubscriber_getById.getOnNextEvents();
        Grammar grammar1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            grammar1ById = listbyid.get(0);
        }
        Log.d(TAG,"testGrammar_byId_result:" + grammar1ById);

        //获取所有信息来源　
        Log.d(TAG,"testGrammar_getAll");
        TestSubscriber<List<Grammar>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getGrammars().toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<Grammar>> listall = testSubscriber_getall.getOnNextEvents();
        List<Grammar> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testGrammar_getAll_result:" + sentences.toString());

        //获取所有信息来源　
        Log.d(TAG,"testGrammar_getAll");
        TestSubscriber<List<Grammar>> testSubscriber_getallbypage = new TestSubscriber<>();
        mBmobRemoteData.getGrammars(0,10).toBlocking().subscribe(testSubscriber_getallbypage);
        testSubscriber_getallbypage.assertNoErrors();
        List<List<Grammar>> listallbypage = testSubscriber_getallbypage.getOnNextEvents();
        List<Grammar> sentencesbypage = null;
        if(listall != null && listall.size() > 0){
            sentencesbypage = listall.get(0);
        }
        Log.d(TAG,"testGrammar_getAll_result:" + sentencesbypage.toString());

        //获取所有信息来源　
        Log.d(TAG,"testGrammar_getSearch");
        TestSubscriber<List<Grammar>> testSubscriber_getsearch = new TestSubscriber<>();
        mBmobRemoteData.getGrammars("Hello",0,10).toBlocking().subscribe(testSubscriber_getsearch);
        testSubscriber_getsearch.assertNoErrors();
        List<List<Grammar>> onNextEvents = testSubscriber_getsearch.getOnNextEvents();
        List<Grammar> sentencelist = null;
        if(listall != null && listall.size() > 0){
            sentencelist = onNextEvents.get(0);
        }
        Log.d(TAG,"testGrammar_getSearch_result:" + sentencelist.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testGrammar_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteGrammarById(grammar.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }

    }


    @Test
    public void testTractate() {
        //添加　
        Log.d(TAG,"testTractate_add");
        Tractate addtractate = new Tractate();
        addtractate.setTractatetypeId("tractateTypeId000");
        addtractate.setContent("Hello World!");
        addtractate.setTranslation("你好 世界!");

        TestSubscriber<Tractate> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractate(addtractate).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<Tractate> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        Tractate tractate = list.get(0);
        Log.d(TAG,"testTractate_add_result:" + tractate.toString());
        //修改
        tractate.setContent("hello word new");
        tractate.setTranslation("你好，新世界!");
        Log.d(TAG,"testTractate_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateTractateRxById(tractate).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testTractate_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testTractate_byId");
        TestSubscriber<Tractate> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getTractateRxById(tractate.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<Tractate> listbyid = testSubscriber_getById.getOnNextEvents();
        Tractate tractate1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            tractate1ById = listbyid.get(0);
        }
        Log.d(TAG,"testTractate_byId_result:" + tractate1ById);

        //获取所有信息来源　
        Log.d(TAG,"testTractate_getAll");
        TestSubscriber<List<Tractate>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getTractateRxByTractateTypeId(tractate1ById.getTractateId(),0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<Tractate>> listall = testSubscriber_getall.getOnNextEvents();
        List<Tractate> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testTractate_getAll_result:" + sentences.toString());


        //获取所有信息来源　
        Log.d(TAG,"testTractate_getSearch");
        TestSubscriber<List<Tractate>> testSubscriber_getsearch = new TestSubscriber<>();
        mBmobRemoteData.getTractatesRx("Hello",0,10).toBlocking().subscribe(testSubscriber_getsearch);
        testSubscriber_getsearch.assertNoErrors();
        List<List<Tractate>> onNextEvents = testSubscriber_getsearch.getOnNextEvents();
        List<Tractate> sentencelist = null;
        if(listall != null && listall.size() > 0){
            sentencelist = onNextEvents.get(0);
        }
        Log.d(TAG,"testTractate_getSearch_result:" + sentencelist.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testTractate_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteTractateRxById(tractate.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateType_deleteById"+"success");
        }

    }

    @Test
    public void testWordGroup() {
        //添加　
        Log.d(TAG,"testWordGroup_add");
        WordGroup addwordGroup = new WordGroup();
        addwordGroup.setName("Defaule");
        addwordGroup.setOpen("true");
        addwordGroup.setUserId("Userid0000");

        TestSubscriber<WordGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordGroup(addwordGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        WordGroup wordGroup = list.get(0);
        Log.d(TAG,"testWordGroup_add_result:" + wordGroup.toString());

        //添加　
        Log.d(TAG,"testWordGroup_add");
        WordGroup addwordGroup1 = new WordGroup();
        addwordGroup1.setName("Defaule");
        addwordGroup1.setOpen("false");
        addwordGroup1.setUserId("Userid0000");

        TestSubscriber<WordGroup> testSubscriber_add1 = new TestSubscriber<>();
        mBmobRemoteData.addWordGroup(addwordGroup1).toBlocking().subscribe(testSubscriber_add1);
        testSubscriber_add1.assertNoErrors();
        List<WordGroup> list1 = testSubscriber_add1.getOnNextEvents();
        Assert.assertNotNull(list1);
        if(list1 == null || list1.size() == 0){
            return;
        }
        WordGroup wordGroup1 = list1.get(0);
        Log.d(TAG,"testWordGroup_add_result:" + wordGroup.toString());

        //修改
        wordGroup.setName("NetWork");
        Log.d(TAG,"testWordGroup_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateWordGroupRxById(wordGroup).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testWordGroup_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testWordGroup_byId");
        TestSubscriber<WordGroup> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupRxById(wordGroup.getObjectId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<WordGroup> listbyid = testSubscriber_getById.getOnNextEvents();
        WordGroup wordGroup1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            wordGroup1ById = listbyid.get(0);
        }
        Log.d(TAG,"testWordGroup_byId_result:" + wordGroup1ById);

        //获取所有信息来源　
        Log.d(TAG,"testWordGroup_getAll");
        TestSubscriber<List<WordGroup>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupRxByUserId(wordGroup1ById.getUserId(),0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<WordGroup>> listall = testSubscriber_getall.getOnNextEvents();
        List<WordGroup> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testWordGroup_getAll_result:" + sentences.toString());


        //获取所有信息来源　
        Log.d(TAG,"testWordGroup_getSearch");
        TestSubscriber<List<WordGroup>> testSubscriber_getsearch = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupsByOpenRx(0,10).toBlocking().subscribe(testSubscriber_getsearch);
        testSubscriber_getsearch.assertNoErrors();
        List<List<WordGroup>> onNextEvents = testSubscriber_getsearch.getOnNextEvents();
        List<WordGroup> sentencelist = null;
        if(listall != null && listall.size() > 0){
            sentencelist = onNextEvents.get(0);
        }
        Log.d(TAG,"testWordGroup_getSearch_result:" + sentencelist.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testWordGroup_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteWordGroupRxById(wordGroup.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testWordGroupType_deleteById"+"success");
        }

        //删除指定的信息来源　
        Log.d(TAG,"testWordGroup_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById1 = new TestSubscriber<>();
        mBmobRemoteData.deleteWordGroupRxById(wordGroup1.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById1);
        testSubscriber_deleteById1.assertNoErrors();
        List<Boolean> listdeleteById1 = testSubscriber_deleteById1.getOnNextEvents();
        if(listdeleteById1 != null && listdeleteById1.size() > 0){
            Log.d(TAG,"testWordGroupType_deleteById"+"success");
        }

    }


    @Test
    public void testWordGroupCollect() {
        //添加　
        Log.d(TAG,"testWordGroupCollect_add");
        WordGroupCollect addWordGroupCollect = new WordGroupCollect();
        addWordGroupCollect.setUserId("UserId0000");
        addWordGroupCollect.setWordgroupId("WordGroupId0000");

        TestSubscriber<WordGroupCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordGroupCollect(addWordGroupCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordGroupCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        WordGroupCollect wordGroupCollect = list.get(0);
        Log.d(TAG,"testWordGroupCollect_add_result:" + wordGroupCollect.toString());


        //获取所有信息来源　
        Log.d(TAG,"testWordGroupCollect_getAll");
        TestSubscriber<List<WordGroupCollect>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getWordGroupCollectRxByUserId("UserId0000",0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<WordGroupCollect>> listall = testSubscriber_getall.getOnNextEvents();
        List<WordGroupCollect> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testWordGroupCollect_getAll_result:" + sentences.toString());


        //删除指定的信息来源　
        Log.d(TAG,"testWordGroupCollect_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteWordGroupCollectRxById(wordGroupCollect.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testWordGroupCollectType_deleteById"+"success");
        }

    }

    @Test
    public void testSentenceGroup() {
        //添加　
        Log.d(TAG,"testSentenceGroup_add");
        SentenceGroup addSentenceGroup = new SentenceGroup();
        addSentenceGroup.setName("Defaule");
        addSentenceGroup.setOpen("true");
        addSentenceGroup.setUserId("Userid0000");

        TestSubscriber<SentenceGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentenceGroup(addSentenceGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<SentenceGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        SentenceGroup sentenceGroup = list.get(0);
        Log.d(TAG,"testSentenceGroup_add_result:" + sentenceGroup.toString());

        //添加　
        Log.d(TAG,"testSentenceGroup_add");
        SentenceGroup addSentenceGroup1 = new SentenceGroup();
        addSentenceGroup1.setName("Defaule");
        addSentenceGroup1.setOpen("false");
        addSentenceGroup1.setUserId("Userid0000");

        TestSubscriber<SentenceGroup> testSubscriber_add1 = new TestSubscriber<>();
        mBmobRemoteData.addSentenceGroup(addSentenceGroup1).toBlocking().subscribe(testSubscriber_add1);
        testSubscriber_add1.assertNoErrors();
        List<SentenceGroup> list1 = testSubscriber_add1.getOnNextEvents();
        Assert.assertNotNull(list1);
        if(list1 == null || list1.size() == 0){
            return;
        }
        SentenceGroup sentenceGroup1 = list1.get(0);
        Log.d(TAG,"testSentenceGroup_add_result:" + sentenceGroup1.toString());

        //修改
        sentenceGroup.setName("NetWork");
        Log.d(TAG,"testSentenceGroup_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateSentenceGroupRxById(sentenceGroup).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testSentenceGroup_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testSentenceGroup_byId");
        TestSubscriber<SentenceGroup> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getSentenceGroupRxById(sentenceGroup.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<SentenceGroup> listbyid = testSubscriber_getById.getOnNextEvents();
        SentenceGroup wordGroup1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            wordGroup1ById = listbyid.get(0);
        }
        Log.d(TAG,"testSentenceGroup_byId_result:" + wordGroup1ById);

        //获取所有信息来源　
        Log.d(TAG,"testSentenceGroup_getAll");
        TestSubscriber<List<SentenceGroup>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getSentenceGroupRxByUserId(wordGroup1ById.getUserId(),0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<SentenceGroup>> listall = testSubscriber_getall.getOnNextEvents();
        List<SentenceGroup> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testSentenceGroup_getAll_result:" + sentences.toString());


        //获取所有信息来源　
        Log.d(TAG,"testSentenceGroup_getSearch");
        TestSubscriber<List<SentenceGroup>> testSubscriber_getsearch = new TestSubscriber<>();
        mBmobRemoteData.getSentenceGroupsByOpenRx(0,10).toBlocking().subscribe(testSubscriber_getsearch);
        testSubscriber_getsearch.assertNoErrors();
        List<List<SentenceGroup>> onNextEvents = testSubscriber_getsearch.getOnNextEvents();
        List<SentenceGroup> sentencelist = null;
        if(listall != null && listall.size() > 0){
            sentencelist = onNextEvents.get(0);
        }
        Log.d(TAG,"testSentenceGroup_getSearch_result:" + sentencelist.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testSentenceGroup_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteSentenceGroupRxById(sentenceGroup.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testSentenceGroupType_deleteById"+"success");
        }

        //删除指定的信息来源　
        Log.d(TAG,"testSentenceGroup_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById1 = new TestSubscriber<>();
        mBmobRemoteData.deleteSentenceGroupRxById(sentenceGroup1.getId()).toBlocking().subscribe(testSubscriber_deleteById1);
        testSubscriber_deleteById1.assertNoErrors();
        List<Boolean> listdeleteById1 = testSubscriber_deleteById1.getOnNextEvents();
        if(listdeleteById1 != null && listdeleteById1.size() > 0){
            Log.d(TAG,"testSentenceGroupType_deleteById"+"success");
        }

    }


    @Test
    public void testSentenceGroupCollect() {
        //添加　
        Log.d(TAG,"testSentenceGroupCollect_add");
        SentenceGroupCollect addSentenceGroupCollect = new SentenceGroupCollect();
        addSentenceGroupCollect.setUserId("UserId0000");
        addSentenceGroupCollect.setSentencegroupId("SentencegroupId0000");

        TestSubscriber<SentenceGroupCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentenceGroupCollect(addSentenceGroupCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<SentenceGroupCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        SentenceGroupCollect sentenceGroupCollect = list.get(0);
        Log.d(TAG,"testSentenceGroupCollect_add_result:" + sentenceGroupCollect.toString());


        //获取所有信息来源　
        Log.d(TAG,"testSentenceGroupCollect_getAll");
        TestSubscriber<List<SentenceGroupCollect>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getSentenceGroupCollectRxByUserId("UserId0000",0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<SentenceGroupCollect>> listall = testSubscriber_getall.getOnNextEvents();
        List<SentenceGroupCollect> sentences = null;
        if(listall != null && listall.size() > 0){
            sentences = listall.get(0);
        }
        Log.d(TAG,"testSentenceGroupCollect_getAll_result:" + sentences.toString());


        //删除指定的信息来源　
        Log.d(TAG,"testSentenceGroupCollect_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteSentenceGroupCollectRxById(sentenceGroupCollect.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testSentenceGroupCollectType_deleteById"+"success");
        }

    }

    @Test
    public void testTractateGroup() {
        //添加　
        Log.d(TAG,"testTractateGroup_add");
        TractateGroup addTractateGroup = new TractateGroup();
        addTractateGroup.setName("Defaule");
        addTractateGroup.setUserId("Userid0000");

        TestSubscriber<TractateGroup> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateGroup(addTractateGroup).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateGroup> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        TractateGroup tractateGroup = list.get(0);
        Log.d(TAG,"testTractateGroup_add_result:" + tractateGroup.toString());

        //修改
        tractateGroup.setName("NetWork");
        Log.d(TAG,"testTractateGroup_update");
        TestSubscriber<Boolean> testSubscriber_update = new TestSubscriber<>();
        mBmobRemoteData.updateTractateGroupRxById(tractateGroup).toBlocking().subscribe(testSubscriber_update);
        testSubscriber_update.assertNoErrors();
        List<Boolean> listupdate = testSubscriber_update.getOnNextEvents();
        if(listupdate != null && listupdate.size() > 0){
            Log.d(TAG,"testTractateGroup_update"+"success");
        }
        //根据Id获取信息来源　
        Log.d(TAG,"testTractateGroup_byId");
        TestSubscriber<TractateGroup> testSubscriber_getById = new TestSubscriber<>();
        mBmobRemoteData.getTractateGroupRxById(tractateGroup.getId()).toBlocking().subscribe(testSubscriber_getById);
        testSubscriber_getById.assertNoErrors();
        List<TractateGroup> listbyid = testSubscriber_getById.getOnNextEvents();
        TractateGroup tractateGroup1ById = null;
        if(listbyid != null && listbyid.size() > 0){
            tractateGroup1ById = listbyid.get(0);
        }
        Log.d(TAG,"testTractateGroup_byId_result:" + tractateGroup1ById);

        //获取所有信息来源　
        Log.d(TAG,"testTractateGroup_getAll");
        TestSubscriber<List<TractateGroup>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getTractateGroupsRxByUserId(tractateGroup1ById.getUserId(),0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<TractateGroup>> listall = testSubscriber_getall.getOnNextEvents();
        List<TractateGroup> tractateGroups = null;
        if(listall != null && listall.size() > 0){
            tractateGroups = listall.get(0);
        }
        Log.d(TAG,"testTractateGroup_getAll_result:" + tractateGroups.toString());

        //删除指定的信息来源　
        Log.d(TAG,"testTractateGroup_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById1 = new TestSubscriber<>();
        mBmobRemoteData.deleteTractateGroupRxById(tractateGroup.getId()).toBlocking().subscribe(testSubscriber_deleteById1);
        testSubscriber_deleteById1.assertNoErrors();
        List<Boolean> listdeleteById1 = testSubscriber_deleteById1.getOnNextEvents();
        if(listdeleteById1 != null && listdeleteById1.size() > 0){
            Log.d(TAG,"testTractateGroupType_deleteById"+"success");
        }

    }


    @Test
    public void testWordCollect() {
        //添加　
        Log.d(TAG,"testWordCollect_add");
        WordCollect addWordCollect = new WordCollect();
        addWordCollect.setUserId("UserId0000");
        addWordCollect.setWordgroupId("WordGroupId0000");
        addWordCollect.setWordId("WordId0000");

        TestSubscriber<WordCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addWordCollect(addWordCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<WordCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        WordCollect wordCollect = list.get(0);
        Log.d(TAG,"testWordCollect_add_result:" + wordCollect.toString());


        //获取所有信息来源　
        Log.d(TAG,"testWordCollect_getAll");
        TestSubscriber<List<WordCollect>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getWordCollectRxByUserIdAndWordGroupId("UserId0000","WordGroupId0000",0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<WordCollect>> listall = testSubscriber_getall.getOnNextEvents();
        List<WordCollect> wordCollects = null;
        if(listall != null && listall.size() > 0){
            wordCollects = listall.get(0);
        }
        Log.d(TAG,"testWordCollect_getAll_result:" + wordCollects.toString());


        //删除指定的信息来源　
        Log.d(TAG,"testWordCollect_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteWordCollectRxById(wordCollect.getObjectId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testWordCollectType_deleteById"+"success");
        }

    }

    @Test
    public void testSentenceCollect() {
        //添加　
        Log.d(TAG,"testSentenceCollect_add");
        SentenceCollect addSentenceCollect = new SentenceCollect();
        addSentenceCollect.setUserId("UserId0000");
        addSentenceCollect.setSentencegroupId("SentenceGroupId0000");
        addSentenceCollect.setSentenceId("SentenceId0000");

        TestSubscriber<SentenceCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addSentenceCollect(addSentenceCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<SentenceCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        SentenceCollect sentenceCollect = list.get(0);
        Log.d(TAG,"testSentenceCollect_add_result:" + sentenceCollect.toString());


        //获取所有信息来源　
        Log.d(TAG,"testSentenceCollect_getAll");
        TestSubscriber<List<SentenceCollect>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getSentenceCollectRxByUserIdAndSentenceGroupId("UserId0000","SentenceGroupId0000",0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<SentenceCollect>> listall = testSubscriber_getall.getOnNextEvents();
        List<SentenceCollect> wordCollects = null;
        if(listall != null && listall.size() > 0){
            wordCollects = listall.get(0);
        }
        Log.d(TAG,"testSentenceCollect_getAll_result:" + wordCollects.toString());


        //删除指定的信息来源　
        Log.d(TAG,"testSentenceCollect_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteSentenceCollectRxById(sentenceCollect.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testSentenceCollectType_deleteById"+"success");
        }

    }


    @Test
    public void testTractateCollect() {
        //添加　
        Log.d(TAG,"testTractateCollect_add");
        TractateCollect addTractateCollect = new TractateCollect();
        addTractateCollect.setUserId("UserId0000");
        addTractateCollect.setTractategroupId("TractateGroupId0000");
        addTractateCollect.setTractateId("TractateId0000");

        TestSubscriber<TractateCollect> testSubscriber_add = new TestSubscriber<>();
        mBmobRemoteData.addTractateCollect(addTractateCollect).toBlocking().subscribe(testSubscriber_add);
        testSubscriber_add.assertNoErrors();
        List<TractateCollect> list = testSubscriber_add.getOnNextEvents();
        Assert.assertNotNull(list);
        if(list == null || list.size() == 0){
            return;
        }
        TractateCollect tractateCollect = list.get(0);
        Log.d(TAG,"testTractateCollect_add_result:" + tractateCollect.toString());


        //获取所有信息来源　
        Log.d(TAG,"testTractateCollect_getAll");
        TestSubscriber<List<TractateCollect>> testSubscriber_getall = new TestSubscriber<>();
        mBmobRemoteData.getTractateCollectRxByUserIdAndTractateGroupId("UserId0000","TractateGroupId0000",0,10).toBlocking().subscribe(testSubscriber_getall);
        testSubscriber_getall.assertNoErrors();
        List<List<TractateCollect>> listall = testSubscriber_getall.getOnNextEvents();
        List<TractateCollect> tractateCollects = null;
        if(listall != null && listall.size() > 0){
            tractateCollects = listall.get(0);
        }
        Log.d(TAG,"testTractateCollect_getAll_result:" + tractateCollects.toString());


        //删除指定的信息来源　
        Log.d(TAG,"testTractateCollect_deleteById");
        TestSubscriber<Boolean> testSubscriber_deleteById = new TestSubscriber<>();
        mBmobRemoteData.deleteTractateCollectRxById(tractateCollect.getId()).toBlocking().subscribe(testSubscriber_deleteById);
        testSubscriber_deleteById.assertNoErrors();
        List<Boolean> listdeleteById = testSubscriber_deleteById.getOnNextEvents();
        if(listdeleteById != null && listdeleteById.size() > 0){
            Log.d(TAG,"testTractateCollectType_deleteById"+"success");
        }

    }

    @Test
    public void testUploadFile(){

        //try {
            //AndroidUtils.newInstance(context).addFile(R.raw.install,"install.tst",true);
            File file = new File("/data/data/com.englishlearn.myapplication/files/hello.mp3");
            TestSubscriber<UploadFile> testSubscriber_deleteById = new TestSubscriber<>();
            mBmobRemoteData.uploadFile(file).toBlocking().subscribe(testSubscriber_deleteById);
            //testSubscriber_deleteById.assertNoErrors();
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/


        // create RequeymRawResource(R.raw.phoneticssymbols,"phoneticssymbols.txt");

    }

}
