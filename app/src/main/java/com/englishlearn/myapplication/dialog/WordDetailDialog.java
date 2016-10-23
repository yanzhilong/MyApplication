package com.englishlearn.myapplication.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

/**
 * Created by yanzl on 16-10-23.
 */

public class WordDetailDialog extends DialogFragment implements View.OnClickListener {

    public static final String WORDTAG = "word";
    public static final String ENSENTENCE = "ensentence";
    public static final String CHSENTENCE = "chsentence";
    private CompositeSubscription mSubscriptions;
    private String wordstring;
    private String ensentence;
    private String chsentence;

    private Word word;

    private TextView wordname;
    private TextView british_phonogram;
    private TextView american_phonogram;
    private TextView translate;
    private TextView correlation;
    private TextView en_sentence;
    private TextView ch_sentence;

    @Inject
    Repository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            wordstring = (String) bundle.get(WORDTAG);
            ensentence = (String) bundle.get(ENSENTENCE);
            chsentence = (String) bundle.get(CHSENTENCE);
        }
        MyApplication.instance.getAppComponent().inject(this);
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.worddetail_dia, container);

        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.add_word).setOnClickListener(this);
        view.findViewById(R.id.british_soundurl).setOnClickListener(this);
        view.findViewById(R.id.american_soundurl).setOnClickListener(this);
        view.findViewById(R.id.add_sentence).setOnClickListener(this);
        view.findViewById(R.id.moresentence).setOnClickListener(this);

        wordname = (TextView) view.findViewById(R.id.wordname);
        british_phonogram = (TextView) view.findViewById(R.id.british_phonogram);
        american_phonogram = (TextView) view.findViewById(R.id.american_phonogram);
        translate = (TextView) view.findViewById(R.id.translate);
        correlation = (TextView) view.findViewById(R.id.correlation);
        en_sentence = (TextView) view.findViewById(R.id.en_sentence);
        ch_sentence = (TextView) view.findViewById(R.id.ch_sentence);

        if(wordstring != null){
            getWord(wordstring);
        }

        showWordBase();

        return view;
    }

    //获取单词
    private void getWord(String wordstring) {
        Subscription subscription = repository.getWordRxByName(wordstring).subscribe(new Subscriber<Word>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                Toast.makeText(WordDetailDialog.this.getContext(),"未查到相关单词",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Word word) {
                WordDetailDialog.this.word = word;
                showWordInfo();
            }
        });
        mSubscriptions.add(subscription);
    }

    //显示基本信息
    private void showWordBase(){
        wordname.setText(wordstring);
        en_sentence.setText(ensentence);
        ch_sentence.setText(chsentence);
    }

    //显示单词详情
    private void showWordInfo(){

        british_phonogram.setText(word.getBritish_phonogram());
        american_phonogram.setText(word.getAmerican_phonogram());
        translate.setText(word.getTranslate());
        correlation.setText(word.getCorrelation());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.close:
                this.dismiss();
                break;
            case R.id.add_word:

                break;
            case R.id.british_soundurl:
                Toast.makeText(WordDetailDialog.this.getContext(),word != null ? word.getBritish_soundurl() : "",Toast.LENGTH_SHORT).show();
                break;
            case R.id.american_soundurl:
                Toast.makeText(WordDetailDialog.this.getContext(),word != null ? word.getAmerican_soundurl() : "",Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_sentence:

                break;
            case R.id.moresentence:

                break;
            default:
                break;
        }
    }

}
