package com.englishlearn.myapplication.core;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.clipboard.ClipboardActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by yanzl on 17-1-2.
 */

public class ClipboardMonitor {

    private static final String TAG = ClipboardMonitor.class.getSimpleName();
    private static ClipboardMonitor INSTANCE;
    private Context context;
    private android.content.ClipboardManager mClipboard;

    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;

    private boolean isAdded = false; // 是否已增加悬浮窗

    private boolean isInit = false; //是否初始化过

    private static WindowManager wm;

    private static WindowManager.LayoutParams params;

    private View floatView;

    private float startX = 0;

    private float startY = 0;

    private float x;

    private float y;

    public ClipboardMonitor(Context context) {
        this.context = context;
    }

    public static synchronized ClipboardMonitor newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ClipboardMonitor(context);
        }
        return INSTANCE;
    }

    public void init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mClipboard = (android.content.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            mClipboard.addPrimaryClipChangedListener(new android.content.ClipboardManager.OnPrimaryClipChangedListener() {
                @SuppressLint("NewApi")
                public void onPrimaryClipChanged() {

                    ClipData data = mClipboard.getPrimaryClip();
                    ClipData.Item item = data.getItemAt(0);
                    /*Intent mIntent = new Intent(context,ClipboardActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);*/
                    Log.d(TAG, "剪切板:" + item.getText());
                    showClipboard(item.getText() + "");
                }
            });
        }
    }


    /**
     * 显示剪切板
     *
     * @param value
     */
    public void showClipboard(String value) {


        //判断是否监听剪切板
        boolean isClipboard = true;
        if (!isClipboard) {
            return;
        }

        Intent intent = new Intent(context, ClipboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ClipboardActivity.OBJECT,value);
        context.startActivity(intent);
       /* if (!isInit) {
            createFloatView();
        }
        if (!isAdded) {
            wm.addView(floatView, params);
            isAdded = true;
        }
        setupCellView(floatView,value);*/
    }

    /**
     * 隐藏窗口
     */
    public void hideClipboard() {
        if (isAdded) {
            wm.removeView(floatView);
            isAdded = false;
        }
    }


    /**
     * 创建悬浮窗
     */
    private void createFloatView() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatView = layoutInflater.inflate(R.layout.dict_popup_window, null);
        View floating_frame = floatView.findViewById(R.id.floating_frame);
        wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

		/*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

        // 设置悬浮窗的长得宽
        //params.width = context.getResources().getDimensionPixelSize(R.dimen.float_width);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        // 设置悬浮窗的Touch监听
        floating_frame.setOnTouchListener(new View.OnTouchListener() {

            int ignoreOffset = 30;//移动忽略值

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        Log.d(TAG,"startX:" + startX + "startY:" + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG,"startX:" + startX + "startY:" + startY);
                        Log.d(TAG,"moveX:" + event.getRawX() + "moveY:" + event.getRawY());
                        if (Math.abs(startY - (int) event.getRawY()) < ignoreOffset){
                            break;
                        }
                        params.x = (int) (x - startX);
                        params.y = (int) (y - startY);
                        wm.updateViewLayout(floatView, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = startY = 0;
                        if (Math.abs(startX - (int) event.getRawX()) < 10
                                && Math.abs(startY - (int) event.getRawY()) < 10) {
                            hideClipboard();
                        }
                        break;
                }
                return true;
            }
        });
        isInit = true;
    }

    /**
     * 设置浮窗view内部子控件
     *
     * @param rootview
     * @param value
     *
     */
    private void setupCellView(View rootview, String value) {
        ImageView closedImg = (ImageView) rootview.findViewById(R.id.float_window_closed);
        TextView titleText = (TextView) rootview.findViewById(R.id.float_window_title);
        titleText.setText(value);
        closedImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isAdded) {
                    wm.removeView(floatView);
                    isAdded = false;
                }
            }
        });
        floatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}
