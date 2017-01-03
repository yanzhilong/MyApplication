package com.englishlearn.myapplication.data.source.cache;

import com.englishlearn.myapplication.core.DownloadStatus;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.source.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;

/**
 * Created by yanzl on 16-11-24.
 * 本地缓存数据仓库
 */

public class CacheDataSource implements DataSource{

    private Map<String,Object> map = new HashMap<>();

    public static CacheDataSource getInstance() {
        return InstanceHolder.instance;
    }

    private Map<String, String> stringMap = new ConcurrentHashMap<>();

    public CacheDataSource(){
    }

    @Override
    public Observable<List<DownloadStatus>> getDownloadList() {
        return null;
    }

    @Override
    public void saveSentenceGroups(String userId, List<SentenceGroup> sentenceGroups) {
        map.put(userId,sentenceGroups);
    }

    @Override
    public List<SentenceGroup> getSentenceGroupByUserId(String userId) {
        return (List<SentenceGroup>) map.get(userId);
    }


    //单例，防止垃圾回收
    static class InstanceHolder {
        final static CacheDataSource instance = new CacheDataSource();
    }
}
