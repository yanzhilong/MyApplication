package com.englishlearn.myapplication.phoneticssymbols;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 音标
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsSymbolsFragment extends Fragment implements PhoneticsSymbolsContract.View {

    private static final String TAG = PhoneticsSymbolsFragment.class.getSimpleName();
    private List<String> phonetics;
    private PhoneticsSymbolsContract.Presenter mPresenter;
    private Context mContext;

    public static PhoneticsSymbolsFragment newInstance() {
        return new PhoneticsSymbolsFragment();
    }

    @Override
    public void setPresenter(PhoneticsSymbolsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = inflater.getContext();

        phonetics = new ArrayList<>();
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");
        phonetics.add("123");
        phonetics.add("1234");
        phonetics.add("1235");

        View root = inflater.inflate(R.layout.phoneticssymbols_frag, container, false);
        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        final LinearLayoutManager mgrlistview = new LinearLayoutManager(this.getContext());
        //VERTICAL纵向，类似ListView，HORIZONTAL<span style="font-family: Arial, Helvetica, sans-serif;">横向，类似Gallery</span>
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        //GridLayout 3列
        GridLayoutManager mgrgridview=new GridLayoutManager(this.getContext(),3);

        recyclerView.setLayoutManager(mgrlistview);

        final PhoneticssAdapter phoneticssAdapter = new PhoneticssAdapter();
        phoneticssAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, phonetics.get(position), Toast.LENGTH_LONG).show();
            }
        });
        //设置适配器
        recyclerView.setAdapter(phoneticssAdapter);

        /*recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                String item = phonetics.get(itemPosition);
                Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
            }
        });*/

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mPresenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class PhoneticssAdapter extends RecyclerView.Adapter<PhoneticssAdapter.ViewHolder>{


        private OnItemClickListener onItemClickListener = null;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //View v = getLayoutInflater().inflate(R.layout.recycler_item, null);

            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.phoneticssymbolsitem, parent, false);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG,"onBindViewHolder" + position);
            holder.textView.setText(phonetics.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(holder.itemView,position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return phonetics.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.phonetics_name);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

}
