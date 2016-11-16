package com.englishlearn.myapplication.sentencegroups.sentences;

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
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.bmob.BatchResult;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDefaultError;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.DeleteConfirmFragment;
import com.englishlearn.myapplication.dialog.SentenceCollectGroupsSelectFragment;
import com.englishlearn.myapplication.dialog.SentenceDetailFragment;
import com.englishlearn.myapplication.dialog.UpdateWordGroupFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesFragment extends Fragment implements View.OnClickListener {

    private static final int SENTENCEFAVORITE = 1;//收藏句子
    public static final int REQUESTCODE = 0;//多选请求码
    public static final String OBJECT = "object";
    public static final String SENTENCECOLLECTGROUP = "sentencecollectgroup";
    public static final String TYPE = "sentencegrouptype";
    private static final String TAG = SentencesFragment.class.getSimpleName();
    private final int PAGESIZE = 100;
    private SentenceGroup sentenceGroup;
    private SentenceCollectGroup sentenceCollectGroup;//句子收藏分组
    private SentenceGroupType sentenceGroupType;
    private MyAdapter myAdapter;
    private int page = 0;
    private List<Sentence> mList;
    private List<SentenceCollect> sentenceCollects;

    private boolean isShowFavorite = false;
    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;
    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮
    private FloatingActionButton fab_deleteorfavorite_wordgroup;
    private FloatingActionButton fab_edit_wordgroup;
    private List<Sentence> checkedsentences;

    public static SentencesFragment newInstance() {
        return new SentencesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() !=null && getArguments().containsKey(OBJECT)){
            sentenceGroup = (SentenceGroup) getArguments().getSerializable(OBJECT);
            sentenceGroupType = (SentenceGroupType) getArguments().getSerializable(TYPE);
        }

        sentenceCollects = new ArrayList<>();
        if(getArguments() !=null && getArguments().containsKey(SENTENCECOLLECTGROUP)){
            sentenceCollectGroup = (SentenceCollectGroup) getArguments().getSerializable(SENTENCECOLLECTGROUP);
            sentenceGroupType = (SentenceGroupType) getArguments().getSerializable(TYPE);
        }


        switch (sentenceGroupType){

            case OTHERSGROUP:
                //普通句组
                break;
            case CREATESGROUP:
                break;
            case CREATEFSGROUP:
                break;
            case FAVORITESGROUP:
                break;
        }

        MyApplication.instance.getAppComponent().inject(this);

        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case REQUESTCODE:
                if(resultCode == Activity.RESULT_OK){
                    List<Integer> checkedlist = data.getIntegerArrayListExtra(MultipleActivity.CHECKEDARRAY);
                    checkedsentences = new ArrayList<>();
                    for(int i = 0; i < mList.size(); i++){
                        if(checkedlist.contains(i)){
                            checkedsentences.add(mList.get(i));
                        }
                    }
                    if(checkedsentences.size() > 0){
                        onSentencesSelected();
                    }
                }
                break;
            case SENTENCEFAVORITE:

                favoriteSentences((SentenceCollectGroup) data.getSerializableExtra(SentenceCollectGroupsSelectFragment.SENTENCEGCOLLECTGROUP));
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.words_frag, container, false);

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

                Sentence sentence = myAdapter.getSentences().get(position);
                Log.d(TAG, sentence.toString());

                SentenceDetailFragment sentenceDetailFragment = SentenceDetailFragment.newInstance(sentence);
                Bundle bundler = sentenceDetailFragment.getArguments();
                bundler.putBoolean(SentenceDetailFragment.SHOWFAVORITE,isShowFavorite);
                sentenceDetailFragment.setArguments(bundler);
                sentenceDetailFragment.show(getFragmentManager().beginTransaction(), "dialog");


                /*Intent intent = new Intent(SentencesFragment.this.getContext(),WordDetailActivity.class);
                intent.putExtra(WordDetailActivity.TRACTATE,sentence);
                startActivity(intent);*/

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

        fab_edit_wordgroup = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_wordgroup);
        fab_edit_wordgroup.setOnClickListener(this);

        fab_deleteorfavorite_wordgroup = (FloatingActionButton) getActivity().findViewById(R.id.fab_deleteorfavorite_wordgroup);
        fab_deleteorfavorite_wordgroup.setOnClickListener(this);

        switch (sentenceGroupType){
            case OTHERSGROUP:
                fab_edit_wordgroup.setVisibility(View.GONE);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                isShowFavorite = true;
                break;
            case CREATESGROUP:
                fab_edit_wordgroup.setVisibility(View.VISIBLE);
                fab_edit_wordgroup.setBackgroundResource(R.drawable.ic_edit);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_delete);
                isShowFavorite = false;
                break;
            case FAVORITESGROUP:
                fab_edit_wordgroup.setVisibility(View.GONE);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_black_24dp);
                isShowFavorite = false;
                break;
            default:
                break;
        }

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
        refershList();//刷新列表
        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        switch (sentenceGroupType){

            case OTHERSGROUP:
                //普通句组
                inflater.inflate(R.menu.menu_sentences_othersgroup, menu);
                break;
            case CREATESGROUP:
                inflater.inflate(R.menu.menu_sentences_createsgroup, menu);
                break;
            case CREATEFSGROUP:
                inflater.inflate(R.menu.menu_sentences_createfsgroup, menu);
                break;
            case FAVORITESGROUP:
                inflater.inflate(R.menu.menu_sentences_favoritesgroup, menu);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (sentenceGroupType){

            case OTHERSGROUP:
                //普通句组
                return othersgroup(item);
            case CREATESGROUP:
                return createsgroup(item);
            case CREATEFSGROUP:
                return createfsgroup(item);
            case FAVORITESGROUP:
                return favoritesgroup(item);
        }
        return true;
    }

    private void onSentencesSelected(){
        switch (sentenceGroupType){

            case OTHERSGROUP:
                //普通句组
                showSentenceCollectGroups();//显示分组信息
                break;
            case CREATESGROUP:
                //删除句子
                showDeleteSentencesConfirm();
                break;
            case CREATEFSGROUP:
                //删除句子
                showDeleteCollectSentencesConfirm();
                break;
            case FAVORITESGROUP:
                showUnFavoriteSentenceGroupCollectConfirm();
                break;
        }
    }

    private void showSentenceCollectGroups(){
        SentenceCollectGroupsSelectFragment sentenceCollectGroupsSelectFragment = new SentenceCollectGroupsSelectFragment();
        Bundle bundle = new Bundle();
        sentenceCollectGroupsSelectFragment.setTargetFragment(this,SENTENCEFAVORITE);
        sentenceCollectGroupsSelectFragment.setArguments(bundle);
        sentenceCollectGroupsSelectFragment.show(this.getFragmentManager(),"sentencegroup");
    }

    private void favoriteSentenGroup(SentenceGroup sentenceGroup){

        SentenceGroupCollect sentenceGroupCollect = new SentenceGroupCollect();
        sentenceGroupCollect.setUser(repository.getUserInfo());
        sentenceGroupCollect.setSentenceGroup(sentenceGroup);
        Subscription subscription = repository.addSentenceGroupCollect(sentenceGroupCollect).subscribe(new Subscriber<SentenceGroupCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(SentencesFragment.this.getContext(),bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(SentencesFragment.this.getContext(),SentencesFragment.this.getContext().getString(R.string.networkerror),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNext(SentenceGroupCollect sentenceGroupCollect1) {

                if(sentenceGroupCollect1 != null){
                    Toast.makeText(SentencesFragment.this.getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SentencesFragment.this.getContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void favoriteSentences(SentenceCollectGroup sentenceCollectGroup){

        final List<SentenceCollect> sentenceCollects = new ArrayList<>();
        for(int i = 0; i < checkedsentences.size(); i++){
            SentenceCollect sentenceCollect = new SentenceCollect();
            sentenceCollect.setUser(repository.getUserInfo());
            sentenceCollect.setSentence(checkedsentences.get(i));
            sentenceCollect.setSentenceCollectGroup(sentenceCollectGroup);
            sentenceCollects.add(sentenceCollect);
        }

        Subscription subscription = repository.addSentenceCollects(sentenceCollects).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    List<BatchResult> list = (List<BatchResult>) bmobRequestException.getObject();
                    StringBuffer stringBuffer = new StringBuffer();
                    if(list.size() == checkedsentences.size()){
                        for(int i = 0; i < checkedsentences.size(); i++){
                            BatchResult batchResult = list.get(i);
                            BmobDefaultError bmobDefaultError = batchResult.getError();
                            if(bmobDefaultError != null){
                                RemoteCode.COMMON createuser = RemoteCode.COMMON.getErrorMessage(bmobDefaultError.getCode());
                                if(createuser == RemoteCode.COMMON.Common_UNIQUE){
                                    String content = checkedsentences.get(i).getContent();
                                    stringBuffer.append(content + "已经收藏在当前分组了" + System.getProperty("line.separator"));
                                }
                            }
                        }
                    }
                    if(stringBuffer.toString().length() > 0){
                        Toast.makeText(SentencesFragment.this.getContext(),stringBuffer.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SentencesFragment.this.getContext(),SentencesFragment.this.getContext().getString(R.string.networkerror),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(SentencesFragment.this.getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }

    private void showMultipleSelect(){

        String[] sentences = new String[mList.size()];
        for(int i = 0; i < mList.size(); i++){
            sentences[i] = mList.get(i).getContent();
        }
        Intent intent = new Intent(this.getContext(),MultipleActivity.class);
        intent.putExtra(MultipleActivity.STRINGARRAY,sentences);
        startActivityForResult(intent,REQUESTCODE);
    }


    //其它人的句组
    private boolean othersgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.favorite_sgroup:
                Log.d(TAG, "收藏句单");
                if(sentenceGroup.getUser().getObjectId().equals(repository.getUserInfo().getObjectId())){
                    Toast.makeText(SentencesFragment.this.getContext(),"不能收藏自己创建的句单",Toast.LENGTH_SHORT).show();
                }else{
                    favoriteSentenGroup(sentenceGroup);
                }
                break;
            case R.id.favorite_sentences:
                Log.d(TAG, "收藏句子");
                if(sentenceGroup.getUser().getObjectId().equals(repository.getUserInfo().getObjectId())){
                    Toast.makeText(SentencesFragment.this.getContext(),"不能收藏自己创建的句子",Toast.LENGTH_SHORT).show();
                }else{
                    showMultipleSelect();
                }
                break;
        }
        return true;
    }

    //我创建的句组
    private boolean createsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.rename_sgroup:
                Log.d(TAG, "修改名称");
                showUpdateWordGroupDialog();
                break;
            case R.id.delete_sgroup:
                Log.d(TAG, "删除句组");
                showDeleteSentenceGroupConfirm();
                break;
            case R.id.delete_sentences:
                Log.d(TAG, "删除句子");
                showMultipleSelect();
                break;
        }
        return true;
    }

    //我创建的收藏句组
    private boolean createfsgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.delete_fsgroup:
                Log.d(TAG, "删除句单");
                showDeleteSentenceCollectGroupConfirm();
                break;
            case R.id.delete_sentences:
                Log.d(TAG, "删除句子");
                showMultipleSelect();
                break;
        }
        return true;
    }

    //我收藏的句组
    private boolean favoritesgroup(MenuItem item){

        switch (item.getItemId()) {
            case R.id.cancel_fsgroup:
                Log.d(TAG, "取消收藏");
                showMultipleSelect();
                break;
        }
        return true;
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
        sentenceCollects.clear();
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNext();

    }

    private void getNext(){
        switch (sentenceGroupType){
            case CREATESGROUP:
            case OTHERSGROUP:
            case FAVORITESGROUP:
                getNextPage();
                break;
            case CREATEFSGROUP:
                getNextCollectPage();
                break;
        }
    }


    //获取下一页
    public void getNextPage() {

        Subscription subscription = repository.getSentencesRxBySentenceGroupId(sentenceGroup.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<Sentence>>() {
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
                Log.d(TAG,"onNext size:" + list.size());

                if(list == null || list.size() == 0){
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                }else{
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

        Subscription subscription = repository.getSentenceCollectRxBySentenceCollectGroupId(sentenceCollectGroup.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<SentenceCollect>>() {
            @Override
            public void onCompleted() {
                loadingComplete();
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List<SentenceCollect> list) {
                Log.d(TAG,"onNext size:" + list.size());

                if(list == null || list.size() == 0){
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                }else{
                    sentenceCollects.addAll(list);
                    for(int i = 0; i < sentenceCollects.size(); i++){
                        mList.add(sentenceCollects.get(i).getSentence());
                    }
                    page++;//页数增加
                    showList(mList);
                }
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


    /**
     * 显示确认删除创建的句组对话框
     */
    private void showDeleteSentenceGroupConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除句组及里面的所有句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceGroup();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    /**
     * 显示确认删除创建的句组对话框
     */
    private void showDeleteSentenceCollectGroupConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceCollectGroup();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }


    /**
     * 显示确认删除对话框
     */
    private void showDeleteSentencesConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"删除选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentences();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    /**
     * 显示确认删除对话框
     */
    private void showUnFavoriteSentenceGroupCollectConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"取水收藏选中的句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                unFavoriteSentenceGroupCollect();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    private void unFavoriteSentenceGroupCollect() {
        
    }

    /**
     * 显示确认删除收藏的句子对话框
     */
    private void showDeleteCollectSentencesConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE,"取消收藏选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                unFavoriteSentences();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }

    /**
     * 显示对话框
     */
    private void showUpdateWordGroupDialog(){
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setOldName(sentenceGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener()
        {
            @Override
            public void onUpdate(String name) {
                updateWordGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(),"update");
    }

    //取消收藏词单
    private void unFavoriteSentences(){

        List<SentenceCollect> list = new ArrayList<>();
        for(int i = 0; i < sentenceCollects.size(); i++){
            for(int j = 0; j < checkedsentences.size(); j++){
                if(sentenceCollects.get(i).getSentence().getObjectId().equals(checkedsentences.get(j).getObjectId())){
                    list.add(sentenceCollects.get(i));
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
                    BmobDefaultError bm = ((BmobRequestException) e).getBmobDefaultError();
                    if(bm != null){
                        deleteFail(bm.getError());
                    }else{
                        deleteFail(RemoteCode.COMMON.getDefauleError().getMessage());
                    }
                }else{
                    Toast.makeText(SentencesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(Boolean b) {
                if(b){
                    deleteSuccess();
                }else{
                    deleteFail("句子删除失败，请稍后再试.");
                }

            }
        });
        mSubscriptions.add(subscription);
    }


    private void deleteSentenceCollectGroup(){
        Subscription subscription = repository.deleteSentenceCollectGroupRxById(sentenceCollectGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    deleteFail(((BmobRequestException)e).getMessage());
                }else{
                    deleteFail(getContext().getString(R.string.networkerror));
                }

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    deleteSuccess();
                }else{
                    deleteFail("删除失败，请确定当前组里面没有收藏的句子");
                }
            }
        });
        mSubscriptions.add(subscription);

    }

    //删除詞单
    private void deleteSentenceGroup(){
        Subscription subscription = repository.deleteSentenceGroupAndSentences(sentenceGroup.getObjectId(),mList).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobDefaultError bm = ((BmobRequestException) e).getBmobDefaultError();
                    if(bm != null){
                        deleteFail(bm.getError());
                    }else{
                        deleteFail(RemoteCode.COMMON.getDefauleError().getMessage());
                    }
                }else{
                    Toast.makeText(SentencesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(Boolean b) {
                if(b){
                    deleteSuccess();
                }else{
                    deleteFail("句子删除失败，请稍后再试.");
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    //删除自己创建的句子
    private void deleteSentences(){
        Subscription subscription = repository.deleteSentences(checkedsentences).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                BmobRequestException bmobRequestException = (BmobRequestException) e;
                List<BatchResult> list = (List<BatchResult>) bmobRequestException.getObject();
                StringBuffer stringBuffer = new StringBuffer();
                if(list.size() == checkedsentences.size()){
                    for(int i = 0; i < checkedsentences.size(); i++){
                        BatchResult batchResult = list.get(i);
                        BmobDefaultError bmobDefaultError = batchResult.getError();
                        if(bmobDefaultError != null){
                            String content = checkedsentences.get(i).getContent();
                            stringBuffer.append(content + "删除失败" + System.getProperty("line.separator"));
                        }
                    }
                }
                if(stringBuffer.toString().length() > 0){
                    Toast.makeText(SentencesFragment.this.getContext(),stringBuffer.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //删除自己收藏的句子
    private void deleteCollectSentences(SentenceCollectGroup sentenceCollectGroup){

        final List<SentenceCollect> sentenceCollects = new ArrayList<>();
        for(int i = 0; i < checkedsentences.size(); i++){
            SentenceCollect sentenceCollect = new SentenceCollect();
            sentenceCollect.setUser(repository.getUserInfo());
            sentenceCollect.setSentence(checkedsentences.get(i));
            sentenceCollect.setSentenceCollectGroup(sentenceCollectGroup);
            sentenceCollects.add(sentenceCollect);
        }

        Subscription subscription = repository.deleteSentenceCollects(sentenceCollects).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                BmobRequestException bmobRequestException = (BmobRequestException) e;
                List<BatchResult> list = (List<BatchResult>) bmobRequestException.getObject();
                StringBuffer stringBuffer = new StringBuffer();
                if(list.size() == checkedsentences.size()){
                    for(int i = 0; i < checkedsentences.size(); i++){
                        BatchResult batchResult = list.get(i);
                        BmobDefaultError bmobDefaultError = batchResult.getError();
                        if(bmobDefaultError != null){
                            String content = checkedsentences.get(i).getContent();
                            stringBuffer.append(content + "取消失败" + System.getProperty("line.separator"));
                        }
                    }
                }
                if(stringBuffer.toString().length() > 0){
                    Toast.makeText(SentencesFragment.this.getContext(),stringBuffer.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 更新词单
     */
    private void updateWordGroup(String name){

        SentenceGroup createsg = new SentenceGroup();
        createsg.setObjectId(sentenceGroup.getObjectId());
        createsg.setUser(repository.getUserInfo());
        createsg.setName(name);
        createsg.setOpen(false);
        Subscription subscription = repository.updateSentenceGroupRxById(createsg).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    if(((BmobRequestException) e).getBmobDefaultError().getCode() == 401){
                        updateWGFail(getString(R.string.words_nameunique));
                    }else{
                        updateWGFail(RemoteCode.COMMON.DEFAULT.getMessage());
                    }
                }else{
                    Toast.makeText(SentencesFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(Boolean b) {
                    updateWGSuccess();
            }
        });
        mSubscriptions.add(subscription);
    }

    //删除失败
    private void deleteFail(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }
    //删除成功
    private void deleteSuccess(){
        Toast.makeText(this.getContext(),R.string.deletesuccess,Toast.LENGTH_SHORT).show();
    }

    //创建失败
    private void updateWGFail(String message){

        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    //创建成功
    private void updateWGSuccess(){

        Toast.makeText(this.getContext(),R.string.updatewordgroupsuccess,Toast.LENGTH_SHORT).show();
    }



    /**
     * 取消收藏
     */
    private void unFavorite(){
        fab_deleteorfavorite_wordgroup.setEnabled(false);
        Subscription subscription = repository.deleteSentenceGroupCollectRxByuserIdAndsentenceGroupId(repository.getUserInfo().getObjectId(),sentenceGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
                unFavoriteResult(false);
            }

            @Override
            public void onNext(Boolean b) {

                unFavoriteResult(true);
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 收藏
     */
    private void favorite(){

        fab_deleteorfavorite_wordgroup.setEnabled(false);
        SentenceGroupCollect sentenceGroupCollect = new SentenceGroupCollect();
        sentenceGroupCollect.setUser(repository.getUserInfo());
        sentenceGroupCollect.setSentenceGroup(sentenceGroup);
        repository.addSentenceGroupCollectByNotSelf(sentenceGroupCollect).subscribe(new Subscriber<SentenceGroupCollect>() {
            @Override
            public void onCompleted() {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    favoriteFail(bmobRequestException.getMessage());
                }else{
                    favoriteFail(getString(R.string.unfavoritefail));
                }

            }

            @Override
            public void onNext(SentenceGroupCollect sentenceGroupCollect1) {

                if(sentenceGroupCollect1 != null){
                    favoriteSuccess();
                }else {
                    favoriteFail(getString(R.string.unfavoritefail));
                }
            }
        });
    }

    private void favoriteSuccess(){
        Toast.makeText(this.getContext(),R.string.favoritesuccess,Toast.LENGTH_SHORT).show();
        fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_black_24dp);
    }

    private void favoriteFail(String message){
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消收藏结果
     * @param isSuccess 成功或失败
     */
    private void unFavoriteResult(boolean isSuccess){
        if(isSuccess){
            Toast.makeText(this.getContext(),R.string.unfavoritesuccess,Toast.LENGTH_SHORT).show();
            fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else {
            Toast.makeText(this.getContext(),R.string.unfavoritefail,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_edit_wordgroup:
                showUpdateWordGroupDialog();
                break;
            case R.id.fab_deleteorfavorite_wordgroup:
                switch (sentenceGroupType){
                    case OTHERSGROUP:
                        favorite();
                        break;
                    case CREATESGROUP:
                        showDeleteSentenceGroupConfirm();
                        break;
                    case FAVORITESGROUP:
                        unFavorite();
                        break;
                    default:
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
        private List<Sentence> sentences;
        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            sentences = new ArrayList<>();
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

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<Sentence> sentences) {
            if (sentences != null) {
                this.sentences.clear();
                this.sentences.addAll(sentences);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position != sentences.size()) {
                Log.d(TAG, "wordgroupstop_item");
                return R.layout.words_frag_item;
            } else {
                if (isGone) {
                    Log.d(TAG, "load_done_layout");
                    return R.layout.words_frag_loaddone_item;
                }
                Log.d(TAG, "load_more_layout");
                return R.layout.words_frag_loadmore_item;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            switch (viewType) {
                case R.layout.words_frag_item:
                    return new ItemViewHolder(v);
                case R.layout.words_frag_loadmore_item:
                    return new LoadingMoreViewHolder(v);
                case R.layout.words_frag_loaddone_item:
                    return new LoadingGoneViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(sentences.get(position).getContent());
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
            return sentences.size() + 1;
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


