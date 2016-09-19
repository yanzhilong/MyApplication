package com.englishlearn.myapplication.registeruser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.User;


/**
 * Created by yanzl on 16-7-20.
 */
public class RegisterUserFragment extends Fragment implements RegisterUserContract.View, View.OnClickListener {

    private static final String TAG = RegisterUserFragment.class.getSimpleName();

    private EditText username;
    private EditText password;
    private Button register;

    private RegisterUserContract.Presenter mPresenter;
    public static RegisterUserFragment newInstance() {
        return new RegisterUserFragment();
    }

    @Override
    public void setPresenter(RegisterUserContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.registeruser_frag, container, false);

        username = (EditText) root.findViewById(R.id.username);
        password = (EditText) root.findViewById(R.id.password);
        register = (Button) root.findViewById(R.id.register);

        register.setOnClickListener(this);
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
    public void registerSuccess() {
        Toast.makeText(this.getContext(),R.string.registersuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                String name = username.getText().toString();
                String pwd = password.getText().toString();

                User user = new User();
                user.setUsername(name);
                user.setPassword(pwd);

                mPresenter.register(user);
                break;
            default:
                break;
        }
    }
}
