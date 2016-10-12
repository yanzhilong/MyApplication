package com.englishlearn.myapplication.wordgroups.words;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.WordGroupCollect;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.RemoteCode;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobDefaultError;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.DeleteConfirmFragment;
import com.englishlearn.myapplication.dialog.UpdateWordGroupFragment;
import com.englishlearn.myapplication.wordgroups.words.word.WordDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordsFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    public static final String TYPE = "wordgrouptype";
    private static final String TAG = WordsFragment.class.getSimpleName();
    private final int PAGESIZE = 10;
    private WordGroup wordGroup;
    private WordGroupType wordGroupType;
    private MyAdapter myAdapter;
    private int page = 0;
    private List<Word> mList;
    private String userId = "943a8a40ed";

    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;
    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮
    private FloatingActionButton fab_deleteorfavorite_wordgroup;
    private FloatingActionButton fab_edit_wordgroup;

    public static WordsFragment newInstance() {
        return new WordsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().containsKey(OBJECT)){
            wordGroup = (WordGroup) getArguments().getSerializable(OBJECT);
            wordGroupType = (WordGroupType) getArguments().getSerializable(TYPE);
        }

        MyApplication.instance.getAppComponent().inject(this);

        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
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

                Word word = myAdapter.getStrings().get(position);
                Log.d(TAG, word.toString());
                Intent intent = new Intent(WordsFragment.this.getContext(),WordDetailActivity.class);
                intent.putExtra(WordDetailActivity.OBJECT,word);
                startActivity(intent);

            }

        });

        myAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getNextPage();
            }
        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);

        fab_edit_wordgroup = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_wordgroup);
        fab_edit_wordgroup.setOnClickListener(this);

        fab_deleteorfavorite_wordgroup = (FloatingActionButton) getActivity().findViewById(R.id.fab_deleteorfavorite_wordgroup);
        fab_deleteorfavorite_wordgroup.setOnClickListener(this);

        switch (wordGroupType){
            case TOP:
                fab_edit_wordgroup.setVisibility(View.GONE);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                break;
            case CREATE:
                fab_edit_wordgroup.setVisibility(View.VISIBLE);
                fab_edit_wordgroup.setBackgroundResource(R.drawable.ic_edit);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_delete);
                break;
            case FAVORITE:
                fab_edit_wordgroup.setVisibility(View.GONE);
                fab_deleteorfavorite_wordgroup.setVisibility(View.VISIBLE);
                fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_black_24dp);
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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNextPage();
    }

    //获取下一页
    public void getNextPage() {

        Subscription subscription = repository.getWordsRxByWordGroupId(wordGroup.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<Word>>() {
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
     * 显示确认删除对话框
     */
    private void showDeleteWordGroupConfirm() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteWordGroup();
            }
        });
        delete.show(getFragmentManager(),"delete");
    }


    /**
     * 显示对话框
     */
    private void showUpdateWordGroupDialog(){
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setOldName(wordGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener()
        {
            @Override
            public void onUpdate(String name) {
                updateWordGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(),"update");
    }

    //删除詞单
    private void deleteWordGroup(){
        Subscription subscription = repository.deleteWordGroupRxById(wordGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
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
                    Toast.makeText(WordsFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
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

        WordGroup createwg = new WordGroup();
        createwg.setObjectId(wordGroup.getObjectId());
        createwg.setUserId(userId);
        createwg.setName(name);
        createwg.setOpen("false");
        Subscription subscription = repository.updateWordGroupRxById(createwg).subscribe(new Subscriber<Boolean>() {
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
                    Toast.makeText(WordsFragment.this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
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
        Subscription subscription = repository.deleteWordGroupCollectRxByuserIdAndwordGroupId(userId,wordGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
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
        WordGroupCollect wordGroupCollect = new WordGroupCollect();
        wordGroupCollect.setUserId(userId);
        wordGroupCollect.setWordgroupId(wordGroup.getObjectId());
        repository.addWordGroupCollect(wordGroupCollect).subscribe(new Subscriber<WordGroupCollect>() {
            @Override
            public void onCompleted() {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {
                fab_deleteorfavorite_wordgroup.setEnabled(true);
                favoriteResult(false);
            }

            @Override
            public void onNext(WordGroupCollect wordGroupCollect) {

                if(wordGroupCollect != null){
                    favoriteResult(true);
                }else {
                    favoriteResult(false);
                }
            }
        });
    }

    /**
     * 收藏结果
     * @param isSuccess 成功或失败
     */
    private void favoriteResult(boolean isSuccess){
        if(isSuccess){
            Toast.makeText(this.getContext(),R.string.favoritesuccess,Toast.LENGTH_SHORT).show();
            fab_deleteorfavorite_wordgroup.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else {
            Toast.makeText(this.getContext(),R.string.favoritefail,Toast.LENGTH_SHORT).show();
        }
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
                switch (wordGroupType){
                    case TOP:
                        favorite();
                        break;
                    case CREATE:
                        showDeleteWordGroupConfirm();
                        break;
                    case FAVORITE:
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
        private List<Word> words;
        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            words = new ArrayList<>();
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

        public List<Word> getStrings() {
            return words;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<Word> words) {
            if (words != null) {
                this.words.clear();
                this.words.addAll(words);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position != words.size()) {
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
                itemViewHolder.name.setText(words.get(position).getName());
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
            return words.size() + 1;
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

