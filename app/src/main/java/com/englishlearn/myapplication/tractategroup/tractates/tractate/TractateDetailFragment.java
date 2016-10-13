package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.util.AndroidUtils;

public class TractateDetailFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = TractateDetailFragment.class.getSimpleName();
    private Tractate tractate;
    private TextView tractatetv;

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

        tractatetv = (TextView) root.findViewById(R.id.tractatetv);
        String decumentdemo = AndroidUtils.newInstance(this.getContext()).getRawResource(R.raw.documentdemo);
        Log.d(TAG,"setText:" + decumentdemo);
        tractatetv.setText(decumentdemo);

        StringBuffer sb = new StringBuffer();
        sb.append("fist line");
        sb.append(System.getProperty("line.separator"));
        sb.append("second line");
        sb.append(System.getProperty("line.separator"));
        sb.append("three" + System.getProperty("line.separator"));
        sb.append("four");
        Log.d(TAG,sb.toString());

        tractatetv.post(new Runnable() {
            @Override
            public void run() {
                AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).getTextViewStringByLine(tractatetv,0);
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
