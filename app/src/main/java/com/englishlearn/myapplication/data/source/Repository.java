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

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateGroup;
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
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

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
    public Observable<PhoneticsWords> addPhoneticsWords(PhoneticsWords phoneticsWords) {
        return null;
    }

    @Override
    public Observable<Boolean> deletePhoneticsWordsById(String phoneticsWordsId) {
        return null;
    }

    @Override
    public Observable<Boolean> updatePhoneticsWordsRxById(PhoneticsWords phoneticsWords) {
        return null;
    }

    @Override
    public Observable<PhoneticsWords> getPhoneticsWordsRxById(String phoneticsWordsId) {
        return null;
    }

    @Override
    public Observable<PhoneticsWords> getPhoneticsWordsRxByPhoneticsId(String phoneticsWordsId) {
        return mBmobDataSource.getPhoneticsWordsRxByPhoneticsId(phoneticsWordsId);
    }

    @Override
    public Observable<List<PhoneticsWords>> getPhoneticsWordsRx() {
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
        return mBmobDataSource.addWord(word);
    }

    @Override
    public Observable<Boolean> deleteWordById(String wordId) {
        return mBmobDataSource.deleteWordById(wordId);
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
    public Observable<Word> getWordRxByName(String name) {
        return mBmobDataSource.getWordRxByName(name);
    }

    @Override
    public Observable<List<Word>> getWordsRxByPhoneticsId(String phoneticsId) {
        return mBmobDataSource.getWordsRxByPhoneticsId(phoneticsId);
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
    public Observable<Tractate> addTractate(Tractate tractate) {
        return mBmobDataSource.addTractate(tractate);
    }

    @Override
    public Observable<Boolean> deleteTractateRxById(String tractateId) {
        return mBmobDataSource.deleteTractateTypeById(tractateId);
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
    public Observable<Boolean> updateSentenceGroupRxById(SentenceGroup sentenceGroup) {
        return mBmobDataSource.updateSentenceGroupRxById(sentenceGroup);
    }

    @Override
    public Observable<SentenceGroup> getSentenceGroupRxById(String sentenceGroupId) {
        return mBmobDataSource.getSentenceGroupRxById(sentenceGroupId);
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId, int page, int pageSize) {
        return mBmobDataSource.getSentenceGroupRxByUserId(userId,page,pageSize);
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
    public Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect) {
        return mBmobDataSource.addSentenceGroupCollect(sentenceGroupCollect);
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
    public Observable<WordCollect> addWordCollect(WordCollect wordCollect) {
        return mBmobDataSource.addWordCollect(wordCollect);
    }

    @Override
    public Observable<Boolean> deleteWordCollectRxById(String wordCollectId) {
        return mBmobDataSource.deleteWordCollectRxById(wordCollectId);
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByUserIdAndWordGroupId(String userId, String wordGroupId, int page, int pageSize) {
        return mBmobDataSource.getWordCollectRxByUserIdAndWordGroupId(userId,wordGroupId,page,pageSize);
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByWordGroupId(String wordGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollect(SentenceCollect sentenceCollect) {
        return mBmobDataSource.addSentenceCollect(sentenceCollect);
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectRxById(String sentenceCollectId) {
        return mBmobDataSource.deleteSentenceCollectRxById(sentenceCollectId);
    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceGroupId(String userId, String sentenceGroupId, int page, int pageSize) {
        return mBmobDataSource.getSentenceCollectRxByUserIdAndSentenceGroupId(userId,sentenceGroupId,page,pageSize);
    }

    @Override
    public Observable<TractateCollect> addTractateCollect(TractateCollect tractateCollect) {
        return mBmobDataSource.addTractateCollect(tractateCollect);
    }

    @Override
    public Observable<Boolean> deleteTractateCollectRxById(String tractateCollectId) {
        return mBmobDataSource.deleteTractateCollectRxById(tractateCollectId);
    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByUserIdAndTractateGroupId(String userId, String tractateGroupId, int page, int pageSize) {
        return mBmobDataSource.getTractateCollectRxByUserIdAndTractateGroupId(userId,tractateGroupId,page,pageSize);
    }

    @Override
    public Observable<UploadFile> uploadFile(File file) {
        return null;
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
}
