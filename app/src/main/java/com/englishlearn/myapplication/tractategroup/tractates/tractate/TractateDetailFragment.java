package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;

public class TractateDetailFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = TractateDetailFragment.class.getSimpleName();
    private Tractate tractate;

    public static TractateDetailFragment newInstance() {
        return new TractateDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(OBJECT)) {
            tractate = (Tractate) getArguments().getSerializable(OBJECT);
            Toast.makeText(this.getContext(),tractate.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.tractatedetail_frag, container, false);


        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            default:
                break;
        }
    }
}
