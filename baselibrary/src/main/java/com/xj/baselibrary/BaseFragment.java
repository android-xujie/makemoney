package com.xj.baselibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lin on 2017/7/16.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();

    protected abstract int bindLayoutID();
    protected abstract void prepareFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(bindLayoutID(), container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareFragment();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
