package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.content.Context;
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

import java.util.List;

public class TractateDetailFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    private static final String TAG = TractateDetailFragment.class.getSimpleName();
    private Tractate tractate;
    private TextView tractatetv;
    TractateHelper tractateHelper;
    private Context mContext;

    public static TractateDetailFragment newInstance() {
        return new TractateDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments().containsKey(OBJECT)) {
            tractate = (Tractate) getArguments().getSerializable(OBJECT);
            Toast.makeText(this.getContext(),tractate.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.tractatedetail_frag, container, false);

        final TextView contenttv = (TextView) root.findViewById(R.id.contenttv);
        float newLineWidth = contenttv.getPaint().measureText(System.getProperty("line.separator")) + 1f;
        contenttv.setPadding(0,0, (int) newLineWidth * 2,0);

        tractatetv = (TextView) root.findViewById(R.id.tractatetv);

        final Tractate tractate = AndroidUtils.newInstance(this.getContext()).getTractateByRaw(R.raw.abundleofsticks);

        final Tractate tractate1 = AndroidUtils.newInstance(this.getContext()).getTractateByRaw(R.raw.newconcept_one_lesson1);

        //分别显示两个TextView
        String content = tractate.getContent().replace("|","");
        contenttv.setText(content);

        //分别显示两个TextView
        String content1 = tractate1.getContent().replace("|","");
        //contenttv.setText(content1);

        //获得英文和中文的段落和句子List
        final List<List<List<String>>> tractateList = AndroidUtils.newInstance(mContext).splitTractate(tractate);

        //获得英文和中文的段落和句子List
        final List<List<List<String>>> tractateList1 = AndroidUtils.newInstance(mContext).splitTractate(tractate1);


        final List<List<String>> english = tractateList.get(0);//英语段落列表
        final List<List<String>> chinese = tractateList.get(1);//译文段落列表

        final List<List<String>> english1 = tractateList1.get(0);//英语段落列表
        final List<List<String>> chinese1 = tractateList1.get(1);//译文段落列表

        contenttv.post(new Runnable() {

            @Override
            public void run() {

                List<String> textViewLine = AndroidUtils.newInstance(mContext).getTextViewStringByLine(contenttv);
                tractateHelper = new TractateHelper(english,chinese,textViewLine,contenttv.getWidth(),contenttv.getPaint());

                //tractateHelper = new TractateHelper(english1,chinese1,textViewLine,contenttv.getWidth(),contenttv.getPaint());

                String result = tractateHelper.getTractateString();

                if(result != null){
                    Log.d(TAG,result);
                    contenttv.setVisibility(View.GONE);
                    tractatetv.setText(result);
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
