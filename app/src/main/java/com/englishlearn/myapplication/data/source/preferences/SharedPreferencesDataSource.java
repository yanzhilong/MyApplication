package com.englishlearn.myapplication.data.source.preferences;

import android.content.Context;
import android.support.annotation.NonNull;

import com.englishlearn.myapplication.core.BasePreferences;
import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import static com.englishlearn.myapplication.R.raw.dicts;

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
        basePreferences.remove("userInfos");
    }

    @Override
    public void saveDicts(Map<String,Dict> dictsMap) {
        Gson gson = new Gson();
        String dictsstr = gson.toJson(dicts);
        basePreferences.putString("dicts",dictsstr);
    }

    @Override
    public void saveDict(Dict dict) {
        Map<String,Dict> map = getDictsBySp();
        map.put(dict.getObjectId(),dict);
        saveDicts(map);
    }

    @Override
    public Map<String,Dict> getDictsBySp() {
        Gson gson = new Gson();
        Map<String,Dict> dictsMap = null;
        String dictsstr = basePreferences.getString("dicts","");
        if(!dictsstr.equals("")){
            Type typemap = new TypeToken<Map<String, Dict>>() {
            }.getType();
            dictsMap = gson.fromJson(dictsstr, typemap);
        }
        return dictsMap;
    }
}
