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

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.source.Repository;
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

public class SentenceDetailFragment extends DialogFragment{

    public static final String SENTENCE = "sentence";
    private static final int SENTENCEFAVORITE = 0;//收藏句子
    public static final int ITEMSELECT = 1;//选择了句子分组
    private static final String TAG = SentenceDetailFragment.class.getSimpleName();
    private CompositeSubscription mSubscriptions;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SENTENCEFAVORITE:

                switch (resultCode){

                    case ITEMSELECT:
                       // String sentencecollectgroupId = data.getStringExtra(SENTENCEGCOLLECTGROUPID);
                       // addSentenceGroup(sentencecollectgroupId);
                        break;
                }

                break;
        }
    }


}
