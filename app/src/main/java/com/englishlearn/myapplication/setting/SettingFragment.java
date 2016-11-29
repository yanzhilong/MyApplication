package com.englishlearn.myapplication.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.UriUtils;

import static android.app.Activity.RESULT_OK;


public class SettingFragment extends PreferenceFragment {

    private static final String TAG = SettingFragment.class.getSimpleName();
    private static final int FILE_SELECT_CODE = 10;
    private String[] folders;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    String newPath = UriUtils.getPath(getActivity(),uri);
                    selectFile(newPath);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //从xml文件加载选项
        /*getPreferenceManager().setSharedPreferencesName("setting");*/
        addPreferencesFromResource(R.xml.preferences);

        //自动刷卡模式
        final ListPreference mdict_custom_folder = (ListPreference)findPreference("mdict_custom_folder");//目录选择
        CheckBoxPreference mdict_custom = (CheckBoxPreference) findPreference("mdict_custom");
        mdict_custom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference mdict_custom = (CheckBoxPreference) preference;
                mdict_custom_folder.setEnabled(mdict_custom.isChecked());
                //保存值
                if(mdict_custom.isChecked()){

                }else{
                    //重置为默认
                    setDefultFolder();
                }
                return true;
            }
        });

        String mdictHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mdict";
        folders = new String[]{"默认:" + mdictHome,"选择目录(单击选择)"};
        //初始化
        if(mdict_custom_folder.getValue() == null || mdict_custom_folder.getValue().equals("")){

            mdict_custom_folder.setValue(folders[0]);
        }
        mdict_custom_folder.setEntries(folders);
        mdict_custom_folder.setEntryValues(folders);
        mdict_custom_folder.setEnabled(mdict_custom.isChecked());
        //如果是选择其它目录则设置其它的，如果不是则设置默认
        if(mdict_custom_folder.getValue().equals(folders[0])){
            mdict_custom_folder.setSummary(mdict_custom_folder.getValue());
        }else{
            String filePath = getFilePath();
            mdict_custom_folder.setSummary(filePath);
        }

        mdict_custom_folder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //判断是选择默认还是自定义
                String select = (String) newValue;
                if(select.equals(folders[0])){
                    preference.setSummary((String)newValue);
                }else{
                    showFileChooser();
                    return false;
                }
                //打开文件选择
                return true;
            }
        });
    }

    private void setDefultFolder() {
        final ListPreference mdict_custom_folder = (ListPreference)findPreference("mdict_custom_folder");//目录选择
        mdict_custom_folder.setValue(folders[0]);
        mdict_custom_folder.setSummary(folders[0]);
        //清除保存在其它地方的文件地址
        clearFilePat();
    }

    //打开文件选择器
    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.getActivity(), "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile(String filePath){
        final ListPreference mdict_custom_folder = (ListPreference)findPreference("mdict_custom_folder");//目录选择
        mdict_custom_folder.setValue(folders[1]);
        mdict_custom_folder.setSummary(filePath);
        //保存文件目录到其它地方
        saveFilePath(filePath);
    }

    static String fileP;
    //保存文件目录到其它地方
    private void saveFilePath(String filePath){
        fileP = filePath;
    }

    //保存文件目录到其它地方
    private String getFilePath(){
        return fileP;
    }

    private void clearFilePat(){
        fileP = "";
    }

}
