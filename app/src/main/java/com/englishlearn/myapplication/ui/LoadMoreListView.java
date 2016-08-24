package com.englishlearn.myapplication.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-8-24.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener{

    private int totalItemCount;//总Item
    private int lastItemCount;//最后一个Item下标＋１(总数)
    private boolean isLoading = false;
    private boolean isEnd = false;
    private LinearLayout load_more;
    private LinearLayout load_end;
    private OnLoadMoreLister onLoadMoreLister;
    private View footView;

    public void setOnLoadMoreLister(OnLoadMoreLister onLoadMoreLister) {
        this.onLoadMoreLister = onLoadMoreLister;
    }

    public LoadMoreListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footView = inflater.inflate(R.layout.loadmorelistview_footer_layout, null);
        load_more = (LinearLayout) footView.findViewById(R.id.load_more);
        load_end = (LinearLayout) footView.findViewById(R.id.load_end);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItemCount==lastItemCount&&scrollState==SCROLL_STATE_IDLE){
            if (!isLoading){
                isLoading = true;
                if(onLoadMoreLister != null){
                    onLoadMoreLister.loadingMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.totalItemCount = totalItemCount;
        this.lastItemCount = firstVisibleItem+visibleItemCount;
    }

    public void loadingComplete(){
        isLoading = false;
        if(this.getFooterViewsCount() == 0){
            this.addFooterView(footView);
        }
        if(isEnd){
            load_end.setVisibility(VISIBLE);
            load_more.setVisibility(GONE);
        }else {
            load_more.setVisibility(VISIBLE);
            load_end.setVisibility(GONE);
        }
    }

    public void setIsEnd(boolean isEnd){
        this.isEnd = isEnd;
    }

    public interface OnLoadMoreLister{
        void loadingMore();
    }
}
