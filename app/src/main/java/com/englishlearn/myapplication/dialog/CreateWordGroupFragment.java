package com.englishlearn.myapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-10-10.
 */
public class CreateWordGroupFragment extends android.support.v4.app.DialogFragment {

    private CreateWordGroupListener createWordGroupListener;

    public void setCreateWordGroupListener(CreateWordGroupListener createWordGroupListener) {
        this.createWordGroupListener = createWordGroupListener;
    }



    public interface CreateWordGroupListener{

        void onClick(String name);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 从布局中加载视图
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View passwordErrorLayout = factory.inflate(R.layout.createwordgroup, null);
        final EditText editName = (EditText) passwordErrorLayout.findViewById(R.id.name);

        return new AlertDialog.Builder(getActivity())
                .setView(passwordErrorLayout)
                .setTitle("创新新的词单")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(createWordGroupListener != null){
                                    createWordGroupListener.onClick(editName.getText().toString().trim());
                                }
                            }
                        }
                )
                .setNegativeButton("取消",
                        null
                )
                .create();
    }
}
