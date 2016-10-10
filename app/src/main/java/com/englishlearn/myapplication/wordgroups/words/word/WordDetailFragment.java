package com.englishlearn.myapplication.wordgroups.words.word;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Word;

public class WordDetailFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = WordDetailFragment.class.getSimpleName();
    private Word word;

    public static WordDetailFragment newInstance() {
        return new WordDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(OBJECT)) {
            word = (Word) getArguments().getSerializable(OBJECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.worddetail_frag, container, false);

        TextView name = (TextView) root.findViewById(R.id.name);
        TextView british_phonogram = (TextView) root.findViewById(R.id.british_phonogram);
        TextView american_phonogram = (TextView) root.findViewById(R.id.american_phonogram);
        TextView translate = (TextView) root.findViewById(R.id.translate);
        TextView correlation = (TextView) root.findViewById(R.id.correlation);
        TextView remark = (TextView) root.findViewById(R.id.remark);
        ImageButton british_soundurl = (ImageButton) root.findViewById(R.id.british_soundurl);
        ImageButton american_soundurl = (ImageButton) root.findViewById(R.id.american_soundurl);

        name.setText(word.getName());
        british_phonogram.setText(word.getBritish_phonogram());
        american_phonogram.setText(word.getAmerican_phonogram());
        translate.setText(word.getTranslate());
        correlation.setText(word.getCorrelation());
        remark.setText(word.getRemark());

        british_soundurl.setOnClickListener(this);
        american_soundurl.setOnClickListener(this);

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.british_soundurl:
                Toast.makeText(this.getContext(),word.getBritish_soundurl(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.american_soundurl:
                Toast.makeText(this.getContext(),word.getAmerican_soundurl(),Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
