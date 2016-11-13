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
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.CreateWordGroupFragment;
import com.englishlearn.myapplication.dialog.ItemSelectFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class SentenceCollectActivity extends AppCompatActivity implements View.OnClickListener,Serializable,ItemSelectFragment.onItemClickListener {

    public static final String ENSENTENCE = "ensentence";
    public static final String CHSENTENCE = "chsentence";
    public static final String SENTENCE = "sentence";
    public static final String CREATESENTENCE = "createsentence";//创建句子
    private static final String TAG = SentenceCollectActivity.class.getSimpleName();
    private String ensentence;
    private String chsentence;

    private boolean isCreateSentence = false;

    private CompositeSubscription mSubscriptions;
    private Sentence sentence;
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
        setContentView(R.layout.sentencecollect_act);

        sentenceGroups = new ArrayList<>();

        MyApplication.instance.getAppComponent().inject(this);

        if (getIntent().hasExtra(ENSENTENCE) && getIntent().hasExtra(CHSENTENCE)) {
            ensentence = getIntent().getStringExtra(ENSENTENCE);
            chsentence = getIntent().getStringExtra(CHSENTENCE);
        }

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
        Subscription subscription = repository.getSentenceGroupRxByUserId(repository.getUserInfo().getObjectId(),isCreateSentence).subscribe(new Subscriber<List<SentenceGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
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

    public void addSentenceCollect(Sentence Sentence,String sentenceGroupId){

        SentenceCollect sentenceCollect = new SentenceCollect();
        sentenceCollect.setUserId(repository.getUserInfo().getObjectId());
        sentenceCollect.setSentencegroupId(sentenceGroupId);
        sentenceCollect.setSentenceId(Sentence.getObjectId());
        Subscription subscription = repository.addSentenceCollect(sentenceCollect).subscribe(new Subscriber<SentenceCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SentenceCollectActivity.this,SentenceCollectActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(SentenceCollect sentenceCollect) {
                Toast.makeText(SentenceCollectActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }

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
                }else{
                    Toast.makeText(this,"获取分组信息失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.affirm:
                if((currentgroupposition == Integer.MAX_VALUE && sentenceGroup == null) || sentence == null){
                    Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
                }else{
                    String wordGroupId = "";
                    if(sentenceGroup != null){
                        wordGroupId = sentenceGroup.getObjectId();
                    }else {
                        wordGroupId = sentenceGroups.get(currentgroupposition).getObjectId();
                    }
                    addSentenceCollect(sentence,wordGroupId);
                }
                break;

            case R.id.createsentencegroup:
                //新建单词分组
                showCreateWordGroupDialog();
                break;
            case R.id.create:
                //创建句子
                if((currentgroupposition == Integer.MAX_VALUE && sentenceGroup == null)){
                    Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
                }else{

                    String wordGroupId = "";
                    if(sentenceGroup != null){
                        wordGroupId = sentenceGroup.getObjectId();
                    }else {
                        wordGroupId = sentenceGroups.get(currentgroupposition).getObjectId();
                    }
                    //addSentenceCollect(sentence,wordGroupId);
                    createSentence(wordGroupId);
                }

                break;
        }
    }

    private void createSentence(final String sentenceGroupId){

        Sentence sentence = new Sentence();
        sentence.setUserId(repository.getUserInfo().getObjectId());
        sentence.setContent(ensentence);
        sentence.setTranslation(chsentence);
        sentence.setRemark("来自文章");
        Subscription subscription = repository.addSentence(sentence).onErrorReturn(new Func1<Throwable, Sentence>() {
                    @Override
                    public Sentence call(Throwable throwable) {
                        Toast.makeText(SentenceCollectActivity.this,"添加句子失败",Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }).flatMap(new Func1<Sentence, Observable<SentenceCollect>>() {
            @Override
            public Observable<SentenceCollect> call(Sentence sentence) {

                SentenceCollect sentenceCollect = new SentenceCollect();
                sentenceCollect.setUserId(repository.getUserInfo().getObjectId());
                sentenceCollect.setSentenceId(sentence.getObjectId());
                sentenceCollect.setSentencegroupId(sentenceGroupId);
                return repository.addSentenceCollect(sentenceCollect);
            }
        }).subscribe(new Subscriber<SentenceCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SentenceCollectActivity.this,SentenceCollectActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(SentenceCollect sentenceCollect) {
                Toast.makeText(SentenceCollectActivity.this,"创建并收藏成功",Toast.LENGTH_SHORT).show();
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
                createWordGroup(name,isCreateSentence);
            }
        });
        createWordGroupFragment.show(getSupportFragmentManager(),"create");
    }


    /**
     * 创建词单
     */
    private void createWordGroup(String name,boolean create){

        this.sentencegroupname = name;
        SentenceGroup createwg = new SentenceGroup();
        createwg.setUserId(repository.getUserInfo().getObjectId());
        createwg.setName(name);
        createwg.setCreate(create);
        createwg.setOpen("false");
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
                    Toast.makeText(SentenceCollectActivity.this,R.string.networkerror,Toast.LENGTH_SHORT).show();
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
    private void createWGSuccess(SentenceGroup wordGroup){
        this.sentenceGroup = wordGroup;
        sentencegroup.setText(sentencegroupname);
        Toast.makeText(this,R.string.createwordgroupsuccess,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClick(int flag, int posion) {

        currentgroupposition = posion;
        sentencegroup.setText(sentenceGroups.get(posion).getName());

    }
}

