package com.englishlearn.myapplication.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.englishlearn.myapplication.data.source.remote.bmob.service.BaiDuSearchService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.IcibaService;
import com.englishlearn.myapplication.data.source.remote.bmob.service.ServiceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-10-2.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BaiDuTest {

    private static final String TAG = BaiDuTest.class.getSimpleName();
    private BaiDuSearchService baiDuSearchService;
    private IcibaService icibaService;//百度接口
    private Context context;

    @Before
    public void setup() {
        baiDuSearchService = ServiceFactory.getInstance().createBaiDuSearchService();
        context = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void cleanUp() {
        //mLocalDataSource.deleteAllSentences();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(baiDuSearchService);
    }


    @Test
    public void getBaiduHtml() {

        TestSubscriber<Response<ResponseBody>> testSubscriber = new TestSubscriber<>();
        baiDuSearchService.getSearchListByName("java",0).toBlocking().subscribe(testSubscriber);
        //testSubscriber_add.assertNoErrors();
        List<Response<ResponseBody>> list = testSubscriber.getOnNextEvents();
        List<Throwable> throwables = testSubscriber.getOnErrorEvents();
        if(throwables != null && throwables.size() > 0){
            for(int i = 0; i < throwables.size(); i++){
                Log.d(TAG,"addWordBYYouDao_error:" + throwables.get(i).getMessage());
            }
        }
        if(list == null || list.size() == 0){
            return;
        }
        Response<ResponseBody>  responseBodyResponse = list.get(0);

        if(responseBodyResponse.isSuccessful()){
            try {
                String result = responseBodyResponse.body().string();
                Log.d(TAG,"result:" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                String result = responseBodyResponse.errorBody().string();
                Log.d(TAG,"error:" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
