package com.englishlearn.myapplication.data.source.remote.bmob;

import com.englishlearn.myapplication.data.source.DataSource;

/**
 * Created by yanzl on 16-9-18.
 */
public interface RemoteData extends DataSource{

    /**
     * 注册用户
     * @param bmobUser
     * @return
     */
    BmobUser register(BmobUser bmobUser);

    /**
     * 登陆
     * @param bmobUser
     * @return
     */
    BmobUser login(BmobUser bmobUser);


    /**
     * 更新
     * @param bmobUpdateUserRequest
     * @return
     */
    BmobUser update(BmobUpdateUserRequest bmobUpdateUserRequest);


}
