package com.englishlearn.myapplication.grammars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
public class GrammarsFragment extends Fragment implements GrammarsContract.View,GrammarsSelectContract.View, View.OnClickListener {

    private static final String TAG = GrammarsFragment.class.getSimpleName();

    private GrammarsContract.Presenter mPresenter;
    private GrammarsSelectContract.Presenter selectPresenter;
    private ListView grammars_listview;
    private GrammarsAdapter grammarsAdapter;
    private RelativeLayout grammars_edit_rela;
    private Button deletes;
    private CheckBox allSelect;
    private FloatingActionButton fab;

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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.grammars_frag, container, false);

        grammarsAdapter = new GrammarsAdapter(selectPresenter);
        grammars_listview = (ListView) root.findViewById(R.id.grammars_listview);
        grammars_edit_rela = (RelativeLayout) root.findViewById(R.id.sentences_edit_rela);
        deletes = (Button) root.findViewById(R.id.deletes);
        allSelect = (CheckBox) root.findViewById(R.id.allSelect);
        grammars_listview.setAdapter(grammarsAdapter);
        // Set up floating action button
        fab =(FloatingActionButton) getActivity().findViewById(R.id.fab_add_grammar);

        fab.setImageResource(R.drawable.ic_add);

        allSelect.setOnClickListener(this);
        deletes.setOnClickListener(this);
        fab.setOnClickListener(this);

        grammars_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(selectPresenter.isEdit()){
                    selectPresenter.onClick(grammarsAdapter.getGrammars().get(position));
                }else{
                    Grammar grammar = grammarsAdapter.getGrammars().get(position);
                    Intent detail = new Intent(GrammarsFragment.this.getContext(), GrammarDetailActivity.class);
                    detail.putExtra(GrammarDetailActivity.GRAMMAR_ID,grammar.getGrammarid());
                    detail.putExtra(GrammarDetailActivity.ID,grammar.getId());
                    startActivity(detail);
                }
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
        inflater.inflate(R.menu.grammars_frag_menu, menu);
    }

    private Menu menu;
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.menu = menu;
        Log.d(TAG,"onPrepareOptionsMenu");
        MenuItem edit = menu.findItem(R.id.grammars_edit);
        MenuItem cancel = menu.findItem(R.id.menu_cancel);

        if(selectPresenter.isEdit())
        {
            edit.setVisible(false);
            cancel.setVisible(true);

        }
        else
        {
            edit.setVisible(true);
            cancel.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grammars_edit:
                Log.d(TAG, "点击编辑项");
                selectPresenter.edit();
                onPrepareOptionsMenu(menu);
                break;
            case R.id.grammars_setting:
                Log.d(TAG, "点击设置项");
                break;
            case R.id.menu_cancel:
                Log.d(TAG, "点击取消项");
                selectPresenter.unedit();
                onPrepareOptionsMenu(menu);
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
        grammarsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showaddGrammar() {
        Log.d(TAG,"showaddGrammar");
        Intent ahowAddGrammarIntent = new Intent(this.getContext(), AddEditGrammarActivity.class);
        startActivity(ahowAddGrammarIntent);
    }

    @Override
    public void showDeleteResult(int success, int fail) {
        selectPresenter.unedit();
        onPrepareOptionsMenu(menu);
        mPresenter.getGrammars();
        Snackbar.make(this.getView(),getResources().getString(R.string.delete_result,success,fail), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void notifyDataSetChanged() {
        grammarsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(GrammarsSelectContract.Presenter presenter) {
        selectPresenter = presenter;
    }

    @Override
    public void showGrammarsEdit() {
        grammars_edit_rela.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void hideGrammarsEdit() {
        grammars_edit_rela.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEditCount(int count) {
        deletes.setText(getString(R.string.edit_delete) + (count > 0 ? "(" +count+")" : ""));
    }

    @Override
    public List<Grammar> getGrammars() {
        return grammarsAdapter.getGrammars();
    }

    @Override
    public void showDeleteEnabled(Boolean enabled) {
        deletes.setEnabled(enabled);
    }

    @Override
    public void showAllSelect() {
        allSelect.setChecked(true);
    }

    @Override
    public void hideAllSelect() {
        allSelect.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deletes:
                mPresenter.deleteGrammars(selectPresenter.getSelects());
                break;
            case R.id.fab_add_grammar:
                mPresenter.addGrammar();
                break;
            case R.id.allSelect:
                selectPresenter.allSelectClick();
                break;
        }
    }

    private class GrammarsAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<Grammar> grammars;

        private GrammarsSelectContract.Presenter selectPresenter;

        public void replace(List<Grammar> grammars){
            if(grammars != null){
                this.grammars.clear();
                this.grammars.addAll(grammars);
                notifyDataSetChanged();
                selectPresenter.dataSetChanged();
            }
        }

        public List<Grammar> getGrammars() {
            return grammars;
        }

        public GrammarsAdapter(GrammarsSelectContract.Presenter selectPresenter){
            grammars = new ArrayList<>();
            this.selectPresenter = selectPresenter;
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
                viewHolder.select = (CheckBox) convertView.findViewById(R.id.select);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Grammar grammar = grammars.get(position);
            viewHolder.name.setText(grammar.getName());
            viewHolder.content.setText(grammar.getContent());

            //是否显示复选框
            if(selectPresenter.isEdit()){
                if(selectPresenter.isSelect(grammar)){
                    viewHolder.select.setChecked(true);
                }else{
                    viewHolder.select.setChecked(false);
                }
                viewHolder.select.setVisibility(View.VISIBLE);
            }else{
                viewHolder.select.setVisibility(View.GONE);
            }
            viewHolder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPresenter.onClick(grammar);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder{
        CheckBox select;//选中
        TextView name;//名称
        TextView content;//说明
    }
}
