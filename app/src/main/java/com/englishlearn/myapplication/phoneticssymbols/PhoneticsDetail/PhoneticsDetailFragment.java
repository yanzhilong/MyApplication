package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.phoneticssymbols.phoneticsdetails.PhoneticsDetailsActivity;

import java.util.List;

public class PhoneticsDetailFragment extends Fragment implements PhoneticsDetailContract.View {

    private static final String TAG = PhoneticsDetailFragment.class.getSimpleName();

    private PhoneticsDetailContract.Presenter mPresenter;
    private PhoneticsSymbols phoneticsSymbols = null;

    public static PhoneticsDetailFragment newInstance() {
        return new PhoneticsDetailFragment();
    }

    @Override
    public void setPresenter(PhoneticsDetailContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.phoneticsdetail_frag, container, false);

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
    public void showWords(List<Word> list) {
        Toast.makeText(this.getContext(),list.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWordsFail() {
        Toast.makeText(this.getContext(),R.string.networkerror, Toast.LENGTH_SHORT).show();
    }
}