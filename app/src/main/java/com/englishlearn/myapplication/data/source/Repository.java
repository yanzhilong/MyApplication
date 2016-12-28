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
import com.englishlearn.myapplication.data.BmobFile;
import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsVoice;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateCollectGroup;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.TractateGroupCollect;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.WordGroupCollect;
import com.englishlearn.myapplication.data.source.local.LocalData;
import com.englishlearn.myapplication.data.source.preferences.SharedPreferencesData;
import com.englishlearn.myapplication.data.source.remote.RemoteData;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDataSource;
import com.englishlearn.myapplication.util.RxUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class Repository implements DataSource,RemoteData,LocalData,SharedPreferencesData {

    private static Repository INSTANCE = null;

    private final RemoteData mRemoteDataSource;

    private final LocalData mLocalDataSource;

    private final RemoteData mBmobDataSource;

    private final SharedPreferencesData mSharedPreferencesData;



    boolean mCacheIsDirty = false;

    private Repository(RemoteData remoteDataSource,
                       LocalData localDataSource,
                       SharedPreferencesData sharedPreferencesData) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        mSharedPreferencesData = sharedPreferencesData;
        mBmobDataSource = BmobDataSource.getInstance();
    }

    public static Repository getInstance(RemoteData remoteData,
                                         LocalData localData,
                                         SharedPreferencesData sharedPreferencesData) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteData, localData,sharedPreferencesData);
        }
        return INSTANCE;
    }

    /**
     * 用于测试
     * @return
     */
    public Observable<List<String>> getTestString(int page,int pageSize){

        final int limit = pageSize;
        final int skip = (page) * pageSize;

        List<String> list = new ArrayList<>();
        for(int i = skip; i < skip + limit; i++){
            list.add("test" + i);
        }
        return Observable.just(list);
    }

    /**
     * 用于测试
     * @return
     */
    public Observable<List<String>> getTestString(){

        List<String> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            list.add("test" + i);
        }
        return Observable.just(list);
    }
    
    @Override
    public Observable<User> register(User user) {
        return mBmobDataSource.register(user);
    }

    @Override
    public Observable<User> createOrLoginUserByPhoneRx(String phone, String smscode) {
        return mBmobDataSource.createOrLoginUserByPhoneRx(phone,smscode);
    }

    @Override
    public Observable<Boolean> checkUserName(String name) {
        return mBmobDataSource.checkUserName(name);
    }

    @Override
    public Observable<Boolean> checkEmail(String email) {
        return mBmobDataSource.checkEmail(email);
    }

    @Override
    public Observable<Boolean> checkMobile(String mobile) {
        return mBmobDataSource.checkMobile(mobile);
    }

    @Override
    public Observable<User> getUserByIdRx(String id) {
        return mBmobDataSource.getUserByIdRx(id);
    }

    @Override
    public Observable<User> getUserByName(String name) {
        return mBmobDataSource.getUserByName(name);
    }

    @Override
    public Observable<User> getUserByEmail(String email) {
        return mBmobDataSource.getUserByEmail(email);
    }

    @Override
    public Observable<User> getUserByMobile(String mobile) {
        return mBmobDataSource.getUserByMobile(mobile);
    }

    @Override
    public Observable<User> login(String name, String password) {
        return mBmobDataSource.login(name,password);
    }

    @Override
    public Observable<User> updateUser(User user) {
        return mBmobDataSource.updateUser(user);
    }

    @Override
    public Observable<Boolean> pwdResetByEmail(String email) {
        return mBmobDataSource.pwdResetByEmail(email);
    }

    @Override
    public Observable<Boolean> pwdResetByMobile(String smsCode, String newpwd) {
        return mBmobDataSource.pwdResetByMobile(smsCode,newpwd);
    }

    @Override
    public Observable<Boolean> pwdResetByOldPwd(String sessionToken, String objectId, String oldPwd, String newPwd) {
        return mBmobDataSource.pwdResetByOldPwd(sessionToken,objectId,oldPwd,newPwd);
    }

    @Override
    public Observable<String> requestSmsCode(String phone) {
        return mBmobDataSource.requestSmsCode(phone);
    }

    @Override
    public Observable<Boolean> emailVerify(String email) {
        return mBmobDataSource.emailVerify(email);
    }

    @Override
    public Observable<Boolean> smsCodeVerify(String smsCode, String mobile) {
        return mBmobDataSource.smsCodeVerify(smsCode,mobile);
    }

    @Override
    public Observable<PhoneticsSymbols> addPhoneticsSymbols(PhoneticsSymbols phoneticsSymbols) {
        return null;
    }

    @Override
    public Observable<Boolean> deletePhoneticsSymbolsById(String phoneticsSymbolsId) {
        return null;
    }

    @Override
    public Observable<Boolean> updatePhoneticsSymbolsRxById(PhoneticsSymbols phoneticsSymbols) {
        return null;
    }

    @Override
    public Observable<PhoneticsSymbols> getPhoneticsSymbolsRxById(String phoneticsSymbolsId) {
        return null;
    }

    @Override
    public Observable<List<PhoneticsSymbols>> getPhoneticsSymbolsRx() {
        return mBmobDataSource.getPhoneticsSymbolsRx();
    }

    @Override
    public Observable<PhoneticsVoice> addPhoneticsSymbolsVoice(PhoneticsVoice phoneticsSymbolsVoice) {
        return null;
    }

    @Override
    public Observable<Boolean> deletePhoneticsSymbolsVoiceById(String phoneticsSymbolsVoiceId) {
        return null;
    }

    @Override
    public Observable<Boolean> updatePhoneticsSymbolsVoiceRxById(PhoneticsVoice phoneticsSymbolsVoice) {
        return null;
    }

    @Override
    public Observable<PhoneticsVoice> getPhoneticsSymbolsVoiceRxById(String phoneticsSymbolsVoiceId) {
        return null;
    }

    @Override
    public Observable<List<PhoneticsVoice>> getPhoneticsSymbolsVoicesRx(String phoneticsSymbolsId) {
        return null;
    }

    @Override
    public Observable<TractateType> addTractateType(TractateType tractateType) {
        return mBmobDataSource.addTractateType(tractateType);
    }

    @Override
    public Observable<Boolean> deleteTractateTypeById(String tractateTypeId) {
        return mBmobDataSource.deleteTractateTypeById(tractateTypeId);
    }

    @Override
    public Observable<Boolean> updateTractateTypeRxById(TractateType tractateType) {
        return mBmobDataSource.updateTractateTypeRxById(tractateType);
    }

    @Override
    public Observable<TractateType> getTractateTypeRxById(String tractateTypeId) {
        return mBmobDataSource.getTractateTypeRxById(tractateTypeId);
    }

    @Override
    public Observable<List<TractateType>> getTractateTypesRx() {
        return mBmobDataSource.getTractateTypesRx();
    }

    @Override
    public Observable<Word> addWord(Word word) {
        return mBmobDataSource.addWord(word).compose(RxUtil.<Word>applySchedulers());
    }

    @Override
    public Observable<Boolean> addWords(List<Word> words) {
        return mBmobDataSource.addWords(words).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> addWordByYouDao(String wordName) {
        return mBmobDataSource.addWordByYouDao(wordName).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> addWordByIciba(String wordName) {
        return mBmobDataSource.addWordByIciba(wordName).compose(RxUtil.<Boolean>applySchedulers());
    }

    public void addWordByHtml(String wordName) {

        mBmobDataSource.addWordByIciba(wordName).compose(RxUtil.<Boolean>applySchedulers())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
        mBmobDataSource.addWordByYouDao(wordName).compose(RxUtil.<Boolean>applySchedulers())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    @Override
    public Observable<Boolean> deleteWordById(String wordId) {
        return mBmobDataSource.deleteWordById(wordId);
    }

    @Override
    public Observable<Boolean> deleteWords(List<Word> words) {
        return mBmobDataSource.deleteWords(words).compose(RxUtil.<Boolean>applySchedulers());
    }

    @Override
    public Observable<Boolean> updateWordRxById(Word word) {
        return mBmobDataSource.updateWordRxById(word);
    }

    @Override
    public Observable<Word> getWordRxById(String wordId) {
        return mBmobDataSource.getWordRxById(wordId);
    }

    @Override
    public Observable<Word> getWordRxByYouDao(String wordname) {
        return mBmobDataSource.getWordRxByYouDao(wordname).compose(RxUtil.<Word>applySchedulers());
    }

    @Override
    public Observable<Word> getWordRxByIciba(String wordname) {
        return mBmobDataSource.getWordRxByIciba(wordname).compose(RxUtil.<Word>applySchedulers());
    }

    @Override
    public Observable<Word> getWordRxByName(String name) {
        return mBmobDataSource.getWordRxByName(name);
    }

    @Override
    public Observable<List<Word>> getWordsRxByPhoneticsId(String phoneticsId) {
        return mBmobDataSource.getWordsRxByPhoneticsId(phoneticsId);
    }

    @Override
    public Observable<List<Word>> getWordsRx() {
        return mBmobDataSource.getWordsRx();
    }

    @Override
    public Observable<List<Word>> getWordsRx(int page, int pageSize) {
        return mBmobDataSource.getWordsRx(page,pageSize);
    }

    @Override
    public Observable<List<Word>> getWordsRxUpdatedAtBefore(int page, int pageSize, Date date) {
        return mBmobDataSource.getWordsRxUpdatedAtBefore(page,pageSize,date);
    }

    @Override
    public Observable<List<Word>> getWordsRxByWordGroupId(String wordgroupId, int page, int pageSize) {
        return mBmobDataSource.getWordsRxByWordGroupId(wordgroupId,page,pageSize);
    }

    @Override
    public Observable<Sentence> addSentence(Sentence sentence) {
        return mBmobDataSource.addSentence(sentence);
    }

    @Override
    public Observable<Boolean> deleteSentenceById(String sentenceId) {
        return mBmobDataSource.deleteSentenceById(sentenceId);
    }

    @Override
    public Observable<Boolean> deleteSentences(List<Sentence> sentences) {
        return mBmobDataSource.deleteSentences(sentences);
    }

    @Override
    public Observable<Boolean> updateSentenceById(Sentence sentence) {
        return mBmobDataSource.updateSentenceById(sentence);
    }

    @Override
    public Observable<Sentence> getSentenceById(String sentenceId) {
        return mBmobDataSource.getSentenceById(sentenceId);
    }

    @Override
    public Observable<List<Sentence>> getSentences(int page, int pageSize) {
        return mBmobDataSource.getSentences(page,pageSize);
    }

    @Override
    public Observable<List<Sentence>> getSentences(String serachWord, int page, int pageSize) {
        return mBmobDataSource.getSentences(serachWord,page,pageSize);
    }

    @Override
    public Observable<List<Sentence>> getSentencesRxBySentenceGroupId(String sentencegroupId, int page, int pageSize) {
        return mBmobDataSource.getSentencesRxBySentenceGroupId(sentencegroupId,page,pageSize);
    }

    @Override
    public Observable<List<Sentence>> getSentencesRxBySentenceCollectGroupId(String sentencecollectgroupId, int page, int pageSize) {
        return mBmobDataSource.getSentencesRxBySentenceCollectGroupId(sentencecollectgroupId,page,pageSize);
    }

    @Override
    public Observable<Grammar> addGrammar(Grammar grammar) {
        return mBmobDataSource.addGrammar(grammar);
    }

    @Override
    public Observable<Boolean> deleteGrammarById(String grammarId) {
        return mBmobDataSource.deleteGrammarById(grammarId);
    }

    @Override
    public Observable<Boolean> updateGrammarRxById(Grammar grammar) {
        return mBmobDataSource.updateGrammarRxById(grammar);
    }

    @Override
    public Observable<Grammar> getGrammarById(String grammarId) {
        return mBmobDataSource.getGrammarById(grammarId);
    }

    @Override
    public Observable<List<Grammar>> getGrammars() {
        return mBmobDataSource.getGrammars();
    }

    @Override
    public Observable<List<Grammar>> getGrammars(int page, int pageSize) {
        return mBmobDataSource.getGrammars(page,pageSize);
    }

    @Override
    public Observable<List<Grammar>> getGrammars(String serachWord, int page, int pageSize) {
        return mBmobDataSource.getGrammars(serachWord,page,pageSize);
    }

    @Override
    public Observable<Tractate> addTractate(File tractate) {
        return mBmobDataSource.addTractate(tractate);
    }

    @Override
    public Observable<Tractate> addTractate(Tractate tractate) {
        return mBmobDataSource.addTractate(tractate);
    }

    @Override
    public Observable<Boolean> deleteTractateRxById(String tractateId) {
        return mBmobDataSource.deleteTractateTypeById(tractateId);
    }

    @Override
    public Observable<Boolean> deleteTractates(List<Tractate> tractates) {
        return mBmobDataSource.deleteTractates(tractates);
    }

    @Override
    public Observable<Boolean> updateTractateRxById(Tractate tractate) {
        return mBmobDataSource.updateTractateRxById(tractate);
    }

    @Override
    public Observable<Tractate> getTractateRxById(String tractateId) {
        return mBmobDataSource.getTractateRxById(tractateId);
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateTypeId(String tractateTypeId, int page, int pageSize) {
        return mBmobDataSource.getTractateRxByTractateTypeId(tractateTypeId,page,pageSize);
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateGroupId(String tractateGroupId, int page, int pageSize) {
        return mBmobDataSource.getTractateRxByTractateGroupId(tractateGroupId,page,pageSize);
    }

    @Override
    public Observable<List<Tractate>> getTractatesRx(String searchword, int page, int pageSize) {
        return mBmobDataSource.getTractatesRx(searchword,page,pageSize);
    }

    @Override
    public Observable<WordGroup> addWordGroup(WordGroup wordGroup) {
        return mBmobDataSource.addWordGroup(wordGroup);
    }

    @Override
    public Observable<Boolean> deleteWordGroupRxById(String wordGroupId) {
        return mBmobDataSource.deleteWordGroupRxById(wordGroupId);
    }

    @Override
    public Observable<Boolean> updateWordGroupRxById(WordGroup wordGroup) {
        return mBmobDataSource.updateWordGroupRxById(wordGroup);
    }

    @Override
    public Observable<WordGroup> getWordGroupRxById(String wordGroupId) {
        return mBmobDataSource.getWordGroupRxById(wordGroupId);
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getWordGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<WordGroup>> getCollectWordGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getCollectWordGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId) {
        return mBmobDataSource.getWordGroupRxByUserId(userId);
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenRx(int page, int pageSize) {
        return mBmobDataSource.getWordGroupsByOpenRx(page,pageSize);
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenAndNotCollectRx(String userId,int page, int pageSize) {
        return mBmobDataSource.getWordGroupsByOpenAndNotCollectRx(userId,page,pageSize);
    }

    @Override
    public Observable<WordGroupCollect> addWordGroupCollect(WordGroupCollect wordGroupCollect) {
        return mBmobDataSource.addWordGroupCollect(wordGroupCollect);
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxById(String wordGroupCollectId) {
        return mBmobDataSource.deleteWordGroupCollectRxById(wordGroupCollectId);
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxByuserIdAndwordGroupId(String userId, String wordGroupId) {
        return mBmobDataSource.deleteWordGroupCollectRxByuserIdAndwordGroupId(userId,wordGroupId);
    }

    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getWordGroupCollectRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserIdAndwordGroupId(String userId, String wordGroupId) {
        return mBmobDataSource.getWordGroupCollectRxByUserIdAndwordGroupId(userId,wordGroupId);
    }

    @Override
    public Observable<SentenceGroup> addSentenceGroup(SentenceGroup sentenceGroup) {
        return mBmobDataSource.addSentenceGroup(sentenceGroup);
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupRxById(String sentenceGroupId) {
        return mBmobDataSource.deleteSentenceGroupRxById(sentenceGroupId);
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupAndSentences(String sentenceGroupId, List<Sentence> sentences) {
        return mBmobDataSource.deleteSentenceGroupAndSentences(sentenceGroupId,sentences);
    }

    @Override
    public Observable<Boolean> updateSentenceGroupRxById(SentenceGroup sentenceGroup) {
        return mBmobDataSource.updateSentenceGroupRxById(sentenceGroup);
    }

    @Override
    public Observable<SentenceGroup> getSentenceGroupRxById(String sentenceGroupId) {
        return mBmobDataSource.getSentenceGroupRxById(sentenceGroupId);
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId) {
        return mBmobDataSource.getSentenceGroupRxByUserId(userId);
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId,boolean create, int page, int pageSize) {
        return mBmobDataSource.getSentenceGroupRxByUserId(userId,create,page,pageSize);
    }

    @Override
    public Observable<List<SentenceGroup>> getCollectSentenceGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getCollectSentenceGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenRx(int page, int pageSize) {
        return mBmobDataSource.getSentenceGroupsByOpenRx(page,pageSize);
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        return mBmobDataSource.getSentenceGroupsByOpenAndNotCollectRx(userId,page,pageSize);
    }

    @Override
    public Observable<SentenceCollectGroup> addSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup) {
        return mBmobDataSource.addSentenceCollectGroup(sentenceCollectGroup);
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectGroupRxById(String sentencecollectGroupId) {
        return mBmobDataSource.deleteSentenceCollectGroupRxById(sentencecollectGroupId);
    }

    @Override
    public Observable<Boolean> updateSentenceCollectGroupRxById(SentenceCollectGroup sentenceCollectGroup) {
        return mBmobDataSource.updateSentenceCollectGroupRxById(sentenceCollectGroup);
    }

    @Override
    public Observable<SentenceCollectGroup> getSentenceCollectGroupRxById(String sentencecollectgroupId) {
        return mBmobDataSource.getSentenceCollectGroupRxById(sentencecollectgroupId);
    }

    @Override
    public Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId) {
        return mBmobDataSource.getSentenceCollectGroupRxByUserId(userId);
    }

    @Override
    public Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getSentenceCollectGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect) {
        return mBmobDataSource.addSentenceGroupCollect(sentenceGroupCollect);
    }

    @Override
    public Observable<SentenceGroupCollect> addSentenceGroupCollectByNotSelf(SentenceGroupCollect sentenceGroupCollect) {
        return mBmobDataSource.addSentenceGroupCollectByNotSelf(sentenceGroupCollect);
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxById(String sentenceGroupCollectId) {
        return mBmobDataSource.deleteSentenceGroupCollectRxById(sentenceGroupCollectId);
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(String userId, String sentencegroupId) {
        return mBmobDataSource.deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(userId,sentencegroupId);
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getSentenceGroupCollectRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserIdAndsentencegroupId(String userId, String sentencegroupId) {
        return null;
    }

    @Override
    public Observable<TractateGroup> addTractateGroup(TractateGroup tractateGroup) {
        return mBmobDataSource.addTractateGroup(tractateGroup);
    }

    @Override
    public Observable<Boolean> deleteTractateGroupRxById(String tractateGroupId) {
        return mBmobDataSource.deleteTractateGroupRxById(tractateGroupId);
    }

    @Override
    public Observable<Boolean> updateTractateGroupRxById(TractateGroup tractateGroup) {
        return mBmobDataSource.updateTractateGroupRxById(tractateGroup);
    }

    @Override
    public Observable<TractateGroup> getTractateGroupRxById(String tractateGroupId) {
        return mBmobDataSource.getTractateGroupRxById(tractateGroupId);
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getTractateGroupsRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId) {
        return mBmobDataSource.getTractateGroupsRxByUserId(userId);
    }

    @Override
    public Observable<List<TractateGroup>> getCollectTractateGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getCollectTractateGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        return mBmobDataSource.getTractateGroupsByOpenAndNotCollectRx(userId,page,pageSize);
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsByOpenRx(int page, int pageSize) {
        return mBmobDataSource.getTractateGroupsByOpenRx(page,pageSize);
    }

    @Override
    public Observable<TractateCollectGroup> addTractateCollectGroup(TractateCollectGroup tractateCollectGroup) {
        return mBmobDataSource.addTractateCollectGroup(tractateCollectGroup);
    }

    @Override
    public Observable<Boolean> deleteTractateCollectGroupRxById(String tractateCollectGroupId) {
        return mBmobDataSource.deleteTractateCollectGroupRxById(tractateCollectGroupId);
    }

    @Override
    public Observable<Boolean> updateTractateCollectGroupRxById(TractateCollectGroup tractateCollectGroup) {
        return mBmobDataSource.updateTractateCollectGroupRxById(tractateCollectGroup);
    }

    @Override
    public Observable<TractateCollectGroup> getTractateCollectGroupRxById(String tractateCollectGroupId) {
        return mBmobDataSource.getTractateCollectGroupRxById(tractateCollectGroupId);
    }

    @Override
    public Observable<List<TractateCollectGroup>> getTractateCollectGroupRxByUserId(String userId) {
        return mBmobDataSource.getTractateCollectGroupRxByUserId(userId);
    }

    @Override
    public Observable<List<TractateCollectGroup>> getTractateCollectGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getTractateCollectGroupRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<TractateGroupCollect> addTractateGroupCollect(TractateGroupCollect tractateGroupCollect) {
        return mBmobDataSource.addTractateGroupCollect(tractateGroupCollect);
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxById(String tractateGroupCollectId) {
        return mBmobDataSource.deleteTractateGroupCollectRxById(tractateGroupCollectId);
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxByuserIdAndtractateGroupId(String userId, String sentencegroupId) {
        return mBmobDataSource.deleteTractateGroupCollectRxByuserIdAndtractateGroupId(userId,sentencegroupId);
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getTractateGroupCollectRxByUserId(userId,page,pageSize);
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserIdAndtractateGroupId(String userId, String tractategroupIdId) {
        return mBmobDataSource.getTractateGroupCollectRxByUserIdAndtractateGroupId(userId,tractategroupIdId);
    }

    @Override
    public Observable<WordCollect> addWordCollect(WordCollect wordCollect) {
        return mBmobDataSource.addWordCollect(wordCollect);
    }

    @Override
    public Observable<Boolean> deleteWordCollectRxById(String wordCollectId) {
        return mBmobDataSource.deleteWordCollectRxById(wordCollectId);
    }

    @Override
    public Observable<Boolean> deleteWordCollects(List<WordCollect> wordCollects) {
        return mBmobDataSource.deleteWordCollects(wordCollects);
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByUserIdAndWordGroupId(String userId, String wordGroupId, int page, int pageSize) {
        return mBmobDataSource.getWordCollectRxByUserIdAndWordGroupId(userId,wordGroupId,page,pageSize);
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByWordGroupId(String wordGroupId, int page, int pageSize) {
        return mBmobDataSource.getWordCollectRxByWordGroupId(wordGroupId,page,pageSize);
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollect(SentenceCollect sentenceCollect) {
        return mBmobDataSource.addSentenceCollect(sentenceCollect);
    }

    @Override
    public Observable<Boolean> addSentenceCollects(List<SentenceCollect> sentenceCollects) {
        return mBmobDataSource.addSentenceCollects(sentenceCollects);
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollectByNotSelf(SentenceCollect sentenceCollect) {
        return mBmobDataSource.addSentenceCollectByNotSelf(sentenceCollect);
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectRxById(String sentenceCollectId) {
        return mBmobDataSource.deleteSentenceCollectRxById(sentenceCollectId);
    }

    @Override
    public Observable<Boolean> deleteSentenceCollects(List<SentenceCollect> sentenceCollects) {
        return mBmobDataSource.deleteSentenceCollects(sentenceCollects);
    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceCollectGroupId(String userId, String sentenceCollectGroupId, int page, int pageSize) {
        return mBmobDataSource.getSentenceCollectRxByUserIdAndSentenceCollectGroupId(userId,sentenceCollectGroupId,page,pageSize);
    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxBySentenceCollectGroupId(String sentenceCollectGroupId, int page, int pageSize) {
        return mBmobDataSource.getSentenceCollectRxBySentenceCollectGroupId(sentenceCollectGroupId,page,pageSize);
    }

    @Override
    public Observable<TractateCollect> addTractateCollect(TractateCollect tractateCollect) {
        return mBmobDataSource.addTractateCollect(tractateCollect);
    }

    @Override
    public Observable<Boolean> addTractateCollects(List<TractateCollect> tractateCollects) {
        return mBmobDataSource.addTractateCollects(tractateCollects);
    }

    @Override
    public Observable<Boolean> deleteTractateCollectRxById(String tractateCollectId) {
        return mBmobDataSource.deleteTractateCollectRxById(tractateCollectId);
    }

    @Override
    public Observable<Boolean> deleteTractateCollects(List<TractateCollect> tractateCollects) {
        return mBmobDataSource.deleteTractateCollects(tractateCollects);
    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByUserIdAndTractateGroupId(String userId, String tractateGroupId, int page, int pageSize) {
        return mBmobDataSource.getTractateCollectRxByUserIdAndTractateGroupId(userId,tractateGroupId,page,pageSize);
    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByTractateCollectGroupId(String tractateCollectGroupId, int page, int pageSize) {
        return mBmobDataSource.getTractateCollectRxByTractateCollectGroupId(tractateCollectGroupId,page,pageSize);
    }

    @Override
    public Observable<BmobFile> uploadFile(File file,String type) {
        return mBmobDataSource.uploadFile(file,type);
    }

    @Override
    public Observable<Boolean> deleteFile(String cdnName, String url) {
        return mBmobDataSource.deleteFile(cdnName,url);
    }

    @Override
    public Observable<Boolean> downloadFile(String url, String filePath) {
        return mBmobDataSource.downloadFile(url,filePath);
    }

    @Override
    public Observable<Dict> addDict(Dict dict) {
        return mBmobDataSource.addDict(dict);
    }

    @Override
    public Observable<Boolean> deleteDictById(String dictId) {
        return mBmobDataSource.deleteDictById(dictId);
    }

    @Override
    public Observable<Boolean> updateDictRxById(Dict dict) {
        return mBmobDataSource.updateDictRxById(dict);
    }

    @Override
    public Observable<List<Dict>> getDicts() {
        return mBmobDataSource.getDicts();
    }

    @Override
    public void saveUserInfo(User user) {
        mSharedPreferencesData.saveUserInfo(user);
    }

    @Override
    public User getUserInfo() {
        return mSharedPreferencesData.getUserInfo();
    }

    @Override
    public void cleanUserInfo() {
        mSharedPreferencesData.cleanUserInfo();
    }

    @Override
    public void saveDicts(Map<String,Dict> dictsMap) {
        mSharedPreferencesData.saveDicts(dictsMap);
    }

    @Override
    public void saveDict(Dict dict) {
        mSharedPreferencesData.saveDict(dict);
    }

    @Override
    public Map<String,Dict> getDictsBySp() {
        return mSharedPreferencesData.getDictsBySp();
    }

    @Override
    public Observable<List<DownloadStatus>> getDownloadList() {
        return mLocalDataSource.getDownloadList();
    }
}
