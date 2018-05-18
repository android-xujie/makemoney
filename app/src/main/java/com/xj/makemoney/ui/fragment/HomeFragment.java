package com.xj.makemoney.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.xj.baselibrary.BaseFragment;
import com.xj.baselibrary.core.ActionBar;
import com.xj.makemoney.R;

/**
 * Created by yunwen on 2018/5/16.
 */

public class HomeFragment extends BaseFragment {

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void prepareFragment() {
        setActionBar();
    }

    private void setActionBar() {
        View action_bar = getActivity().findViewById(R.id.action_bar);
        ActionBar actionBar = ActionBar.init(action_bar);
        actionBar.setTitle("HOME");
        actionBar.setBackground(R.color.gray);
    }
}
