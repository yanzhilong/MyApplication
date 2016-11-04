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

        String resouse = AndroidUtils.newInstance(context).getRawResource(R.raw.newconcept_one);
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

}
