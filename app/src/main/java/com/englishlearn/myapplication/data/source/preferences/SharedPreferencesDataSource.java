package com.englishlearn.myapplication.data.source.preferences;

import android.content.Context;
import android.support.annotation.NonNull;

import com.englishlearn.myapplication.core.BasePreferences;
import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.User;
import com.google.gson.Gson;

/**
 * Created by yanzl on 16-10-11.
 */
public class SharedPreferencesDataSource implements SharedPreferencesData{


    private static SharedPreferencesDataSource INSTANCE;
    private BasePreferences basePreferences;

    // Prevent direct instantiation.
    private SharedPreferencesDataSource(@NonNull Context context) {
        basePreferences = BasePreferences.newInstance(context);
    }

    public static SharedPreferencesDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void saveUserInfo(User user) {

        Gson gson = new Gson();
        String userinfo = gson.toJson(user);
        basePreferences.putString("userInfo",userinfo);
    }

    @Override
    public User getUserInfo() {
        Gson gson = new Gson();
        User user = null;
        String userinfo = basePreferences.getString("userInfo","");
        if(!userinfo.equals("")){
            user = gson.fromJson(userinfo,User.class);
        }
        return user;
    }

    @Override
    public void cleanUserInfo() {
        basePreferences.remove("userInfo");
    }

    @Override
    public void saveDict(Dict dict) {
        Gson gson = new Gson();
        String udictinfo = gson.toJson(dict);
        basePreferences.putString("dictInfo",udictinfo);
    }

    @Override
    public Dict getDict() {
        Gson gson = new Gson();
        Dict dict = null;
        String udictnfo = basePreferences.getString("dictInfo","");
        if(!udictnfo.equals("")){
            dict = gson.fromJson(udictnfo,Dict.class);
        }
        return dict;
    }
}
