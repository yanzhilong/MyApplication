package com.englishlearn.myapplication.activityforresult.multiple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.englishlearn.myapplication.R;

import java.util.ArrayList;

//多选实现
public class MultipleActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String STRINGARRAY = "stringarray";
    public static final String CHECKEDARRAY = "checkedarray";
    private static final String TAG = MultipleActivity.class.getSimpleName();
    private String[] values;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_act);

        if (getIntent().hasExtra(STRINGARRAY)) {
            values = (String[]) getIntent().getSerializableExtra(STRINGARRAY);
        }

        findViewById(R.id.allselect).setOnClickListener(this);
        findViewById(R.id.reselect).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list);
        ListAdapter listAdapter = new ArrayAdapter<String>(this,R.layout.multiple_itemlayout,values){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final MultipleView view;
                if(convertView == null) {
                    view = new MultipleView(MultipleActivity.this);
                } else {
                    view = (MultipleView)convertView;
                }
                view.setText(getItem(position));
                return view;
            }
        };

        listView.setAdapter(listAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_multiple);

        /*Fragment fragment = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.allselect:
                for(int i=0;i<values.length;i++){
                    listView.setItemChecked(i, true);
                }
                break;
            case R.id.reselect:
                for(int i=0;i<values.length;i++){
                    if(listView.isItemChecked(i)){
                        listView.setItemChecked(i, false);
                    }else{
                        listView.setItemChecked(i, true);
                    }
                }
                break;
            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED);
                break;
            case R.id.confirm:

                Intent intent = getIntent();
                SparseBooleanArray checkedArray = listView.getCheckedItemPositions();
                ArrayList<Integer> checkoutpositions = new ArrayList<>();

                for (int i = 0; i < values.length; i++) {
                    //checkoutpositions.add(i);
                    Log.d(TAG,"全部" + i);
                    if (checkedArray.get(i)){
                        Log.d(TAG,"选中" + i);
                        checkoutpositions.add(i);
                    }
                }
                intent.putIntegerArrayListExtra(CHECKEDARRAY,checkoutpositions);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }
    }
}

