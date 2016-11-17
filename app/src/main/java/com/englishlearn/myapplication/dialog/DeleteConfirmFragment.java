package com.englishlearn.myapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Created by yanzl on 16-10-10.
 */
public class DeleteConfirmFragment extends DialogFragment {

    private DeleteConfirmListener deleteConfirmListener;
    public static String TITLE = "title";//次标题
    private String title;

    public void setDeleteConfirmListener(DeleteConfirmListener deleteConfirmListener) {
        this.deleteConfirmListener = deleteConfirmListener;
    }

    public interface DeleteConfirmListener{

        void onDelete();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(TITLE)){
            title = bundle.getString(TITLE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title != null ? title : "确定删除吗?")
                .setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(deleteConfirmListener != null){
                                    deleteConfirmListener.onDelete();
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
