package com.englishlearn.myapplication.sentences;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.addeditsentence.AddEditSentenceActivity;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.sentencedetail.SentenceDetailActivity;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesFragment extends Fragment implements SentencesContract.View {

    private static final String TAG = SentencesFragment.class.getSimpleName();
    private SentencesAdapter sentencesAdapter;
    private SentencesContract.Presenter mPresenter;

    private ListView sentences_listview;

    public static SentencesFragment newInstance() {
        return new SentencesFragment();
    }

    @Override
    public void setPresenter(SentencesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sentencesAdapter = new SentencesAdapter(new ArrayList<Sentence>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.sentences_frag, container, false);

        sentences_listview = (ListView) root.findViewById(R.id.sentences_listview);
        sentences_listview.setAdapter(sentencesAdapter);
        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_sentence);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addSentence();
            }
        });

        sentences_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sentence sentence = sentencesAdapter.getSentences().get(position);
                Intent detail = new Intent(SentencesFragment.this.getContext(), SentenceDetailActivity.class);
                detail.putExtra(SentenceDetailActivity.SENTENCE_ID,sentence.getmId());
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
        mPresenter.getSentences();
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
    public void showSentences(List<Sentence> sentences) {
        Log.d(TAG,"showSentences" + sentences.size());
        sentencesAdapter.replace(sentences);
    }

    @Override
    public void emptySentences() {
        Log.d(TAG,"emptySentences");
    }

    @Override
    public void showaddSentence() {

        Log.d(TAG,"showaddSentence");
        Intent ahowAddSentenctIntent = new Intent(this.getContext(), AddEditSentenceActivity.class);
        startActivity(ahowAddSentenctIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private class SentencesAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public List<Sentence> getSentences() {
            return sentences;
        }

        private List<Sentence> sentences;

        public void replace(List<Sentence> sentences){
            this.sentences = sentences;
            notifyDataSetChanged();
        }

        public void addSentences(List<Sentence> sentences){
            this.sentences.addAll(sentences);
            notifyDataSetChanged();
        }

        public SentencesAdapter(List<Sentence> sentences){
            this.sentences = sentences;
        }

        @Override
        public int getCount() {
            return sentences.size();
        }

        @Override
        public Object getItem(int position) {
            return sentences.get(position);
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
                convertView = inflater.inflate(R.layout.sentence_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                viewHolder.translation = (TextView) convertView.findViewById(R.id.translation);
                viewHolder.grammars = (LinearLayout) convertView.findViewById(R.id.grammars);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Sentence sentence = sentences.get(position);
            viewHolder.content.setText(sentence.getContent());
            viewHolder.translation.setText(sentence.getTranslation());
            //显示语法
            List<Grammar> grammars = sentence.getGrammarList();
            if(grammars != null){
                viewHolder.grammars.removeAllViews();
                initGrammars(viewHolder.grammars,grammars);
            }
            return convertView;
        }
    }

    /**
     * 添加TextView到LinearLayout中
     * @param linearLayout
     * @param grammars
     */
    private void initGrammars(LinearLayout linearLayout,List<Grammar> grammars){
        for(Grammar grammar:grammars){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = (int) AndroidUtils.dipToPixels(this.getContext(),10f);
            TextView textView = new TextView(this.getContext());
            textView.setText(grammar.getName());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            textView.setTextColor(getResources().getColor(R.color.text_color_grey));
            textView.setBackgroundResource(R.drawable.grammar_tip);
            textView.setPadding(10,3,10,3);
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);
        }
    }

    static class ViewHolder{
        TextView content;//内容
        TextView translation;//译文
        LinearLayout grammars;//语法
    }
}