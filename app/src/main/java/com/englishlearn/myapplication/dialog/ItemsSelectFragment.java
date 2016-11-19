package com.englishlearn.myapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-11-2.
 */
public class ItemsSelectFragment extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String ITEMS = "items";
    public static final String TITLE = "title";
    public static final String CREATENAME = "createname";
    public static final String NOTITLEANDCREATE = "notitleandcreate";

    public static final String POSITION = "position";//选择的列表
    public static final String CREATE = "create";//创建新的..
    public static final String FLAG = "flag";//用于一个界面区分不同的点击事件

    String[] items = null;
    ListView mylist;
    private int flag = 0;
    private String titlestr;
    private String createstr;
    private Button create;
    private boolean notitleandcreate;//不要标题和创建按钮
    private TextView title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            items = bundle.getStringArray(ITEMS);
            if(items == null){
                items = new String[0];
            }

            flag = bundle.getInt(FLAG);
            titlestr = bundle.getString(TITLE);
            createstr = bundle.getString(CREATENAME);
            notitleandcreate = bundle.getBoolean(NOTITLEANDCREATE);
        }
    }

    public void notifyDataSetChanged(String[] arrays){

        ArrayAdapter< String > adapter = new ArrayAdapter< String >(getActivity(),
                android.R.layout.simple_list_item_1, arrays);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.itemsselect_dia, null, false);
        mylist = (ListView) view.findViewById(R.id.listview);
        create = (Button) view.findViewById(R.id.create);
        create.setOnClickListener(this);
        title = (TextView) view.findViewById(R.id.title);

        if(notitleandcreate){
            title.setVisibility(View.GONE);
            create.setVisibility(View.GONE);
        }

        if(titlestr != null){
            title.setText(titlestr);
        }

        if(createstr != null){
            title.setText(titlestr);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notifyDataSetChanged(items);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        setOnActivityResult(position,false);
        dismiss();
    }

    private void setOnActivityResult(int position,boolean create){
        Intent intent = getActivity().getIntent();
        intent.putExtra(POSITION,position);
        intent.putExtra(CREATE,create);
        intent.putExtra(FLAG,flag);

        getTargetFragment().onActivityResult(getTargetRequestCode(),0,intent);
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.create){
            setOnActivityResult(-1,true);
        }
    }
}
