package com.englishlearn.myapplication.tractategroup.preview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.dialog.WordDetailDialog;
import com.englishlearn.myapplication.tractategroup.tractate.TractateHelper;
import com.englishlearn.myapplication.util.AndroidUtils;

import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TractatePreviewActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String FILEPATH = "filepath";
    private static final String TAG = TractatePreviewActivity.class.getSimpleName();
    private String filepath;

    private Spannable spannable = null;
    private Tractate tractate;
    private TextView tractatetv;
    TractateHelper tractateHelper;
    private Context mContext;
    private String result;
    private MyClickableSpan clickableSpan;
    private List<List<String>> chinese;
    private List<List<String>> english;
    private TextView contenttv;
    private TextView loading;
    private WordDetailDialog wordDetailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractatepreview_act);

        if (getIntent().hasExtra(FILEPATH)) {
            filepath = getIntent().getStringExtra(FILEPATH);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractatepreview);

        mContext = this;
        wordDetailDialog = new WordDetailDialog();

        loading = (TextView) findViewById(R.id.loading);
        contenttv = (TextView) findViewById(R.id.contenttv);
        float newLineWidth = contenttv.getPaint().measureText(System.getProperty("line.separator")) + 1f;
        contenttv.setPadding(0,0, (int) newLineWidth * 2,0);

        tractatetv = (TextView) findViewById(R.id.tractatetv);

        findViewById(R.id.byline).setOnClickListener(this);
        findViewById(R.id.byparagraph).setOnClickListener(this);
        findViewById(R.id.byenglish).setOnClickListener(this);

        final Tractate tractate = AndroidUtils.newInstance(this).getTractateByfilePath(filepath);


        //分别显示两个TextView
        String content = tractate.getContent().replace("|","");
        contenttv.setText(content);

        //获得英文和中文的段落和句子List
        final List<List<List<String>>> tractateList = AndroidUtils.newInstance(mContext).splitTractate(tractate);


        //英语段落列表
        english = tractateList.get(0);
        //译文段落列表
        chinese = tractateList.get(1);

        setByparagraph();
        //setBysentence();
        //setJustEnglish();


    }

    //只显示英文
    private void setJustEnglish(){

        contenttv.setVisibility(View.GONE);
        tractateHelper = new TractateHelper(english,chinese);
        String result = tractateHelper.getTractateByEnglishString();
        //获得句子下标
        List<List<int[]>> sents = tractateHelper.getSentenceIndexs();

        setClickableSpan(sents,tractatetv,result);

    }

    //一段英文一段中文
    private void setByparagraph(){

        contenttv.setVisibility(View.GONE);
        tractateHelper = new TractateHelper(english,chinese);
        String result = tractateHelper.getTractateByParagraphString();
        //获得句子下标
        List<List<int[]>> sents = tractateHelper.getSentenceIndexs();

        setClickableSpan(sents,tractatetv,result);

    }


    private void setClickableSpan(List<List<int[]>> sents,TextView textview,String textString){

        textview.setMovementMethod(new MovementMethod());
        textview.setText(textString, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) textview.getText();

        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(textString);
        int start = iterator.first();

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {

            String possibleWord = textString.substring(start, end);
            //是字符数字，排除中文
            if (Character.isLetterOrDigit(possibleWord.charAt(0)) && !p.matcher(String.valueOf(possibleWord.charAt(0))).find()) {
                int[] ints = getSentenceIndex(sents,possibleWord,start,end);

                ClickableSpan clickSpan = new MyClickableSpan(possibleWord, ints[0], ints[1], english, chinese);

                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if(p.matcher(String.valueOf(possibleWord.charAt(0))).find()){
                ClickableSpan clickSpan = new MyChineseClickableSpan();

                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

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

    //一行英文一行中文
    private void setBysentence(){

        loading.setVisibility(View.VISIBLE);
        tractatetv.setText("");
        contenttv.setVisibility(View.VISIBLE);
        contenttv.post(new Runnable() {

            @Override
            public void run() {

                List<String> textViewLine = AndroidUtils.newInstance(mContext).getTextViewStringByLine(contenttv);
                contenttv.setVisibility(View.GONE);
                tractateHelper = new TractateHelper(english,chinese,textViewLine,contenttv.getWidth(),contenttv.getPaint());

                result = tractateHelper.getTractateString();

                if(result != null){
                    Log.d(TAG, result);
                    contenttv.setVisibility(View.GONE);


                    //获得句子下标
                    List<List<int[]>> sents = tractateHelper.getSentenceIndexs();

                    setClickableSpan(sents,tractatetv,result);

                }
                loading.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class MyClickableSpan extends ClickableSpan {

        String mWord;
        String englishSentence;
        String chineseSentence;
        private TextPaint textpaint;
        boolean isAddBackGroundColor = false;

        public MyClickableSpan(String mWord, int paragraph, int sentence, List<List<String>> english, List<List<String>> chinese) {
            this.mWord = mWord;
            englishSentence = english.get(paragraph).get(sentence);
            chineseSentence = chinese.get(paragraph).get(sentence);
        }

        @Override
        public void onClick(View widget) {

            if(clickableSpan != this){
                if(clickableSpan != null){
                    clickableSpan.cancelBackGroundColor();
                }
                clickableSpan = this;
            }
            addBackgroundColor(widget);

            Bundle bundle = new Bundle();
            bundle.putString(WordDetailDialog.WORDTAG,mWord);
            bundle.putString(WordDetailDialog.ENSENTENCE,englishSentence);
            bundle.putString(WordDetailDialog.CHSENTENCE,chineseSentence);
            wordDetailDialog.setArguments(bundle);
            wordDetailDialog.show(TractatePreviewActivity.this.getSupportFragmentManager(),"dialog");
            //显示单词
            /*Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT)
                    .show();*/
                /*//显示句子
                Toast.makeText(widget.getContext(), englishSentence.substring(0,3) + englishSentence.substring(englishSentence.length() - 2,englishSentence.length() - 1) + "\\n" + chineseSentence.substring(0,1) + chineseSentence.substring(chineseSentence.length() - 2,chineseSentence.length() - 1), Toast.LENGTH_SHORT)
                        .show();*/
            /*Toast.makeText(widget.getContext(), englishSentence.substring(0,10), Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG,englishSentence + "\\\n" + chineseSentence);*/
        }

        public void addBackgroundColor(View widget){
            isAddBackGroundColor = true;
            updateDrawState(textpaint);
            widget.invalidate();
        }

        public void cancelBackGroundColor(){
            isAddBackGroundColor = false;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            this.textpaint = ds;
            if(isAddBackGroundColor){
                textpaint.bgColor = Color.GRAY;
                textpaint.setARGB(255, 255, 255, 255);
            }
        }
    }


    private class MyChineseClickableSpan extends ClickableSpan{

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //ds.setTextSize(14);
            ds.setColor(getResources().getColor(R.color.text_color_grey1));
        }
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
                        if(clickableSpan != null){
                            clickableSpan.cancelBackGroundColor();
                        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.byline:
                loading.setVisibility(View.VISIBLE);
                setBysentence();

                break;
            case R.id.byparagraph:
                loading.setVisibility(View.VISIBLE);
                setByparagraph();
                loading.setVisibility(View.GONE);
                break;
            case R.id.byenglish:
                loading.setVisibility(View.VISIBLE);
                setJustEnglish();
                loading.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }
}
