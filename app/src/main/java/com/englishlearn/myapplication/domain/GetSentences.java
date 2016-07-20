package com.englishlearn.myapplication.domain;

import com.englishlearn.myapplication.UseCase;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

import rx.Observable;

/**
 * Created by yanzl on 16-7-20.
 */
public class GetSentences extends UseCase<Observable<List<Sentence>>,UseCase.Params> {


    @Override
    protected Observable<Observable<List<Sentence>>> execute(Params params) {
        return null;
    }

}
