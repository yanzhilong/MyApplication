package com.englishlearn.myapplication.registeruser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.User;


/**
 * Created by yanzl on 16-7-20.
 */
public class RegisterUserFragment extends Fragment implements RegisterUserContract.View, View.OnClickListener {

    private static final String TAG = RegisterUserFragment.class.getSimpleName();

    private TextView user_objectid;
    private TextView user_name;
    private TextView user_mobile;
    private TextView user_email;
    private TextView sessiontoken;

    private EditText username;
    private EditText email;
    private EditText mobile;
    private EditText smscode;
    private EditText password;
    private EditText oldpassword;
    private EditText newpassword;
    private Button registerbyname;
    private Button registerbymail;
    private Button requestsmscode;
    private Button registerbymobile;
    private Button emailverify;
    private Button smscodeverify;


    private Button resetpwdbyemail;
    private Button resetpwdbysmscode;
    private Button resetpwdbyoldpwd;

    private Button usernamelogin;
    private Button emaillogin;
    private Button mobilelogin;
    private Button logout;

    private Button updateuser;





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

        user_objectid = (TextView) root.findViewById(R.id.user_objectid);
        user_name = (TextView) root.findViewById(R.id.user_name);
        user_mobile = (TextView) root.findViewById(R.id.user_mobile);
        user_email = (TextView) root.findViewById(R.id.user_email);
        sessiontoken = (TextView) root.findViewById(R.id.sessiontoken);

        username = (EditText) root.findViewById(R.id.username);
        email = (EditText) root.findViewById(R.id.email);
        mobile = (EditText) root.findViewById(R.id.mobile);
        smscode = (EditText) root.findViewById(R.id.smscode);
        password = (EditText) root.findViewById(R.id.password);
        oldpassword = (EditText) root.findViewById(R.id.oldpassword);
        newpassword = (EditText) root.findViewById(R.id.newpassword);

        registerbyname = (Button) root.findViewById(R.id.registerbyname);
        registerbymail = (Button) root.findViewById(R.id.registerbymail);
        requestsmscode = (Button) root.findViewById(R.id.requestsmscode);
        registerbymobile = (Button) root.findViewById(R.id.registerbymobile);
        emailverify = (Button) root.findViewById(R.id.emailverify);
        smscodeverify = (Button) root.findViewById(R.id.smscodeverify);

        resetpwdbyemail = (Button) root.findViewById(R.id.resetpwdbyemail);
        resetpwdbysmscode = (Button) root.findViewById(R.id.resetpwdbysmscode);
        resetpwdbyoldpwd = (Button) root.findViewById(R.id.resetpwdbyoldpwd);

        usernamelogin = (Button) root.findViewById(R.id.usernamelogin);
        emaillogin = (Button) root.findViewById(R.id.emaillogin);
        mobilelogin = (Button) root.findViewById(R.id.mobilelogin);
        logout = (Button) root.findViewById(R.id.logout);
        updateuser = (Button) root.findViewById(R.id.updateuser);

        registerbyname.setOnClickListener(this);
        registerbymail.setOnClickListener(this);
        requestsmscode.setOnClickListener(this);
        registerbymobile.setOnClickListener(this);
        emailverify.setOnClickListener(this);
        smscodeverify.setOnClickListener(this);

        resetpwdbyemail.setOnClickListener(this);
        resetpwdbysmscode.setOnClickListener(this);
        resetpwdbyoldpwd.setOnClickListener(this);

        usernamelogin.setOnClickListener(this);
        emaillogin.setOnClickListener(this);
        mobilelogin.setOnClickListener(this);
        logout.setOnClickListener(this);
        updateuser.setOnClickListener(this);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.checkoutUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void registerSuccess() {
        Toast.makeText(this.getContext(),R.string.registersuccess,Toast.LENGTH_LONG).show();
        mPresenter.login();
    }

    @Override
    public void registerAndLoginSuccess() {
        Toast.makeText(this.getContext(),R.string.registerandloginsuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByMailSuccess() {
        Toast.makeText(this.getContext(),R.string.alreadysendmail,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByMailFail() {
        Toast.makeText(this.getContext(),R.string.sendmailfail,Toast.LENGTH_LONG).show();
    }

    @Override
    public void requestSmsCodeSuccess() {
        Toast.makeText(this.getContext(),R.string.smscodeverifysuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void requestSmsCodeFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void requestSmsCodeFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void emailVerifySuccess() {
        Toast.makeText(this.getContext(),R.string.alreadysendverifymail,Toast.LENGTH_LONG).show();
    }

    @Override
    public void emailVerifyFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void emailVerifyFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void smsCodeVerifySuccess() {
        Toast.makeText(this.getContext(),R.string.smscodeverifysuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void smsCodeVerifyFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void smsCodeVerifyFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByMobileSuccess() {
        Toast.makeText(this.getContext(),R.string.resetpasswordsuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByMobileFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByMobileFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByOldPwdSuccess() {
        Toast.makeText(this.getContext(),R.string.resetpasswordsuccess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByOldPwdFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetPasswordByOldPwdFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginFail() {
        Toast.makeText(this.getContext(),R.string.networkerror,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginFail(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(User user) {
        Toast.makeText(this.getContext(),R.string.loginsuccess,Toast.LENGTH_LONG).show();
        showUser(user);

    }

    @Override
    public void updateSuccess(User user) {
        Toast.makeText(this.getContext(),R.string.updatesuccess,Toast.LENGTH_LONG).show();
        showUser(user);
    }

    @Override
    public void showUser(User user) {
        Log.d(TAG,user.toString());
        if(user.getUsername() != null && !user.getUsername().equals("")){
            user_name.setText(user.getUsername());
        }else if(user.getEmail() != null && !user.getEmail().equals("")) {
            user_name.setText(user.getEmail());
        }else if(user.getMobilePhoneNumber() != null && !user.getMobilePhoneNumber().equals("")) {
            user_name.setText(user.getMobilePhoneNumber());
        }

        user_objectid.setText(user.getObjectId() != null ? user.getObjectId():"");
        user_email.setText(user.getEmail() != null ? user.getEmail():"");
        user_mobile.setText(user.getMobilePhoneNumber() != null ? user.getMobilePhoneNumber():"");

        if(user.getSessionToken() != null){
            sessiontoken.setText(user.getSessionToken());
        }
    }

    @Override
    public void logout() {
        user_name.setText("");
        sessiontoken.setText("");
    }

    @Override
    public void onClick(View v) {

        String name = username.getText().toString();
        String pwd = password.getText().toString();
        String mail = email.getText().toString();
        String phone = mobile.getText().toString();
        String code = smscode.getText().toString();
        String oldpwd = oldpassword.getText().toString();
        String newpwd = newpassword.getText().toString();


        switch (v.getId()){
            case R.id.registerbyname:

                User user = new User();
                user.setUsername(name);
                user.setPassword(pwd);

                mPresenter.register(user);
                break;
            case R.id.registerbymail:

                User usermail = new User();
                usermail.setUsername(name);
                usermail.setEmail(mail);
                usermail.setPassword(pwd);

                mPresenter.register(usermail);
                break;
            case R.id.requestsmscode:
                mPresenter.requestSmsCode(phone);
                break;
            case R.id.registerbymobile:

                mPresenter.register(phone,code);
                break;
            case R.id.emailverify:
                mPresenter.emailVerify(mail);
                break;
            case R.id.smscodeverify:
                mPresenter.smsCodeVerify(code,phone);
                break;
            case R.id.resetpwdbyemail:

                mPresenter.resetPassword(mail);
                break;
            case R.id.resetpwdbysmscode:
                mPresenter.resetPassword(code,newpwd);
                break;
            case R.id.resetpwdbyoldpwd:
                mPresenter.resetPassword(name,oldpwd,newpwd);
                break;
            case R.id.usernamelogin:
                mPresenter.loginByName(name,pwd);
                break;
            case R.id.emaillogin:
                mPresenter.loginByEmail(mail,pwd);
                break;
            case R.id.mobilelogin:
                mPresenter.loginByMobile(phone,pwd);
                break;
            case R.id.logout:
                mPresenter.logout();
                break;
            case R.id.updateuser:
                User userupdate = new User();
                userupdate.setUsername(name);
                //userupdate.setMobilePhoneNumber(phone);
                userupdate.setEmail(mail);
                User tmp = mPresenter.getLoginUser();
                if(tmp != null){
                    userupdate.setObjectId(tmp.getObjectId());
                    userupdate.setSessionToken(tmp.getSessionToken());
                    mPresenter.updateuser(userupdate);
                }
                break;
            default:
                break;
        }
    }
}
