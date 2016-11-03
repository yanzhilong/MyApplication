package com.englishlearn.myapplication.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanzl on 16-11-3.
 */

public class RegexBaseTest {

    @Test
    public void findString(){

        String line = "        Lesson 1   Excuse me!\n" +
                "                 对不起！\n" +
                "\n" +
                "Listen to the tape then answer this question. Whose handbag is it?";
        String regex = "(.*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);

        // 现在创建 matcher 对象
        Matcher m = pattern.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
        } else {
            System.out.println("NO MATCH");
        }

    }

}
