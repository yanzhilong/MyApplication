package com.englishlearn.myapplication.grammardetail;

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
import com.englishlearn.myapplication.addeditgrammar.AddEditGrammarActivity;
import com.englishlearn.myapplication.data.Grammar;


/**
 * Created by yanzl on 16-7-28.
 */
public class GrammarDetailFragment extends Fragment implements GrammarDetailContract.View {

    private static final String TAG = GrammarDetailFragment.class.getSimpleName();
    private TextView name;
    private TextView content;
    private Grammar grammar;

    private GrammarDetailContract.Presenter mPresenter;
    private DialogInterface.OnClickListener deleteAffirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.deleteGrammar(grammar);
        }
    };
    public static GrammarDetailFragment newInstance() {
        return new GrammarDetailFragment();
    }

    @Override
    public void setPresenter(GrammarDetailContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.grammardetail_frag, container, false);

        name = (TextView) root.findViewById(R.id.name);
        content = (TextView) root.findViewById(R.id.content);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_grammar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editGrammar();
            }
        });

        FloatingActionButton fabdel =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_delete_grammar);

        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grammar != null) {
                    showDeleteGrammarAffirm();
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
        mPresenter.getGrammar();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showGrammar(Grammar grammar) {
        Log.d(TAG,"showGrammar");
        this.grammar = grammar;
        if(grammar != null){
            name.setText(grammar.getName());
            content.setText(grammar.getContent());
        }
    }

    @Override
    public void showEditGrammar() {
        if(grammar == null){
            showEmptyGrammar();
            return;
        }
        Intent edit = new Intent(GrammarDetailFragment.this.getContext(), AddEditGrammarActivity.class);
        edit.putExtra(AddEditGrammarActivity.GRAMMAR_ID,grammar.getGrammarid());
        edit.putExtra(AddEditGrammarActivity.ID,grammar.getId());
        startActivity(edit);
    }

    @Override
    public void showEmptyGrammar() {
        Snackbar.make(this.getView(),getResources().getString(R.string.grammardetail_empty_grammar), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteGrammarAffirm() {
        if(grammar == null){
            showEmptyGrammar();
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
