package com.englishlearn.myapplication.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.englishlearn.myapplication.data.source.local.LocalDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created by yanzl on 16-7-24.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LocalDataSourceTest {

    private static final String CONTENT1 = "content1";

    private static final String CONTENT2 = "content2";

    private static final String CONTENT3 = "content3";

    private LocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        mLocalDataSource = mLocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanUp() {
        //mLocalDataSource.deleteAllSentences();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveSentences_retrievesSentences() {
        // Given a new Sentences
        /*final Sentence sentence = new Sentence(CONTENT1, "",null);

        // When saved into the persistent repository
        mLocalDataSource.addSentence(sentence);

        // Then the task can be retrieved from the persistent repository
        Sentence resultsentence = mLocalDataSource.getSentences(sentence.getContent()).get(0);
        System.out.println(sentence);
        System.out.println(resultsentence);

        assertEquals(resultsentence.getContent(),sentence.getContent());*/

    }



}
