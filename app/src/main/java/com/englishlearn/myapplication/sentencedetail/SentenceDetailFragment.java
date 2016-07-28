package com.englishlearn.myapplication.sentencedetail;

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
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceDetailFragment extends Fragment implements SentenceDetailContract.View {

    private static final String TAG = SentenceDetailFragment.class.getSimpleName();

    private TextView content;
    private TextView translation;

    private SentenceDetailContract.Presenter mPresenter;
    public static SentenceDetailFragment newInstance() {
        return new SentenceDetailFragment();
    }

    @Override
    public void setPresenter(SentenceDetailContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.sentencedetail_frag, container, false);

        content = (TextView) root.findViewById(R.id.content);
        translation = (TextView) root.findViewById(R.id.translation);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_sentence);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.addSentence();
            }
        });


        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getSentence();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
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
                return true;
        }
        return false;
    }

    @Override
    public void showSentence(Sentence sentence) {
        Log.d(TAG,"showSentence");
        if(sentence != null){
            content.setText(sentence.getContent());
            translation.setText(sentence.getTranslation());
        }
    }
}
