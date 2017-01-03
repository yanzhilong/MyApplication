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

package com.englishlearn.myapplication.data.source;


import com.englishlearn.myapplication.core.DownloadStatus;
import com.englishlearn.myapplication.data.SentenceGroup;

import java.util.List;

import rx.Observable;

/**
 * 数据接口
 */
public interface

DataSource {


    Observable<List<DownloadStatus>> getDownloadList();//获得下载列表


    /**
     * 保存句子分组列表
     * @param sentenceGroups
     * @return
     */
    void saveSentenceGroups(String userId, List<SentenceGroup> sentenceGroups);

    /**
     * 从本地获取句子分组列表
     * @param userId
     * @return
     */
    List<SentenceGroup> getSentenceGroupByUserId(String userId);
}