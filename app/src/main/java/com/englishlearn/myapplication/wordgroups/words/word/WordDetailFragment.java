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

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class WordDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = WordDetailFragment.class.getSimpleName();
    private String wordName;
    private Word word;
    private TextView british_phonogram;
    private TextView american_phonogram;
    private TextView translate;
    private TextView correlation;
    private TextView remark;
    private CompositeSubscription mSubscriptions;

    public static WordDetailFragment newInstance() {
        return new WordDetailFragment();
    }

    @Inject
    Repository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.instance.getAppComponent().inject(this);
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        Bundle bundle = getArguments();
        if(bundle != null){
            wordName = bundle.getString(WordDetailActivity.WORDNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.worddetail_frag, container, false);

        TextView name = (TextView) root.findViewById(R.id.name);
        british_phonogram = (TextView) root.findViewById(R.id.british_phonogram);
        american_phonogram = (TextView) root.findViewById(R.id.american_phonogram);
        translate = (TextView) root.findViewById(R.id.translate);
        correlation = (TextView) root.findViewById(R.id.correlation);
        remark = (TextView) root.findViewById(R.id.remark);
        ImageButton british_soundurl = (ImageButton) root.findViewById(R.id.british_soundurl);
        ImageButton american_soundurl = (ImageButton) root.findViewById(R.id.american_soundurl);

        name.setText(wordName);


        british_soundurl.setOnClickListener(this);
        american_soundurl.setOnClickListener(this);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        if(wordName != null){
            getWordDetail(wordName);
        }else{
            Toast.makeText(this.getContext(),"未发现单词信息",Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    private void showWordDetail(Word word){
        british_phonogram.setText(word.getBritish_phonogram());
        american_phonogram.setText(word.getAmerican_phonogram());
        translate.setText(word.getTranslate());
        correlation.setText(word.getCorrelation());
        remark.setText(word.getRemark());
    }


    private void getWordDetail(String name){
        Subscription subscription = repository.getWordRxByName(name).subscribe(new Subscriber<Word>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Word word) {
                if(word != null){
                    showWordDetail(word);
                }else{
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscriptions.unsubscribe();
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
