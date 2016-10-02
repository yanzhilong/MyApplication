package com.englishlearn.myapplication.phoneticssymbols;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails.PhoneticsDetailsActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 音标
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsSymbolsFragment extends Fragment implements PhoneticsSymbolsContract.View {

    private static final String TAG = PhoneticsSymbolsFragment.class.getSimpleName();
    private List<PhoneticsSymbols> phonetics;
    private PhoneticsSymbolsContract.Presenter mPresenter;
    private Context mContext;
    private PhoneticssAdapter phoneticssAdapter;

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
        phonetics = new ArrayList<>();
        mPresenter = new PhoneticsSymbolsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = inflater.getContext();

        View root = inflater.inflate(R.layout.phoneticssymbols_frag, container, false);
        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        final LinearLayoutManager mgrlistview = new LinearLayoutManager(this.getContext());
        //VERTICAL纵向，类似ListView，HORIZONTAL<span style="font-family: Arial, Helvetica, sans-serif;">横向，类似Gallery</span>
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);
        //GridLayout 3列
        GridLayoutManager mgrgridview=new GridLayoutManager(this.getContext(),3);
        recyclerView.setLayoutManager(mgrlistview);
        phoneticssAdapter = new PhoneticssAdapter(phonetics);
        phoneticssAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(mContext, phonetics.get(i).toString(), Toast.LENGTH_LONG).show();
                PhoneticsSymbols phoneticsSymbols = phonetics.get(i);
                Intent intent = new Intent(PhoneticsSymbolsFragment.this.getContext(), PhoneticsDetailsActivity.class);
                intent.putExtra(PhoneticsDetailsActivity.PHONETICS,phoneticsSymbols);
                startActivity(intent);
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
        mPresenter.getPhoneticsSymbols();
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

    @Override
    public void showPhoneticsSymbols(List<PhoneticsSymbols> list) {
        phonetics.clear();
        phonetics.addAll(list);
        phoneticssAdapter.replaceData(list);
    }

    @Override
    public void showPhoneticsSymbolsFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();

    }

    private class PhoneticssAdapter extends RecyclerView.Adapter<PhoneticssAdapter.ViewHolder>{

        private List<PhoneticsSymbols> phonetics;

        private AdapterView.OnItemClickListener onItemClickListener = null;

        public PhoneticssAdapter(List<PhoneticsSymbols> phonetics) {
            this.phonetics = phonetics;
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<PhoneticsSymbols> phonetics){
            if(phonetics != null){
                this.phonetics.clear();
                this.phonetics.addAll(phonetics);
                notifyDataSetChanged();
            }
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
            holder.textView.setText(phonetics.get(position).getIpaname());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(null,holder.itemView,position,0);
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

}
