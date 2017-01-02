package com.englishlearn.myapplication.testmain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.clipboard.ClipboardActivity;
import com.englishlearn.myapplication.grammars.GrammarActivity;
import com.englishlearn.myapplication.loginuser.LoginUserActivity;
import com.englishlearn.myapplication.musicplay.MusicPlayActivity;
import com.englishlearn.myapplication.registeruser.RegisterUserActivity;
import com.englishlearn.myapplication.search.SearchActivity;
import com.englishlearn.myapplication.searchsentences.SearchSentencesActivity;
import com.englishlearn.myapplication.sentence.SentencessActivity;
import com.englishlearn.myapplication.sentencegroup.SentenceGroupActivity;
import com.englishlearn.myapplication.sentencegroupcollect.SentenceGroupCollectActivity;
import com.englishlearn.myapplication.sentencegroups.sentences.sentencecollect.CreateSentenceActivity;
import com.englishlearn.myapplication.test.dialog.DialogActivity;
import com.englishlearn.myapplication.tractatecollect.TractateCollectActivity;
import com.englishlearn.myapplication.tractategroup.TractateGroupActivity;
import com.englishlearn.myapplication.tractatetype.TractateTypeActivity;
import com.englishlearn.myapplication.updateuser.UpdateUserActivity;
import com.englishlearn.myapplication.word.WordActivity;
import com.englishlearn.myapplication.worddetail.WordDetail;
import com.englishlearn.myapplication.wordgroupcollect.WordGroupCollectActivity;
import com.englishlearn.myapplication.wordgroups.words.wordcollect.WordCollectActivity;

public class TestMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmain_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.testmain_title);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sentences:
                Intent sentenceintent = new Intent(this,SentencessActivity.class);
                this.startActivity(sentenceintent);
                break;
            case R.id.grammars:
                Intent grammarintent = new Intent(this,GrammarActivity.class);
                this.startActivity(grammarintent);
                break;
            case R.id.search:
                Intent searchintent = new Intent(this,SearchActivity.class);
                this.startActivity(searchintent);
                break;
            case R.id.searchsentences:
                Intent searchsentencesintent = new Intent(this,SearchSentencesActivity.class);
                this.startActivity(searchsentencesintent);
                break;
            case R.id.registeruser:
                Intent registeruserintent = new Intent(this,RegisterUserActivity.class);
                this.startActivity(registeruserintent);
                break;
            case R.id.loginuser:
                Intent loginuserintent = new Intent(this,LoginUserActivity.class);
                this.startActivity(loginuserintent);
                break;
            case R.id.updateuser:
                Intent updateuserintent = new Intent(this,UpdateUserActivity.class);
                this.startActivity(updateuserintent);
                break;
            case R.id.messagesource:
                Intent messagesourceintent = new Intent(this,UpdateUserActivity.class);
                this.startActivity(messagesourceintent);
                break;
            case R.id.tractatetype:
                Intent tractatetypeintent = new Intent(this,TractateTypeActivity.class);
                this.startActivity(tractatetypeintent);
                break;
            case R.id.word:
                Intent wordintent = new Intent(this,WordActivity.class);
                this.startActivity(wordintent);
                break;
            case R.id.wordgroupcollect:
                Intent wordgroupcollectintent = new Intent(this,WordGroupCollectActivity.class);
                this.startActivity(wordgroupcollectintent);
                break;
            case R.id.sentencegroup:
                Intent sentencegroupintent = new Intent(this,SentenceGroupActivity.class);
                this.startActivity(sentencegroupintent);
                break;
            case R.id.sentencegroupcollect:
                Intent sentencegroupcollectintent = new Intent(this,SentenceGroupCollectActivity.class);
                this.startActivity(sentencegroupcollectintent);
                break;
            case R.id.tractategroup:
                Intent tractategroupintent = new Intent(this,TractateGroupActivity.class);
                this.startActivity(tractategroupintent);
                break;
            case R.id.wordcollect:
                Intent wordcollectintent = new Intent(this,WordCollectActivity.class);
                this.startActivity(wordcollectintent);
                break;
            case R.id.sentencecollect:
                Intent sentencecollectintent = new Intent(this,CreateSentenceActivity.class);
                this.startActivity(sentencecollectintent);
                break;
            case R.id.tractatecollect:
                Intent tractatecollectintent = new Intent(this,TractateCollectActivity.class);
                this.startActivity(tractatecollectintent);
                break;
            case R.id.worddetail:
                Intent worddetail = new Intent(this,WordDetail.class);
                this.startActivity(worddetail);
                break;
            case R.id.testdialog:
                Intent dialogactivity = new Intent(this,DialogActivity.class);
                this.startActivity(dialogactivity);
                break;
            case R.id.musicplay:
                Intent musicplayactivity = new Intent(this,MusicPlayActivity.class);
                this.startActivity(musicplayactivity);
                break;
            case R.id.clipboard:
                Intent clipboardactivity = new Intent(this,ClipboardActivity.class);
                this.startActivity(clipboardactivity);
                break;
            default:
                break;
        }
    }
}
