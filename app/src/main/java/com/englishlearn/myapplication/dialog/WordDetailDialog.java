package com.englishlearn.myapplication.dialog;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.core.MdictManager;
import com.englishlearn.myapplication.data.MDict;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.sentencegroups.sentences.sentencecollect.CreateSentenceActivity;
import com.englishlearn.myapplication.service.MusicService;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailFragment;
import com.englishlearn.myapplication.wordgroups.words.wordcollect.WordCollectActivity;

import java.io.IOException;

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
    public static final String DIALOGLISTENER = "dialoglistener";
    public static final String TRACTATE = "tractate";//文章
    private static final String TAG = WordDetailDialog.class.getSimpleName();
    private CompositeSubscription mSubscriptions;
    private String wordstring;
    private String ensentence;
    private String chsentence;

    private Word word;
    private Tractate tractate;
    private TextView wordname;
    private TextView aliasname;//真实名
    private TextView british_phonogram;
    private TextView american_phonogram;
    private TextView translate;
    private TextView correlation;
    private TextView en_sentence;
    private TextView ch_sentence;

    private MusicService musicService;
    private Intent musicIntent;
    private MDict mDict;

    @Inject
    Repository repository;


    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public static WordDetailDialog newInstance(String word,String englishSentence,String chineseSentence,Tractate tractate){

        WordDetailDialog wordDetailDialog = new WordDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putString(WORDTAG, word);
        bundle.putString(ENSENTENCE, englishSentence);
        bundle.putString(CHSENTENCE, chineseSentence);
        bundle.putSerializable(TRACTATE, tractate);
        wordDetailDialog.setArguments(bundle);

        return wordDetailDialog;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        getActivity();
        if(bundle != null){
            wordstring = (String) bundle.get(WORDTAG);
            ensentence = (String) bundle.get(ENSENTENCE);
            chsentence = (String) bundle.get(CHSENTENCE);
            tractate = (Tractate) bundle.getSerializable(TRACTATE);
        }
        MyApplication.instance.getAppComponent().inject(this);
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        setCancelable(false);//不能取消
        if(musicIntent==null){
            musicIntent = new Intent(this.getContext(), MusicService.class);
            getActivity().bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);
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
        aliasname = (TextView) view.findViewById(R.id.aliasname);
        british_phonogram = (TextView) view.findViewById(R.id.british_phonogram);
        american_phonogram = (TextView) view.findViewById(R.id.american_phonogram);
        translate = (TextView) view.findViewById(R.id.translate);
        correlation = (TextView) view.findViewById(R.id.correlation);
        en_sentence = (TextView) view.findViewById(R.id.en_sentence);
        ch_sentence = (TextView) view.findViewById(R.id.ch_sentence);

        if(wordstring != null){
            mDict = getMdict(wordstring);
            if(mDict != null && mDict.getWord() != null){
                this.word = mDict.getWord();
                showWordInfo();
            }else{
                getWord(wordstring);
            }
        }
        showWordBase();
        return view;
    }

    //获取单词
    private void getWord(final String wordstring) {
        Subscription subscription = repository.getWordRxByName(wordstring).subscribe(new Subscriber<Word>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(WordDetailDialog.this.getContext(),bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();
                    repository.addWordByHtml(wordstring);
                }else{
                    Toast.makeText(WordDetailDialog.this.getContext(),"未查到相关单词",Toast.LENGTH_SHORT).show();
                }
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

        if(word.getAliasName() != null && !word.getAliasName().equals(wordstring)){
            aliasname.setText("[" + word.getAliasName() + "]");
        }
        british_phonogram.setText(word.getBritish_phonogram());
        american_phonogram.setText(word.getAmerican_phonogram());
        translate.setText(word.getTranslate());
        correlation.setText(word.getCorrelation());
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick");
                switch (v.getId()){
            case R.id.close:
                Log.d(TAG,"onClose");
                getTargetFragment().onActivityResult(getTargetRequestCode(), TractateDetailFragment.CLOSE, getActivity().getIntent());
                this.dismiss();
                break;
            case R.id.add_word:
                if(wordstring != null){
                    Intent intent = new Intent(this.getContext(),WordCollectActivity.class);
                    intent.putExtra(WordCollectActivity.WORD,wordstring);
                    startActivity(intent);
                }else{
                    Toast.makeText(WordDetailDialog.this.getContext(),"未获取到单词信息",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.british_soundurl:
                Toast.makeText(WordDetailDialog.this.getContext(),word != null ? word.getBritish_soundurl() : "",Toast.LENGTH_SHORT).show();
                if(word != null && word.getBritish_soundurl() != null){
                    play(word.getBritish_soundurl());
                }
                break;
            case R.id.american_soundurl:
                Toast.makeText(WordDetailDialog.this.getContext(),word != null ? word.getAmerican_soundurl() : "",Toast.LENGTH_SHORT).show();
                if(word != null && word.getAmerican_soundurl() != null){
                    play(word.getAmerican_soundurl());
                }
                break;
            case R.id.add_sentence:
                Intent intentsentence = new Intent(this.getContext(),CreateSentenceActivity.class);
                intentsentence.putExtra(CreateSentenceActivity.ENSENTENCE,ensentence);
                intentsentence.putExtra(CreateSentenceActivity.CHSENTENCE,chsentence);
                intentsentence.putExtra(CreateSentenceActivity.TRACTATE,tractate);
                intentsentence.putExtra(CreateSentenceActivity.CREATESENTENCE,true);
                startActivity(intentsentence);
                break;
            case R.id.moresentence:
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param soundUrl
     */
    private void play(String soundUrl){

        if(mDict != null){
            mDict.play();
        }else if(soundUrl != null && !soundUrl.equals("")){
            try {
                musicService.playUrl(soundUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    private MDict getMdict(String wordName){
        return MdictManager.newInstance(this.getContext()).getMDict(wordName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unbindService(musicConnection);
        //musicService.unbindService(musicConnection);
    }
}
