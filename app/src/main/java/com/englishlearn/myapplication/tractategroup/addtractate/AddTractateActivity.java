package com.englishlearn.myapplication.tractategroup.addtractate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.dialog.TractateTypeFragment;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailActivity;
import com.englishlearn.myapplication.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AddTractateActivity extends AppCompatActivity implements View.OnClickListener, Serializable,TractateTypeFragment.onItemClickListener {

    private static final String TAG = AddTractateActivity.class.getSimpleName();
    private static final int FILE_SELECT_CODE = 10;

    private EditText tractatetype;
    private EditText selectfile;
    private EditText selectfiledirectory;
    private TextView error;

    private List<TractateType> mList;
    private String[] tractates = null;
    private CompositeSubscription mSubscriptions;

    private AddTractateHelper addTractateHelper;
    @Inject
    Repository repository;
    private boolean isdirectory; //是否是选择目录


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    String newPath = UriUtils.getPath(this,uri);
                    if(isdirectory){
                        selectDirectory(newPath);
                    }else{
                        selectFile(newPath);

                    }

                    /*//得到
                    File file = new File(path);
                    File parentFile = file.getParentFile();
                    File[] files = parentFile.listFiles();
                    for(int i = 0; i < files.length; i++){
                        String tractate = AndroidUtils.getStringByFilePath(files[i].getAbsolutePath());
                    }*/
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void selectFile(String filePath){
        selectfile.setText(filePath);
        try {
            Tractate tractate = addTractateHelper.getTractateByFilePath(filePath);
        } catch (TractateLegalException e) {
            showError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showError(TractateEnum.UNKNOW.getMessage());
            e.printStackTrace();
        } catch(Exception e){
            showError(TractateEnum.UNKNOW.getMessage());
            e.printStackTrace();
        }
    }

    private TractateType getTractate(String name){
        for(int i = 0; i < mList.size(); i++){
            if(name.equals(mList.get(i).getName())){
                return mList.get(i);
            }
        }
        return null;
    }

    private void selectDirectory(String filePath){
        File file = new File(filePath);

        selectfile.setText(file.getParentFile().getPath());
        File[] files = file.getParentFile().listFiles();

        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < files.length; i++){
            try {
                Tractate tractate = addTractateHelper.getTractateByFilePath(files[i].getPath());
            } catch (TractateLegalException e) {
                stringBuffer.append(files[i].getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch (IOException e) {
                stringBuffer.append(files[i].getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch(Exception e){
                stringBuffer.append(files[i].getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            }
        }
        showError(stringBuffer.toString());
    }

    private void addTractate(String filePath){
        File file = new File(filePath);
        if(file.isDirectory()){
            File[] files = file.getParentFile().listFiles();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0; i < files.length; i++){
                try {
                    Tractate tractate = addTractateHelper.getTractateByFilePath(files[i].getPath());
                    addTractate(tractate);
                } catch (TractateLegalException e) {
                    stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                    e.printStackTrace();
                } catch (IOException e) {
                    stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                    e.printStackTrace();
                } catch(Exception e){
                    stringBuffer.append(files[i].getName() + e.getMessage() + "上传失败" + System.getProperty("line.separator"));
                    e.printStackTrace();
                }
            }
            showError(stringBuffer.toString());
        }else{
            StringBuffer stringBuffer = new StringBuffer();
            try {
                Tractate tractate = addTractateHelper.getTractateByFilePath(filePath);
                addTractate(tractate);
            } catch (TractateLegalException e) {
                stringBuffer.append(file.getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch (IOException e) {
                stringBuffer.append(file.getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            } catch(Exception e){
                stringBuffer.append(file.getName() + e.getMessage() + System.getProperty("line.separator"));
                e.printStackTrace();
            }
        }
    }

    private StringBuffer addTractateResult = new StringBuffer();
    /**
     * 增加文章
     * @param tractate
     */
    private void addTractate(final Tractate tractate) {
        tractate.setUserId(repository.getUserInfo().getObjectId());
        tractate.setTractatetypeId(getTractate(tractatetype.getText().toString()).getObjectId());
        Subscription sub = repository.addTractate(tractate).subscribe(new Subscriber<Tractate>() {
            @Override
            public void onCompleted() {
                Toast.makeText(AddTractateActivity.this,"添加结束",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(AddTractateActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                addTractateResult.append(tractate.getTitle() + "添加失败");
            }

            @Override
            public void onNext(Tractate tractate) {
                Toast.makeText(AddTractateActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                addTractateResult.append(tractate.getTitle() + "添加成功");
            }
        });
        mSubscriptions.add(sub);
    }


    private void showError(String message) {
        error.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtractate_act);

        MyApplication.instance.getAppComponent().inject(this);

        addTractateHelper = new AddTractateHelper(this);
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
        selectfiledirectory = (EditText) findViewById(R.id.selectfiledirectory);
        selectfiledirectory.setOnClickListener(this);
        error = (TextView) findViewById(R.id.error);
        error.setMovementMethod(ScrollingMovementMethod.getInstance());
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

                showFileChooser(false);
                break;
            case R.id.selectfiledirectory:
                showFileChooser(true);
                break;
            case R.id.preview:
                String filepath = selectfile.getText().toString();
                Tractate tractate = null;
                try {
                    tractate = addTractateHelper.getTractateByFilePath(filepath);
                    Intent intent = new Intent(this,TractateDetailActivity.class);
                    intent.putExtra(TractateDetailActivity.PREVIEW,true);
                    intent.putExtra(TractateDetailActivity.TRACTATE,tractate);
                    startActivity(intent);
                } catch (TractateLegalException e) {
                    showError(e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    showError(TractateEnum.UNKNOW.getMessage());
                } catch (Exception e){
                    showError(e.getMessage());
                    showError(TractateEnum.UNKNOW.getMessage());
                }

                break;
            case R.id.add:
                addTractate(selectfile.getText().toString());
                break;
        }
    }

    @Override
    public void onItemClick(int posion) {
        tractatetype.setText(tractates[posion]);
    }

    //打开文件选择器
    private void showFileChooser(boolean directory) {

        this.isdirectory = directory;

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

