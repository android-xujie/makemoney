package com.xj.loginlibrary.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.xj.baselibrary.BaseActivity;
import com.xj.loginlibrary.R;

/**
 * Created by yunwen on 2018/5/15.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private TextView btSignupSubmit;

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

        btSignupSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_signup_submit) {
            //获取view在屏幕中的位置以及宽高做为参数传到第二个activity
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("issignup", "true");
            intent.putExtra("left", location[0]);
            intent.putExtra("top", location[1]);
            intent.putExtra("width", view.getWidth());
            intent.putExtra("height", view.getHeight());
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}
