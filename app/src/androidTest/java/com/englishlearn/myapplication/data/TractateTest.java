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
     * 第一行：标题
     * 第二行：备注
     * #开头是注释
     * 每一行一句英文
     * !!!后面是翻译内容，不包括标题
     * 每一行一句中文
     * @throws IOException
     */
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

    /**
     * 添加文章，以标点符号间隔每一句
     * @throws IOException
     */
    @Test
    public void addTractateByPunctuation() throws IOException {

        InputStream inputStream = context.getResources().openRawResource(R.raw.myfather);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        List<String> english_paragraph = new ArrayList<>();//英文段落数
        List<String> chinese_paragraph = new ArrayList<>();//中文段落数

        boolean isTranslation = false;//是否是译文

        String title = bufferedReader.readLine();
        String remark = bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null){

            Log.d(TAG,line);
            if(!line.startsWith("#") && !line.equals("")){

                if(!isTranslation && !line.startsWith("!!!")){
                    Log.d(TAG,"英文:"+line);
                    english_paragraph.add(line);

                }else if(line.startsWith("!!!")){
                    //是译文
                    isTranslation = true;
                }else{
                    Log.d(TAG,"中文:"+line);
                    chinese_paragraph.add(line);

                }
            }
        }
        bufferedReader.close();
        inputStream.close();
        Log.d(TAG,"英文段落数：" + english_paragraph.size() + "" + chinese_paragraph.size());
        Log.d(TAG,"中文段落数：" + chinese_paragraph.size());
        if(english_paragraph.size() != chinese_paragraph.size()){
            Log.d(TAG,"英文和译文行数不同");
            return;
        }

        //判断每一个段落的标点符号数量
        for(int i = 0; i < english_paragraph.size(); i++){

            String englishParagraph = english_paragraph.get(i);
            String chinestParagraph = chinese_paragraph.get(i);

            String regEx = "[\\u4e00-\\u9fa5，。！、……a-zA-Z\\d]+";
            Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(englishParagraph);
            Matcher m1 = p.matcher(chinestParagraph);
            boolean rs = m.find();
            boolean rs1 = m1.find();
            Log.d(TAG,rs1 ? "中文合法" : "中文不合法");
            Log.d(TAG,rs ? "英文合法" : "英文不合法");
            Log.d(TAG,rs ? "中文合法" : "英文不合法");
            Log.d(TAG,rs ? "英文合法" : "英文不合法");
            /*int count = 0;
            while(m.find()){
                count ++;
            }
            System.out.println("ABC的个数 : " + count);*/

        }

        //判断每一个段落的标点符号数量
        for(int i = 0; i < english_paragraph.size(); i++){

            String englishParagraph = english_paragraph.get(i);
            String chinestParagraph = chinese_paragraph.get(i);

            String english = "[,.?;!]";//英文标点
            String chinese = "[，。？；！、]";//中文标点
            Pattern p = Pattern.compile(english,Pattern.CASE_INSENSITIVE);
            Pattern p1 = Pattern.compile(chinese,Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(englishParagraph);
            Matcher m1 = p1.matcher(chinestParagraph);
            int count = 0;
            int count1 = 0;
            while(m.find()){
                count ++;
            }
            while(m1.find()){
                count1 ++;
            }
            Log.d(TAG,"英文第"+i+"段的标点个数 : " + count + "chinese:" + count1);
            Log.d(TAG,"中文第"+i+"段的标点个数 : " + count1);
        }


        /*StringBuffer content = new StringBuffer();
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
        }*/
    }
}
