package com.englishlearn.myapplication.clipboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;

import javax.inject.Inject;

import cn.mdict.mdx.MdxEngine;
import rx.Subscriber;
import rx.Subscription;

public class ClipboardActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = ClipboardActivity.class.getSimpleName();
    private String value;
    List<SentenceGroup> mSentenceGroups;
    String[] sentencegrouparray =  new String[] {"请先创建分组"};

    private EditText sentenceedit;
    private EditText translationedit;
    private Spinner sentencegroup;

    @Inject
    Repository repository;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_act);



        MyApplication.instance.getAppComponent().inject(this);

        sentenceedit = (EditText) findViewById(R.id.content);
        translationedit = (EditText) findViewById(R.id.translation);
        sentencegroup = (Spinner) findViewById(R.id.sentencegroup);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                sentencegrouparray);
        sentencegroup.setAdapter(adapter);
        getSentenceGroup();

        onNewIntent(getIntent());

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.floating_view);

        int floatingWindowHeight = display.getHeight() * 3 / 7;

        initFloatingWindow(floatingWindowHeight);

        View localView = findViewById(R.id.floating_frame);

        final Context context = this;            /*
             * localView.setOnClickListener(new View.OnClickListener() {
			 *
			 * @Override public void onClick(View view) { ((FloatingForm)
			 * context).finish(); } });
			 */

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            int lastX
                    ,
                    lastY;
            int initX
                    ,
                    initY;
            int ignoreOffset = 30;
            final int ADJUST_HEIGHT = 1;
            final int SCROLL_WEBVIEW = 2;
            int adjustMode = -1;
            boolean inAdjusting = false;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // event.getp
                // dictView.sw
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        initX = lastX;
                        initY = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(initX - (int) event.getRawX()) > ignoreOffset
                                || Math.abs(initY - (int) event.getRawY()) > ignoreOffset) {

                            if (Math.abs(initX - (int) event.getRawX()) > ignoreOffset) {
                                if (!inAdjusting) {
                                    adjustMode = ADJUST_HEIGHT;
                                    inAdjusting = true;
                                }

                                if (adjustMode == ADJUST_HEIGHT) {
                                    WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                    Display display = manager
                                            .getDefaultDisplay();
                                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.floating_view);
                                    // Gets the layout params that will allow
                                    // you to
                                    // resize the layout
                                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout
                                            .getLayoutParams();

                                    // int dx = (int) event.getRawX() - lastX;
                                    // int dy = (int) event.getRawY() - lastY;
                                    int height = params.height
                                            + ((int) event.getRawX() - lastX);
                                    if (height < display.getHeight() / 10) {
                                        height = display.getHeight() / 10;
                                    }
                                    if (height > display.getHeight() * 9 / 10) {
                                        height = display.getHeight() * 9 / 10;
                                    }

                                    // Changes the height and width to the
                                    // specified
                                    // *pixels*
                                    MdxEngine
                                            .getSettings()
                                            .setPrefFloatingWindowHeight(height);
                                    params.height = height;
                                    layout.setLayoutParams(params);
                                }
                            }
                            /*if (Math.abs(initX - (int) event.getRawX()) < ignoreOffset) {
                                if (!inAdjusting) {
                                    adjustMode = SCROLL_WEBVIEW;
                                    inAdjusting = true;
                                }
                                if (adjustMode == SCROLL_WEBVIEW) {
                                    int scrollOffsetY = (lastY
                                            - (int) event.getRawY()) * 2;
                                    int currentY = dictView.getHtmlView().getScrollY();
                                    if (scrollOffsetY < 0
                                            && Math.abs(scrollOffsetY) > currentY) {
                                        scrollOffsetY = -currentY;
                                    }
                                    dictView.getHtmlView().scrollBy(dictView.getHtmlView().getScrollX(), scrollOffsetY);
                                    //final int actualOffsetY = scrollOffsetY;
                                    // if (scrollOffsetY > 0
                                    // && currentY + scrollOffsetY >
                                    // webViewHeight) {
                                    // scrollOffsetY = webViewHeight - currentY;
                                    // }
                                    // dictView.getHtmlView().get
                                    *//*
                                    TimerTask task = new TimerTask() {
							            float t = 0;
							            float sig = -Math.signum(actualOffsetY);
							            float v0 = Math.abs(actualOffsetY)/50;

							            @Override
							            public void run() {
							                t += 0.1;
							                double vt = v0- t*t;
							                if (vt >= 0)
							                {
							                	dictView.getHtmlView()
												.scrollBy(
														dictView.getHtmlView()
																.getScrollX(),
																(int) (sig*vt));
							                    //scrollByDeltaY((float) (sig*vt));
							                }
							                else
							                {
							                    mScrollTimer.cancel();
							                    return;
							                }
							            }
							        };

							        mScrollTimer = new Timer();
							        mScrollTimer.schedule(task, 0, 90);
							        *//*

                                }
                            }*/
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        adjustMode = -1;
                        inAdjusting = false;
                        if (Math.abs(initX - (int) event.getRawX()) < 10
                                && Math.abs(initY - (int) event.getRawY()) < 10) {
                            ((ClipboardActivity) context).finish();
                        }
                        break;
                }
                return true;
            }
        };

        localView.setOnTouchListener(gestureListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent");
        if (intent.hasExtra(OBJECT)) {
            value = intent.getStringExtra(OBJECT);
        }
        sentenceedit.setText(value != null ? value : "");
    }

    @SuppressLint("NewApi")
    private void initFloatingWindow(Integer height) {

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.floating_view);

        // Gets the layout params that will allow you to resize the layout
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout
                .getLayoutParams();
        Boolean fullScreen = true;
        Integer layoutGravity = Gravity.TOP;
        Integer leftMargin = 0;// getIntent().getIntExtra("EXTRA_MARGIN_LEFT",
        // 0);//
        Integer rightMargin = 0;// getIntent().getIntExtra("EXTRA_MARGIN_RIGHT",
        // 0);
        Integer topMargin = 0;// getIntent().getIntExtra("EXTRA_MARGIN_TOP", 0);
        Integer bottomMargin = 0;// getIntent().getIntExtra("EXTRA_MARGIN_BOTTOM",
        // 0);
        WindowManager wm = getWindowManager();

        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        if (fullScreen)
            getWindow().addFlags(1024); // No title full screen
        else
            getWindow().clearFlags(1024);

        // Changes the height and width to the specified *pixels*
        //params.height = height;
        params.gravity = layoutGravity;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        params.topMargin = topMargin;
        params.bottomMargin = bottomMargin;

        layout.setLayoutParams(params);
    }

    private void getSentenceGroup(){

        List<SentenceGroup> list = repository.getSentenceGroupByUserId(repository.getUserInfo().getObjectId());
        if(list != null){
            showSentenceGroups(list);
            return;
        }

        Subscription subscription = repository.getSentenceGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<SentenceGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<SentenceGroup> list) {

            }
        });
    }

    private void showSentenceGroups(List<SentenceGroup> list){
        mSentenceGroups = list;
        sentencegrouparray = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            sentencegrouparray[i] = list.get(i).getName();
        }
        adapter = new ArrayAdapter<String>(
                ClipboardActivity.this, android.R.layout.simple_spinner_item,
                sentencegrouparray);
        sentencegroup.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 创建句子
     */
    private void createSentence(){

        int position = sentencegroup.getSelectedItemPosition();
        if(sentencegrouparray != null && sentencegrouparray.length >= 1){
            SentenceGroup sentenceGroup = mSentenceGroups.get(position);
            createSentence(sentenceGroup);
        }
    }


    private void createSentence(final SentenceGroup sentenceGroup){

        Sentence sentence = new Sentence();
        User user = repository.getUserInfo();
        user.setPointer();
        sentence.setUser(user);
        sentence.setContent(sentenceedit.getText().toString());
        sentenceGroup.setPointer();
        sentence.setSentenceGroup(sentenceGroup);
        sentence.setTranslation(translationedit.getText().toString());
        sentence.setRemark("");
        Subscription subscription = repository.addSentence(sentence).subscribe(new Subscriber<Sentence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(ClipboardActivity.this,ClipboardActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
                ClipboardActivity.this.finish();
            }

            @Override
            public void onNext(Sentence sentence) {
                Toast.makeText(ClipboardActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                ClipboardActivity.this.finish();
            }
        });
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.add:
                createSentence();
                break;
            default:
                break;
        }
    }
}

