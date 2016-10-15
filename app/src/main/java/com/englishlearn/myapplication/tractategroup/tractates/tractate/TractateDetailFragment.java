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
import com.englishlearn.myapplication.data.AddTractate;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.util.List;

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
        //String decumentdemo = AndroidUtils.newInstance(this.getContext()).getRawResource(R.raw.myfather);
        final AddTractate addTractate = AndroidUtils.newInstance(this.getContext()).checkoutTractateByRaw(R.raw.myfather);
        Log.d(TAG,"setText:" + addTractate.getTractate().getContent());

        TextView contenttv = (TextView) root.findViewById(R.id.contenttv);
        TextView translationtv = (TextView) root.findViewById(R.id.translationtv);

        //分别显示两个TextView
       /* contenttv.setText(addTractate.getTractate().getContent());
        translationtv.setText(addTractate.getTractate().getTranslation());*/

        tractatetv.setText(addTractate.getTractate().getContent() + System.getProperty("line.separator") + addTractate.getTractate().getTranslation());

        StringBuffer sb = new StringBuffer();
        sb.append("fist line");
        sb.append(System.getProperty("line.separator"));
        sb.append("second line");
        sb.append(System.getProperty("line.separator"));
        sb.append("three" + System.getProperty("line.separator"));
        sb.append("four");
        Log.d(TAG,sb.toString());

        final StringBuffer textViewNew = new StringBuffer();

        tractatetv.post(new Runnable() {
            @Override
            public void run() {
                //英文每一行内容
                List<String> list = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).getTextViewStringByLine(tractatetv);

                List<List<List<String>>> lists = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).splitTractate(addTractate.getTractate());
                List<List<String>> chineses = lists.get(1);//获得中文的段落

                int paragraph = 0;//段落
                int sentence = 0;//当前行

                for(String englishSentence : list){
                    //加一行英文
                    textViewNew.append(englishSentence + System.getProperty("line.separator"));
                    //判断当前行是否是第几个段落，第几行

                    textViewNew.append(chineses.get(0).get(0) + System.getProperty("line.separator"));
                    break;
                }

                //tractatetv.setText(textViewNew);



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
