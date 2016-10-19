package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
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
    TractateHelper tractateHelper;

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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.tractatedetail_frag, container, false);

        tractatetv = (TextView) root.findViewById(R.id.tractatetv);
        //String decumentdemo = AndroidUtils.newInstance(this.getContext()).getRawResource(R.raw.myfather);
        final AddTractate addTractate = AndroidUtils.newInstance(this.getContext()).checkoutTractateByRaw(R.raw.myfather);
        final Tractate tractate = AndroidUtils.newInstance(this.getContext()).getTractateByRaw(R.raw.abundleofsticks);
        Log.d(TAG,"setText:" + addTractate.getTractate().getContent());

        final TextView contenttv = (TextView) root.findViewById(R.id.contenttv);
        final TextView translationtv = (TextView) root.findViewById(R.id.translationtv);


        //分别显示两个TextView
        //contenttv.setText(addTractate.getTractate().getContent());
        String content = tractate.getContent().replace("\\|","");
        contenttv.setText(content);
        /*translationtv.setText(addTractate.getTractate().getTranslation());*/

        //1. 将英文和中文的内容分解成List
        //获得英文和中文的段落和句子List
        final List<List<List<String>>> tractateList = AndroidUtils.newInstance(this.getContext()).splitTractate(addTractate.getTractate());
        //英文段落
        final List<List<String>> englishParagraph = tractateList.get(0);
        final List<List<String>> chineseParagraph = tractateList.get(1);

        //得到英文的每一段的每一句，添加到一个StringBuffer中

       // tractatetv.setText(addTractate.getTractate().getContent() + System.getProperty("line.separator") + addTractate.getTractate().getTranslation());

        StringBuffer sb = new StringBuffer();
        sb.append("fist line");
        sb.append(System.getProperty("line.separator"));
        sb.append("second line");
        sb.append(System.getProperty("line.separator"));
        sb.append("three" + System.getProperty("line.separator"));
        sb.append("four");
        Log.d(TAG,sb.toString());

        final StringBuffer textViewNew = new StringBuffer();

        final String[] chineseFirst = new String[1];
        contenttv.post(new Runnable() {

            @Override
            public void run() {



                //tractateHelper = new TractateHelper(TractateDetailFragment.this.getContext(),addTractate.getTractate(),contenttv,contenttv.getPaint());
                tractateHelper = new TractateHelper(TractateDetailFragment.this.getContext(),tractate,contenttv,contenttv.getPaint());

                String result = tractateHelper.getTractateString();

                if(result != null){
                    //tractatetv.setText("Hello World\\n\\n\\n");
                    Log.d(TAG,result);
                    tractatetv.setText(result);
                }

                if( 1 > 0){
                    return;
                }

                //1. 将英文和中文的内容分解成List(上面已经分好了)
                //英文采用先放到一个TextView中再取来的加"\n"的方式，这个TextView要比真正显示的要padding5dp，因为要留空间加換行符
                //按英文一行中文一行的方式插入StringBuffer中
                StringBuffer english = new StringBuffer();//用来记录英文，便于单独加效果，也便于只显示英文
                StringBuffer chinese = new StringBuffer();//同上
                StringBuffer tractate = new StringBuffer();//用于最后放入TextView中
                int eParagraph = 0;//用于记录当前段落数(英)
                int cParagraph = 0;//同上(中)
                int eSentence = 0;//用于记录当前句子数(英)
                int cSentence = 0;//同上(中)

                int width = contenttv.getWidth();//当前TextView宽度
                TextPaint textPaint = contenttv.getPaint();//用于计算占用空间

                //英文每一行内容
                List<String> list = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).getTextViewStringByLine(contenttv);

                for(int i = 0; i < list.size(); i++){
                    //判断当前行是否是換行符,是跳过
                    if(list.get(i).equals(System.getProperty("line.separator"))){
                        break;
                    }
                    //判断当前行英文包含的句子数


                }





             /*   //英文每一行内容
                List<String> list = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).getTextViewStringByLine(contenttv);

                //每一行重新加上一个空行，重新设置回去
                StringBuffer contentNew = new StringBuffer();
                for(int i = 0; i < list.size(); i ++){
                    contentNew.append(list.get(i) + System.getProperty("line.separator") + System.getProperty("line.separator"));

                }

                contenttv.setText("");
                translationtv.setText(contentNew.toString());*/

             /*   //获得第一行是否有一句話，有的話截取家句話计算占用的空间
                for(int i = 0; i < list.size(); i ++){
                    Pattern english = Pattern.compile(AndroidUtils.englishregex,Pattern.CASE_INSENSITIVE);
                    String[] englishsentences = english.split(list.get(i));
                    if(englishsentences.length > 0){
                        //第一句有分句，取出第一句的占用位置，
                        TextPaint textPaint = contenttv.getPaint();
                        float textPaintWidth = textPaint.measureText(englishsentences[0]+".");
                        //判断中文第一句的宽度
                        chineseFirst[0] = chineseParagraph.get(0).get(0)+".";
                        while (textPaint.measureText(chineseFirst[0]) < textPaintWidth){
                            chineseFirst[0] += " ";
                        }
                        //计算出第一句后面需要的空格
                        //中文每一行内容
                        //每一行重新加上一个空行，重新设置回去
                        StringBuffer translationNew = new StringBuffer();
                *//*for(int i = 0; i < list.size(); i ++){
                    translationNew.append(System.getProperty("line.separator") + list.get(i) + System.getProperty("line.separator"));

                }*//*
                        translationNew.append(System.getProperty("line.separator") + chineseFirst[0] + "他是");
                        translationtv.setText(translationNew.toString());
                        break;
                    }
                }
*/

               /* List<List<List<String>>> lists = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).splitTractate(addTractate.getTractate());
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
*/
                //tractatetv.setText(textViewNew);



            }
        });


        /*translationtv.post(new Runnable() {
            @Override
            public void run() {
                //中文每一行内容
                List<String> list = AndroidUtils.newInstance(TractateDetailFragment.this.getContext()).getTextViewStringByLine(translationtv);

                //每一行重新加上一个空行，重新设置回去
                StringBuffer translationNew = new StringBuffer();
                *//*for(int i = 0; i < list.size(); i ++){
                    translationNew.append(System.getProperty("line.separator") + list.get(i) + System.getProperty("line.separator"));

                }*//*
                translationNew.append(chineseFirst[0] + "他是");
                translationtv.setText(translationNew.toString());
            }
        });*/

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
