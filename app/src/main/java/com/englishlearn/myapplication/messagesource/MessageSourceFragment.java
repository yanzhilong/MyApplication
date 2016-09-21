package com.englishlearn.myapplication.messagesource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.englishlearn.myapplication.R;


/**
 * Created by yanzl on 16-7-20.
 */
public class MessageSourceFragment extends Fragment implements MessageSourceContract.View, View.OnClickListener {

    private static final String TAG = MessageSourceFragment.class.getSimpleName();

    private MessageSourceContract.Presenter mPresenter;
    private EditText messagesource_name;
    private EditText messagesource_id;
    private TextView messagesources;

    public static MessageSourceFragment newInstance() {
        return new MessageSourceFragment();
    }

    @Override
    public void setPresenter(MessageSourceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.messagesource_frag, container, false);

        messagesource_name = (EditText) root.findViewById(R.id.messagesource_name);
        messagesource_id = (EditText) root.findViewById(R.id.messagesource_id);
        messagesources = (TextView) root.findViewById(R.id.messagesources);
        Button messagesource_add = (Button) root.findViewById(R.id.messagesource_add);
        Button messagesource_delete = (Button) root.findViewById(R.id.messagesource_delete);
        Button messagesource_update = (Button) root.findViewById(R.id.messagesource_update);
        Button messagesource_get = (Button) root.findViewById(R.id.messagesource_get);
        Button messagesource_getbyid = (Button) root.findViewById(R.id.messagesource_getbyid);

        messagesource_add.setOnClickListener(this);
        messagesource_delete.setOnClickListener(this);
        messagesource_update.setOnClickListener(this);
        messagesource_get.setOnClickListener(this);
        messagesource_getbyid.setOnClickListener(this);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onClick(View v) {
        String messagesource_namestr = messagesource_name.getText().toString();
        String messagesource_idstr = messagesource_id.getText().toString();

        switch (v.getId()){
            case R.id.messagesource_add:

                break;
            case R.id.messagesource_delete:

                break;
            case R.id.messagesource_update:

                break;
            case R.id.messagesource_get:

                break;
            case R.id.messagesource_getbyid:

                break;
        }
    }

    @Override
    public void addMsSourceSuccess() {

    }

    @Override
    public void addMsSourceFail() {

    }

    @Override
    public void addMsSourceFail(String message) {

    }

    @Override
    public void deleteMsSourceSuccess() {

    }

    @Override
    public void deleteMsSourceFail() {

    }

    @Override
    public void deleteMsSourceFail(String message) {

    }

    @Override
    public void updaMsSourceSuccess() {

    }

    @Override
    public void updaMsSourceFail() {

    }

    @Override
    public void updaMsSourceFail(String message) {

    }

    @Override
    public void getMsSourcesSuccess() {

    }

    @Override
    public void getMsSourcesFail() {

    }

    @Override
    public void getMsSourcesFail(String message) {

    }

    @Override
    public void getMsSourceByIdSuccess() {

    }

    @Override
    public void getMsSourceByIdFail() {

    }

    @Override
    public void getMsSourceByIdFail(String message) {

    }
}
