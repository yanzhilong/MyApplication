package com.englishlearn.myapplication.grammars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.addeditgrammar.AddEditGrammarActivity;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.grammardetail.GrammarDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarsFragment extends Fragment implements GrammarsContract.View {

    private static final String TAG = GrammarsFragment.class.getSimpleName();

    private GrammarsContract.Presenter mPresenter;
    private ListView grammars_listview;
    private GrammarsAdapter grammarsAdapter;

    public static GrammarsFragment newInstance() {
        return new GrammarsFragment();
    }

    @Override
    public void setPresenter(GrammarsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grammarsAdapter = new GrammarsAdapter(new ArrayList<Grammar>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.grammars_frag, container, false);

        grammars_listview = (ListView) root.findViewById(R.id.grammars_listview);
        grammars_listview.setAdapter(grammarsAdapter);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_grammar);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addGrammar();
            }
        });

        grammars_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grammar grammar = grammarsAdapter.getGrammars().get(position);
                Intent detail = new Intent(GrammarsFragment.this.getContext(), GrammarDetailActivity.class);
                detail.putExtra(GrammarDetailActivity.GRAMMAR_ID,grammar.getGrammarid());
                detail.putExtra(GrammarDetailActivity.ID,grammar.getId());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_base, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "点击菜单Item");
        switch (item.getItemId()) {
            case R.id.menu_base:
                Log.d(TAG, "点击菜单项");
                break;
        }
        return true;
    }

    @Override
    public void showGrammars(List<Grammar> grammars) {
        Log.d(TAG,"showGrammars" + grammars.size());
        grammarsAdapter.replace(grammars);
    }

    @Override
    public void emptyGrammars() {
        Log.d(TAG,"emptyGrammars");
    }

    @Override
    public void showaddGrammar() {
        Log.d(TAG,"showaddGrammar");
        Intent ahowAddGrammarIntent = new Intent(this.getContext(), AddEditGrammarActivity.class);
        startActivity(ahowAddGrammarIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private class GrammarsAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Grammar> grammars;

        public void replace(List<Grammar> grammars){
            this.grammars = grammars;
            notifyDataSetChanged();
        }

        public List<Grammar> getGrammars() {
            return grammars;
        }

        public void addSentences(List<Grammar> grammars){
            this.grammars.addAll(grammars);
            notifyDataSetChanged();
        }

        public GrammarsAdapter(List<Grammar> grammars){
            this.grammars = grammars;;
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
            viewHolder.name.setText(grammar.getName());
            viewHolder.content.setText(grammar.getContent());
            return convertView;
        }
    }

    static class ViewHolder{
        TextView name;//名称
        TextView content;//说明
    }
}
