package com.xj.baselibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yunwen on 2018/5/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public boolean isForGround = false;

    protected abstract int bindLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(bindLayoutID());

        prepareActivity();
    }

    protected abstract void prepareActivity();

    private void setFullScreen() {

    }
}
