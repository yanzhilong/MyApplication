package com.englishlearn.myapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-10-10.
 */
public class UpdateWordGroupFragment extends android.support.v4.app.DialogFragment {

    public static String TITLE = "title";//次标题
    private String title;
    private UpdateWordGroupListener updateWordGroupListener;

    private String oldName;

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public void setUpdateWordGroupListener(UpdateWordGroupListener updateWordGroupListener) {
        this.updateWordGroupListener = updateWordGroupListener;
    }

    public interface UpdateWordGroupListener{

        void onUpdate(String name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(TITLE)){
            title = bundle.getString(TITLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 从布局中加载视图
        LayoutInflater factory = LayoutInflater.from(this.getActivity());
        final View passwordErrorLayout = factory.inflate(R.layout.createwordgroup, null);
        final EditText editName = (EditText) passwordErrorLayout.findViewById(R.id.name);
        if(oldName != null){
            editName.setText(oldName);
            editName.setSelection(oldName.length());//将光标移至文字末尾
        }
        return new AlertDialog.Builder(getActivity())
                .setView(passwordErrorLayout)
                .setTitle(title != null ? title : "修改名称")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(updateWordGroupListener != null){
                                    updateWordGroupListener.onUpdate(editName.getText().toString());
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
