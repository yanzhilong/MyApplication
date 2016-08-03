package com.englishlearn.myapplication.data.source.remote.bmob.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class UpdateListenerFuture extends UpdateListener {

    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public UpdateListenerFuture(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(BmobException e) {
        bmobException = e;
        countDownLatch.countDown();
    }
    public boolean save() throws InterruptedException, ExecutionException, BmobException {
        countDownLatch.await();
        if(bmobException != null){
            throw bmobException;
        }
        return bmobException == null;
    }

    public BmobException getBmobException() {
        return bmobException;
    }
}
