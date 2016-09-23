package com.englishlearn.myapplication.tractatetype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractateTypeFragment extends Fragment implements TractateTypeContract.View, View.OnClickListener {

    private static final String TAG = TractateTypeFragment.class.getSimpleName();

    private TractateTypeContract.Presenter mPresenter;
    private EditText tractatetype_name;
    private EditText tractatetype_id;
    private TextView tractatetypes;

    public static TractateTypeFragment newInstance() {
        return new TractateTypeFragment();
    }

    @Override
    public void setPresenter(TractateTypeContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.tractatetype_frag, container, false);

        tractatetype_name = (EditText) root.findViewById(R.id.tractatetype_name);
        tractatetype_id = (EditText) root.findViewById(R.id.tractatetype_id);
        tractatetypes = (TextView) root.findViewById(R.id.tractatetypes);
        Button tractatetype_add = (Button) root.findViewById(R.id.tractatetype_add);
        Button tractatetype_delete = (Button) root.findViewById(R.id.tractatetype_delete);
        Button tractatetype_update = (Button) root.findViewById(R.id.tractatetype_update);
        Button tractatetype_get = (Button) root.findViewById(R.id.tractatetype_get);
        Button tractatetype_getbyid = (Button) root.findViewById(R.id.tractatetype_getbyid);

        tractatetype_add.setOnClickListener(this);
        tractatetype_delete.setOnClickListener(this);
        tractatetype_update.setOnClickListener(this);
        tractatetype_get.setOnClickListener(this);
        tractatetype_getbyid.setOnClickListener(this);

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
        String tractatetype_namestr = tractatetype_name.getText().toString();
        String tractatetype_idstr = tractatetype_id.getText().toString();

        switch (v.getId()){
            case R.id.tractatetype_add:

                break;
            case R.id.tractatetype_delete:

                break;
            case R.id.tractatetype_update:

                break;
            case R.id.tractatetype_get:

                break;
            case R.id.tractatetype_getbyid:

                break;
        }
    }

    @Override
    public void addTractateTypeSuccess() {
        Toast.makeText(this.getContext(),R.string.addmessagesource_success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void addTractateTypeFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void addTractateTypeFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteTractateTypeSuccess() {
        Toast.makeText(this.getContext(),R.string.addtractatetype_success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteTractateTypeFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteTractateTypeFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updaTractateTypeSuccess() {
        Toast.makeText(this.getContext(),R.string.updatetractatetype_success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updaTractateTypeFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updaTractateTypeFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypesSuccess() {
        Toast.makeText(this.getContext(),R.string.addmessagesource_success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypesFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypesFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypeByIdSuccess() {
        Toast.makeText(this.getContext(),R.string.gettractatetype_success,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypeByIdFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void getTractateTypeByIdFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }
}
