package com.englishlearn.myapplication.tractategroup.tractates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.adapter.MultipleAdapter;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateCollectGroup;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.tractategroup.TractateGroupsActivity;
import com.englishlearn.myapplication.tractategroup.addtractate.AddTractateHelper;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TRACTATECOLLECTGROUP;
import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TRACTATEGROUP;
import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TYPE;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractatesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TractatesFragment.class.getSimpleName();
    private static final int TRACTATESFAVORITE = 1;//收藏多条句子


    private final int FAVORITEFLAG = 11;//收藏多个文章
    private static final int SELECTTCG = 12;//选择收藏分组
    private static final int CREATECOLGROUP = 13;//创建新的收藏分组
    private final int DELETECOLLECTS = 21;//删除收藏的句子
    private final int UPDATESCOLLECTN = 22;//修改收藏分组名
    private final int DELETECOLLECTG =23;//删除收藏分组
    private final int DELETETRACTATE =31;//删除自己的文章
    private final int UPDATETGROUP =32;//修改文章分组名称
    private final int DELETETGROUP =33;//删除文章分组

    private TractateGroup tractateGroup;
    private TractateCollectGroup tractateCollectGroup;
    private TractateGroupType type;

    private final int PAGESIZE = 100;
    private MyAdapter myAdapter;
    private int page = 0;
    private ArrayList<Tractate> mList;
    private List<TractateCollect> tractateCollects;
    private List<TractateCollect> seletedTractateCollects;
    private List<Tractate> seletedTractates;
    private List<Tractate> favoritetractates;//待收藏句子列表
    private ArrayList<TractateCollectGroup> tractateCollectGroups;

    private LinearLayoutManager mgrlistview;
    private CompositeSubscription mSubscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_delete;

    public static TractatesFragment newInstance() {
        return new TractatesFragment();
    }

    private TractateCollectMultipleAdapter tractateCollectMultipleAdapter;
    private TractatesMultipleAdapter tractatesMultipleAdapter;

    @Inject
    Repository repository;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tractatesMultipleAdapter.onActivityResult(requestCode,resultCode,data);
        tractateCollectMultipleAdapter.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"onCreate" + "savedInstanceState" + savedInstanceState);
        MyApplication.instance.getAppComponent().inject(this);


        Bundle bundle = getArguments();
        if(bundle != null){
            tractateCollectGroup = (TractateCollectGroup) bundle.getSerializable(TRACTATECOLLECTGROUP);
            tractateGroup = (TractateGroup) bundle.getSerializable(TRACTATEGROUP);
            type = (TractateGroupType) bundle.getSerializable(TYPE);
        }

        tractateCollects = new ArrayList<>();
        favoritetractates = new ArrayList<>();
        seletedTractates = new ArrayList<>();
        seletedTractateCollects = new ArrayList<>();
        tractateCollectGroups = new ArrayList<>();
        if(savedInstanceState != null){
            mList = (ArrayList<Tractate>) savedInstanceState.getSerializable("list");
            page = savedInstanceState.getInt("page");
        }else{
            mList = new ArrayList();
        }
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        tractateCollectMultipleAdapter = new TractateCollectMultipleAdapter(this,getActivity());
        tractatesMultipleAdapter = new TractatesMultipleAdapter(this,getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.tractates_frag, container, false);

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        mgrlistview = new LinearLayoutManager(this.getContext());
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        //GridLayout 3列
        GridLayoutManager mgrgridview = new GridLayoutManager(this.getContext(), 3);

        recyclerView.setLayoutManager(mgrlistview);

        //recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Tractate tractate = myAdapter.getTractates().get(position);
                Intent intent = new Intent(TractatesFragment.this.getContext(), TractateDetailActivity.class);
                intent.putExtra(TractateDetailActivity.TRACTATE, tractate);
                startActivity(intent);
                Log.d(TAG, tractate.toString());

            }

        });

        myAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getNext();
            }
        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);


        fab_edit = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(this);

        fab_delete = (FloatingActionButton) getActivity().findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(this);


        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "下拉刷新");
                refershList();
            }
        });

        refershList();
        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState");
        outState.putSerializable("list",mList);
        outState.putInt("page",page);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        switch (type){

            case OTHERTGROUP:
                //普通句组
                inflater.inflate(R.menu.menu_tractates_othersgroup, menu);
                break;
            case CREATETGROUP:
                inflater.inflate(R.menu.menu_tractates_createsgroup, menu);
                break;
            case CREATEFTGROUP:
                inflater.inflate(R.menu.menu_tractates_createfsgroup, menu);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (type){

            case OTHERTGROUP:
                //普通句组
                return othersgroup(item);
            case CREATETGROUP:
                return createsgroup(item);
            case CREATEFTGROUP:
                return createfsgroup(item);
        }
        return true;
    }



    @Override
    public void onStart() {
        super.onStart();
        showList(mList);
    }

    @Override
    public void onPause() {
        super.onPause();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 刷新列表
     */
    public void refershList() {
        page = 0;
        mList.clear();
        myAdapter.replaceData(mList);
        tractateCollects.clear();
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNext();

    }

    private void getNext(){
        switch (type){
            case CREATETGROUP:
            case OTHERTGROUP:
                getNextPage();
                break;
            case CREATEFTGROUP:
                getNextCollectPage();
                break;
        }
    }


    //获取下一页
    public void getNextPage() {

        Subscription subscription = repository.getTractateRxByTractateGroupId(tractateGroup.getObjectId(), page, PAGESIZE).subscribe(new Subscriber<List<Tractate>>() {
            @Override
            public void onCompleted() {
                loadingComplete();
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List list) {
                Log.d(TAG, "onNext size:" + list.size());

                if (list == null || list.size() == 0) {
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                } else {
                    page++;//页数增加
                    mList.addAll(list);
                    showList(mList);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    //获取下一页
    public void getNextCollectPage() {

        Subscription subscription = repository.getTractateCollectRxByTractateCollectGroupId(tractateCollectGroup.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<TractateCollect>>() {
            @Override
            public void onCompleted() {
                loadingComplete();
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List<TractateCollect> list) {
                Log.d(TAG,"onNext size:" + list.size());

                if(list == null || list.size() == 0){
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                }else{
                    tractateCollects.addAll(list);
                    for(int i = 0; i < tractateCollects.size(); i++){
                        mList.add(tractateCollects.get(i).getTractateId());
                    }
                    page++;//页数增加
                    showList(mList);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    private class TractatesMultipleAdapter extends MultipleAdapter<TractateGroup,Tractate>{

        public TractatesMultipleAdapter(Fragment fragment, Activity activity) {
            super(fragment, activity);
        }

        @Override
        protected TractateGroup getListGroup() {
            return tractateGroup;
        }

        @Override
        protected void operationListGroup(TractateGroup tractateGroup, int flag) {
        }

        @Override
        protected List<Tractate> getList() {
            return mList;
        }

        @Override
        protected void operationList(List<Tractate> list, TractateGroup group, int tag) {

            if(tag == FAVORITEFLAG){
                favoriteTopTractates(list);
            }else if(tag == DELETETRACTATE){
                seletedTractates = list;
                showConfirmDialog(DELETETRACTATE,"确认删除所有文章?");
            }
        }

        @Override
        protected boolean selectCreate(int flag) {

            if(flag == SELECTTCG){
                this.createAndUpdate("创建新的收藏分组",null,CREATECOLGROUP);
            }
            return false;
        }

        @Override
        protected boolean selectResult(int position, int flag) {

            if(flag == SELECTTCG){
                TractateCollectGroup tractateCollectGroup = tractateCollectGroups.get(position);
                favoriteTractates(favoritetractates,tractateCollectGroup);
            }

            return false;
        }

        @Override
        protected String getMultipleItemName(Tractate o) {
            return o.getTitle();
        }

        @Override
        protected void createComplete(String name,int createflag) {

            if(createflag == CREATECOLGROUP){
                createTractateCollectGroup(name);
            }else if(createflag == UPDATETGROUP){
                updateTractateGroup(name);
            }

        }

        @Override
        protected void confirm(int confirmflag) {

            if(confirmflag == DELETETRACTATE){
                deleteTractates(seletedTractates);
            }else if(confirmflag == DELETETGROUP){
                deleteTractateGroup(tractateGroup);
            }
        }
    }




    private class TractateCollectMultipleAdapter extends MultipleAdapter<TractateCollectGroup,TractateCollect>{

        public TractateCollectMultipleAdapter(Fragment fragment, Activity activity) {
            super(fragment, activity);
        }

        @Override
        protected TractateCollectGroup getListGroup() {
            return tractateCollectGroup;
        }

        @Override
        protected void operationListGroup(TractateCollectGroup tractateCollectGroup, int flag) {

        }

        @Override
        protected List<TractateCollect> getList() {
            return tractateCollects;
        }

        @Override
        protected void operationList(List<TractateCollect> list, TractateCollectGroup group, int tag) {
            if(tag == DELETECOLLECTS){
                seletedTractateCollects.clear();
                seletedTractateCollects.addAll(list);
                showConfirmDialog(DELETECOLLECTS,"删除选中收藏？");
            }
        }

        @Override
        protected boolean selectCreate(int flag) {
            return false;
        }

        @Override
        protected boolean selectResult(int position, int flag) {
            return false;
        }

        @Override
        protected String getMultipleItemName(TractateCollect o) {
            return o.getTractateId().getTitle();
        }

        @Override
        protected void createComplete(String name,int createflag) {

            if(createflag == UPDATESCOLLECTN){
                updateTractateCollect(name);
            }
        }

        @Override
        protected void confirm(int confirmflag) {
            if(confirmflag == DELETECOLLECTS){
                deleteTractateCollects(seletedTractateCollects);
            }else if(confirmflag == DELETECOLLECTG){
                deleteTractateCollectGroup();
            }
        }
    }

    //删除收藏分组
    private void deleteTractateCollectGroup() {

        Subscription subscription = repository.deleteTractateCollectGroupRxById(tractateCollectGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                }else{
                    deleteFail(getContext().getString(R.string.networkerror));
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                deleteGroupSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //修改收藏分组名称
    private void updateTractateCollect(final String name) {

        final TractateCollectGroup tractateCollectGroupupdate = new TractateCollectGroup();
        tractateCollectGroupupdate.setObjectId(tractateCollectGroup.getObjectId());
        tractateCollectGroupupdate.setName(name);
        Subscription subscription = repository.updateTractateCollectGroupRxById(tractateCollectGroupupdate).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(getContext(),bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                tractateCollectGroup.setName(name);
                Toast.makeText(getContext(),R.string.updatewordgroupsuccess,Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    //获得文章收藏分组
    private void getTractateCollectGroups(){


        Subscription subscription = repository.getTractateCollectGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<TractateCollectGroup>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,e.toString());
                Toast.makeText(TractatesFragment.this.getContext(),"获取分组信息失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<TractateCollectGroup> list) {
                if(list != null){
                    Log.d(TAG,"onNext size:" + list.size());
                    tractateCollectGroups.clear();
                    tractateCollectGroups.addAll(list);
                    String[] tractatecollectgrouparray = new String[tractateCollectGroups.size()];
                    for (int i = 0; i < tractateCollectGroups.size(); i++){
                        tractatecollectgrouparray[i] = tractateCollectGroups.get(i).getName();
                    }

                    tractatesMultipleAdapter.notifyDataSetChanged(tractatecollectgrouparray);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 创建文章收藏分组
     */
    private void createTractateCollectGroup(String name){

        if(name.isEmpty()){
            Toast.makeText(this.getContext(),"名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        TractateCollectGroup tractateCollectGroup = new TractateCollectGroup();
        tractateCollectGroup.setUserId(repository.getUserInfo());
        tractateCollectGroup.setName(name);
        Subscription subscription = repository.addTractateCollectGroup(tractateCollectGroup)
                .subscribe(new Subscriber<TractateCollectGroup>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof BmobRequestException){
                            if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401)
                                Toast.makeText(getContext(),getString(R.string.nameunique),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNext(TractateCollectGroup tractateCollectGroup) {

                        if(tractateCollectGroup != null) {
                            //addSentenceCollect(sentenceCollectGroup.getObjectId());
                            tractateCollectGroups.add(tractateCollectGroup);
                            favoriteTractates(favoritetractates,tractateCollectGroup);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }


    /**
     * 收藏多个文章
     */
    private void favoriteTractates(List<Tractate> tractates,TractateCollectGroup tractateCollectGroup) {

        final List<TractateCollect> tractateCollects = new ArrayList<>();
        for(int i = 0; i < tractates.size(); i++){
            TractateCollect tractateCollect = new TractateCollect();
            User user = repository.getUserInfo();
            tractateCollect.setUserId(user);
            Tractate tractate = tractates.get(i);
            tractateCollect.setTractateId(tractate);
            tractateCollect.setTractateCollectgId(tractateCollectGroup);
            tractateCollects.add(tractateCollect);
        }

        Subscription subscription = repository.addTractateCollects(tractateCollects).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(getContext(),bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),getContext().getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    /**
     * 收藏热门句单里的多个句子
     */
    private void favoriteTopTractates(List<Tractate> tractates) {
        this.favoritetractates = tractates;
        tractatesMultipleAdapter.selectItems(SELECTTCG);
        //查找收藏分组
        getTractateCollectGroups();
    }


    /**
     * 更新句子分组
     */
    private void updateTractateGroup(final String name){

        TractateGroup updatetractateGroup = new TractateGroup();
        updatetractateGroup.setObjectId(tractateGroup.getObjectId());
        updatetractateGroup.setName(name);
        Subscription subscription = repository.updateTractateGroupRxById(updatetractateGroup).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    updateWGFail(bmobRequestException.getMessage());
                }else{
                    Toast.makeText(TractatesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                tractateGroup.setName(name);
                Toast.makeText(TractatesFragment.this.getContext(),R.string.updatewordgroupsuccess,Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    //删除句子分组
    private void deleteTractateGroup(TractateGroup tractateGroup){
        Subscription subscription = repository.deleteTractateGroupRxById(tractateGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                }else{
                    Toast.makeText(TractatesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteGroupSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //批量删除文章
    private void deleteTractates(List<Tractate> tractates){

        Subscription subscription = repository.deleteTractates(tractates).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                }else{
                    Toast.makeText(TractatesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //菜单事件****************************************************************************************************
    //其它人的句组
    private boolean othersgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.favorite_tractates:
                Log.d(TAG, "收藏文章");
                //favoriteTopTractates();
                if(tractateGroup.getUserId().getObjectId().equals(repository.getUserInfo().getObjectId())){
                    Toast.makeText(TractatesFragment.this.getContext(),"不能收藏自己创建的文章",Toast.LENGTH_SHORT).show();
                }else{
                    tractatesMultipleAdapter.operationList(FAVORITEFLAG);
                }
                break;
        }
        return true;
    }


    //我创建的句组
    private boolean createsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.delete_tractates:
                Log.d(TAG, "删除文章");
                //deleteTractates();
                tractatesMultipleAdapter.operationList(DELETETRACTATE);
                break;
        }
        return true;
    }

    //我创建的收藏句组
    private boolean createfsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.delete_tractates:
                Log.d(TAG, "删除文章");
                //deleteTractateCollects();
                tractateCollectMultipleAdapter.operationList(DELETECOLLECTS);
                break;
        }
        return true;
    }


    //删除失败
    private void deleteFail(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }


    //删除分组成功
    private void deleteGroupSuccess(){
        deleteSuccess();
        Intent intent = new Intent(this.getContext(),TractateGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //删除成功
    private void deleteSuccess(){
        Toast.makeText(this.getContext(),R.string.deletesuccess,Toast.LENGTH_SHORT).show();
    }

    //创建失败
    private void updateWGFail(String message){

        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }


    //批量删除文章
    private void deleteTractateCollects(List<TractateCollect> tractateCollects){

        Subscription subscription = repository.deleteTractateCollects(tractateCollects).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                }else{
                    Toast.makeText(TractatesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }


    //加载完成
    public void loadingComplete() {
        Log.d(TAG, "loadingComplete");
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载失败
     */
    public void loadingFail(Throwable e) {
        Log.d(TAG, "loadingFail:" + e.toString());
        e.printStackTrace();
    }

    //显示列表
    private void showList(List list) {
        Log.d(TAG, "showList:" + list.toString());
        myAdapter.replaceData(list);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_edit:
                //showUpdateWordGroupDialog();
                switch (type){
                    case CREATETGROUP:
                        tractatesMultipleAdapter.createAndUpdate("修改当前分组",tractateGroup.getName(),UPDATETGROUP);
                        break;
                    case CREATEFTGROUP:
                        tractateCollectMultipleAdapter.createAndUpdate("修改收藏分组",tractateCollectGroup.getName(),UPDATESCOLLECTN);
                        break;
                }
                break;
            case R.id.fab_delete:
                switch (type){
                    case CREATETGROUP:
                        tractatesMultipleAdapter.showConfirmDialog(DELETETGROUP,"删除当前分组?");
                        break;
                    case CREATEFTGROUP:
                        tractateCollectMultipleAdapter.showConfirmDialog(DELETECOLLECTG,"删除当前分组");
                        break;
                }
                break;
            default:
                break;
        }
    }


    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //加载更多接口
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class MyAdapter extends RecyclerView.Adapter {

        private boolean isGone = false;//是否加载完成
        private OnLoadMoreListener mOnLoadMoreListener;
        private List<Tractate> tractates;
        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            tractates = new ArrayList<>();
        }

        //已经加载完成了
        public void loadingGone() {
            isGone = true;
        }

        //还有更多
        public void hasMore() {
            isGone = false;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        public List<Tractate> getTractates() {
            return tractates;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<Tractate> tractates) {
            if (tractates != null) {
                this.tractates.clear();
                this.tractates.addAll(tractates);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position != tractates.size()) {
                Log.d(TAG, "wordgroupstop_item");
                return R.layout.tractates_frag_item;
            } else {
                if (isGone) {
                    Log.d(TAG, "load_done_layout");
                    return R.layout.tractates_frag_loaddone_item;
                }
                Log.d(TAG, "load_more_layout");
                return R.layout.tractates_frag_loadmore_item;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            switch (viewType) {
                case R.layout.tractates_frag_item:
                    return new ItemViewHolder(v);
                case R.layout.tractates_frag_loadmore_item:
                    return new LoadingMoreViewHolder(v);
                case R.layout.tractates_frag_loaddone_item:
                    return new LoadingGoneViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                String title = tractates.get(position).getTitle().replace(AddTractateHelper.MARK,"");

                itemViewHolder.name.setText(title);
            } else if (holder instanceof LoadingMoreViewHolder && mOnLoadMoreListener != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnLoadMoreListener.onLoadMore();
                    }
                }, 100);
            }
        }

        @Override
        public int getItemCount() {
            return tractates.size() + 1;
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(itemView, getAdapterPosition());
                        }
                    }
                });

                name = (TextView) itemView.findViewById(R.id.name);
            }
        }

        //加载更多
        class LoadingMoreViewHolder extends RecyclerView.ViewHolder {

            public LoadingMoreViewHolder(View itemView) {
                super(itemView);
            }
        }

        //加载完成
        class LoadingGoneViewHolder extends RecyclerView.ViewHolder {

            public LoadingGoneViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}


