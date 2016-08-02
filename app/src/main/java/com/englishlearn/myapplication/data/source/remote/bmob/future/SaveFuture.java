package com.englishlearn.myapplication.data.source.remote.bmob.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class SaveFuture extends SaveListener<String> {

    private volatile String result = null;
    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public SaveFuture(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(String s, BmobException e) {
        bmobException = e;
        this.result = s;
        countDownLatch.countDown();
    }

    public BmobException getBmobException() {
        return bmobException;
    }

    public String get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }
}
