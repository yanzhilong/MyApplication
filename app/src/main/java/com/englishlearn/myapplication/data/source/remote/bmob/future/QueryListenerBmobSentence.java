package com.englishlearn.myapplication.data.source.remote.bmob.future;

import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class QueryListenerBmobSentence extends QueryListener<BmobSentence> {

    private volatile BmobSentence bmobSentence = null;
    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public QueryListenerBmobSentence(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(BmobSentence bmobSentence, BmobException e) {
        bmobException = e;
        this.bmobSentence = bmobSentence;
        countDownLatch.countDown();
    }

    public BmobException getBmobException() {
        return bmobException;
    }

    public BmobSentence get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return bmobSentence;
    }
}
