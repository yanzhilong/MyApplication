package com.englishlearn.myapplication.data.source.remote;

import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.DataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobCreateUserResult;

import rx.Observable;

/**
 * Created by yanzl on 16-9-18.
 */
public interface RemoteData extends DataSource{

    /**
     * 注册用户
     * @param user
     * @return
     */
    Observable<BmobCreateUserResult> register(User user);

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
}
