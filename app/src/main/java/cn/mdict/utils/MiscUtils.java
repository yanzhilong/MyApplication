/*
 * Copyright (C) 2013. Rayman Zhang <raymanzhang@gmail.com>
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

package cn.mdict.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import cn.mdict.mdx.MdxDictBase;
import cn.mdict.mdx.MdxEngine;
import cn.mdict.mdx.MdxEngineSetting;
import cn.mdict.mdx.MdxUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class MiscUtils {
    static MediaPlayer mediaPlayer = null;
    static AudioTrack outTrack=null;
    static boolean appInited = false;
    private static final String TAG = "MDict.MiscUtil";


    public static Boolean checkNetworkStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null
                && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    public static String getAudioFileNameForWord(String word, String extension) {
        StringBuilder audioFileName = new StringBuilder("\\");
        if (word.length() == 0) return audioFileName.toString();

        String stripedWord = word.trim();
        audioFileName.append(stripedWord.replace(".", ""));
        audioFileName.append(extension);
        return audioFileName.toString();
    }

    public static String getErrorMessage(Throwable e) {
        if (e.getMessage() == null)
            return e.toString();
        else
            return e.getMessage();
    }

    public static void hideSIP(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            //imm.showSoftInput(ic_view, 0); //????????
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//?????????
        }
    }

    public static void showSIP(Activity activity, View view) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void showSIP(Activity activity, int control_id) {
        showSIP(activity, activity.findViewById(control_id));
    }

    public static void toggleSIP(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }

    public static void parseLink(String url, StringBuffer scheme, StringBuffer host, StringBuffer fregment) {
        final String seperator = "://";
        if (url.length() > 0 && url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }

        int pos = url.indexOf(seperator);
        if (pos >= 0)
            scheme.append(url.substring(0, pos));
        pos += seperator.length();
        int pos1 = url.indexOf("#", pos);
        if (pos1 > 0) {
            host.append(url.substring(pos, pos1));
            fregment.append(url.substring(pos1 + 1));
        } else {
            host.append(url.substring(pos));
        }
    }

    public class WaveInfo {
        WaveInfo(int format, int channels, int rate, int bits, int size, int bodyOffset) {
            this.format = format;
            this.channels = channels;
            this.rate = rate;
            this.bits = bits;
            this.size = size;
            this.bodyOffset = bodyOffset;
        }

        public int format, channels, rate, bits, size, bodyOffset;
    }

    public static WaveInfo parseWaveHeader(byte[] waveData) {
        final int HEADER_SIZE = 48;
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(waveData, 0, buffer.capacity());

        int dataMark = 0;
        int dataSize = 0;

        buffer.rewind();
        dataMark = buffer.getInt(); //dataMark for "RIFF"
        dataSize = buffer.getInt();
        dataMark = buffer.getInt(); //dataMark for "WAVE"
        dataMark = buffer.getInt(); //dataMark for "fmt "
        dataSize = buffer.getInt();
        int dataOffset = buffer.position();
        int format = buffer.getShort();
        int channels = buffer.getShort();
        int rate = buffer.getInt();
        int avgrate = buffer.getInt();
        int blockAlign = buffer.getShort();
        int bitsPerSample = buffer.getShort();

        int waveDataPos = dataOffset + dataSize;

        do {
            buffer.rewind();
            buffer.put(waveData, waveDataPos, 8);
            buffer.rewind();
            dataMark = buffer.getInt();
            dataSize = buffer.getInt();
            waveDataPos += (dataSize + 8);
        } while (dataMark != 0x61746164);

        WaveInfo result = (new MiscUtils()).new WaveInfo(format, channels, rate, bitsPerSample, dataSize, waveDataPos - dataSize);
        return result;
    }

    public static void playMedia(byte[] mediaData) {
        try {
            if ( mediaPlayer!=null ){
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
            }else
                mediaPlayer = new MediaPlayer();
            String tmpFile = MdxEngine.getTempDir() + "audio.wav";
            IOUtil.saveBytesToFile(tmpFile, mediaData);
            mediaPlayer.setDataSource(tmpFile);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    if (mediaPlayer == mp) {
                        mediaPlayer = null;
                    } else {
                        Log.d(TAG, "mediaPlayer unmatched!");
                    }
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playWave(byte[] waveData) {
        WaveInfo info = MiscUtils.parseWaveHeader(waveData);
//        WaveInfo info1= MiscUtils.parseWaveHeader1(waveData);

        int channelConfig, encoding;
        if (info.channels == 1)
            channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        else if (info.channels == 2)
            channelConfig = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        else
            channelConfig = AudioFormat.CHANNEL_CONFIGURATION_INVALID;

        if (info.bits == 8)
            encoding = AudioFormat.ENCODING_PCM_8BIT;
        else if (info.bits == 16)
            encoding = AudioFormat.ENCODING_PCM_16BIT;
        else {
            //encoding=AudioFormat.ENCODING_INVALID;
            playMedia(waveData);
            return;
        }

        int sampleRate = info.rate;
        int bufSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, encoding);
        bufSize= Math.max(bufSize,8*1024);
        if (outTrack!=null){
            if (outTrack.getPlayState()== AudioTrack.PLAYSTATE_PLAYING){
                outTrack.pause();
                outTrack.flush();
                outTrack.release();
            }
        }
        outTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                channelConfig, encoding, bufSize, AudioTrack.MODE_STREAM);
        final AudioTrack player=outTrack;
        player.play();
        int audioSize = waveData.length - info.bodyOffset;
        int dataOffset = info.bodyOffset;
        while (audioSize > 0) {
            int curBufSize = bufSize;
            if (audioSize < bufSize) {
                curBufSize = audioSize;
            }
            if (player.write(waveData, dataOffset, curBufSize)<=0)
                break;
            dataOffset += curBufSize;
            audioSize -= curBufSize;
        }
        //We will release the audio track resource before app exit.
        //player.release();
    }
    public static void releaseAudioTrack(){
        if (outTrack!=null){
            outTrack.release();
        }
    }

    private static byte[] getWaveDataForPath(MdxDictBase dict, int dictId, String path) {
        byte[] result = null;
        if (dict != null && dict.isValid()) {
            result = dict.getDictData(dictId, path, true);
        }

        if (result == null) {
            result = MdxEngine.getSharedMdxData(path, true);
        }

        if (result != null && result.length > 0) {
            // TODO: Ogg decode?
            if (result.length > 3) {
                if (result[0] == 'O' && result[1] == 'g' && result[2] == 'g') {
                    return MdxUtils.decodeSpeex(result, true);
                } else {
                    return result;
                }
            }
        }
        return null;
    }

    public static boolean playAudioForWord(MdxDictBase dict, int dictId, String headword) {
        if (headword.length() > 0) {
            return playAudio(dict, dictId, getAudioFileNameForWord(headword, ".spx"))
                    || playAudio(dict, dictId, getAudioFileNameForWord(headword, ".wav"))
                    || playAudio(dict, dictId, getAudioFileNameForWord(headword, ".mp3"));
        } else
            return false;
    }

    public static boolean playAudio(MdxDictBase dict, int dictId, String path) {
        final byte[] waveData = getWaveDataForPath(dict, dictId, path);
        if (waveData != null && waveData.length > 0) {
            try {
                if (MdxEngine.getSettings().getPrefPlayAudioInBackground()){
                    new Thread("PlayWaveThread"){
                        @Override
                        public void run(){
                            MiscUtils.playWave(waveData);
                        }
                    }.start();
                }else
                    MiscUtils.playWave(waveData);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int convertDipToPx(Resources res, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
    }

    static boolean findSpeechForWord(MdxDictBase dict, String headword, String extension) {
        String path = getAudioFileNameForWord(headword, extension);
        boolean hasSpeech = false;
        if (dict != null)
            hasSpeech = dict.hasDataEntry(path, true);
        if (!hasSpeech)
            hasSpeech = MdxEngine.hasSharedMdxData(path, true);
        return hasSpeech;
    }


    public static boolean isTablet(Context context) {
        return getScreenSize(context) > 6.0f;
    }

    public static float getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.xdpi;
        float screenHeight = dm.heightPixels / dm.ydpi;
        double size = Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight);
        // Tablet devices should have a screen size greater than 6 inches
        return (float) size;
    }

    public static boolean shouldUseSplitViewMode(Context context) {
        switch (MdxEngine.getSettings().getPrefSplitViewMode()) {
            case MdxEngineSetting.kSplitViewModeOff:
                return false;
            case MdxEngineSetting.kSplitViewModeOn:
                return true;
            case MdxEngineSetting.kSplitViewModeAuto:
                //Should change this according screen size and resolution.
                Log.d(TAG, "Screen size:" + String.valueOf(getScreenSize(context)));
                Log.d(TAG, "Orientation:" + String.valueOf(context.getResources().getConfiguration().orientation));
                return (getScreenSize(context) > 3.5) && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
        return false;
    }

    public static void MakeItemVisible(final ListView listView, final int position){
        if (listView.getFirstVisiblePosition()!=position && listView.getAdapter().getCount()>0 ){
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.setSelectionFromTop(position,0);
                }
            });
            listView.setSelection(position);
        }
    }

    public static String getFileNameMainPart(String filePath){
        int pos=filePath.lastIndexOf('/');
        String fileName;
        if (pos<0)
            fileName=filePath;
        else if (pos<filePath.length()-1)
            fileName=filePath.substring(pos+1, filePath.length());
        else
            fileName="";
        if ( fileName.length()>0 ){
            pos=fileName.lastIndexOf('.');
            if ( pos<0 )
                return fileName;
            else
                return fileName.substring(0, pos);
        }else
            return fileName;
    }

    public static void setOrientationSensorBySetting(Activity activity){
        if (MdxEngine.getSettings().getPrefLockRotation()) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        } else
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }
    /*
    public static View getItemViewForActionItem(ActionBarImpl actionBar, MenuItem item){
        View actionView=item.getActionView();
        if (actionView==null){
            if ( item.getItemId()== android.R.id.home ){
                actionView= actionBar.mActionView.mHomeLayout;
            }else{
                actionView= actionBar.mActionView.mActionMenuPresenter.findViewForItem(item);
                //MenuItemImpl itemImpl=(MenuItemImpl)item;
                //actionView=itemImpl.getActionView();
            }
        }
        return actionView;
    }
    */

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean updateAppWithDownloadManager(Context context, String url, String title, String description, final String target){
        try {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager downloadManager;
            downloadManager = (DownloadManager) context.getSystemService(serviceString);

            Uri uri = Uri.parse(url);
            Request request = new Request(uri);
            //request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
            File targetFile=new File(target);
            File parentDir=targetFile.getParentFile();
            if ( !parentDir.mkdirs() && !parentDir.isDirectory()){
                return false;
            }
            request.setDestinationUri(Uri.fromFile(targetFile));
            request.setTitle(title);
            request.setDescription(description);
            request.setAllowedOverRoaming(false);
            request.addRequestHeader("User-Agent", IOUtil.HttpUserAgent);
            //request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            final long myDownloadReference = downloadManager.enqueue(request);
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (myDownloadReference == reference) {
                        installApk(context, target);
                    }
                }
            };
            context.registerReceiver(receiver, filter);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void installApk(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

        /*
        <version>1.0</version>
        <build>10</build>
        <url>http://mdict.cn/download/MDict-1.0.apk</url>
        <description>Changelog: xxxx </description>
     */

    public static class AppInfo{
        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getBuild() {
            return build;
        }

        public void setBuild(int build) {
            this.build = build;
        }

        private String version="";
        private String url="";
        private String description="";
        private int build =-1;
    }

    public static AppInfo parseAppUpdateInfo(byte[] appUpdateInfo){
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new ByteArrayInputStream(appUpdateInfo), "utf-8");
            int type = parser.getEventType();
            AppInfo info=new AppInfo();
            while(type != XmlPullParser.END_DOCUMENT ){
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if("version".equals(parser.getName())){
                            info.setVersion(parser.nextText());	//获取版本号
                        }else if ("url".equals(parser.getName())){
                            info.setUrl(parser.nextText());	//获取要升级的APK文件
                        }else if ("description".equals(parser.getName())){
                            info.setDescription(parser.nextText());	//获取该文件的信息
                        }else if ("build".equals(parser.getName())){
                            info.setBuild(Integer.parseInt(parser.nextText()));
                        }
                        break;
                }
                type = parser.next();
            }
            return info;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
