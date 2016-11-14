package com.englishlearn.myapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yanzl on 16-11-2.
 */
public class SentenceCollectGroupsSelectFragment extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String ITEMS = "tractatetype";
    public static final String SENTENCELIST = "sentencegrouplist";
    public static final String SENTENCE = "sentence";
    public static final String ITEMCLICKLISTENER = "ItemClickListener";
    public static final String FLAG = "flag";//用于一个界面区分不同的点击事件

    private Sentence sentence;
    TextView createGroup;
    String[] tractatetype = null;
    private ArrayList<SentenceCollectGroup> sentenceGroups;
    ListView mylist;
    private int flag = 0;
    private onItemClickListener onItemClickListener;
    @Inject
    Repository repository;
    private CompositeSubscription mSubscriptions;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            onItemClickListener = (SentenceCollectGroupsSelectFragment.onItemClickListener) bundle.getSerializable(ITEMCLICKLISTENER);
            tractatetype = bundle.getStringArray(ITEMS);
            sentenceGroups = (ArrayList<SentenceCollectGroup>) bundle.getSerializable(SENTENCELIST);
            sentence = (Sentence) bundle.getSerializable(SENTENCE);
            flag = bundle.getInt(FLAG);
        }
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sentencecollectgroupsselect_frag, null, false);
        createGroup = (TextView) view.findViewById(R.id.createGroup);
        createGroup.setOnClickListener(this);
        mylist = (ListView) view.findViewById(R.id.listview);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter< String > adapter = new ArrayAdapter< String >(getActivity(),
                android.R.layout.simple_list_item_1, tractatetype);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        /*if(onItemClickListener != null){
            onItemClickListener.onItemClick(flag,position);
        }*/
        Intent intent = getActivity().getIntent();
        intent.putExtra(SentenceDetailFragment.SENTENCEGCOLLECTGROUPID,sentenceGroups.get(position).getObjectId());
        getTargetFragment().onActivityResult(getTargetRequestCode(), SentenceDetailFragment.ITEMSELECT,intent);
        this.dismiss();

        //addSentenceCollect(sentenceGroups.get(position).getObjectId());
        //dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createGroup:
                showCreageGroup();
                //this.dismiss();
                break;
        }
    }

    private void showCreageGroup(){

        CreateSentenceCollectGroupFragment createSentenceCollectGroupFragment = new CreateSentenceCollectGroupFragment();
        createSentenceCollectGroupFragment.setCreateListener(new CreateSentenceCollectGroupFragment.CreateListener() {
            @Override
            public void onClick(String name) {
                createSentenceCollectGroup(name);
            }
        });
        createSentenceCollectGroupFragment.show(getFragmentManager(),"create");
    }


    /**
     * 创建词单
     */
    private void createSentenceCollectGroup(String name){

        SentenceCollectGroup sentenceCollectGroup = new SentenceCollectGroup();
        sentenceCollectGroup.setUserId(repository.getUserInfo().getObjectId());
        sentenceCollectGroup.setName(name);
        Subscription subscription = repository.addSentenceCollectGroup(sentenceCollectGroup)
                .subscribe(new Subscriber<SentenceCollectGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof BmobRequestException){
                            if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                                createWGFail(getString(R.string.nameunique));
                        }else{
                            Toast.makeText(SentenceCollectGroupsSelectFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(SentenceCollectGroup sentenceCollectGroup) {

                        if(sentenceCollectGroup != null) {
                            addSentenceCollect(sentenceCollectGroup.getObjectId());
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }


    //创建失败
    private void createWGFail(String message){

        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    //创建成功
    private void createWGSuccess(){

        Toast.makeText(this.getContext(),R.string.createwordgroupsuccess,Toast.LENGTH_SHORT).show();

    }


    public void addSentenceCollect(String sentencecollectgroupId){

        Intent intent = getActivity().getIntent();
        intent.putExtra(SentenceDetailFragment.SENTENCEGCOLLECTGROUPID,sentencecollectgroupId);
        getTargetFragment().onActivityResult(getTargetRequestCode(), SentenceDetailFragment.ITEMSELECT,intent);
        this.dismiss();

        if(1 > 0){
            return;
        }
        SentenceCollect sentenceCollect = new SentenceCollect();
        sentenceCollect.setUserId(repository.getUserInfo().getObjectId());
        sentenceCollect.setSentenceId(sentence.getObjectId());
        sentenceCollect.setScollectgroupId(sentencecollectgroupId);
        Subscription subscription = repository.addSentenceCollectByNotSelf(sentenceCollect).subscribe(new Subscriber<SentenceCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    favoriteFail(bmobRequestException.getMessage());
                }else{
                    favoriteFail(getString(R.string.unfavoritefail));
                }
            }

            @Override
            public void onNext(SentenceCollect sentenceCollect) {
                if(sentenceCollect != null){
                    favoriteSuccess();
                }else {
                    favoriteFail(getString(R.string.unfavoritefail));
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void favoriteSuccess(){
        Toast.makeText(this.getContext(),R.string.favoritesuccess,Toast.LENGTH_SHORT).show();
    }

    private void favoriteFail(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    public interface onItemClickListener extends Serializable{
        void onItemClick(int flag, int posion);
    }
}
