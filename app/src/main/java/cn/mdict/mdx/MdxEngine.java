/*
 * Copyright (C) 2012. Rayman Zhang <raymanzhang@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.mdict.mdx;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.englishlearn.myapplication.R;
import cn.mdict.utils.IOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Class MdxEngine ...
 *
 * @author rayman
 *         Created on 11-12-31
 */
public class MdxEngine {
    static {
        System.loadLibrary("iconv");
        System.loadLibrary("mdx");
    }
    private static final String TAG = "MDict.DictView";
    private static final String dictIconDefault="content://mdict.cn/res/book.png";

    private MdxEngine() {
        fInstance = getAppInstanceN();
    }

    /**
     * Method getSettings returns preference of the application.
     *
     * @return the view preference of the application.
     */
    static public MdxEngineSetting getSettings() {
        return appSetting;
    }


    /**
     * Method getLibMgr returns the libMgr of this MdxEngine object.
     *
     * @return the libMgr (type MdxLibraryMgrRef) of this MdxEngine object.
     */
    static public MdxLibraryMgrRef getLibMgr() {
        return new MdxLibraryMgrRef(appOne.getLibMgrN());
    }

    /**
     * Method opendDictById opens a dictionary by passing the dictId and a dict object reference.
     * the pass in dict will be closed if it's a opened dict.
     *
     * @param dictId
     * @param dict
     * @return
     */
    static public int openDictById(int dictId, boolean adjustDictOrder, MdxDictBase dict) {
        int res = appOne.openDictN(dictId, "", getSettings().getAppOwner().trim(), adjustDictOrder, dict);
        if (res == MdxDictBase.kMdxSuccess) {
            MdxEngine.getSettings().setPrefLastDictId(dictId);
            dict.setDefaultIconUrl(dictIconDefault);
        }
        return res;
    }

    static public boolean openDictById(int dictId, MdxDictBase dict) {
        int res = appOne.openDictN(dictId, "", "", false, dict);
        if (res == MdxDictBase.kMdxSuccess) {
            return true;
        }
        return false;
    }

    static public int openDictByPref(DictPref dictPref, MdxDictBase dict) {
        int res = appOne.openDictByPrefN(dictPref, "", getSettings().getAppOwner().trim(), dict);
        if (res == MdxDictBase.kMdxSuccess) {
            dict.setDefaultIconUrl(dictIconDefault);
        }
        return res;
    }

    static public int openDictByName(String dictName, MdxDictBase dict) {
        DictPref dictPref = DictPref.createDictPref();
        dictPref.setDictName(dictName);
        dictPref.setDictId(1);
        return openDictByPref(dictPref, dict);
    }


    //初始化词典引擎
    static public boolean setupEnv(Context context) {
        if (appInited){
            Log.d(TAG, "Mdx nngine already inited, skip setup action");
            return true;
        }
        if (appSetting == null)
            appSetting = new MdxEngineSetting(context);
        baseContext = context;

        AssetManager assets = context.getAssets();

        String mdictHome = baseContext.getFilesDir().getAbsolutePath() + "/mdict";
       // String mdictHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mdict";
        //String mdictHome=context.getExternalFilesDir(null).getAbsolutePath();
        String resDir = mdictHome + "/data"; //getFilesDir().getAbsolutePath();
        String docDir = mdictHome + "/doc";
        String tmpDir = mdictHome + "/tmp";
        String mediaDir = MdxEngine.getSettings().getExtraDictDir();
        String fontsDir = mdictHome + "/fonts";
        String audioLibDir = mdictHome + "/audiolib";

        //初始创建文件夹
        IOUtil.createDir(mdictHome);
        IOUtil.createDir(resDir);
        IOUtil.createDir(docDir);
        IOUtil.createDir(tmpDir);
        IOUtil.createDir(fontsDir);
        IOUtil.createDir(audioLibDir);

        String versionFileName = resDir + "/version";

        //Optimize copy action by write a version file after successful copy.
        //Only overwritten asset files if different version found
//        if (!checkInstalledVersionNumber(context, versionFileName) || SysUtil.isDebuggable(context)) {
//            IOUtil.copyAssetToFile(assets, "ResDB.dat", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "html_begin.html", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "html_end.html", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "block_begin_h.html", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "block_end_h.html", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "union_grp_title.html", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "code.js", true, resDir, null);
//            IOUtil.copyAssetToFile(assets, "droid_sans.ttf", false, resDir, null);
//            IOUtil.copyAssetToFile(assets, "mdict.css", false, resDir, null);
//            IOUtil.saveStringToFile(versionFileName, Integer.valueOf(SysUtil.getVersionCode(context)).toString(), "utf-8");
//        }

        ArrayList<String> extraSearchPath = new ArrayList<String>();
        //docDir is in the searchPath by default, so don't need to add it.
        mediaDir = mediaDir.trim();
        if (mediaDir != null && mediaDir.length() != 0 && mediaDir.compareTo(docDir) != 0)
            extraSearchPath.add(mediaDir);

        appInited = appOne.initAppN(mdictHome, resDir, tmpDir, extraSearchPath);
        if (appInited) {
            //Maybe we shall not register icon in notification center here, beacause it's setting up engine not app
            if (getSettings().getPrefShowInNotification()) {
                registerNotification();
            }
        }
        return appInited;
    }

    public static int openLastDict(MdxDictBase dict) {
        if (!appInited)
            return MdxDictBase.kMdxDatabaseNotInited;
        int result = MdxDictBase.kMdxDatabaseNotInited;
        MdxEngineSetting prefs = MdxEngine.getSettings();
        if (prefs.getPrefMultiDictLookkupMode()) {
            DictPref dictPref = MdxEngine.getLibMgr().getRootDictPref();
            dictPref.setUnionGroup(true);
            MdxEngine.getLibMgr().updateDictPref(dictPref);
        }

        if (prefs.getPrefLastDictId() == DictPref.kInvalidDictPrefId
                || (result = MdxEngine.openDictById(prefs.getPrefLastDictId(), prefs.getPrefsUseLRUForDictOrder(), dict)) != MdxDictBase.kMdxSuccess) {
            if (MdxEngine.getLibMgr().getRootDictPref().getChildCount() > 0) {
                DictPref dictPref;
                if (prefs.getPrefMultiDictLookkupMode()) {
                    dictPref = MdxEngine.getLibMgr().getRootDictPref();//选择多个词典
                } else
                    dictPref = MdxEngine.getLibMgr().getRootDictPref().getChildDictPrefAtIndex(0);//选择一个词典
                if (dictPref != null) {
                    result = MdxEngine.openDictById(dictPref.getDictId(), prefs.getPrefsUseLRUForDictOrder(), dict);
                }
            }
        }

        if (!dict.isValid()) {
            Log.d("MDX", "Fail to open dictionary, error code:" + result);
        } else {
            saveEngineSettings();
        }
        return result;
    }

    static private String makeFontCSS(String fontPath, String cssTemplate){
        String relativeFontPath;
        if ( fontPath.startsWith(MdxEngine.getDataHomeDir()) ){
            relativeFontPath=fontPath.substring(MdxEngine.getDataHomeDir().length());
        }else
            relativeFontPath=fontPath;
        return cssTemplate.replace("$FontPath$", relativeFontPath);
    }


    static public void registerNotification() {
        /*NotificationManager nm = (NotificationManager) baseContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.app_icon_medium, baseContext.getString(R.string.app_name), System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        Intent intent = new Intent(baseContext, MainForm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                baseContext, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(baseContext, baseContext.getString(R.string.app_name), "", contentIntent);
        nm.notify(R.string.app_name, notification);*/
    }

    static public void unregisterNotification() {
        NotificationManager nm = (NotificationManager) baseContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(R.string.app_name);
    }

    /**
     * Method saveEngineSettings ...
     *
     * @return int
     */
    static public int saveEngineSettings() {
        return appOne.saveAllPreferenceN();
    }


    /**
     * Method getSharedMdxData ...
     *
     * @param dataName   of type String
     * @param convertKey of type boolean
     * @return byte[]
     */
    static public byte[] getSharedMdxData(String dataName, boolean convertKey) {
        return appOne.getGlobalDataN(dataName, convertKey);
    }

    /**
     * Method hasSharedMdxData ...
     *
     * @param dataName   of type String
     * @param convertKey of type boolean
     * @return boolean
     */
    static public boolean hasSharedMdxData(String dataName, boolean convertKey) {
        return appOne.hasGlobalDataN(dataName, convertKey);
    }

    /**
     * Method refreshDictList ...
     */
    static public void refreshDictList() {
        appOne.refreshLibListN();
    }

    static public String getTempDir() {
        return appOne.getTmpDirN();
    }

    static public String getDataHomeDir() {
        return appOne.getDataHomeDirN();
    }

    static public String getDocDir() {
        return appOne.getDocDirN();
    }

    static public String getResDir() {
        return appOne.getResDirN();
    }

    static public void findExternalFonts(ArrayList<String> extFonts){
        appOne.findExternalFontsN(extFonts);
    }

    /**
     * Merge 'newList' into 'target', remove all non-exist node from target.
     * @param baseDir  Not used in android platform
     * @param target target dict group to be merged to
     * @param newList up-to-date dict lists(All available dicts)
     * @param enableNewDict default status of new dict
     * @param libMgr libManager
     * @param removeNonExistFromLibMgr shall we remove non-exist dict from libMgr too?
     * Code example:
     *   MdxEngine.refreshDictList(); //Search lib dir and update default group
     *   DictPref allDicts=MdxEngine.getLibMgr().getDictPref(DictPref.kDefaultGrpId);
     *   DictPref targetGrp=MdxEngine.getLibMgr().getDictPref(101); //target group to be updated
     *   if (allDicts!=null){
     *       MdxEngine.mergeDictListWithUpdate("", targetGrp, allDicts, true, MdxEngine.getLibMgr(), false);
     *   }
     */
    public static native void mergeDictListWithUpdate(String baseDir, DictPref target, DictPref newList,
                                                      boolean enableNewDict, MdxLibraryMgrRef libMgr, boolean removeNonExistFromLibMgr);
    // Native declarations
    private native int openDictN(int dictID, String deviceId, String email, boolean adjustDictOrder, MdxDictBase dict); // dict is java MdxDictBase

    private native int openDictByPrefN(DictPref dictPref, String deviceId, String email, MdxDictBase dict);

    private native int getAppInstanceN();

    private native int getLibMgrN();

    private native boolean initAppN(String appHomeDir, String resDir, String tmpDir, List<String> extraSearchPath);

    private native int saveAllPreferenceN();

    private native byte[] getGlobalDataN(String dataName, boolean convertKey);

    private native boolean hasGlobalDataN(String dataName, boolean convertKey);

    private native void refreshLibListN();

    private native String getTmpDirN();

    private native String getDocDirN();

    private native String getDataHomeDirN();

    private native String getResDirN();

    private native void findExternalFontsN(ArrayList<String> extFonts);

    private int fInstance;

    private static Context baseContext = null;
    private static MdxEngine appOne = new MdxEngine();
    private static MdxEngineSetting appSetting = null;
    private static boolean appInited = false;

}
