package com.englishlearn.myapplication.testmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.grammars.GrammarActivity;
import com.englishlearn.myapplication.loginuser.LoginUserActivity;
import com.englishlearn.myapplication.messagesource.MessageSourceActivity;
import com.englishlearn.myapplication.registeruser.RegisterUserActivity;
import com.englishlearn.myapplication.search.SearchActivity;
import com.englishlearn.myapplication.searchsentences.SearchSentencesActivity;
import com.englishlearn.myapplication.sentencecollect.SentenceCollectActivity;
import com.englishlearn.myapplication.sentencegroup.SentenceGroupActivity;
import com.englishlearn.myapplication.sentencegroupcollect.SentenceGroupCollectActivity;
import com.englishlearn.myapplication.sentence.SentencesActivity;
import com.englishlearn.myapplication.tractatecollect.TractateCollectActivity;
import com.englishlearn.myapplication.tractategroup.TractateGroupActivity;
import com.englishlearn.myapplication.tractatetype.TractateTypeActivity;
import com.englishlearn.myapplication.updateuser.UpdateUserActivity;
import com.englishlearn.myapplication.word.WordActivity;
import com.englishlearn.myapplication.wordcollect.WordCollectActivity;
import com.englishlearn.myapplication.worddetail.WordDetail;
import com.englishlearn.myapplication.wordgroupcollect.WordGroupCollectActivity;

/**
 * Created by yanzl on 16-7-20.
 */
public class TestMainFragment extends Fragment implements TestMainContract.View, View.OnClickListener {

    private static final String TAG = TestMainFragment.class.getSimpleName();
    private TestMainContract.Presenter mPresenter;

    public static TestMainFragment newInstance() {
        return new TestMainFragment();
    }

    @Override
    public void setPresenter(TestMainContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.testmain_frag, container, false);

        Button sentence = (Button) root.findViewById(R.id.sentences);
        Button grammar = (Button) root.findViewById(R.id.grammars);
        Button search = (Button) root.findViewById(R.id.search);
        Button searchsentences = (Button) root.findViewById(R.id.searchsentences);
        Button registeruser = (Button) root.findViewById(R.id.registeruser);
        Button loginuser = (Button) root.findViewById(R.id.loginuser);
        Button updateuser = (Button) root.findViewById(R.id.updateuser);
        Button messagesource = (Button) root.findViewById(R.id.messagesource);
        Button tractatetype = (Button) root.findViewById(R.id.tractatetype);
        Button word = (Button) root.findViewById(R.id.word);
        Button wordgroup = (Button) root.findViewById(R.id.wordgroup);
        Button wordgroupcollect = (Button) root.findViewById(R.id.wordgroupcollect);
        Button sentencegroup = (Button) root.findViewById(R.id.sentencegroup);
        Button sentencegroupcollect = (Button) root.findViewById(R.id.sentencegroupcollect);
        Button tractategroup = (Button) root.findViewById(R.id.tractategroup);
        Button wordcollect = (Button) root.findViewById(R.id.wordcollect);
        Button sentencecollect = (Button) root.findViewById(R.id.sentencecollect);
        Button tractatecollect = (Button) root.findViewById(R.id.tractatecollect);
        Button worddetail = (Button) root.findViewById(R.id.worddetail);

        sentence.setOnClickListener(this);
        grammar.setOnClickListener(this);
        search.setOnClickListener(this);
        searchsentences.setOnClickListener(this);
        registeruser.setOnClickListener(this);
        loginuser.setOnClickListener(this);
        updateuser.setOnClickListener(this);
        messagesource.setOnClickListener(this);
        tractatetype.setOnClickListener(this);
        word.setOnClickListener(this);
        wordgroup.setOnClickListener(this);
        wordgroupcollect.setOnClickListener(this);
        sentencegroup.setOnClickListener(this);
        sentencegroupcollect.setOnClickListener(this);
        tractategroup.setOnClickListener(this);
        wordcollect.setOnClickListener(this);
        sentencecollect.setOnClickListener(this);
        tractatecollect.setOnClickListener(this);
        worddetail.setOnClickListener(this);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sentences:
                Intent sentenceintent = new Intent(this.getContext(),SentencesActivity.class);
                this.startActivity(sentenceintent);
                break;
            case R.id.grammars:
                Intent grammarintent = new Intent(this.getContext(),GrammarActivity.class);
                this.startActivity(grammarintent);
                break;
            case R.id.search:
                Intent searchintent = new Intent(this.getContext(),SearchActivity.class);
                this.startActivity(searchintent);
                break;
            case R.id.searchsentences:
                Intent searchsentencesintent = new Intent(this.getContext(),SearchSentencesActivity.class);
                this.startActivity(searchsentencesintent);
                break;
            case R.id.registeruser:
                Intent registeruserintent = new Intent(this.getContext(),RegisterUserActivity.class);
                this.startActivity(registeruserintent);
                break;
            case R.id.loginuser:
                Intent loginuserintent = new Intent(this.getContext(),LoginUserActivity.class);
                this.startActivity(loginuserintent);
                break;
            case R.id.updateuser:
                Intent updateuserintent = new Intent(this.getContext(),UpdateUserActivity.class);
                this.startActivity(updateuserintent);
                break;
            case R.id.messagesource:
                Intent messagesourceintent = new Intent(this.getContext(),MessageSourceActivity.class);
                this.startActivity(messagesourceintent);
                break;
            case R.id.tractatetype:
                Intent tractatetypeintent = new Intent(this.getContext(),TractateTypeActivity.class);
                this.startActivity(tractatetypeintent);
                break;
            case R.id.word:
                Intent wordintent = new Intent(this.getContext(),WordActivity.class);
                this.startActivity(wordintent);
                break;
            case R.id.wordgroupcollect:
                Intent wordgroupcollectintent = new Intent(this.getContext(),WordGroupCollectActivity.class);
                this.startActivity(wordgroupcollectintent);
                break;
            case R.id.sentencegroup:
                Intent sentencegroupintent = new Intent(this.getContext(),SentenceGroupActivity.class);
                this.startActivity(sentencegroupintent);
                break;
            case R.id.sentencegroupcollect:
                Intent sentencegroupcollectintent = new Intent(this.getContext(),SentenceGroupCollectActivity.class);
                this.startActivity(sentencegroupcollectintent);
                break;
            case R.id.tractategroup:
                Intent tractategroupintent = new Intent(this.getContext(),TractateGroupActivity.class);
                this.startActivity(tractategroupintent);
                break;
            case R.id.wordcollect:
                Intent wordcollectintent = new Intent(this.getContext(),WordCollectActivity.class);
                this.startActivity(wordcollectintent);
                break;
            case R.id.sentencecollect:
                Intent sentencecollectintent = new Intent(this.getContext(),SentenceCollectActivity.class);
                this.startActivity(sentencecollectintent);
                break;
            case R.id.tractatecollect:
                Intent tractatecollectintent = new Intent(this.getContext(),TractateCollectActivity.class);
                this.startActivity(tractatecollectintent);
                break;
            case R.id.worddetail:
                Intent worddetail = new Intent(this.getContext(),WordDetail.class);
                this.startActivity(worddetail);
                break;
            default:
                break;
        }
    }
}
