package com.englishlearn.myapplication.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.englishlearn.myapplication.R;

import java.io.Serializable;

/**
 * Created by yanzl on 16-11-2.
 */
public class ItemSelectFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String ITEMS = "items";
    public static final String ITEMCLICKLISTENER = "ItemClickListener";
    public static final String FLAG = "flag";//用于一个界面区分不同的点击事件


    String[] tractatetype = null;
    ListView mylist;
    private int flag = 0;
    private onItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            onItemClickListener = (ItemSelectFragment.onItemClickListener) bundle.getSerializable(ITEMCLICKLISTENER);
            tractatetype = bundle.getStringArray(ITEMS);
            flag = bundle.getInt(FLAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.itemselect_dia, null, false);
        mylist = (ListView) view.findViewById(R.id.listview);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter< String > adapter = new ArrayAdapter< String >(getActivity(),
                android.R.layout.simple_list_item_1, tractatetype);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(flag,position);
        }
        dismiss();
    }

    public interface onItemClickListener extends Serializable{
        void onItemClick(int flag,int posion);
    }
}
