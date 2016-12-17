package com.englishlearn.myapplication.test.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.dialog.ItemsDialogFragment;

public class DialogActivity extends AppCompatActivity {

    private static final String TAG = DialogActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);


    }

    public void onClick(View v){

        switch (v.getId()){
            case R.id.itemdialog:
                showItemDiologFragment();
                break;

        }
    }

    private void showItemDiologFragment(){

        ItemsDialogFragment itemsDialogFragment = new ItemsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsDialogFragment.TITLE,"选择");
        bundle.putStringArray(ItemsDialogFragment.ITEMS,new String[]{"item1","item2","item3"});
        bundle.putSerializable(ItemsDialogFragment.ITEMCLICKLISTENER,new ItemsDialogFragment.onItemClickListener(){

            @Override
            public void onItemClick(int posion) {
                Log.d(TAG,"posion：" + posion);
            }
        });
        itemsDialogFragment.setArguments(bundle);
        itemsDialogFragment.show(getSupportFragmentManager(),"itemselect");
    }
}
