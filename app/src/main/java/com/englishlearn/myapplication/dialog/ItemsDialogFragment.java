package com.englishlearn.myapplication.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.Serializable;

/**
 * Created by yanzl on 16-12-17.
 */
public class ItemsDialogFragment extends DialogFragment {

    public static final String ITEMS = "items";
    public static final String TITLE = "title";
    public static final String ITEMCLICKLISTENER = "ItemClickListener";

    private String title;
    private String[] items = null;

    private onItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString(TITLE);
            onItemClickListener = (onItemClickListener) bundle.getSerializable(ITEMCLICKLISTENER);
            items = bundle.getStringArray(ITEMS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!title.isEmpty()) {
            builder.setTitle(title);
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(which);
                }
            }
        });
        return builder.create();
    }

    public interface onItemClickListener extends Serializable {
        void onItemClick(int posion);
    }
}
