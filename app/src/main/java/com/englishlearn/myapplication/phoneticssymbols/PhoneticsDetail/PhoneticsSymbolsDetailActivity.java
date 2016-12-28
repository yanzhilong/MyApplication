package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.PhoneticsVoice;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.englishlearn.myapplication.R.id.phonetics_videourl;

public class PhoneticsSymbolsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = PhoneticsSymbolsDetailActivity.class.getSimpleName();
    private PhoneticsSymbols phoneticsSymbols;
    private CompositeSubscription mSubscriptions;
    private MyAdapter myAdapter;

    private TextView phonetics_ipaname;
    private TextView phonetics_kkname;
    private ImageButton phonetics_soundurl;
    private TextView phonetics_type;
    private TextView phonetics_content;


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

        phonetics_ipaname = (TextView) findViewById(R.id.phonetics_ipaname);
        phonetics_kkname = (TextView) findViewById(R.id.phonetics_kkname);
        phonetics_soundurl = (ImageButton) findViewById(R.id.phonetics_soundurl);
        phonetics_type = (TextView) findViewById(R.id.phonetics_type);
        phonetics_content = (TextView) findViewById(R.id.phonetics_content);

        phonetics_soundurl.setOnClickListener(this);

        phonetics_ipaname.setText(phoneticsSymbols.getIpaname());
        phonetics_kkname.setText(phoneticsSymbols.getKkname());
        phonetics_type.setText(phoneticsSymbols.getType());
        phonetics_content.setText(phoneticsSymbols.getContent());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //GridLayout 3列
        GridLayoutManager mgrgridview=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {

                PhoneticsVoice phoneticsVoice = myAdapter.getWords().get(position);
                Toast.makeText(PhoneticsSymbolsDetailActivity.this,phoneticsVoice.getFile().getUrl(),Toast.LENGTH_SHORT).show();
                Log.d(TAG,phoneticsVoice.toString());
            }

        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWords();
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
        Toast.makeText(this,list.toString(), Toast.LENGTH_SHORT).show();
        myAdapter.replaceData(list);
    }

    public void showWordsFail() {
        Toast.makeText(this,R.string.networkerror, Toast.LENGTH_SHORT).show();
    }

    void getWords() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phonetics_soundurl:
                //Toast.makeText(this,phoneticsSymbols.getSoundurl(),Toast.LENGTH_LONG).show();

                break;
            case phonetics_videourl:
                //Toast.makeText(this,phoneticsSymbols.getVideourl(),Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
                this.words.addAll(words);
                notifyDataSetChanged();
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
            holder.word_name.setText(words.get(position).getName());
            holder.word_british_phonogram.setText(words.get(position).getFile().getUrl());
        }

        @Override
        public int getItemCount() {
            return words.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView word_name;
            TextView word_british_phonogram;

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
                word_british_phonogram = (TextView) itemView.findViewById(R.id.word_british_phonogram);

            }
        }
    }

    //接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}



