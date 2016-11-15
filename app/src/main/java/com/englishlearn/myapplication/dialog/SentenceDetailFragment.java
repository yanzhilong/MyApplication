package com.englishlearn.myapplication.dialog;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.service.MusicService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yanzl on 16-10-23.
 */

public class SentenceDetailFragment extends DialogFragment implements View.OnClickListener{

    public static final String SENTENCE = "sentence";
    private static final int SENTENCEFAVORITE = 0;//收藏句子
    public static final int ITEMSELECT = 1;//选择了句子分组
    public static final String SHOWFAVORITE = "showfavorite";// 是否显示收藏按钮
    private static final String TAG = SentenceDetailFragment.class.getSimpleName();
    public static final String SENTENCEGCOLLECTGROUPID = "sentencecollectgroupid";
    private CompositeSubscription mSubscriptions;

    private boolean showfavorite;
    private Sentence sentence;
    private TextView content;
    private TextView translation;

    private ArrayList<SentenceCollectGroup> sentenceGroups;
    private String[] sentencegroupss;

    private MusicService musicService;
    private Intent musicIntent;

    @Inject
    Repository repository;


    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public static SentenceDetailFragment newInstance(Sentence sentence){

        SentenceDetailFragment sentenceDetailFragment = new SentenceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SENTENCE, sentence);
        sentenceDetailFragment.setArguments(bundle);

        return sentenceDetailFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        getActivity();
        if(bundle != null){
            sentence = (Sentence) bundle.getSerializable(SENTENCE);
            showfavorite = bundle.getBoolean(SHOWFAVORITE,false);
        }
        sentenceGroups = new ArrayList<>();
        sentencegroupss = new String[0];
        MyApplication.instance.getAppComponent().inject(this);
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        if(musicIntent==null){
            musicIntent = new Intent(this.getContext(), MusicService.class);
            getActivity().bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.sentencedetail_frag, container);

        content = (TextView) view.findViewById(R.id.content);
        translation = (TextView) view.findViewById(R.id.translation);
        view.findViewById(R.id.favorite).setOnClickListener(this);
        if(!showfavorite)
        {
            view.findViewById(R.id.favorite).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sentence != null){
            content.setText(sentence.getContent());
            translation.setText(sentence.getTranslation());
        }

        Subscription subscription = repository.getSentenceCollectGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<SentenceCollectGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void onNext(List<SentenceCollectGroup> list) {
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

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick");
                switch (v.getId()){
            case R.id.favorite:
                Log.d(TAG,"favorite");
                showSentenceCollectGroups();
                //this.dismiss();
                break;
            default:
                break;
        }
    }

    private void showSentenceCollectGroups(){
        /*if(sentencegroupss != null){
            SentenceCollectGroupsSelectFragment sentenceCollectGroupsSelectFragment = new SentenceCollectGroupsSelectFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SentenceCollectGroupsSelectFragment.ITEMCLICKLISTENER,this);
            bundle.putSerializable(SentenceCollectGroupsSelectFragment.ITEMS,sentencegroupss);
            bundle.putSerializable(SentenceCollectGroupsSelectFragment.SENTENCE,sentence);
            bundle.putSerializable(SentenceCollectGroupsSelectFragment.SENTENCELIST,sentenceGroups);
            sentenceCollectGroupsSelectFragment.setTargetFragment(this,SENTENCEFAVORITE);
            sentenceCollectGroupsSelectFragment.setArguments(bundle);
            sentenceCollectGroupsSelectFragment.show(this.getFragmentManager(),"sentencegroup");
        }else{
            Toast.makeText(this.getContext(),"获取分组信息失败",Toast.LENGTH_SHORT).show();
        }*/
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SENTENCEFAVORITE:

                switch (resultCode){

                    case ITEMSELECT:
                        String sentencecollectgroupId = data.getStringExtra(SENTENCEGCOLLECTGROUPID);
                        addSentenceCollect(sentencecollectgroupId);
                        break;
                }

                break;
        }
    }

    public void addSentenceCollect(String sentencecollectgroupId){
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

}
