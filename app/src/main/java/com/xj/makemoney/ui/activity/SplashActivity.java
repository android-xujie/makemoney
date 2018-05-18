package com.xj.makemoney.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xj.makemoney.R;
import com.xj.makemoney.contants.Contants;
import com.xj.makemoney.guidepage.SplashPagerAdapter;

/**
 * Created by yunwen on 2018/5/15.
 */

public class SplashActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AppCompatButton buttonFinish;
    private ImageButton buttonPre;
    private ImageButton buttonNext;
    private ImageView[] indicators;

    private int bgColors[];

    private int currentPosition;

    private static final int MSG_DATA_INSERT_FINISH = 1;

    private Handler handler = new Handler(new HandlerCallback());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(Contants.KEY_FIRST_LAUNCH, true)) {

            setContentView(R.layout.activity_splash);

            new InitCompaniesDataTask().execute();

            initViews();

            initData();

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int colorUpdate = (Integer) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position : position + 1]);
                    viewPager.setBackgroundColor(colorUpdate);
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                    updateIndicators(position);
                    viewPager.setBackgroundColor(bgColors[position]);
                    buttonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                    buttonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                    buttonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            buttonFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putBoolean(Contants.KEY_FIRST_LAUNCH, false);
                    ed.apply();
                    navigateToMainActivity();
                    //enableShortcuts();
                }
            });

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition += 1;
                    viewPager.setCurrentItem(currentPosition, true);
                }
            });

            buttonPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition -= 1;
                    viewPager.setCurrentItem(currentPosition, true);
                }
            });

        } else {

            navigateToMainActivity();

            finish();
        }

    }

    private void initViews() {
        SplashPagerAdapter pagerAdapter = new SplashPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        buttonFinish = (AppCompatButton) findViewById(R.id.buttonFinish);
        buttonFinish.setText(R.string.splash_finish_button_description_wait);
        buttonFinish.setEnabled(false);
        buttonNext = (ImageButton) findViewById(R.id.imageButtonNext);
        buttonPre = (ImageButton) findViewById(R.id.imageButtonPre);
        indicators = new ImageView[] {
                (ImageView) findViewById(R.id.imageViewIndicator0),
                (ImageView) findViewById(R.id.imageViewIndicator1),
                (ImageView) findViewById(R.id.imageViewIndicator2)};
    }

    private void initData() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.cyan_500),
                ContextCompat.getColor(this, R.color.light_blue_500)};
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.splash_indicator_selected : R.drawable.splash_indicator_unselected
            );
        }
    }

    private class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_DATA_INSERT_FINISH:

                    buttonFinish.setText(R.string.splash_finish_button_description);
                    buttonFinish.setEnabled(true);
                    break;
            }
            return true;
        }
    }

    private class InitCompaniesDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
//            CompaniesRepository.getInstance(CompaniesLocalDataSource.getInstance())
//                    .initData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            handler.sendEmptyMessage(MSG_DATA_INSERT_FINISH);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

//    private void enableShortcuts() {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            ShortcutManager manager =  getSystemService(ShortcutManager.class);
//            ShortcutInfo addPkg = new ShortcutInfo.Builder(this, "shortcut_add_package")
//                    .setLongLabel(getString(R.string.shortcut_label_add_package))
//                    .setShortLabel(getString(R.string.shortcut_label_add_package))
//                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_add))
//                    .setIntent(
//                            new Intent(OnboardingActivity.this, AddPackageActivity.class)
//                                    .setAction(Intent.ACTION_VIEW)
//                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
//                    )
//                    .build();
//            ShortcutInfo scanCode = new ShortcutInfo.Builder(this, "shortcut_scan_code")
//                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_filter_center_focus))
//                    .setLongLabel(getString(R.string.shortcut_label_scan_code))
//                    .setShortLabel(getString(R.string.shortcut_label_scan_code))
//                    .setIntent(
//                            new Intent(OnboardingActivity.this, AddPackageActivity.class)
//                                    .setAction("io.github.marktony.espresso.mvp.addpackage.AddPackageActivity")
//                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
//                    )
//                    .build();
//            ShortcutInfo search = new ShortcutInfo.Builder(this, "shortcut_search")
//                    .setIcon(Icon.createWithResource(this, R.drawable.ic_shortcut_search))
//                    .setLongLabel(getString(R.string.shortcut_label_search))
//                    .setShortLabel(getString(R.string.shortcut_label_search))
//                    .setIntent(
//                            new Intent(OnboardingActivity.this, SearchActivity.class)
//                                    .setAction(Intent.ACTION_VIEW)
//                                    .addCategory(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
//                    )
//                    .build();
//            manager.setDynamicShortcuts(Arrays.asList(addPkg, scanCode, search));
//        }

//    }

    private void navigateToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
