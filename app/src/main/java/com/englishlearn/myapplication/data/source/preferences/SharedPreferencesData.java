package com.englishlearn.myapplication.data.source.preferences;

import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.User;

import java.util.Map;

/**
 * Created by yanzl on 16-10-11.
 */
public interface SharedPreferencesData {

    /**
     * 登陆后保存用户信息
     * @param user
     */
    void saveUserInfo(User user);

    /**
     * 获取当前登陆用户
     * @return
     */
    User getUserInfo();

    /**
     * 退出登陆时候使用
     */
    void cleanUserInfo();

    /**
     * 保存词典版本信息
     */
    void saveDicts(Map<String,Dict> dictsMap);

    /**
     * 保存单个词典
     * @param dict
     */
    void saveDict(Dict dict);

    /**
     * 得到词典
     */
    Map<String,Dict> getDictsBySp();


}
