package com.englishlearn.myapplication.core;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.englishlearn.myapplication.clipblard.ClipboardActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by yanzl on 17-1-2.
 */

public class ClipboardMonitor {

    private static final String TAG = ClipboardMonitor.class.getSimpleName();
    private static ClipboardMonitor INSTANCE;
    private Context context;
    private android.content.ClipboardManager mClipboard;

    public ClipboardMonitor(Context context) {
        this.context = context;
    }

    public static synchronized ClipboardMonitor newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ClipboardMonitor(context);
        }
        return INSTANCE;
    }

    public void init(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mClipboard = (android.content.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            mClipboard.addPrimaryClipChangedListener(new android.content.ClipboardManager.OnPrimaryClipChangedListener() {
                @SuppressLint("NewApi")
                public void onPrimaryClipChanged() {

                    ClipData data = mClipboard.getPrimaryClip();
                    ClipData.Item item = data.getItemAt(0);
                    Intent mIntent = new Intent(context,ClipboardActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);
                    Log.d(TAG,"剪切板:"+item.getText());
                }
            });
        }
    }
}
