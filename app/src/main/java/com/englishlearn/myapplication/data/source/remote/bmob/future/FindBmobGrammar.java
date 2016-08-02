package com.englishlearn.myapplication.data.source.remote.bmob.future;

import com.englishlearn.myapplication.data.source.remote.bmob.BmobGrammar;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yanzl on 16-8-2.
 */
public class FindBmobGrammar<T> extends FindListener<BmobGrammar>{

    private volatile List<BmobGrammar> result = null;
    private BmobException bmobException;
    private final CountDownLatch countDownLatch;

    public FindBmobGrammar(){
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void done(List<BmobGrammar> list, BmobException e) {
        bmobException = e;
        this.result = list;
        countDownLatch.countDown();
    }

    public BmobException getBmobException() {
        return bmobException;
    }

    public List<BmobGrammar> get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }
}
