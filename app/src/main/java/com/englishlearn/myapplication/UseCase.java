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

package com.englishlearn.myapplication;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Use cases are the entry points to the domain layer.
 *
 * @param <T> the request type
 * @param <P> the response type
 */
public abstract class UseCase<T , P extends UseCase.Params> {


    protected abstract Observable<T> execute(P p);

    /**
     * 在子线程执行
     * @return
     */
    public Observable<T> excuteIo(final P p){
        return Observable.just("")
                .observeOn(Schedulers.io()) // 指定 Subscriber发生在 IO 线程
                .flatMap(new Func1<String, Observable<T>>() {
                    @Override
                    public Observable call(String s) {
                        return execute(p);
                    }
                }).observeOn(AndroidSchedulers.mainThread()); // 指定 Subscriber 的回调发生在主线程;
    }

    /**
     * 在子线程执行
     * @return
     */
    public Observable<T> excuteMain(final P p){
        return Observable.just("")
                .flatMap(new Func1<String, Observable<T>>() {
                    @Override
                    public Observable call(String s) {
                        return execute(p);
                    }
                }); // 指定 Subscriber 的回调发生在主线程;
    }

    /**
     * Data passed to a request.
     */
    public interface Params {
    }

}
