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
import com.englishlearn.myapplication.activityforresult.multiple.MultipleActivity;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateCollect;
import com.englishlearn.myapplication.data.TractateCollectGroup;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.DeleteConfirmFragment;
import com.englishlearn.myapplication.dialog.SentenceCollectGroupsSelectFragment;
import com.englishlearn.myapplication.dialog.UpdateWordGroupFragment;
import com.englishlearn.myapplication.sentencegroups.SentenceGroupsActivity;
import com.englishlearn.myapplication.tractategroup.addtractate.AddTractateHelper;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.englishlearn.myapplication.sentencegroups.sentences.SentencesFragment.REQUESTCODE;
import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TRACTATECOLLECTGROUP;
import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TRACTATEGROUP;
import static com.englishlearn.myapplication.tractategroup.tractates.TractatesActivity.TYPE;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractatesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TractatesFragment.class.getSimpleName();
    private static final int TRACTATESFAVORITE = 1;//收藏多条句子

    private TractateGroup tractateGroup;
    private TractateCollectGroup tractateCollectGroup;
    private TractateGroupType type;

    private final int PAGESIZE = 100;
    private MyAdapter myAdapter;
    private int page = 0;
    private ArrayList<Tractate> mList;
    private List<TractateCollect> tractateCollects;
    private List<Tractate> favoritetractates;//待收藏句子列表

    private LinearLayoutManager mgrlistview;
    private CompositeSubscription mSubscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_delete;

    public static TractatesFragment newInstance() {
        return new TractatesFragment();
    }

    @Inject
    Repository repository;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case REQUESTCODE:
                if(resultCode == Activity.RESULT_OK){
                    List<Integer> checkedlist = data.getIntegerArrayListExtra(MultipleActivity.CHECKEDARRAY);
                    List<Tractate> checkedtractates = new ArrayList<>();
                    for(int i = 0; i < mList.size(); i++){
                        if(checkedlist.contains(i)){
                            checkedtractates.add(mList.get(i));
                        }
                    }
                    if(checkedtractates.size() > 0){
                        onTractateSelected(checkedtractates);
                    }
                }
                break;
            case TRACTATESFAVORITE:
                SentenceCollectGroup sentenceCollectGroup = (SentenceCollectGroup) data.getSerializableExtra(SentenceCollectGroupsSelectFragment.SENTENCEGCOLLECTGROUP);
                favoriteTopSentences(sentenceCollectGroup);
                break;
        }
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

        if(savedInstanceState != null){
            mList = (ArrayList<Tractate>) savedInstanceState.getSerializable("list");
            page = savedInstanceState.getInt("page");
        }else{
            mList = new ArrayList();
        }
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
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
                inflater.inflate(R.menu.menu_sentences_othersgroup, menu);
                break;
            case CREATETGROUP:
                inflater.inflate(R.menu.menu_sentences_createsgroup, menu);
                break;
            case CREATEFTGROUP:
                inflater.inflate(R.menu.menu_sentences_createfsgroup, menu);
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

    //统一方法***************************************************************************************
    //显示多选界面
    private void showMultipleSelect(){

        String[] tractates = new String[mList.size()];
        for(int i = 0; i < mList.size(); i++){
            tractates[i] = mList.get(i).getContent();
        }
        Intent intent = new Intent(this.getContext(),MultipleActivity.class);
        intent.putExtra(MultipleActivity.STRINGARRAY,tractates);
        startActivityForResult(intent,REQUESTCODE);
    }

    //选择多个句子返回
    private void onTractateSelected(List<Tractate> tractates){
        switch (type){

            case OTHERTGROUP:
                //普通句组
                favoriteTopTractates(tractates);//显示分组信息
                break;
            case CREATETGROUP:
                //删除句子
                deleteTractatesAffirm(tractates);
                break;
            case CREATEFTGROUP:
                //删除句子
                deleteSentenceCollectsAffirm(tractates);
                break;
        }
    }


    /**
     * 收藏热门句单里的多个句子
     */
    private void favoriteTopTractates() {
        if(tractateCollectGroup.getUserId().getObjectId().equals(repository.getUserInfo().getObjectId())){
            Toast.makeText(TractatesFragment.this.getContext(),"不能收藏自己创建的句子",Toast.LENGTH_SHORT).show();
        }else{
            showMultipleSelect();
        }
    }

    /**
     * 收藏热门句单里的多个句子
     */
    private void favoriteTopTractates(List<Tractate> tractates) {
        this.favoritetractates = tractates;
        if(tractateGroup.getUserId().getObjectId().equals(repository.getUserInfo().getObjectId())){
            Toast.makeText(TractatesFragment.this.getContext(),"不能收藏自己创建的句子",Toast.LENGTH_SHORT).show();
        }else{
            SentenceCollectGroupsSelectFragment sentenceCollectGroupsSelectFragment = new SentenceCollectGroupsSelectFragment();
            Bundle bundle = new Bundle();
            sentenceCollectGroupsSelectFragment.setTargetFragment(this, TRACTATESFAVORITE);
            sentenceCollectGroupsSelectFragment.setArguments(bundle);
            sentenceCollectGroupsSelectFragment.show(this.getFragmentManager(),"sentencecollectgroup");
        }
    }

    /**
     * 批量收藏句子到指定句子收藏分组
     * @param tractateCollectGroup
     */
    private void favoriteTopTractates(TractateCollectGroup tractateCollectGroup){

        final List<TractateCollect> tractateCollects = new ArrayList<>();
        for(int i = 0; i < favoritetractates.size(); i++){
            TractateCollect tractateCollect = new TractateCollect();
            User user = repository.getUserInfo();
            tractateCollect.setUserId(user);
            Tractate tractate = favoritetractates.get(i);
            tractateCollect.setTractateId(tractate);
            tractateCollectGroup.setPointer();
            tractateCollect.setTractateCollectGroupId(tractateCollectGroup);
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
                    Toast.makeText(TractatesFragment.this.getContext(),bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TractatesFragment.this.getContext(),TractatesFragment.this.getContext().getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(TractatesFragment.this.getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    //我创建的分组列表进来的******************************************************************************

    /**
     * 修改句组名称
     */
    private void updateTractateGroup(){
        Bundle bundle = new Bundle();
        bundle.putString(UpdateWordGroupFragment.TITLE,"修改句组名称");
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setArguments(bundle);
        updateWordGroupFragment.setOldName(tractateGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener()
        {
            @Override
            public void onUpdate(String name) {
                updateTractateGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(),"update");
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

    /**
     * 删除创建的句组
     */
    private void deleteTractateGroup() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteTractateGroup(tractateGroup);
            }
        });
        delete.show(getFragmentManager(),"delete");
    }


    //删除句子分组
    private void deleteTractateGroup(TractateGroup tractateGroup){
        Subscription subscription = repository.deleteTractateGroupRxById(tractateGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
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
                deleteGroupSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //批量删除句子
    private void deleteTractates(){
        showMultipleSelect();
    }

    //批量删除句子确认
    private void deleteTractatesAffirm(final List<Tractate> tractates){

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteTractates(tractates);
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    //批量删除句子
    private void deleteTractates(List<Tractate> tractates){

        Subscription subscription = repository.deleteTractates(tractates).subscribe(new Subscriber<Boolean>() {
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


    //我创建的收藏句组******************************************************************************

    /**
     * 修改句组名称
     */
    private void updateSentenceCollectGroup(){
        Bundle bundle = new Bundle();
        bundle.putString(UpdateWordGroupFragment.TITLE,"修改句组名称");
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setArguments(bundle);
        updateWordGroupFragment.setOldName(sentenceCollectGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener()
        {
            @Override
            public void onUpdate(String name) {
                updateSentenceCollectGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(),"update");
    }

    /**
     * 更新句子分组
     */
    private void updateSentenceCollectGroup(final String name){

        SentenceCollectGroup updatesentenceCollectGroup = new SentenceCollectGroup();
        updatesentenceCollectGroup.setObjectId(sentenceCollectGroup.getObjectId());
        updatesentenceCollectGroup.setName(name);
        Subscription subscription = repository.updateSentenceCollectGroupRxById(updatesentenceCollectGroup).subscribe(new Subscriber<Boolean>() {
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
                sentenceCollectGroup.setName(name);
                Toast.makeText(TractatesFragment.this.getContext(),R.string.updatewordgroupsuccess,Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }




    //删除收藏句组确认
    private void deleteSentenceCollectGroupAffirm(){

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceCollectGroup(sentenceCollectGroup);
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    //删除收藏的句子分组
    private void deleteSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup){

        Subscription subscription = repository.deleteSentenceCollectGroupRxById(sentenceCollectGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
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

    //批量删除收藏的句子
    private void deleteTractateCollects(){

        showMultipleSelect();
    }

    //批量删除收藏句子确认
    private void deleteSentenceCollectsAffirm(final List<Sentence> sentenceCollects){

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteTractateCollects(sentenceCollects);
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    //批量删除句子
    private void deleteTractateCollects(List<Tractate> tractates){

        List<SentenceCollect> list = new ArrayList<>();
        for(int i = 0; i < tractateCollects.size(); i++){
            for(int j = 0; j < tractates.size(); j++){
                if(tractateCollects.get(i).getSentence().getObjectId().equals(tractates.get(j).getObjectId())){
                    list.add(tractateCollects.get(i));
                }
            }
        }
        Subscription subscription = repository.deleteSentenceCollects(list).subscribe(new Subscriber<Boolean>() {
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




    //菜单事件****************************************************************************************************
    //其它人的句组
    private boolean othersgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.favorite_tractates:
                Log.d(TAG, "收藏文章");
                favoriteTopTractates();
                break;
        }
        return true;
    }


    //我创建的句组
    private boolean createsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.delete_tractates:
                Log.d(TAG, "删除文章");
                deleteTractates();
                break;
        }
        return true;
    }

    //我创建的收藏句组
    private boolean createfsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.delete_tractates:
                Log.d(TAG, "删除文章");
                deleteTractateCollects();
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
        Intent intent = new Intent(this.getContext(),SentenceGroupsActivity.class);
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
                        updateTractateGroup();
                        break;
                    case CREATEFTGROUP:
                        updateSentenceCollectGroup();
                        break;
                }
                break;
            case R.id.fab_delete:
                switch (type){
                    case CREATETGROUP:
                        deleteTractateGroup();
                        break;
                    case CREATEFTGROUP:
                        deleteSentenceCollectGroupAffirm();
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


