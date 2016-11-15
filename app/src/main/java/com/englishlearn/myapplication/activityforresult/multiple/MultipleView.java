package com.englishlearn.myapplication.activityforresult.multiple;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.englishlearn.myapplication.R;

/**
 * Created by yanzl on 16-11-15.
 */
public class MultipleView extends FrameLayout implements Checkable {

    private CheckBox checkBox;//多选框
    private TextView itemName;//名称


    public MultipleView(Context context) {
        super(context);
        View.inflate(context, R.layout.multiple_itemlayout, this);
        itemName = (TextView) findViewById(R.id.itemname);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
    }

    public void setText(String text) {
        itemName.setText(text);
    }

    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void toggle() {
        checkBox.toggle();
    }
}
