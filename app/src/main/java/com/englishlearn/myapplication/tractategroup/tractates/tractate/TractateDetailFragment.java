package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

public class TractateDetailFragment extends Fragment implements View.OnClickListener {

    public static final String OBJECT = "object";
    private Spannable spannable = null;
    private static final String TAG = TractateDetailFragment.class.getSimpleName();
    private Tractate tractate;
    private TextView tractatetv;
    TractateHelper tractateHelper;
    private Context mContext;
    private String result;

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

                result = tractateHelper.getTractateString();

                if(result != null){
                    Log.d(TAG, result);
                    contenttv.setVisibility(View.GONE);
                    //tractatetv.setText(result);


                    //获得句子下标
                    List<List<int[]>> sents = tractateHelper.getSentenceIndexs();

                    /*for(int i = 0; i < sents.size(); i++){
                        List<int[]> list = sents.get(i);

                        for (int j = 0; j < list.size(); j++){
                            int[] sentent = list.get(j);
                            Log.d(TAG,"第"+i+"段第"+j+"句" + result.substring(sentent[0],sentent[1]));
                        }
                    }*/

                    tractatetv.setMovementMethod(new MovementMethod());
                    tractatetv.setText(result, TextView.BufferType.SPANNABLE);
                    Spannable spans = (Spannable) tractatetv.getText();

                    BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
                    iterator.setText(result);
                    int start = iterator.first();

                    for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                            .next()) {

                        String possibleWord = result.substring(start, end);
                        if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                            int[] ints = getSentenceIndex(sents,possibleWord,start,end);

                            ClickableSpan clickSpan = getClickableSpan(possibleWord, ints[0], ints[1], english, chinese);
                            spans.setSpan(clickSpan, start, end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }

            }
        });


        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }


    private int[] getSentenceIndex(List<List<int[]>> sents,String word,int start,int end){
        int[] ints = new int[2];

        //循环每一句，设置可点击
        for(int i = 0; i < sents.size(); i++){
            List<int[]> list = sents.get(i);

            for (int j = 0; j < list.size(); j++){
                int[] sentent = list.get(j);

                if(start >= sentent[0] && end <= sentent[1]){
                    ints[0] = i;
                    ints[1] = j;
                }
            }
        }
        if(ints[0] == 0 && ints[1] == 0){
            Log.d(TAG,"单词" + word + "未找到" + start + ":" + end);
        }

        return ints;
    }



    /**
     *
     * @param word 单词
     * @param paragraph 所属段落
     * @param sentence 所属句子
     * @return
     */
    private ClickableSpan getClickableSpan(final String word, final int paragraph, final int sentence, final List<List<String>> english, final List<List<String>> chinese) {
        return new ClickableSpan() {
            final String mWord;
            final String englishSentence;
            final String chineseSentence;
            {
                mWord = word;
                englishSentence = english.get(paragraph).get(sentence);
                chineseSentence = chinese.get(paragraph).get(sentence);
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);
                //显示单词
                Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT)
                        .show();
                /*//显示句子
                Toast.makeText(widget.getContext(), englishSentence.substring(0,3) + englishSentence.substring(englishSentence.length() - 2,englishSentence.length() - 1) + "\\n" + chineseSentence.substring(0,1) + chineseSentence.substring(chineseSentence.length() - 2,chineseSentence.length() - 1), Toast.LENGTH_SHORT)
                        .show();*/
                Toast.makeText(widget.getContext(), englishSentence.substring(0,10), Toast.LENGTH_SHORT)
                        .show();
                Log.d(TAG,englishSentence + "\\\n" + chineseSentence);
            }

            public void updateDrawState(TextPaint ds) {
                //super.updateDrawState(ds);
                ds.setUnderlineText(false); //去掉下划线
            }
        };
    }

/**
 *
 * 空白点击不会显示最后一个单词
 */
public class MovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {


        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            if (off >= widget.getText().length()) {
                // Return true so click won't be triggered in the leftover empty space
                if (spannable != null) {
                    Selection.removeSelection(spannable);
                }
                return true;
            } else {
                spannable = buffer;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

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
