
package com.englishlearn.myapplication.data.source.remote;

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
import com.englishlearn.myapplication.data.TractateGroupCollect;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.WordGroupCollect;
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * 从网络操作数据
 */
public class RemoteDataSource implements RemoteData {

    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RemoteDataSource() {}


    @Override
    public Observable<User> register(User user) {
        return null;
    }

    @Override
    public Observable<User> createOrLoginUserByPhoneRx(String phone, String smscode) {
        return null;
    }

    @Override
    public Observable<Boolean> checkUserName(String name) {
        return null;
    }

    @Override
    public Observable<Boolean> checkEmail(String email) {
        return null;
    }

    @Override
    public Observable<Boolean> checkMobile(String mobile) {
        return null;
    }

    @Override
    public Observable<User> getUserByIdRx(String id) {
        return null;
    }

    @Override
    public Observable<User> getUserByName(String name) {
        return null;
    }

    @Override
    public Observable<User> getUserByEmail(String email) {
        return null;
    }

    @Override
    public Observable<User> getUserByMobile(String mobile) {
        return null;
    }

    @Override
    public Observable<User> login(String name, String password) {
        return null;
    }

    @Override
    public Observable<User> updateUser(User user) {
        return null;
    }

    @Override
    public Observable<Boolean> pwdResetByEmail(String email) {
        return null;
    }

    @Override
    public Observable<Boolean> pwdResetByMobile(String smsCode, String newpwd) {
        return null;
    }

    @Override
    public Observable<Boolean> pwdResetByOldPwd(String sessionToken, String objectId, String oldPwd, String newPwd) {
        return null;
    }

    @Override
    public Observable<String> requestSmsCode(String phone) {
        return null;
    }

    @Override
    public Observable<Boolean> emailVerify(String email) {
        return null;
    }

    @Override
    public Observable<Boolean> smsCodeVerify(String smsCode, String mobile) {
        return null;
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
        return null;
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
        return null;
    }

    @Override
    public Observable<List<PhoneticsWords>> getPhoneticsWordsRx() {
        return null;
    }

    @Override
    public Observable<TractateType> addTractateType(TractateType tractateType) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateTypeById(String tractateTypeId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateTractateTypeRxById(TractateType tractateType) {
        return null;
    }

    @Override
    public Observable<TractateType> getTractateTypeRxById(String tractateTypeId) {
        return null;
    }

    @Override
    public Observable<List<TractateType>> getTractateTypesRx() {
        return null;
    }

    @Override
    public Observable<Word> addWord(Word word) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteWordById(String wordId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateWordRxById(Word word) {
        return null;
    }

    @Override
    public Observable<Word> getWordRxById(String wordId) {
        return null;
    }

    @Override
    public Observable<Word> getWordRxByName(String name) {
        return null;
    }

    @Override
    public Observable<List<Word>> getWordsRxByPhoneticsId(String phoneticsId) {
        return null;
    }

    @Override
    public Observable<List<Word>> getWordsRxByWordGroupId(String wordgroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<Sentence> addSentence(Sentence sentence) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSentenceById(String sentenceId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateSentenceById(Sentence sentence) {
        return null;
    }

    @Override
    public Observable<Sentence> getSentenceById(String sentenceId) {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentences(int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentences(String serachWord, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Sentence>> getSentencesRxBySentenceGroupId(String sentencegroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<Grammar> addGrammar(Grammar grammar) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteGrammarById(String wordId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateGrammarRxById(Grammar grammar) {
        return null;
    }

    @Override
    public Observable<Grammar> getGrammarById(String grammarId) {
        return null;
    }

    @Override
    public Observable<List<Grammar>> getGrammars() {
        return null;
    }

    @Override
    public Observable<List<Grammar>> getGrammars(int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Grammar>> getGrammars(String serachWord, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<Tractate> addTractate(File tractate) {
        return null;
    }

    @Override
    public Observable<Tractate> addTractate(Tractate tractate) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateRxById(String tractateId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateTractateRxById(Tractate tractate) {
        return null;
    }

    @Override
    public Observable<Tractate> getTractateRxById(String tractateId) {
        return null;
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateTypeId(String tractateTypeId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Tractate>> getTractateRxByTractateGroupId(String tractateGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<Tractate>> getTractatesRx(String searchword, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<WordGroup> addWordGroup(WordGroup wordGroup) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteWordGroupRxById(String wordGroupId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateWordGroupRxById(WordGroup wordGroup) {
        return null;
    }

    @Override
    public Observable<WordGroup> getWordGroupRxById(String wordGroupId) {
        return null;
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<WordGroup>> getCollectWordGroupRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupRxByUserId(String userId) {
        return null;
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenRx(int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<WordGroup>> getWordGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<WordGroupCollect> addWordGroupCollect(WordGroupCollect wordGroupCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxById(String wordGroupCollectId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteWordGroupCollectRxByuserIdAndwordGroupId(String userId, String wordGroupId) {
        return null;
    }

    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserIdAndwordGroupId(String userId, String wordGroupId) {
        return null;
    }

    @Override
    public Observable<SentenceGroup> addSentenceGroup(SentenceGroup sentenceGroup) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupRxById(String sentenceGroupId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateSentenceGroupRxById(SentenceGroup sentenceGroup) {
        return null;
    }

    @Override
    public Observable<SentenceGroup> getSentenceGroupRxById(String sentenceGroupId) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroup>> getCollectSentenceGroupRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenRx(int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroup>> getSentenceGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxById(String sentenceGroupCollectId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(String userId, String sentencegroupId) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserIdAndsentencegroupId(String userId, String sentencegroupId) {
        return null;
    }


    @Override
    public Observable<TractateGroup> addTractateGroup(TractateGroup tractateGroup) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateGroupRxById(String tractateGroupId) {
        return null;
    }

    @Override
    public Observable<Boolean> updateTractateGroupRxById(TractateGroup tractateGroup) {
        return null;
    }

    @Override
    public Observable<TractateGroup> getTractateGroupRxById(String tractateGroupId) {
        return null;
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId) {
        return null;
    }

    @Override
    public Observable<List<TractateGroup>> getCollectTractateGroupRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<TractateGroup>> getTractateGroupsByOpenAndNotCollectRx(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<TractateGroupCollect> addTractateGroupCollect(TractateGroupCollect tractateGroupCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxById(String tractateGroupCollectId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateGroupCollectRxByuserIdAndtractateGroupId(String userId, String sentencegroupId) {
        return null;
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserId(String userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserIdAndtractateGroupId(String userId, String tractategroupIdId) {
        return null;
    }

    @Override
    public Observable<WordCollect> addWordCollect(WordCollect wordCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteWordCollectRxById(String wordCollectId) {
        return null;
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByUserIdAndWordGroupId(String userId, String wordGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<List<WordCollect>> getWordCollectRxByWordGroupId(String wordGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<SentenceCollect> addSentenceCollect(SentenceCollect sentenceCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSentenceCollectRxById(String sentenceCollectId) {
        return null;
    }

    @Override
    public Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceGroupId(String userId, String sentenceGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<TractateCollect> addTractateCollect(TractateCollect tractateCollect) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteTractateCollectRxById(String tractateCollectId) {
        return null;
    }

    @Override
    public Observable<List<TractateCollect>> getTractateCollectRxByUserIdAndTractateGroupId(String userId, String tractateGroupId, int page, int pageSize) {
        return null;
    }

    @Override
    public Observable<UploadFile> uploadFile(File file) {
        return null;
    }
}
