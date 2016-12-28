package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.config.ApplicationConfig;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;

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

import static com.englishlearn.myapplication.config.ApplicationConfig.phoneticsSymbolsArray;
import static com.englishlearn.myapplication.config.ApplicationConfig.phoneticsSymbolsTypes;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhoneticsSymbolsTest {

    private static final String TAG = PhoneticsSymbolsTest.class.getSimpleName();
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

        String line;

        while ((line = bufferedReader.readLine()) != null){

            Log.d(TAG,line);
            if(!line.startsWith("#") && !line.equals("")){
                if(line.contains("p")){
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
                WordGroup wordGroup = new WordGroup();

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

    /**
     * 音标数据库初始化,增加音标
     */
    @Test
    public void testAddPhoneticsSymbolsNew() throws IOException {

        List<PhoneticsSymbols> phoneticsSymbolses = new ArrayList<>();

        for(int i = 0; i < phoneticsSymbolsArray.length; i++){

            PhoneticsSymbols phoneticsSymbols = new PhoneticsSymbols();
            String[] names = phoneticsSymbolsArray[i];
            phoneticsSymbols.setIpaname(names[0]);
            phoneticsSymbols.setKkname(names.length > 1 ? names[1] : names[0]);
            phoneticsSymbols.setAlias(ApplicationConfig.PSBASENAME + i);
            phoneticsSymbols.setType(phoneticsSymbolsTypes[i]);
            phoneticsSymbolses.add(phoneticsSymbols);
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





}
