package com.englishlearn.myapplication.data.source.remote.bmob.future;

import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammar;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class QueryListenerBmobGrammar extends QueryListener<BmobGrammar> {

    private volatile BmobGrammar bmobGrammar = null;
    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public QueryListenerBmobGrammar(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(BmobGrammar bmobGrammar, BmobException e) {
        bmobException = e;
        this.bmobGrammar = bmobGrammar;
        countDownLatch.countDown();
    }

    public BmobException getBmobException() {
        return bmobException;
    }

    public BmobGrammar get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return bmobGrammar;
    }
}
