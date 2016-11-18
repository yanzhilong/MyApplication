package com.englishlearn.myapplication.data.source.remote;

import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsWords;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
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
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.UploadFile;

import java.io.File;
import java.util.List;

import rx.Observable;

/**
 * Created by yanzl on 16-9-18.
 */
public interface RemoteData extends DataSource{

    //用户模块
    //*****************************************************************************
    /**
     * 注册用户
     * @param user
     * @return
     */
    Observable<User> register(User user);

    /**
     * 创建或登陆用户
     * @param phone
     * @param smscode
     * @return
     */
    Observable<User> createOrLoginUserByPhoneRx(String phone,String smscode);

    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
    Observable<Boolean> checkUserName(String name);

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    Observable<Boolean> checkEmail(String email);

    /**
     * 判断手机号是否注册
     * @param mobile
     * @return
     */
    Observable<Boolean> checkMobile(String mobile);

    /**
     * 搜索用户
     * @param id
     * @return
     */
    Observable<User> getUserByIdRx(String id);

    /**
     * 搜索用户
     * @param name
     * @return
     */
    Observable<User>getUserByName(String name);

    /**
     * 搜索用户
     * @param email
     * @return
     */
    Observable<User>getUserByEmail(String email);

    /**
     * 搜索用户
     * @param mobile
     * @return
     */
    Observable<User>getUserByMobile(String mobile);

    /**
     * 登陆用户
     * @param name 用户名，邮箱，手机号
     * @param password 密码
     * @return
     */
    Observable<User> login(String name,String password);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Observable<User> updateUser(User user);


    /**
     * 发送邮件重置密码
     * @param email
     * @return
     */
    Observable<Boolean> pwdResetByEmail(String email);


    /**
     * 修改密码通过验证码
     * @param smsCode 验证码
     * @param newpwd　新密码
     * @return
     */
    Observable<Boolean> pwdResetByMobile(String smsCode,String newpwd);


    /**
     * 修改密码通过旧密码
     * @param sessionToken
     * @param objectId
     * @param oldPwd
     * @param newPwd
     * @return
     */
    Observable<Boolean> pwdResetByOldPwd(String sessionToken,String objectId,String oldPwd,String newPwd);


    /**
     * 请求短信验证码
     * @param phone 电话号码
     * @return
     */
    Observable<String> requestSmsCode(String phone);


    /**
     * 验证邮箱
     * @param email
     * @return
     */
    Observable<Boolean> emailVerify(String email);

    /**
     * 验证短信验证码
     * @param smsCode
     * @param mobile
     * @return
     */
    Observable<Boolean> smsCodeVerify(String smsCode,String mobile);

    //音标模块
    //*****************************************************************************
    /**
     * 增加音标
     * @param phoneticsSymbols
     * @return
     */
    Observable<PhoneticsSymbols> addPhoneticsSymbols(PhoneticsSymbols phoneticsSymbols);

    /**
     * 删除音标
     * @param phoneticsSymbolsId
     * @return
     */
    Observable<Boolean> deletePhoneticsSymbolsById(String phoneticsSymbolsId);

    /**
     * 修改音标
     * @param phoneticsSymbols
     * @return
     */
    Observable<Boolean> updatePhoneticsSymbolsRxById(PhoneticsSymbols phoneticsSymbols);

    /**
     * 根据id获取音标
     * @param phoneticsSymbolsId
     * @return
     */
    Observable<PhoneticsSymbols> getPhoneticsSymbolsRxById(String phoneticsSymbolsId);


    /**
     * 获取所有音标
     * @return
     */
    Observable<List<PhoneticsSymbols>> getPhoneticsSymbolsRx();

    //音标关联单词模块
    //*****************************************************************************
    /**
     * 增加音标关联单词
     * @param phoneticsWords
     * @return
     */
    Observable<PhoneticsWords> addPhoneticsWords(PhoneticsWords phoneticsWords);

    /**
     * 删除音标关联单词
     * @param phoneticsWordsId
     * @return
     */
    Observable<Boolean> deletePhoneticsWordsById(String phoneticsWordsId);

    /**
     * 修改音标关联单词
     * @param phoneticsWords
     * @return
     */
    Observable<Boolean> updatePhoneticsWordsRxById(PhoneticsWords phoneticsWords);

    /**
     * 根据id获取音标关联单词
     * @param phoneticsWordsId
     * @return
     */
    Observable<PhoneticsWords> getPhoneticsWordsRxById(String phoneticsWordsId);


    /**
     * 根据音标id获取单词收藏分组
     * @param phoneticsWordsId
     * @return
     */
    Observable<PhoneticsWords> getPhoneticsWordsRxByPhoneticsId(String phoneticsWordsId);


    /**
     * 获取所有音标关联单词
     * @return
     */
    Observable<List<PhoneticsWords>> getPhoneticsWordsRx();


    //文章分类模块
    //*****************************************************************************

    /**
     * 增加文章分类
     * @param tractateType
     * @return
     */
    Observable<TractateType> addTractateType(TractateType tractateType);

    /**
     * 删除文章分类
     * @param tractateTypeId
     * @return
     */
    Observable<Boolean> deleteTractateTypeById(String tractateTypeId);

    /**
     * 修改文章分类
     * @param tractateType
     * @return
     */
    Observable<Boolean> updateTractateTypeRxById(TractateType tractateType);

    /**
     * 根据id获取文章分类
     * @param tractateTypeId
     * @return
     */
    Observable<TractateType> getTractateTypeRxById(String tractateTypeId);


    /**
     * 获取所有文章分类
     * @return
     */
    Observable<List<TractateType>> getTractateTypesRx();


    //单词模块
    //*****************************************************************************

    /**
     * 增加单词
     * @param word
     * @return
     */
    Observable<Word> addWord(Word word);

    /**
     * 删除单词
     * @param wordId
     * @return
     */
    Observable<Boolean> deleteWordById(String wordId);

    /**
     * 修改单词
     * @param word
     * @return
     */
    Observable<Boolean> updateWordRxById(Word word);

    /**
     * 根据id获取单词
     * @param wordId
     * @return
     */
    Observable<Word> getWordRxById(String wordId);


    /**
     * 根据名称获取单词
     * @param name
     * @return
     */
    Observable<Word> getWordRxByName(String name);


    /**
     * 根据音标id获取单词列表
     * @param phoneticsId
     * @return
     */
    Observable<List<Word>> getWordsRxByPhoneticsId(String phoneticsId);

    /**
     * 根据单词收藏分组id获取单词列表
     * @param wordgroupId
     * @return
     */
    Observable<List<Word>> getWordsRxByWordGroupId(String wordgroupId,int page,int pageSize);


    //句子模块
    //*****************************************************************************

    /**
     * 添加句子
     * @param sentence
     * @return
     */
    Observable<Sentence> addSentence(Sentence sentence);


    /**
     * 删除句子
     * @param sentenceId
     * @return
     */
    Observable<Boolean> deleteSentenceById(String sentenceId);


    /**
     * 删除多条句子
     * @param sentences
     * @return
     */
    Observable<Boolean> deleteSentences(List<Sentence> sentences);

    /**
     * 修改句子
     * @param sentence
     * @return
     */
    Observable<Boolean> updateSentenceById(Sentence sentence);

    /**
     * 根据id获取句子
     * @param sentenceId
     * @return
     */
    Observable<Sentence> getSentenceById(String sentenceId);

    /**
     * 获取所有句子(分页)
     * @return
     */
    Observable<List<Sentence>> getSentences(int page,int pageSize);

    /**
     * 获取所有匹配关键词的句子(分页)
     * @return
     */
    Observable<List<Sentence>> getSentences(String serachWord,int page,int pageSize);


    /**
     * 根据句子分组id获取句子列表
     * @param sentencegroupId
     * @return
     */
    Observable<List<Sentence>> getSentencesRxBySentenceGroupId(String sentencegroupId,int page,int pageSize);

    /**
     * 根据句子收藏分组id获取句子列表
     * @param sentencecollectgroupId
     * @return
     */
    Observable<List<Sentence>> getSentencesRxBySentenceCollectGroupId(String sentencecollectgroupId,int page,int pageSize);
    //语法模块
    //*****************************************************************************

    /**
     * 增加语法
     * @param grammar
     * @return
     */
    Observable<Grammar> addGrammar(Grammar grammar);

    /**
     * 删除语法
     * @param grammarId
     * @return
     */
    Observable<Boolean> deleteGrammarById(String grammarId);

    /**
     * 修改语法
     * @param grammar
     * @return
     */
    Observable<Boolean> updateGrammarRxById(Grammar grammar);

    /**
     * 根据id获取单语法
     * @param grammarId
     * @return
     */
    Observable<Grammar> getGrammarById(String grammarId);

    /**
     * 获取所有语法
     * @return
     */
    Observable<List<Grammar>> getGrammars();

    /**
     * 获取所有语法(分页)
     * @return
     */
    Observable<List<Grammar>> getGrammars(int page,int pageSize);

    /**
     * 获取所有匹配关键词的语法(分页)
     * @return
     */
    Observable<List<Grammar>> getGrammars(String serachWord,int page,int pageSize);


    //文章模块
    //*****************************************************************************

    /**
     * 增加文章
     * @param tractate
     * @return
     */
    Observable<Tractate> addTractate(File tractate);


    /**
     * 增加文章
     * @param tractate
     * @return
     */
    Observable<Tractate> addTractate(Tractate tractate);

    /**
     * 删除文章
     * @param tractateId
     * @return
     */
    Observable<Boolean> deleteTractateRxById(String tractateId);

    /**
     * 修改文章
     * @param tractate
     * @return
     */
    Observable<Boolean> updateTractateRxById(Tractate tractate);

    /**
     * 根据id获取文章
     * @param tractateId
     * @return
     */
    Observable<Tractate> getTractateRxById(String tractateId);


    /**
     * 根据分类id获取文章列表分页展示,已经收藏的不展示，分级是开放的不展示
     * 按收藏人数排序
     * @param tractateTypeId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<Tractate>> getTractateRxByTractateTypeId(String tractateTypeId, int page,int pageSize);



    /**
     * 根据收藏分組id获取文章列表分页展示
     * @param tractateGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<Tractate>> getTractateRxByTractateGroupId(String tractateGroupId,int page,int pageSize);


    /**
     * 根据分类id,关键词的正则,获取文章列表分页展示
     * @param searchword
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<Tractate>> getTractatesRx(String searchword,int page,int pageSize);


    //单词收藏分组模块
    //*****************************************************************************

    /**
     * 增加单词收藏分组
     * @param wordGroup
     * @return
     */
    Observable<WordGroup> addWordGroup(WordGroup wordGroup);

    /**
     * 删除单词收藏分组
     * @param wordGroupId
     * @return
     */
    Observable<Boolean> deleteWordGroupRxById(String wordGroupId);

    /**
     * 修改单词收藏分组
     * @param wordGroup
     * @return
     */
    Observable<Boolean> updateWordGroupRxById(WordGroup wordGroup);

    /**
     * 根据id获取单词收藏分组
     * @param wordGroupId
     * @return
     */
    Observable<WordGroup> getWordGroupRxById(String wordGroupId);

    /**
     * 根据userId获取单词收藏分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordGroup>> getWordGroupRxByUserId(String userId,int page,int pageSize);


    /**
     * 根据userId获取所收藏的单词分組
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordGroup>> getCollectWordGroupRxByUserId(String userId,int page,int pageSize);

    /**
     * 根据userId获取单词收藏分组分页展示
     * @param userId
     * @return
     */
    Observable<List<WordGroup>> getWordGroupRxByUserId(String userId);

    /**
     * 获取所有公开的单词分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordGroup>> getWordGroupsByOpenRx(int page,int pageSize);


    /**
     * 获取所有公开的未收藏的单词分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordGroup>> getWordGroupsByOpenAndNotCollectRx(String userId,int page,int pageSize);



    //单词分組收藏模块
    //*****************************************************************************

    /**
     * 增加单词分組收藏
     * @param wordGroupCollect
     * @return
     */
    Observable<WordGroupCollect> addWordGroupCollect(WordGroupCollect wordGroupCollect);

    /**
     * 删除单词分組收藏
     * @param wordGroupCollectId
     * @return
     */
    Observable<Boolean> deleteWordGroupCollectRxById(String wordGroupCollectId);


    /**
     * 删除单词分組收藏
     * @param userId
     * @param wordGroupId
     * @return
     */
    Observable<Boolean> deleteWordGroupCollectRxByuserIdAndwordGroupId(String userId,String wordGroupId);


    /**
     * 根据userId获取单词分组收藏分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserId(String userId,int page,int pageSize);


    /**
     * 根据userId和wordGroupId获取单词分组收藏分页展示
     * @param userId
     * @param wordGroupId
     * @return
     */
    Observable<List<WordGroupCollect>> getWordGroupCollectRxByUserIdAndwordGroupId(String userId,String wordGroupId);

    //句子分组模块
    //*****************************************************************************

    /**
     * 增加句子分组
     * @param sentenceGroup
     * @return
     */
    Observable<SentenceGroup> addSentenceGroup(SentenceGroup sentenceGroup);

    /**
     * 删除句子分组
     * @param sentenceGroupId
     * @return
     */
    Observable<Boolean> deleteSentenceGroupRxById(String sentenceGroupId);

    /**
     * 删除句子分组,及分组下面的句子组
     * @param sentenceGroupId
     * @return
     */
    Observable<Boolean> deleteSentenceGroupAndSentences(String sentenceGroupId,List<Sentence> sentences);

    /**
     * 修改句子分组
     * @param sentenceGroup
     * @return
     */
    Observable<Boolean> updateSentenceGroupRxById(SentenceGroup sentenceGroup);

    /**
     * 根据id获取句子分组
     * @param sentenceGroupId
     * @return
     */
    Observable<SentenceGroup> getSentenceGroupRxById(String sentenceGroupId);

    /**
     * 根据userId获取句子分组分页展示
     * @param userId
     * @return
     */
    Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId);

    /**
     * 根据userId获取句子分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceGroup>> getSentenceGroupRxByUserId(String userId,boolean create, int page, int pageSize);



    /**
     * 获取userId的句子分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceGroup>> getCollectSentenceGroupRxByUserId(String userId, int page, int pageSize);


    /**
     * 获取所有公开的句子分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceGroup>> getSentenceGroupsByOpenRx(int page,int pageSize);


    /**
     * 获取所有公开的未收藏的短语分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceGroup>> getSentenceGroupsByOpenAndNotCollectRx(String userId,int page,int pageSize);


    //句子收藏分组模块
    //*****************************************************************************

    /**
     * 增加句子收藏分组
     * @param sentenceCollectGroup
     * @return
     */
    Observable<SentenceCollectGroup> addSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup);

    /**
     * 删除句子收藏分组
     * @param sentencecollectGroupId
     * @return
     */
    Observable<Boolean> deleteSentenceCollectGroupRxById(String sentencecollectGroupId);

    /**
     * 修改句子收藏分组
     * @param sentenceCollectGroup
     * @return
     */
    Observable<Boolean> updateSentenceCollectGroupRxById(SentenceCollectGroup sentenceCollectGroup);

    /**
     * 根据id获取句子收藏分组
     * @param sentencecollectgroupId
     * @return
     */
    Observable<SentenceCollectGroup> getSentenceCollectGroupRxById(String sentencecollectgroupId);

    /**
     * 根据userId获取句子收藏分组
     * @param userId
     * @return
     */
    Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId);

    /**
     * 根据userId获取句子收藏分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceCollectGroup>> getSentenceCollectGroupRxByUserId(String userId, int page, int pageSize);


    //句子分組收藏模块
    //*****************************************************************************

    /**
     * 增加句子分組收藏
     * @param sentenceGroupCollect
     * @return
     */
    Observable<SentenceGroupCollect> addSentenceGroupCollect(SentenceGroupCollect sentenceGroupCollect);

    /**
     * 增加句子分組收藏,这个句子不是
     * @param sentenceGroupCollect
     * @return
     */
    Observable<SentenceGroupCollect> addSentenceGroupCollectByNotSelf(SentenceGroupCollect sentenceGroupCollect);

    /**
     * 删除句子分組收藏
     * @param sentenceGroupCollectId
     * @return
     */
    Observable<Boolean> deleteSentenceGroupCollectRxById(String sentenceGroupCollectId);


    /**
     * 根据userId和分组收藏Id删除句子分組收藏
     * @param userId
     * @param sentencegroupId
     * @return
     */
    Observable<Boolean> deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(String userId, String sentencegroupId);


    /**
     * 根据userId获取句子分组收藏分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserId(String userId,int page,int pageSize);


    /**
     * 根据userId和句子分组id获取句子分组收藏分页展示(一般是只返回一个的)
     * @param userId
     * @param sentencegroupId
     * @return
     */
    Observable<List<SentenceGroupCollect>> getSentenceGroupCollectRxByUserIdAndsentencegroupId(String userId, String sentencegroupId);



    //文章收藏分组模块
    //*****************************************************************************

    /**
     * 增加文章收藏分组
     * @param tractateGroup
     * @return
     */
    Observable<TractateGroup> addTractateGroup(TractateGroup tractateGroup);

    /**
     * 删除文章收藏分组
     * @param tractateGroupId
     * @return
     */
    Observable<Boolean> deleteTractateGroupRxById(String tractateGroupId);

    /**
     * 修改文章收藏分组
     * @param tractateGroup
     * @return
     */
    Observable<Boolean> updateTractateGroupRxById(TractateGroup tractateGroup);

    /**
     * 根据id获取文章收藏分组
     * @param tractateGroupId
     * @return
     */
    Observable<TractateGroup> getTractateGroupRxById(String tractateGroupId);

    /**
     * 根据userId获取文章收藏分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId, int page, int pageSize);

    /**
     * 根据userId获取文章收藏分组
     * @param userId
     * @return
     */
    Observable<List<TractateGroup>> getTractateGroupsRxByUserId(String userId);


    /**
     * 根据userId获取所收藏的文章分组分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateGroup>> getCollectTractateGroupRxByUserId(String userId, int page, int pageSize);


    /**
     * 获取所有公开的未收藏的文章分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateGroup>> getTractateGroupsByOpenAndNotCollectRx(String userId,int page,int pageSize);


    /**
     * 获取所有公开的未收藏的文章分组分页展示,按时间降序
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateGroup>> getTractateGroupsByOpenRx(int page,int pageSize);


    //文章分組收藏模块
    //*****************************************************************************

    /**
     * 增加文章分組收藏
     * @param tractateGroupCollect
     * @return
     */
    Observable<TractateGroupCollect> addTractateGroupCollect(TractateGroupCollect tractateGroupCollect);

    /**
     * 删除文章分組收藏
     * @param tractateGroupCollectId
     * @return
     */
    Observable<Boolean> deleteTractateGroupCollectRxById(String tractateGroupCollectId);


    /**
     * 根据userId和分组收藏Id删除文章分組收藏
     * @param userId
     * @param sentencegroupId
     * @return
     */
    Observable<Boolean> deleteTractateGroupCollectRxByuserIdAndtractateGroupId(String userId, String sentencegroupId);


    /**
     * 根据userId获取文章分组收藏分页展示
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserId(String userId,int page,int pageSize);


    /**
     * 根据userId和句子分组id获取文章分组收藏分页展示(一般是只返回一个的)
     * @param userId
     * @param tractategroupIdId
     * @return
     */
    Observable<List<TractateGroupCollect>> getTractateGroupCollectRxByUserIdAndtractateGroupId(String userId, String tractategroupIdId);



    //单词收藏模块
    //*****************************************************************************

    /**
     * 增加单词分組收藏
     * @param wordCollect
     * @return
     */
    Observable<WordCollect> addWordCollect(WordCollect wordCollect);

    /**
     * 删除单词分組收藏
     * @param wordCollectId
     * @return
     */
    Observable<Boolean> deleteWordCollectRxById(String wordCollectId);


    /**
     * 根据userId获取单词分组收藏分页展示
     * @param userId
     * @param wordGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordCollect>> getWordCollectRxByUserIdAndWordGroupId(String userId,String wordGroupId,int page,int pageSize);


    /**
     * 根据收藏分组Id获取单词分组收藏分页展示
     * @param wordGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<WordCollect>> getWordCollectRxByWordGroupId(String wordGroupId,int page,int pageSize);


    //句子收藏模块
    //*****************************************************************************

    /**
     * 增加句子收藏
     * @param sentenceCollect
     * @return
     */
    Observable<SentenceCollect> addSentenceCollect(SentenceCollect sentenceCollect);

    /**
     * 增加句子收藏(多条)
     * @param sentenceCollects
     * @return
     */
    Observable<Boolean> addSentenceCollects(List<SentenceCollect> sentenceCollects);

    /**
     * 增加句子收藏
     * @param sentenceCollect
     * @return
     */
    Observable<SentenceCollect> addSentenceCollectByNotSelf(SentenceCollect sentenceCollect);


    /**
     * 删除句子收藏
     * @param sentenceCollectId
     * @return
     */
    Observable<Boolean> deleteSentenceCollectRxById(String sentenceCollectId);

    /**
     * 删除多条句子收藏
     * @param sentenceCollects
     * @return
     */
    Observable<Boolean> deleteSentenceCollects(List<SentenceCollect> sentenceCollects);


    /**
     * 根据userId和句子分組Id获取句子收藏，分页
     * @param userId
     * @param sentenceCollectGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceCollect>> getSentenceCollectRxByUserIdAndSentenceCollectGroupId(String userId, String sentenceCollectGroupId, int page, int pageSize);

    /**
     * 根据userId和句子分組Id获取句子收藏，分页
     * @param sentenceCollectGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<SentenceCollect>> getSentenceCollectRxBySentenceCollectGroupId(String sentenceCollectGroupId, int page, int pageSize);


    //文章收藏模块
    //*****************************************************************************

    /**
     * 增加句子收藏
     * @param tractateCollect
     * @return
     */
    Observable<TractateCollect> addTractateCollect(TractateCollect tractateCollect);

    /**
     * 删除句子收藏
     * @param tractateCollectId
     * @return
     */
    Observable<Boolean> deleteTractateCollectRxById(String tractateCollectId);


    /**
     * 根据userId和文章分組Id获取句子，分页
     * @param userId
     * @param tractateGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateCollect>> getTractateCollectRxByUserIdAndTractateGroupId(String userId,String tractateGroupId, int page, int pageSize);



    /**
     * 根据TractateCollectGroupId获取文章，分页
     * @param tractateCollectGroupId
     * @param page
     * @param pageSize
     * @return
     */
    Observable<List<TractateCollect>> getTractateCollectRxByTractateCollectGroupId(String tractateCollectGroupId, int page, int pageSize);

    //上传文件
    Observable<UploadFile> uploadFile(File file);
}
