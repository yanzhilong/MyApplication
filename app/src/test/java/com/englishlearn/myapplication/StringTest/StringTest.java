package com.englishlearn.myapplication.StringTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yanzl on 16-10-18.
 */

public class StringTest {

    @Test
    public void indexOfTest(){

        int lastIndex = "hello,world!".indexOf("ello") +"ello".length();
        assertEquals(lastIndex,5);//这个下标是从豆号开始
    }

    @Test
    public void substringTest(){
        String result = "hello,world!".substring(0,2);
        assertEquals(result,"he");
    }
}
