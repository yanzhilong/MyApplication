package com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails;

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
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.Word;

import java.util.List;


/**
 * Created by yanzl on 16-7-20.
 */
public class PhoneticsDetailsFragment extends Fragment implements PhoneticsDetailsContract.View {

    private static final String TAG = PhoneticsDetailsFragment.class.getSimpleName();

    private PhoneticsDetailsContract.Presenter mPresenter;
    private PhoneticsSymbols phoneticsSymbols = null;

    public static PhoneticsDetailsFragment newInstance() {
        return new PhoneticsDetailsFragment();
    }

    @Override
    public void setPresenter(PhoneticsDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneticsSymbols = (PhoneticsSymbols) getArguments().get(PhoneticsDetailsActivity.PHONETICS);
        Log.d(TAG,phoneticsSymbols.toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.common_frag, container, false);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getWords();
    }

    @Override
    public void onPause() {
        super.onPause();
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
    public void showWords(List<Word> list) {
        Toast.makeText(this.getContext(),list.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWordsFail() {
        Toast.makeText(this.getContext(),R.string.networkerror, Toast.LENGTH_SHORT).show();
    }
}