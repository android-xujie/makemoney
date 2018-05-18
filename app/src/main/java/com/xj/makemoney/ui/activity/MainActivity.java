package com.xj.makemoney.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xj.baselibrary.BaseActivity;
import com.xj.baselibrary.utils.L;
import com.xj.makemoney.R;
import com.xj.makemoney.ui.fragment.HomeFragment;
import com.xj.makemoney.view.TabLayout;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectListener {

    private static final String TAG = "MainActivity";
    private String[] mTitles = {"Home", "Explore","Task", "More"};
    private long exitTime = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private int[] mIconUnselectedIds = {
            R.mipmap.home_unselect, R.mipmap.home_unselect, R.mipmap.home_unselect, R.mipmap.home_unselect};
    private int[] mIconSelectedIds = {
            R.mipmap.home_select, R.mipmap.home_select, R.mipmap.home_select, R.mipmap.home_select};
    private TabLayout tabLayout;
    private FrameLayout container;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void prepareActivity() {
        container = (FrameLayout) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tab_menu);
        initTabMenu();
        initNavMenu();
    }

    private void initNavMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);//获取头布局
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
                if (item.getItemId() == R.id.favorite ) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                L.e(TAG + "---" + item.getItemId() + "---" + R.id.favorite);
                Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }

    private void initTabMenu(){
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        mFragments.add(new HomeFragment());
        for (int i = 0; i < mTitles.length; i++)
            tabLayout.addTab(mTitles[i], mIconSelectedIds[i], mIconUnselectedIds[i], i);
        tabLayout.setTabData(getSupportFragmentManager(), R.id.container, mFragments);
        tabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }


}
