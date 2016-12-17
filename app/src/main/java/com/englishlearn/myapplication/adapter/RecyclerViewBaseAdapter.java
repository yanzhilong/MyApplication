package com.englishlearn.myapplication.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-12-17.
 * 分页加载的RecyclerView的蕨类
 */

public abstract class RecyclerViewBaseAdapter extends RecyclerView.Adapter {

    private final static String TAG = RecyclerViewBaseAdapter.class.getSimpleName();

    private boolean complete = false;//是否加载完成

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnItemClickListener onItemClickListener = null;

    //已经加载完成了
    public void loadingGone() {
        complete = true;
    }

    //还有更多
    public void hasMore() {
        complete = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != getItemCount() - 1) {
            return getItemViewTypeBase(position);
        } else {
            if (complete) {
                return R.layout.recycleradapter_loadcomplete_item;
            }
            return R.layout.recycleradapter_loadmore_item;
        }
    }

    public abstract int getItemViewTypeBase(int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.recycleradapter_loadmore_item:
                return new LoadMoreViewHolder(v);
            case R.layout.recycleradapter_loadcomplete_item:
                return new LoadCompleteViewHolder(v);
            default:
                return onCreateViewHolderBase(parent, viewType);
        }
    }

    //子类返回ItemViewHolder
    public abstract RecyclerView.ViewHolder onCreateViewHolderBase(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadMoreViewHolder && mOnLoadMoreListener != null && getItemCount() != 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnLoadMoreListener.onLoadMore();
                }
            }, 100);
        } else if (holder instanceof LoadCompleteViewHolder) {

        } else if (getItemCount() > 1) {
            onBindViewHolderBase(holder, position);
        }
    }

    public abstract void onBindViewHolderBase(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return getItemCountBase() + 1;
    }

    public abstract int getItemCountBase();

    //加载更多
    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    //加载完成
    class LoadCompleteViewHolder extends RecyclerView.ViewHolder {

        public LoadCompleteViewHolder(View itemView) {
            super(itemView);
        }
    }


    //加载更多接口
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }


}
