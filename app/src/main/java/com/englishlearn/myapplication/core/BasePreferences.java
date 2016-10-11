package com.englishlearn.myapplication.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Created by yanzl on 16-10-11.
 */
public class BasePreferences implements SharedPreferences.Editor,SharedPreferences {

    private final String SharedPreferencesName = "LeanEnglish";
    private Context context;
    private static BasePreferences basePreferences;

    public BasePreferences(Context context) {
        this.context = context;
    }

    public static synchronized BasePreferences newInstance(Context context){
        if(basePreferences == null){
            basePreferences = new BasePreferences(context);
        }
        return basePreferences;
    }

    public void save(String key,String value){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }






    @Override
    public SharedPreferences.Editor putString(String key, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor putStringSet(String key, Set<String> values) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,values);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor putInt(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor putLong(String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor putFloat(String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor remove(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
        return editor;
    }

    @Override
    public SharedPreferences.Editor clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        return editor;
    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public void apply() {

    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getStringSet(key,defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getLong(key,defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getFloat(key,defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    @Override
    public boolean contains(String key) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }
}
