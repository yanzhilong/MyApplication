package com.englishlearn.myapplication.sentencegroups.sentences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.englishlearn.myapplication.adapter.RecyclerViewBaseAdapter;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.data.SentenceCollect;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.SentenceGroup;
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.dialog.DeleteConfirmFragment;
import com.englishlearn.myapplication.dialog.SentenceCollectGroupsSelectFragment;
import com.englishlearn.myapplication.dialog.SentenceDetailFragment;
import com.englishlearn.myapplication.dialog.UpdateWordGroupFragment;
import com.englishlearn.myapplication.sentencegroups.SentenceGroupsActivity;

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

    private static final int SENTENCESFAVORITE = 1;//收藏多条句子
    public static final int REQUESTCODE = 0;//多选请求码
    public static final String OBJECT = "object";
    public static final String SENTENCECOLLECTGROUP = "sentencecollectgroup";
    public static final String SENTENCEGROUPCOLLECT = "sentencegroupcollect";//收藏的单词分组
    public static final String TYPE = "sentencegrouptype";
    private static final String TAG = SentencesFragment.class.getSimpleName();
    private final int PAGESIZE = 100;
    private SentenceGroup sentenceGroup;
    private SentenceCollectGroup sentenceCollectGroup;//句子收藏分组
    private SentenceGroupCollect sentenceGroupCollect;
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
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_favorite;
    private FloatingActionButton fab_delete;

    private List<Sentence> favoritesentences;//待收藏句子列表

    public static SentencesFragment newInstance() {
        return new SentencesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            sentenceGroup = (SentenceGroup) bundle.getSerializable(SentencesActivity.SENTENCEGROUP);
            sentenceGroupType = (SentenceGroupType) bundle.getSerializable(SentencesActivity.TYPE);
            sentenceCollectGroup = (SentenceCollectGroup) bundle.getSerializable(SentencesActivity.SENTENCECOLLECTGROUP);
            sentenceGroupCollect = (SentenceGroupCollect) bundle.getSerializable(SentencesActivity.SENTENCEGROUPCOLLECT);
            if (sentenceGroupCollect != null) {
                sentenceGroup = sentenceGroupCollect.getSentenceGroup();
            }
        }

        sentenceCollects = new ArrayList<>();

        switch (sentenceGroupType) {

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

        switch (requestCode) {

            case REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    List<Integer> checkedlist = data.getIntegerArrayListExtra(MultipleActivity.CHECKEDARRAY);
                    List<Sentence> checkedsentences = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (checkedlist.contains(i)) {
                            checkedsentences.add(mList.get(i));
                        }
                    }
                    if (checkedsentences.size() > 0) {
                        onSentencesSelected(checkedsentences);
                    }
                }
                break;
            case SENTENCESFAVORITE:
                SentenceCollectGroup sentenceCollectGroup = (SentenceCollectGroup) data.getSerializableExtra(SentenceCollectGroupsSelectFragment.SENTENCEGCOLLECTGROUP);
                favoriteTopSentences(sentenceCollectGroup);
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
        myAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Sentence sentence = myAdapter.getSentences().get(position);
                Log.d(TAG, sentence.toString());

                SentenceDetailFragment sentenceDetailFragment = SentenceDetailFragment.newInstance(sentence);
                Bundle bundler = sentenceDetailFragment.getArguments();
                sentenceDetailFragment.setArguments(bundler);
                sentenceDetailFragment.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });

        myAdapter.setOnLoadMoreListener(new RecyclerViewBaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getNext();
            }
        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);

        fab_edit = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(this);

        fab_favorite = (FloatingActionButton) getActivity().findViewById(R.id.fab_favorite);
        fab_favorite.setOnClickListener(this);

        fab_delete = (FloatingActionButton) getActivity().findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(this);

        switch (sentenceGroupType) {
            case OTHERSGROUP:
                fab_edit.setVisibility(View.GONE);
                fab_delete.setVisibility(View.GONE);
                fab_favorite.setVisibility(View.VISIBLE);
                break;
            case CREATESGROUP:
                fab_edit.setVisibility(View.VISIBLE);
                fab_delete.setVisibility(View.VISIBLE);
                fab_favorite.setVisibility(View.GONE);
                break;
            case CREATEFSGROUP:
                fab_edit.setVisibility(View.VISIBLE);
                fab_delete.setVisibility(View.VISIBLE);
                fab_favorite.setVisibility(View.GONE);
                break;
            case FAVORITESGROUP:
                fab_edit.setVisibility(View.GONE);
                fab_delete.setVisibility(View.VISIBLE);
                fab_favorite.setVisibility(View.GONE);
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

        switch (sentenceGroupType) {

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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (sentenceGroupType) {

            case OTHERSGROUP:
                //普通句组
                return othersgroup(item);
            case CREATESGROUP:
                return createsgroup(item);
            case CREATEFSGROUP:
                return createfsgroup(item);
        }
        return true;
    }


    //统一方法***************************************************************************************
    //显示多选界面
    private void showMultipleSelect() {

        String[] sentences = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            sentences[i] = mList.get(i).getContent();
        }
        Intent intent = new Intent(this.getContext(), MultipleActivity.class);
        intent.putExtra(MultipleActivity.STRINGARRAY, sentences);
        startActivityForResult(intent, REQUESTCODE);
    }

    //选择多个句子返回
    private void onSentencesSelected(List<Sentence> sentences) {
        switch (sentenceGroupType) {

            case OTHERSGROUP:
                //普通句组
                favoriteTopSentences(sentences);//显示分组信息
                break;
            case CREATESGROUP:
                //删除句子
                deleteSetnencesAffirm(sentences);
                break;
            case CREATEFSGROUP:
                //删除句子
                deleteSentenceCollectsAffirm(sentences);
                break;
            case FAVORITESGROUP:
                //
                break;
        }
    }


    //热门分组列表进来的******************************************************************************

    /**
     * 收藏句单
     */
    private void favoriteSentenGroup() {

        if (sentenceGroup.getUser().getObjectId().equals(repository.getUserInfo().getObjectId())) {
            Toast.makeText(SentencesFragment.this.getContext(), "不能收藏自己创建的句单", Toast.LENGTH_SHORT).show();
            return;
        }

        SentenceGroupCollect sentenceGroupCollect = new SentenceGroupCollect();
        User user = repository.getUserInfo();
        user.setPointer();
        sentenceGroupCollect.setUser(user);
        sentenceGroup.setPointer();
        sentenceGroupCollect.setSentenceGroup(sentenceGroup);
        Subscription subscription = repository.addSentenceGroupCollect(sentenceGroupCollect).subscribe(new Subscriber<SentenceGroupCollect>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(SentencesFragment.this.getContext(), bmobRequestException.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), SentencesFragment.this.getContext().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNext(SentenceGroupCollect sentenceGroupCollect) {

                if (sentenceGroupCollect != null) {
                    Toast.makeText(SentencesFragment.this.getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), "收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 收藏热门句单里的多个句子
     */
    private void favoriteTopSentences() {
        if (sentenceGroup.getUser().getObjectId().equals(repository.getUserInfo().getObjectId())) {
            Toast.makeText(SentencesFragment.this.getContext(), "不能收藏自己创建的句子", Toast.LENGTH_SHORT).show();
        } else {
            showMultipleSelect();
        }
    }

    /**
     * 收藏热门句单里的多个句子
     */
    private void favoriteTopSentences(List<Sentence> sentences) {
        this.favoritesentences = sentences;
        if (sentenceGroup.getUser().getObjectId().equals(repository.getUserInfo().getObjectId())) {
            Toast.makeText(SentencesFragment.this.getContext(), "不能收藏自己创建的句子", Toast.LENGTH_SHORT).show();
        } else {
            SentenceCollectGroupsSelectFragment sentenceCollectGroupsSelectFragment = new SentenceCollectGroupsSelectFragment();
            Bundle bundle = new Bundle();
            sentenceCollectGroupsSelectFragment.setTargetFragment(this, SENTENCESFAVORITE);
            sentenceCollectGroupsSelectFragment.setArguments(bundle);
            sentenceCollectGroupsSelectFragment.show(this.getFragmentManager(), "sentencecollectgroup");
        }
    }

    /**
     * 批量收藏句子到指定句子收藏分组
     *
     * @param sentenceCollectGroup
     */
    private void favoriteTopSentences(SentenceCollectGroup sentenceCollectGroup) {

        final List<SentenceCollect> sentenceCollects = new ArrayList<>();
        for (int i = 0; i < favoritesentences.size(); i++) {
            SentenceCollect sentenceCollect = new SentenceCollect();
            User user = repository.getUserInfo();
            sentenceCollect.setUser(user);
            Sentence sentence = favoritesentences.get(i);
            sentenceCollect.setSentence(sentence);
            sentenceCollectGroup.setPointer();
            sentenceCollect.setSentenceCollectGroup(sentenceCollectGroup);
            sentenceCollects.add(sentenceCollect);
        }

        Subscription subscription = repository.addSentenceCollects(sentenceCollects).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(SentencesFragment.this.getContext(), bmobRequestException.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), SentencesFragment.this.getContext().getString(R.string.networkerror), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Toast.makeText(SentencesFragment.this.getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    //我创建的分组列表进来的******************************************************************************

    /**
     * 修改句组名称
     */
    private void updateSentenceGroup() {
        Bundle bundle = new Bundle();
        bundle.putString(UpdateWordGroupFragment.TITLE, "修改句组名称");
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setArguments(bundle);
        updateWordGroupFragment.setOldName(sentenceGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener() {
            @Override
            public void onUpdate(String name) {
                updateSentenceGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(), "update");
    }

    /**
     * 更新句子分组
     */
    private void updateSentenceGroup(final String name) {

        SentenceGroup updatesentenceGroup = new SentenceGroup();
        updatesentenceGroup.setObjectId(sentenceGroup.getObjectId());
        updatesentenceGroup.setName(name);
        Subscription subscription = repository.updateSentenceGroupRxById(updatesentenceGroup).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    updateWGFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                sentenceGroup.setName(name);
                Toast.makeText(SentencesFragment.this.getContext(), R.string.updatewordgroupsuccess, Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 删除创建的句组
     */
    private void deleteSentenceGroup() {
        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE, "删除当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceGroup(sentenceGroup);
            }
        });
        delete.show(getFragmentManager(), "delete");
    }


    //删除句子分组
    private void deleteSentenceGroup(SentenceGroup sentenceGroup) {
        Subscription subscription = repository.deleteSentenceGroupRxById(sentenceGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
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
    private void deleteSetnences() {
        showMultipleSelect();
    }

    //批量删除句子确认
    private void deleteSetnencesAffirm(final List<Sentence> sentences) {

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE, "删除选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentences(sentences);
            }
        });
        delete.show(getFragmentManager(), "delete");
    }

    //批量删除句子
    private void deleteSentences(List<Sentence> sentences) {

        Subscription subscription = repository.deleteSentences(sentences).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
                refershList();
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


    //我创建的收藏句组******************************************************************************

    /**
     * 修改句组名称
     */
    private void updateSentenceCollectGroup() {
        Bundle bundle = new Bundle();
        bundle.putString(UpdateWordGroupFragment.TITLE, "修改句组名称");
        UpdateWordGroupFragment updateWordGroupFragment = new UpdateWordGroupFragment();
        updateWordGroupFragment.setArguments(bundle);
        updateWordGroupFragment.setOldName(sentenceCollectGroup.getName());
        updateWordGroupFragment.setUpdateWordGroupListener(new UpdateWordGroupFragment.UpdateWordGroupListener() {
            @Override
            public void onUpdate(String name) {
                updateSentenceCollectGroup(name);
            }
        });
        updateWordGroupFragment.show(getFragmentManager(), "update");
    }

    /**
     * 更新句子分组
     */
    private void updateSentenceCollectGroup(final String name) {

        SentenceCollectGroup updatesentenceCollectGroup = new SentenceCollectGroup();
        updatesentenceCollectGroup.setObjectId(sentenceCollectGroup.getObjectId());
        updatesentenceCollectGroup.setName(name);
        Subscription subscription = repository.updateSentenceCollectGroupRxById(updatesentenceCollectGroup).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    updateWGFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                sentenceCollectGroup.setName(name);
                Toast.makeText(SentencesFragment.this.getContext(), R.string.updatewordgroupsuccess, Toast.LENGTH_SHORT).show();
            }
        });
        mSubscriptions.add(subscription);
    }


    //删除收藏句组确认
    private void deleteSentenceCollectGroupAffirm() {

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE, "删除当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceCollectGroup(sentenceCollectGroup);
            }
        });
        delete.show(getFragmentManager(), "delete");
    }

    //删除收藏的句子分组
    private void deleteSentenceCollectGroup(SentenceCollectGroup sentenceCollectGroup) {

        Subscription subscription = repository.deleteSentenceCollectGroupRxById(sentenceCollectGroup.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                } else {
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
    private void deleteSentenceCollects() {

        showMultipleSelect();
    }

    //批量删除收藏句子确认
    private void deleteSentenceCollectsAffirm(final List<Sentence> sentenceCollects) {

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE, "删除选中的句子?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceCollects(sentenceCollects);
            }
        });
        delete.show(getFragmentManager(), "delete");
    }

    //批量删除句子
    private void deleteSentenceCollects(List<Sentence> sentences) {

        List<SentenceCollect> list = new ArrayList<>();
        for (int i = 0; i < sentenceCollects.size(); i++) {
            for (int j = 0; j < sentences.size(); j++) {
                if (sentenceCollects.get(i).getSentence().getObjectId().equals(sentences.get(j).getObjectId())) {
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
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
                refershList();
            }
        });
        mSubscriptions.add(subscription);
    }


    //我收藏的句组***********************************************************************************

    //取消收藏当前句组确认
    private void deleteSentenceGroupCollectAffirm() {

        DeleteConfirmFragment delete = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DeleteConfirmFragment.TITLE, "取消收藏当前句组?");
        delete.setArguments(bundle);
        delete.setDeleteConfirmListener(new DeleteConfirmFragment.DeleteConfirmListener() {
            @Override
            public void onDelete() {
                deleteSentenceGroupCollect();
            }
        });
        delete.show(getFragmentManager(), "delete");
    }

    //取消收藏当前句组
    private void deleteSentenceGroupCollect() {

        Subscription subscription = repository.deleteSentenceGroupCollectRxById(sentenceGroupCollect.getObjectId()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    deleteFail(bmobRequestException.getMessage());
                } else {
                    Toast.makeText(SentencesFragment.this.getContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean b) {
                deleteSuccess();
                startSentenceGroupsActivity();
            }
        });
        mSubscriptions.add(subscription);
    }


    //菜单事件****************************************************************************************************
    //其它人的句组
    private boolean othersgroup(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favorite_sentences:
                Log.d(TAG, "收藏句子");
                favoriteTopSentences();
                break;
        }
        return true;
    }


    //我创建的句组
    private boolean createsgroup(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_sentences:
                Log.d(TAG, "删除句子");
                deleteSetnences();
                break;
        }
        return true;
    }

    //我创建的收藏句组
    private boolean createfsgroup(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_sentences:
                Log.d(TAG, "删除句子");
                deleteSentenceCollects();
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
        myAdapter.replaceData(mList);
        sentenceCollects.clear();
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNext();

    }

    private void getNext() {
        switch (sentenceGroupType) {
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

        Subscription subscription = repository.getSentencesRxBySentenceGroupId(sentenceGroup.getObjectId(), page, PAGESIZE).subscribe(new Subscriber<List<Sentence>>() {
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

        Subscription subscription = repository.getSentenceCollectRxBySentenceCollectGroupId(sentenceCollectGroup.getObjectId(), page, PAGESIZE).subscribe(new Subscriber<List<SentenceCollect>>() {
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
                Log.d(TAG, "onNext size:" + list.size());

                if (list == null || list.size() == 0) {
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                } else {
                    sentenceCollects.addAll(list);
                    for (int i = 0; i < sentenceCollects.size(); i++) {
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


    //删除失败
    private void deleteFail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //删除分组成功
    private void deleteGroupSuccess() {
        deleteSuccess();
        startSentenceGroupsActivity();
    }

    //删除成功
    private void deleteSuccess() {
        Toast.makeText(this.getContext(), R.string.deletesuccess, Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除成功后回退
     */
    private void startSentenceGroupsActivity(){

        Intent intent = new Intent(getContext(),SentenceGroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //创建失败
    private void updateWGFail(String message) {

        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_edit:
                //showUpdateWordGroupDialog();
                switch (sentenceGroupType) {
                    case CREATESGROUP:
                        updateSentenceGroup();
                        break;
                    case CREATEFSGROUP:
                        updateSentenceCollectGroup();
                        break;
                }
                break;
            case R.id.fab_delete:
                switch (sentenceGroupType) {
                    case CREATESGROUP:
                        deleteSentenceGroup();
                        break;
                    case CREATEFSGROUP:
                        deleteSentenceCollectGroupAffirm();
                        break;
                    case FAVORITESGROUP:
                        deleteSentenceGroupCollectAffirm();
                        break;
                }

                break;
            case R.id.fab_favorite:
                switch (sentenceGroupType) {
                    case OTHERSGROUP:
                        favoriteSentenGroup();
                        break;
                }
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends RecyclerViewBaseAdapter {

        private List<Sentence> sentences;

        public MyAdapter() {
            sentences = new ArrayList<>();
        }

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void replaceData(List<Sentence> sentences) {
            if (sentences != null) {
                this.sentences.clear();
                this.sentences.addAll(sentences);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewTypeBase(int position) {
            return R.layout.words_frag_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderBase(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolderBase(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.name.setText(sentences.get(position).getContent());
        }

        @Override
        public int getItemCountBase() {
            return sentences.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ItemViewHolder extends RecyclerViewBaseAdapter.ViewHolder {
            TextView name;

            public ItemViewHolder(final View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
            }
        }
    }

}


