package com.englishlearn.myapplication.sentencegroups.sentences.sentencecollect;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDefaultError;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.CreateWordGroupFragment;
import com.englishlearn.myapplication.dialog.ItemSelectFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CreateSentenceActivity extends AppCompatActivity implements View.OnClickListener,Serializable,ItemSelectFragment.onItemClickListener {

    public static final String ENSENTENCE = "ensentence";
    public static final String CHSENTENCE = "chsentence";
    public static final String SENTENCE = "sentence";
    public static final String CREATESENTENCE = "createsentence";//创建句子
    public static final String TRACTATE = "tractate";//文章
    private static final String TAG = CreateSentenceActivity.class.getSimpleName();
    private String ensentence;
    private String chsentence;

    private boolean isCreateSentence = false;

    private CompositeSubscription mSubscriptions;
    private Sentence sentence;
    private Tractate tractate;
    private ArrayList<SentenceGroup> sentenceGroups;
    private String[] sentencegroupss;
    private TextView sentencegroup;
    private EditText sentenceedit;
    private EditText translationedit;
    private int currentgroupposition = Integer.MAX_VALUE;//当前选择的类型
    private SentenceGroup sentenceGroup;

    @Inject
    Repository repository;
    private String sentencegroupname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createsentence_act);

        sentenceGroups = new ArrayList<>();

        MyApplication.instance.getAppComponent().inject(this);

        if (getIntent().hasExtra(ENSENTENCE) && getIntent().hasExtra(CHSENTENCE)) {
            ensentence = getIntent().getStringExtra(ENSENTENCE);
            chsentence = getIntent().getStringExtra(CHSENTENCE);
        }

        tractate = (Tractate) getIntent().getSerializableExtra(TRACTATE);
        isCreateSentence = getIntent().getBooleanExtra(CREATESENTENCE,false);
        if(getIntent().hasExtra(SENTENCE)){
            sentence = (Sentence) getIntent().getSerializableExtra(SENTENCE);
        }

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        sentencegroup = (TextView) findViewById(R.id.sentencegroup);
        sentenceedit = (EditText) findViewById(R.id.content);
        translationedit = (EditText) findViewById(R.id.translation);

        if(ensentence != null && chsentence != null){
            sentenceedit.setText(ensentence);
            translationedit.setText(chsentence);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_sentencecollect);

        /*Fragment fragment = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }*/
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        Subscription subscription = repository.getSentenceGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<SentenceGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    BmobDefaultError bmobDefaultError = (BmobDefaultError) bmobRequestException.getObject();
                    RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                    if(createuser == RemoteCode.COMMON.Common_NOTFOUND){
                        sentencegroupss = new String[0];
                    }
                }
                Log.d(TAG,e.toString());
            }

            @Override
            public void onNext(List<SentenceGroup> list) {
                if(list != null){
                    Log.d(TAG,"onNext size:" + list.size());
                    sentenceGroups.clear();
                    sentenceGroups.addAll(list);

                    sentencegroupss = new String[sentenceGroups.size()];
                    for (int i = 0; i < sentenceGroups.size(); i++){
                        sentencegroupss[i] = sentenceGroups.get(i).getName();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    /*public void addSentenceGroup(Sentence Sentence, SentenceGroup sentenceGroup){

        SentenceCollect sentenceCollect = new SentenceCollect();
        sentenceCollect.setUser(repository.getUserInfo());
        sentenceCollect.setSentenceCollectGroup(sentenceGroup);
        Sentence.setPointer();
        sentenceCollect.setSentence(Sentence);
        Subscription subscription = repository.addSentenceCollect(sentenceCollect).subscribe(new Subscriber<SentenceCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CreateSentenceActivity.this,CreateSentenceActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(SentenceCollect sentenceCollect) {
                Toast.makeText(CreateSentenceActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sentencegroupselect:
                if(sentencegroupss != null && sentencegroupss.length > 0){
                    ItemSelectFragment tractateTypeFragment = new ItemSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ItemSelectFragment.ITEMCLICKLISTENER,this);
                    bundle.putSerializable(ItemSelectFragment.ITEMS,sentencegroupss);
                    bundle.putInt(ItemSelectFragment.FLAG,R.id.tractatetype);
                    tractateTypeFragment.setArguments(bundle);
                    tractateTypeFragment.show(getSupportFragmentManager(),"wordgroup");
                }else if(sentencegroupss != null) {
                    Toast.makeText(this,"请先创建分组",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"获取分组信息失败",Toast.LENGTH_SHORT).show();
                }
                break;
            /*case R.id.affirm:
                if((currentgroupposition == Integer.MAX_VALUE && sentenceGroup == null) || sentence == null){
                    Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
                }else{
                    SentenceGroup sentenceGroup1 = null;
                    if(sentenceGroup != null){
                        sentenceGroup1 = sentenceGroup;
                    }else {
                        sentenceGroup1 = sentenceGroups.get(currentgroupposition);
                    }
                    addSentenceGroup(sentence, sentenceGroup1);
                }
                break;*/

            case R.id.createsentencegroup:
                //新建单词分组
                showCreateWordGroupDialog();
                break;
            case R.id.create:
                //创建句子
                if((currentgroupposition == Integer.MAX_VALUE && sentenceGroup == null)){
                    Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
                }else{

                    SentenceGroup sentenceGroup = null;
                    if(this.sentenceGroup != null){
                        sentenceGroup = this.sentenceGroup;
                    }else {
                        sentenceGroup = sentenceGroups.get(currentgroupposition);
                    }
                    //addSentenceGroup(sentence,wordGroupId);
                    createSentence(sentenceGroup);
                }
                break;
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
        sentence.setRemark(tractate != null ? tractate.getTitle() : "");
        Subscription subscription = repository.addSentence(sentence).subscribe(new Subscriber<Sentence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(CreateSentenceActivity.this,CreateSentenceActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Sentence sentence) {
                Toast.makeText(CreateSentenceActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 显示对话框
     */
    private void showCreateWordGroupDialog(){
        CreateWordGroupFragment createWordGroupFragment = new CreateWordGroupFragment();
        createWordGroupFragment.setCreateWordGroupListener(new CreateWordGroupFragment.CreateWordGroupListener() {
            @Override
            public void onClick(String name) {
                createWordGroup(name);
            }
        });
        createWordGroupFragment.show(getSupportFragmentManager(),"create");
    }


    /**
     * 创建词单
     */
    private void createWordGroup(String name){

        this.sentencegroupname = name;
        SentenceGroup createwg = new SentenceGroup();
        User user = repository.getUserInfo();
        user.setPointer();
        createwg.setUser(user);
        createwg.setName(name);
        createwg.setOpen(false);
        Subscription subscription = repository.addSentenceGroup(createwg).subscribe(new Subscriber<SentenceGroup>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                        createWGFail(getString(R.string.wordgroups_nameunique));
                }else{
                    Toast.makeText(CreateSentenceActivity.this,R.string.networkerror,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(SentenceGroup sentenceGroup) {

                if(sentenceGroup != null){
                    createWGSuccess(sentenceGroup);
                }else{

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
    private void createWGSuccess(SentenceGroup sentenceGroup){
        this.sentenceGroup = sentenceGroup;
        sentencegroup.setText(sentencegroupname);
        Toast.makeText(this,R.string.createwordgroupsuccess,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClick(int flag, int posion) {

        currentgroupposition = posion;
        sentencegroup.setText(sentenceGroups.get(posion).getName());

    }
}

