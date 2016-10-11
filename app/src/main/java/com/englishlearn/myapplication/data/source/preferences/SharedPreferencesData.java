package com.englishlearn.myapplication.data.source.preferences;

import com.englishlearn.myapplication.data.User;

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
}
