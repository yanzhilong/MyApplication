package com.englishlearn.myapplication.regex;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.CommonTest;
import com.englishlearn.myapplication.util.AndroidUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzl on 16-11-3.
 */

public class RegexBaseTest {

    private static final String TAG = CommonTest.class.getSimpleName();
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
    public void findString(){

        String englishTitle = "";
        String chineseTitle = "";

        String resouse = AndroidUtils.newInstance(context).getStringByResource(R.raw.newconcept_one);
        //英文标题
        String entitleRegex = "\\w[\\w,\\s]*.";
        Pattern entitlepattern = Pattern.compile(entitleRegex);
        Matcher entitlematcher = entitlepattern.matcher(resouse);
        if(entitlematcher.find()){
            englishTitle = entitlematcher.group(1);
        }
        String s = "([A-Z]*|\\s)*:";
        //中文标题
        String cntitleRegex = "[\\u4E00-\\u9FA5]\\S*";
        Pattern cntitlepattern = Pattern.compile(cntitleRegex);
        Matcher cntitlematcher = cntitlepattern.matcher(resouse);
        if(cntitlematcher.find()){
            chineseTitle = cntitlematcher.group(1);
        }
    }

    @Test
    public void regexTest(){

        String resouse = "abcdefgabc";
        //中文标题
        String cntitleRegex = "(?<=abc).*";
        Pattern cntitlepattern = Pattern.compile(cntitleRegex);
        Matcher cntitlematcher = cntitlepattern.matcher(resouse);
        if(cntitlematcher.find()){
            String reresult = cntitlematcher.group(1);
            System.out.println(reresult);
        }
    }

    //查找.后面不是跟空格的，并加上空格
    @Test
    public void regexDotTest(){

        String resouse = "abcde. fg.a.中b.c";
        //中文标题
        String cntitleRegex = "\\.\\S";
        Pattern cntitlepattern = Pattern.compile(cntitleRegex);
        Matcher cntitlematcher = cntitlepattern.matcher(resouse);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(resouse);
        while(cntitlematcher.find()){
            String reresult = cntitlematcher.group();
            int start = cntitlematcher.start();
            int end = cntitlematcher.end();

            stringBuffer.insert(start," ");
            System.out.println(reresult + start + "" + end);
        }
        System.out.println(stringBuffer.toString());

        String newresouse = DotTest(resouse);
        System.out.println(newresouse);
    }

    private String DotTest(String res){

        String cntitleRegex = "\\.\\S";
        Pattern cntitlepattern = Pattern.compile(cntitleRegex);
        Matcher cntitlematcher = cntitlepattern.matcher(res);
        if(cntitlematcher.find()){
            int start = cntitlematcher.start();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(res);
            stringBuffer.insert(start + 1," ");
            return DotTest(stringBuffer.toString());
        }
        return res;
    }


}
