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

        int lastIndex1 = "hello,world!".indexOf("ello",1);
        assertEquals(lastIndex1,1);//从1下标开始找，包含1下标
    }

    @Test
    public void substringTest(){
        String result = "hello,world!".substring(0,2);
        assertEquals(result,"he");

        //substring,后面一位可以越界一位，返回的不包括最后一位
        String result1 = "hello,world!".substring(0,"hello,world!".length());
        assertEquals(result1,"hello,world!");

        String result2 = "hello,world!".substring(0,1);
        assertEquals(result2,"h");
    }

    @Test
    public void valueofTest(){
        String result = String.valueOf("hello,world!".toCharArray(),0,2);
        assertEquals(result,"he");

        String result１ = String.valueOf("hello,world!".toCharArray(),2,"hello,world!".length() - 2);
        assertEquals(result１,"llo,world!");
    }

    @Test
    public void containsTest(){
        boolean result = "hello,world!".contains("");
        assertEquals(result,true);
        boolean result1 = (""+System.getProperty("line.separator")).contains(System.getProperty("line.separator"));
        assertEquals(result1,true);
    }

    @Test
    public void splitTest(){
        String[] result = "Hello World!".split("\\|");
        System.out.println(result[0]);
        assertEquals(result.length,1);
    }
}
