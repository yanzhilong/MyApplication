package com.englishlearn.myapplication.data.source.remote.bmob.future;

import com.englishlearn.myapplication.data.source.remote.bmob.BmobSentence;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class FindListenerBmobSentence extends FindListener<BmobSentence>{

    private volatile List<BmobSentence> result = null;
    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public FindListenerBmobSentence(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(List<BmobSentence> list, BmobException e) {
        bmobException = e;
        this.result = list;
        countDownLatch.countDown();
    }

    public BmobException getBmobException() {
        return bmobException;
    }

    public List<BmobSentence> get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }
}
