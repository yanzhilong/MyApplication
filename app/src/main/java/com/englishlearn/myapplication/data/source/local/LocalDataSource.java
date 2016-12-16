/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.englishlearn.myapplication.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.englishlearn.myapplication.core.DownloadStatus;
import com.englishlearn.myapplication.core.DownloadUtil;
import com.englishlearn.myapplication.util.RxUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalDataSource implements LocalData {

    private final static String TAG = LocalDataSource.class.getSimpleName();

    private static LocalDataSource INSTANCE;

    private DbHelper mDbHelper;

    private Context mContext;



    // Prevent direct instantiation.
    private LocalDataSource(@NonNull Context context) {
        mDbHelper = new DbHelper(context);
        mContext = context;
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public Observable<List<DownloadStatus>> getDownloadList() {
        return Observable.just(true).flatMap(new Func1<Boolean, Observable<List<DownloadStatus>>>() {
            @Override
            public Observable<List<DownloadStatus>> call(Boolean aBoolean) {
                List<DownloadStatus> downloadStatusList = DownloadUtil.newInstance(mContext).getAllDownloadList();
                Log.d(TAG,"getAllDownloadList:" + Thread.currentThread().getName());
                return Observable.just(downloadStatusList);
            }
        }).compose(RxUtil.<List<DownloadStatus>>applySchedulers());
    }
}
