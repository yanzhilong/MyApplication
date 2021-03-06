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
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.CreateTractateGroupFragment;
import com.englishlearn.myapplication.dialog.ItemSelectFragment;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailActivity;
import com.englishlearn.myapplication.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AddTractateActivity extends AppCompatActivity implements View.OnClickListener, Serializable,ItemSelectFragment.onItemClickListener {

    private static final String TAG = AddTractateActivity.class.getSimpleName();
    private static final int FILE_SELECT_CODE = 10;

    private EditText tractatetype;
    private EditText selectfile;
    private EditText selecttractategroup;
    private TextView error;

    private List<TractateType> mList;
    private List<TractateGroup> tractateGroups;
    private String[] tractates = null;
    private String[] tractategroups = null;
    private int currenttypeposition = Integer.MAX_VALUE;//当前选择的类型
    private int currentgroupposition = Integer.MAX_VALUE;//当前选择的主文章分组
    private TractateGroup tractateGroup;//当前选择的分级
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
                    selectFile(newPath);
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


    private void addTractate(String filePath){
        File file = new File(filePath);

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
        showError(stringBuffer.toString());
    }

    /**
     * 增加文章
     * @param tractate
     */
    private void addTractate(final Tractate tractate) {
        error.setText("");
        tractate.setUserId(repository.getUserInfo());
        if(currenttypeposition == Integer.MAX_VALUE){
            Toast.makeText(this,"请选择分类",Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentgroupposition == Integer.MAX_VALUE){
            Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
            return;
        }

        tractate.setTractatetypeId(mList.get(currenttypeposition));

        //设置排序
        String title = tractate.getTitle();
        String sortregex = "\\d+";//查找连续的数字
        Pattern cntitlepattern = Pattern.compile(sortregex);
        Matcher cntitlematcher = cntitlepattern.matcher(title);
        int sort = Integer.MAX_VALUE;
        if(cntitlematcher.find()){
            sort = Integer.valueOf(cntitlematcher.group());
        }
        tractate.setSort(sort);
        tractate.setTractateGroupId(tractateGroup);
        Subscription sub = repository.addTractate(tractate)
                .subscribe(new Subscriber<Tractate>() {
            @Override
            public void onCompleted() {
                //Toast.makeText(AddTractateActivity.this,"添加结束",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                        Toast.makeText(AddTractateActivity.this,"添加文章失败",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddTractateActivity.this,R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Tractate tractate) {
                Toast.makeText(AddTractateActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                error.append(tractate.getTitle() + "添加成功" + System.getProperty("line.separator"));
            }
        });
        mSubscriptions.add(sub);
    }

    /**
     * 显示对话框
     */
    private void showCreateTractateGroupDialog(){
        CreateTractateGroupFragment createTractateGroupFragment = new CreateTractateGroupFragment();
        createTractateGroupFragment.setCreateListener(new CreateTractateGroupFragment.CreateListener() {
            @Override
            public void onClick(String name) {
                createTractateGroup(name);
            }
        });
        createTractateGroupFragment.show(getSupportFragmentManager(),"create");
    }


    /**
     * 创建词单
     */
    private void createTractateGroup(String name){

        TractateGroup tractateGroup = new TractateGroup();
        tractateGroup.setUserId(repository.getUserInfo());
        tractateGroup.setName(name);
        tractateGroup.setOpen(false);
        Subscription subscription = repository.addTractateGroup(tractateGroup)
                .subscribe(new Subscriber<TractateGroup>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                        createWGFail(getString(R.string.nameunique));
                }else{
                    Toast.makeText(AddTractateActivity.this,R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(TractateGroup tractateGroup) {

                if(tractateGroup != null) {
                    AddTractateActivity.this.tractateGroup = tractateGroup;
                    selecttractategroup.setText(tractateGroup.getName());
                    createWGSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    //创建失败
    private void createWGFail(String message){

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    //创建成功
    private void createWGSuccess(){

        Toast.makeText(this,R.string.createwordgroupsuccess,Toast.LENGTH_SHORT).show();

    }


    private void showError(String message) {
        error.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtractate_act);

        Log.d(TAG,"onCreage" + "savedInstanceState" + savedInstanceState);

        if(savedInstanceState != null){
            currenttypeposition = savedInstanceState.getInt("currenttype",Integer.MAX_VALUE);
            currentgroupposition = savedInstanceState.getInt("currentgroup",Integer.MAX_VALUE);
        }

        MyApplication.instance.getAppComponent().inject(this);

        addTractateHelper = new AddTractateHelper(this);
        mList = new ArrayList();
        tractateGroups = new ArrayList<>();
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

        findViewById(R.id.createtractategroup).setOnClickListener(this);
        tractatetype = (EditText) findViewById(R.id.tractatetype);
        selectfile = (EditText) findViewById(R.id.selectfile);
        selecttractategroup = (EditText) findViewById(R.id.selecttractategroup);
        selecttractategroup.setOnClickListener(this);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currenttype",currenttypeposition);
        outState.putInt("currentgroup",currentgroupposition);
        Log.d(TAG,"onSaveInstanceState");
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

        Subscription subscription1 = repository.getTractateGroupsRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<TractateGroup>>() {
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
                    tractateGroups.clear();
                    tractateGroups.addAll(list);

                    tractategroups = new String[tractateGroups.size()];
                    for (int i = 0; i < tractateGroups.size(); i++){
                        tractategroups[i] = tractateGroups.get(i).getName();
                    }
                }
            }
        });
        mSubscriptions.add(subscription1);
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
                    ItemSelectFragment tractateTypeFragment = new ItemSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ItemSelectFragment.ITEMCLICKLISTENER,this);
                    bundle.putSerializable(ItemSelectFragment.ITEMS,tractates);
                    bundle.putInt(ItemSelectFragment.FLAG,R.id.tractatetype);
                    tractateTypeFragment.setArguments(bundle);
                    tractateTypeFragment.show(getSupportFragmentManager(),"tractatetype");
                }else{
                    Toast.makeText(this,"获取文章类型失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.selecttractategroup:

                if(tractategroups != null && tractategroups.length > 0){
                    ItemSelectFragment tractateTypeFragment = new ItemSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ItemSelectFragment.ITEMCLICKLISTENER,this);
                    bundle.putSerializable(ItemSelectFragment.ITEMS,tractategroups);
                    bundle.putInt(ItemSelectFragment.FLAG,R.id.selecttractategroup);
                    tractateTypeFragment.setArguments(bundle);
                    tractateTypeFragment.show(getSupportFragmentManager(),"tractategroup");
                }else{
                    Toast.makeText(this,"获取文章类型失败",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.selectfile:
                showFileChooser();
                break;
            case R.id.createtractategroup:
                showCreateTractateGroupDialog();
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
    public void onItemClick(int flag,int posion) {
        switch (flag){
            case R.id.tractatetype:
                currenttypeposition = posion;
                tractatetype.setText(tractates[posion]);
                break;
            case R.id.selecttractategroup:
                currentgroupposition = posion;
                AddTractateActivity.this.tractateGroup = tractateGroups.get(posion);
                selecttractategroup.setText(tractategroups[posion]);
                break;
        }

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

