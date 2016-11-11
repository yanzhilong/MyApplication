package com.englishlearn.myapplication.StringTest;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by yanzl on 16-11-11.
 */
public class ArraysTest {

    @Test
    public void copyOfRange(){

        int[] resource = {0,1,2,3,4,5,6};
        int[] range1 = Arrays.copyOfRange(resource,0,resource.length);
        System.out.println(Arrays.toString(range1));//0,1,2.3,4,5,6

        int[] range2 = Arrays.copyOfRange(resource,1,resource.length);
        System.out.println(Arrays.toString(range2));//1,2.3,4,5,6
    }
}
