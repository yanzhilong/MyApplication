package com.englishlearn.myapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yanzl on 16-11-2.
 */
public class SentenceCollectGroupsSelectFragment extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String SENTENCEGCOLLECTGROUPID = "sentencecollectgroupid";
    private static final String TAG = SentenceCollectGroupsSelectFragment.class.getSimpleName();

    TextView createGroup;
    private ArrayList<SentenceCollectGroup> sentenceGroups;
    private String[] sentencegrouparray;
    private ListView listView;
    @Inject
    Repository repository;
    private CompositeSubscription mSubscriptions;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        sentenceGroups = new ArrayList<>();
        sentencegrouparray = new String[0];
        MyApplication.instance.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sentencecollectgroupsselect_frag, null, false);
        createGroup = (TextView) view.findViewById(R.id.createGroup);
        createGroup.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.listview);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSentenceCollectGroups();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter(sentencegrouparray);
    }

    private void setAdapter(String[] valuse){
        ArrayAdapter< String > adapter = new ArrayAdapter< String >(getActivity(),
                android.R.layout.simple_list_item_1, valuse);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(SENTENCEGCOLLECTGROUPID,sentenceGroups.get(position).getObjectId());
        getTargetFragment().onActivityResult(getTargetRequestCode(),0,intent);
        this.dismiss();
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

        if(name.isEmpty()){
            Toast.makeText(this.getContext(),"名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
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


    private void getSentenceCollectGroups(){


        Subscription subscription = repository.getSentenceCollectGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<SentenceCollectGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,e.toString());
                Toast.makeText(SentenceCollectGroupsSelectFragment.this.getContext(),"获取分组信息失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<SentenceCollectGroup> list) {
                if(list != null){
                    Log.d(TAG,"onNext size:" + list.size());
                    sentenceGroups.clear();
                    sentenceGroups.addAll(list);
                    sentencegrouparray = new String[sentenceGroups.size()];
                    for (int i = 0; i < sentenceGroups.size(); i++){
                        sentencegrouparray[i] = sentenceGroups.get(i).getName();
                    }
                    setAdapter(sentencegrouparray);
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
    }

    private void favoriteSuccess(){
        Toast.makeText(this.getContext(),R.string.favoritesuccess,Toast.LENGTH_SHORT).show();
    }

    private void favoriteFail(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
