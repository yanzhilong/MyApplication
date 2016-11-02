package com.englishlearn.myapplication.tractategroup.addtractate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.dialog.TractateTypeFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AddTractateActivity extends AppCompatActivity implements View.OnClickListener, Serializable,TractateTypeFragment.onItemClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = AddTractateActivity.class.getSimpleName();
    private static final int FILE_SELECT_CODE = 10;
    private Object object;

    private EditText tractatetype;
    private EditText selectfile;


    private List<TractateType> mList;
    private String[] tractates = null;
    private CompositeSubscription mSubscriptions;

    @Inject
    Repository repository;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = getPath(this, uri);
                    selectfile.setText(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtractate_act);

        MyApplication.instance.getAppComponent().inject(this);

        if (getIntent().hasExtra(OBJECT)) {
            object = getIntent().getSerializableExtra(OBJECT);
        }
        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_addtractate);

        tractatetype = (EditText) findViewById(R.id.tractatetype);
        selectfile = (EditText) findViewById(R.id.selectfile);
        tractatetype.setOnClickListener(this);
        selectfile.setOnClickListener(this);
        findViewById(R.id.preview).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        /*Fragment fragment = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        Subscription subscription = repository.getTractateTypesRx().subscribe(new Subscriber<List<TractateType>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List list) {
                if(list != null && list.size() > 0){
                    Log.d(TAG,"onNext size:" + list.size());
                    mList.clear();
                    mList.addAll(list);

                    tractates = new String[mList.size()];
                    for (int i = 0; i < mList.size(); i++){
                        tractates[i] = mList.get(i).getName();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tractatetype:

                if(tractates != null && tractates.length > 0){
                    TractateTypeFragment tractateTypeFragment = new TractateTypeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TractateTypeFragment.ITEMCLICKLISTENER,this);
                    bundle.putSerializable(TractateTypeFragment.TRACTATETYPES,tractates);
                    tractateTypeFragment.setArguments(bundle);
                    tractateTypeFragment.show(getSupportFragmentManager(),"tractatetype");
                }else{
                    Toast.makeText(this,"获取文章类型失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.selectfile:
                showFileChooser();
                break;
            case R.id.preview:

                break;
            case R.id.add:

                break;
        }
    }

    @Override
    public void onItemClick(int posion) {
        tractatetype.setText(tractates[posion]);
    }

    //打开文件选择器
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }

        public static String getPath(Context context, Uri uri) {

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = { "_data" };
                Cursor cursor = null;

                try {
                    cursor = context.getContentResolver().query(uri, projection,null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    // Eat it
                }
            }

            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }
}

