package com.englishlearn.myapplication.wordgroups.words.wordcollect;

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
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordCollect;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.source.Repository;
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

//收藏单词
public class WordCollectActivity extends AppCompatActivity implements View.OnClickListener,Serializable,ItemSelectFragment.onItemClickListener {

    public static final String WORD = "word";
    private static final String TAG = WordCollectActivity.class.getSimpleName();
    private CompositeSubscription mSubscriptions;
    private Word word;
    private ArrayList<WordGroup> wordGroups;
    private String[] wordgroupss;
    private TextView wordgroup;
    private EditText wordedit;
    private int currentgroupposition = Integer.MAX_VALUE;//当前选择的类型
    private WordGroup wordGroup;

    @Inject
    Repository repository;
    private String wordgroupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcollect_act);

        MyApplication.instance.getAppComponent().inject(this);

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        if (getIntent().hasExtra(WORD)) {
            word = (Word) getIntent().getSerializableExtra(WORD);
        }
        wordgroup = (TextView) findViewById(R.id.wordgroup);
        wordedit = (EditText) findViewById(R.id.word);
        if(word != null){
            wordedit.setText(word.getName());
        }
        wordGroups = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_wordcollect);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Subscription subscription = repository.getWordGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<WordGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<WordGroup> list) {
                if(list != null && list.size() > 0){
                    Log.d(TAG,"onNext size:" + list.size());
                    wordGroups.clear();
                    wordGroups.addAll(list);

                    wordgroupss = new String[wordGroups.size()];
                    for (int i = 0; i < wordGroups.size(); i++){
                        wordgroupss[i] = wordGroups.get(i).getName();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void addWordCollect(Word word,String wordGroupId){

        WordCollect wordCollect = new WordCollect();
        wordCollect.setUserId(repository.getUserInfo().getObjectId());
        wordCollect.setWordgroupId(wordGroupId);
        wordCollect.setWordId(word.getObjectId());
        Subscription subscription = repository.addWordCollect(wordCollect).subscribe(new Subscriber<WordCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(WordCollectActivity.this,WordCollectActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(WordCollect wordCollect) {
                Toast.makeText(WordCollectActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wordgroupselect:
                if(wordgroupss != null && wordgroupss.length > 0){
                    ItemSelectFragment tractateTypeFragment = new ItemSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ItemSelectFragment.ITEMCLICKLISTENER,this);
                    bundle.putSerializable(ItemSelectFragment.ITEMS,wordgroupss);
                    bundle.putInt(ItemSelectFragment.FLAG,R.id.tractatetype);
                    tractateTypeFragment.setArguments(bundle);
                    tractateTypeFragment.show(getSupportFragmentManager(),"wordgroup");
                }else{
                    Toast.makeText(this,"获取分组信息失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.affirm:
                if((currentgroupposition == Integer.MAX_VALUE && wordGroup == null) || word == null){
                    Toast.makeText(this,"请选择分组",Toast.LENGTH_SHORT).show();
                }else{
                    String wordGroupId = "";
                    if(wordgroup != null){
                        wordGroupId = wordGroup.getObjectId();
                    }else {
                        wordGroupId = wordGroups.get(currentgroupposition).getObjectId();
                    }
                    addWordCollect(word,wordGroupId);
                }
                break;

            case R.id.createwordgroup:
                //新建单词分组
                showCreateWordGroupDialog();
                break;
        }
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

        this.wordgroupname = name;
        WordGroup createwg = new WordGroup();
        createwg.setUserId(repository.getUserInfo().getObjectId());
        createwg.setName(name);
        createwg.setOpen("false");
        Subscription subscription = repository.addWordGroup(createwg).subscribe(new Subscriber<WordGroup>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                        createWGFail(getString(R.string.wordgroups_nameunique));
                }else{
                    Toast.makeText(WordCollectActivity.this,R.string.networkerror,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(WordGroup wordGroup) {

                if(wordGroup != null){
                    createWGSuccess(wordGroup);
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
    private void createWGSuccess(WordGroup wordGroup){
        this.wordGroup = wordGroup;
        wordgroup.setText(wordgroupname);
        Toast.makeText(this,R.string.createwordgroupsuccess,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClick(int flag, int posion) {

        currentgroupposition = posion;
        wordgroup.setText(wordGroups.get(posion).getName());

    }
}

