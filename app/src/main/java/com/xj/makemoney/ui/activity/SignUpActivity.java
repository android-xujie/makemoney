package com.xj.makemoney.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xj.baselibrary.BaseActivity;
import com.xj.baselibrary.utils.L;
import com.xj.baselibrary.utils.ToastUtils;
import com.xj.baselibrary.utils.Validator;
import com.xj.makemoney.R;
import com.xj.makemoney.contants.Contants;
import com.xj.makemoney.model.UserInfo;

import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by yunwen on 2018/5/15.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private TextView btSignupSubmit;
    private EditText et_username;
    private EditText et_email;
    private EditText et_pwd;
    private EditText et_confirm_pwd;
    private String username;
    private String email;
    private String pwd;
    private String confirmpwd;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_signup;
    }

    @Override
    protected void prepareActivity() {
        initView();
    }

    private void initView() {
        btSignupSubmit = (TextView) findViewById(R.id.bt_signup_submit);
        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        btSignupSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(final View view) {
        int i = view.getId();
        if (i == R.id.bt_signup_submit) {
            if (judgeInput()) {
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(username);
                bmobUser.setPassword(pwd);
                bmobUser.setEmail(email);
                bmobUser.signUp(new SaveListener<UserInfo>() {
                    @Override
                    public void done(UserInfo userInfo, BmobException e) {
                        L.e(TAG,userInfo + "---" + e);
                        if(e==null){
                            ToastUtils.showToast("添加数据成功，返回objectId为："+userInfo);
                            Contants.ISUP_SUCCESS = true;
                            finish();
                            //获取view在屏幕中的位置以及宽高做为参数传到第二个activity
                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.putExtra("issignup", "true");
                            intent.putExtra("left", location[0]);
                            intent.putExtra("top", location[1]);
                            intent.putExtra("width", view.getWidth());
                            intent.putExtra("height", view.getHeight());
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }else{
                            ToastUtils.showToast("创建数据失败：" + e.getMessage());
                            Contants.ISUP_SUCCESS = false;
                        }
                    }
                });
            }
        }
    }


    /**判断输入框内容是否符合要求*/
    private boolean judgeInput(){
        boolean canLogin = true;
        username = et_username.getText().toString();
        email = et_email.getText().toString();
        pwd = et_pwd.getText().toString();
        confirmpwd = et_confirm_pwd.getText().toString();
        boolean isusernamespace = Pattern.compile("\\s+").matcher(username).find();
        boolean isemailspace = Pattern.compile("\\s+").matcher(email).find();
        boolean ispwdspace = Pattern.compile("\\s+").matcher(pwd).find();
        boolean isconfirmpwdspace = Pattern.compile("\\s+").matcher(confirmpwd).find();

        if (username.trim().isEmpty()){
            ToastUtils.showToast("UserName Can Not Be Empty");
            canLogin = false;
            return canLogin;
        }
        if (email.trim().isEmpty()){
            ToastUtils.showToast("Email Can Not Be Empty");
            canLogin = false;
            return canLogin;
        }
        if (pwd.trim().isEmpty()){
            ToastUtils.showToast("Password Can Not Be Empty");
            canLogin = false;
            return canLogin;
        }
        if (confirmpwd.trim().isEmpty()){
            ToastUtils.showToast("Comfirm Password Can Not Be Empty");
            canLogin = false;
            return canLogin;
        }
        if (isusernamespace) {
            ToastUtils.showToast("UserName Can Not include spaces");
            canLogin = false;
            return canLogin;
        }
        if (isemailspace) {
            ToastUtils.showToast("Email Can Not include spaces");
            canLogin = false;
            return canLogin;
        }
        if (ispwdspace) {
            ToastUtils.showToast("Password Can Not include spaces");
            canLogin = false;
            return canLogin;
        }
        if (isconfirmpwdspace) {
            ToastUtils.showToast("Comfirm Password Can Not include spaces");
            canLogin = false;
            return canLogin;
        }

        if (!pwd.equals(confirmpwd)) {
            ToastUtils.showToast("Comfirm Password and Password is not same");
            canLogin = false;
            return canLogin;
        }

        if (!Validator.isEmail(email)) {
            ToastUtils.showToast("Email format problem");
            canLogin = false;
            return canLogin;
        }
        return canLogin;
    }
}
