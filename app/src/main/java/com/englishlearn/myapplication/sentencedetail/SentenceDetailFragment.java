package com.englishlearn.myapplication.sentencedetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.addeditsentence.AddEditSentenceActivity;
import com.englishlearn.myapplication.data.Sentence;


/**
 * Created by yanzl on 16-7-20.
 */
public class SentenceDetailFragment extends Fragment implements SentenceDetailContract.View {

    private static final String TAG = SentenceDetailFragment.class.getSimpleName();
    private TextView content;
    private TextView translation;
    private Sentence sentence;

    private SentenceDetailContract.Presenter mPresenter;
    private DialogInterface.OnClickListener deleteAffirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.deleteSentence(sentence);
        }
    };
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
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_sentence);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editSentence();
            }
        });

        FloatingActionButton fabdel =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_delete_sentence);

        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sentence != null) {
                    showDeleteSentenceAffirm();
                }
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
    public void showSentence(Sentence sentence) {
        Log.d(TAG,"showSentence" + sentence);
        this.sentence = sentence;
        if(this.sentence != null){
            content.setText(sentence.getContent());
            translation.setText(sentence.getTranslation());
        }else{
            showEmptySentence();
        }
    }

    @Override
    public void showEditSentence() {
        if(sentence == null){
            showEmptySentence();
            return;
        }
        Intent edit = new Intent(SentenceDetailFragment.this.getContext(), AddEditSentenceActivity.class);
        edit.putExtra(AddEditSentenceActivity.SENTENCE_ID,sentence.getSentenceid());
        edit.putExtra(AddEditSentenceActivity.ID,sentence.getId());
        startActivity(edit);
    }

    @Override
    public void showEmptySentence() {
        Snackbar.make(this.getView(),getResources().getString(R.string.sentencedetail_empty_sentence), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteSentenceAffirm() {
        if(sentence == null){
            showEmptySentence();
            return;
        }
        DeleteAffirmDialogFragment deleteAffirmDialogFragment = new DeleteAffirmDialogFragment();
        deleteAffirmDialogFragment.setOnClickListener(deleteAffirmListener);
        deleteAffirmDialogFragment.show(getFragmentManager(),"delete");
    }

    @Override
    public void showDeleteSuccess() {
        this.getActivity().onBackPressed();
    }

    @Override
    public void showDeleteFail() {
        Snackbar.make(this.getView(),getResources().getString(R.string.detail_deletefail), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static class DeleteAffirmDialogFragment extends DialogFragment {

        private DialogInterface.OnClickListener onClickListener;

        public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.detail_delete_confirm))
                    .setNegativeButton(R.string.detail_cancel,
                            null
                    )
                    .setPositiveButton(R.string.detail_delete,
                            onClickListener
                    )
                    .create();
        }
    }
}
