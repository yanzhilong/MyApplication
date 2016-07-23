package com.englishlearn.myapplication.addeditgrammar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-7-20.
 */
public class AddEditGrammarFragment extends Fragment implements AddEditGrammarContract.View {

    private static final String TAG = AddEditGrammarFragment.class.getSimpleName();

    private EditText name;
    private EditText content;

    private AddEditGrammarContract.Presenter mPresenter;
    public static AddEditGrammarFragment newInstance() {
        return new AddEditGrammarFragment();
    }

    @Override
    public void setPresenter(AddEditGrammarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.addeditgrammar_frag, container, false);

        name = (EditText) root.findViewById(R.id.name);
        content = (EditText) root.findViewById(R.id.content);
        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_grammar_done);

        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveSentence(name.getText().toString(),content.getText().toString());
            }
        });

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setName() {

    }

    @Override
    public void setContent() {

    }

    @Override
    public void showGrammars() {

    }


    @Override
    public void addGrammarSuccess() {
        Log.d(TAG, "addGrammarSuccess");
        name.setText("");
        content.setText("");
        Snackbar.make(this.getView(),getResources().getString(R.string.addgrammarsuccess), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
