package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.core.MdictManager;
import com.englishlearn.myapplication.data.MDict;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsVoice;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.service.Playback;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class PhoneticsSymbolsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = PhoneticsSymbolsDetailActivity.class.getSimpleName();
    private PhoneticsSymbols phoneticsSymbols;
    private CompositeSubscription mSubscriptions;
    private MyAdapter myAdapter;

    private TextView phonetics_name;
    private TextView phonetics_type;
    private TextView phonetics_content;
    private LinearLayout voicelayout;

    @Inject
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoneticssymbolsdetail_act);

        MyApplication.instance.getAppComponent().inject(this);

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        if (getIntent().hasExtra(OBJECT)) {
            phoneticsSymbols = (PhoneticsSymbols) getIntent().getSerializableExtra(OBJECT);
        }

        phonetics_name = (TextView) findViewById(R.id.phonetics_name);
        phonetics_type = (TextView) findViewById(R.id.phonetics_type);
        phonetics_content = (TextView) findViewById(R.id.phonetics_content);
        voicelayout = (LinearLayout) findViewById(R.id.voicelayout);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("英:");
        stringBuffer.append("[");
        stringBuffer.append(phoneticsSymbols.getIpaname());
        stringBuffer.append("]");
        stringBuffer.append("美:");
        stringBuffer.append("[");
        stringBuffer.append(phoneticsSymbols.getKkname());
        stringBuffer.append("]");
        stringBuffer.append(" 发音:");

        phonetics_name.setText(stringBuffer);

        StringBuffer stringBuffertype = new StringBuffer();
        stringBuffertype.append("音标类型:");
        stringBuffertype.append(phoneticsSymbols.getType());
        phonetics_type.setText(stringBuffertype);
        phonetics_content.setText(phoneticsSymbols.getContent());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        /*RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }*/

        //GridLayout 3列
        GridLayoutManager mgrgridview=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {

                PhoneticsVoice phoneticsVoice = myAdapter.getWords().get(position);
                //Toast.makeText(PhoneticsSymbolsDetailActivity.this,phoneticsVoice.getFile().getUrl(),Toast.LENGTH_SHORT).show();
                Log.d(TAG,phoneticsVoice.toString());
            }

            @Override
            public void onClick(View view, int position) {
                PhoneticsVoice phoneticsVoice = myAdapter.getWords().get(position);
                Playback.newInstance(PhoneticsSymbolsDetailActivity.this).play(phoneticsVoice.getFile().getUrl());
                //Toast.makeText(PhoneticsSymbolsDetailActivity.this,phoneticsVoice.getFile().getUrl(),Toast.LENGTH_SHORT).show();
                Log.d(TAG,phoneticsVoice.toString());
            }

        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);
        getPhoneticsSymbolsVoicesRx();
    }

    /**
     * 初始读音
     * @param list
     */
    private void initVoices(List<PhoneticsVoice> list) {

        voicelayout.removeAllViews();
        for(PhoneticsVoice phoneticsVoice : list){
            ImageButton imageButton = (ImageButton) LayoutInflater.from(this).inflate(R.layout.voiceimagebutton, null);
           // ImageButton imageButton = new ImageButton(this);
            imageButton.setOnClickListener(this);
            imageButton.setTag(phoneticsVoice.getFile().getUrl());
            //imageButton.setImageDrawable(getResources().getDrawable(R.drawable.volume_play,null));
//            android:src="@drawable/volume_play"
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)AndroidUtils.dipToPixels(this,50),(int)AndroidUtils.dipToPixels(this,50));
            voicelayout.addView(imageButton,layoutParams);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSubscriptions.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showWords(List<PhoneticsVoice> list) {

        //分离音标读音和单词读音
        List<PhoneticsVoice> phoneticsVoices = new ArrayList<>();
        List<PhoneticsVoice> issymbols = new ArrayList<>();


        for(PhoneticsVoice phoneticsVoice:list){
            if(!phoneticsVoice.isSymbols()){
                //添加单词实体
                MDict mdict = getMdict(phoneticsVoice.getName());
                if(mdict != null){
                    phoneticsVoice.setWord(mdict.getWord());
                }else{
                    getWord(phoneticsVoice.getName());
                }
                phoneticsVoices.add(phoneticsVoice);
            }else{
                issymbols.add(phoneticsVoice);
            }
        }
        initVoices(issymbols);//初始化读音
        myAdapter.replaceData(phoneticsVoices);
    }

    public void showWordsFail() {
        Toast.makeText(this,R.string.networkerror, Toast.LENGTH_SHORT).show();
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
                    repository.addWordByHtml(wordstring);
                }
            }

            @Override
            public void onNext(Word word) {
                showWordInfo(word);
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 显示单词音标在列表中
     * @param word
     */
    private void showWordInfo(Word word) {

        List<PhoneticsVoice> phoneticsVoices = myAdapter.getWords();
        for(int i = 0; i < phoneticsVoices.size(); i++){
            PhoneticsVoice phoneticsVoice = phoneticsVoices.get(i);
            if(phoneticsVoice.getName().equals(word.getName())){
                phoneticsVoice.setWord(word);
                myAdapter.notifyItemChanged(i);
                break;
            }
        }
        //myAdapter.replaceData(phoneticsVoices);
    }


    /**
     * 获取音标相关读音
     */
    private void getPhoneticsSymbolsVoicesRx() {
        Subscription subscription = repository.getPhoneticsSymbolsVoicesRx(phoneticsSymbols.getObjectId()).
                subscribe(new Subscriber<List<PhoneticsVoice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showWordsFail();
                    }

                    @Override
                    public void onNext(List<PhoneticsVoice> words) {
                        showWords(words);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private MDict getMdict(String wordName){
        return MdictManager.newInstance(this).getMDict(wordName);
    }

    @Override
    public void onClick(View v) {

        String url = (String) v.getTag();
        Playback.newInstance(this).play(url);
        Log.d(TAG,url);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<PhoneticsVoice> words;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            words = new ArrayList<>();
        }

        public List<PhoneticsVoice> getWords() {
            return words;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<PhoneticsVoice> words){
            if(words != null){
                this.words.clear();
                //this.words.addAll(words);
                for(int i = 0; i < words.size(); i++){
                    this.words.add(words.get(i));
                    notifyDataSetChanged();
                }
                //notifyDataSetChanged();
            }
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.phoneticssymbolsdetailitem, parent, false);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG,"onBindViewHolder" + position);
            PhoneticsVoice phoneticsVoice = words.get(position);
            holder.word_name.setText(words.get(position).getName());
            if(phoneticsVoice.getWord() != null){
                Word word = phoneticsVoice.getWord();
                StringBuffer british = new StringBuffer();
                StringBuffer american = new StringBuffer();
                if(!TextUtils.isEmpty(word.getBritish_phonogram())){
                    british.append("英:" + word.getBritish_phonogram());
                }
                if(!TextUtils.isEmpty(word.getAmerican_phonogram())){
                    american.append("美:" + word.getAmerican_phonogram());
                }
                holder.british_phonogram.setText(british.length() == 0 ? "" : british);
                holder.american_phonogram.setText(american.length() == 0 ? "" : american);
            }
        }

        @Override
        public int getItemCount() {
            return words.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView word_name;
            TextView british_phonogram;
            TextView american_phonogram;
            ImageButton voiceurl;


            public ViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(onItemClickListener != null){
                            onItemClickListener.onItemClick(itemView,getAdapterPosition());
                        }
                    }
                });

                word_name = (TextView) itemView.findViewById(R.id.word_name);
                british_phonogram = (TextView) itemView.findViewById(R.id.british_phonogram);
                american_phonogram = (TextView) itemView.findViewById(R.id.american_phonogram);
                voiceurl = (ImageButton) itemView.findViewById(R.id.voiceurl);

                voiceurl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener != null){
                            onItemClickListener.onClick(itemView,getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    //接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onClick(View view,int position);
    }
}



