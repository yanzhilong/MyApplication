package com.englishlearn.myapplication.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.grammardetail.GrammarsDetailActivity;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarFragment extends Fragment implements GrammarContract.View {

    private static final String TAG = GrammarFragment.class.getSimpleName();

    private GrammarContract.Presenter mPresenter;
    private ListView grammars_listview;
    private GrammarsAdapter grammarsAdapter;
    public static GrammarFragment newInstance() {
        return new GrammarFragment();
    }

    @Override
    public void setPresenter(GrammarContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.grammar_frag, container, false);

        mPresenter = new GrammarPresenter(this);

        grammarsAdapter = new GrammarsAdapter();
        grammars_listview = (ListView) root.findViewById(R.id.grammars_listview);
        grammars_listview.setAdapter(grammarsAdapter);

        grammars_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Grammar grammar = grammarsAdapter.getGrammars().get(position);
                    Intent detail = new Intent(GrammarFragment.this.getContext(), GrammarsDetailActivity.class);
                    detail.putExtra(GrammarsDetailActivity.ID,grammar.getObjectId());
                    startActivity(detail);
            }
        });

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getGrammars();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showGrammars(List<Grammar> grammars) {
        Log.d(TAG,"showGrammars" + grammars.size());
        grammarsAdapter.replace(grammars);
    }

    @Override
    public void emptyGrammars() {
        Log.d(TAG,"emptyGrammars");
        grammarsAdapter.replace(new ArrayList<Grammar>());
        grammarsAdapter.notifyDataSetChanged();
    }

    private class GrammarsAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<Grammar> grammars;

        public void replace(List<Grammar> grammars){
            if(grammars != null){
                this.grammars.clear();
                this.grammars.addAll(grammars);
                notifyDataSetChanged();
            }
        }

        public List<Grammar> getGrammars() {
            return grammars;
        }

        public GrammarsAdapter(){
            grammars = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return grammars.size();
        }

        @Override
        public Object getItem(int position) {
            return grammars.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(inflater == null){
                inflater = LayoutInflater.from(parent.getContext());
            }
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.grammar_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Grammar grammar = grammars.get(position);
            viewHolder.name.setText(grammar.getTitle());
            viewHolder.content.setText(grammar.getContent());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView name;//名称
        TextView content;//说明
    }
}
