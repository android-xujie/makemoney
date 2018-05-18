package com.xj.makemoney.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xj.baselibrary.BaseActivity;
import com.xj.baselibrary.core.ActionBar;
import com.xj.baselibrary.utils.L;
import com.xj.baselibrary.utils.ToastUtils;
import com.xj.makemoney.R;
import com.xj.makemoney.contants.Contants;
import com.xj.makemoney.model.Person;
import com.xj.makemoney.model.UserInfo;
import com.xj.makemoney.view.JellyInterpolator;
import com.xj.makemoney.view.Point;
import com.xj.makemoney.view.PointEvaluator;

import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by yunwen on 2018/5/15.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private TextView btn_login;
    private View layout_progress;
    private View input_layout;
    private LinearLayout input_layout_name,input_layout_psw;
    private EditText et_username;
    private EditText et_userpwd;
    private String username;
    private String userpwd;
    private ActionBar actionBar;

    private int mWidth;
    private int mHeight;


    private FrameLayout reveal_layout;
    private RelativeLayout scale_layout;
    private RelativeLayout layout_sign_up;
    private LinearLayout action_bar;
    private TextView tv_sign_up;
    private TextView tv_success;
    private View line;
    private TextView right_btn2;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void prepareActivity() {

        initActionBar();
        initView();

    }

    private void initActionBar() {
        actionBar=ActionBar.init(this);
        actionBar.setRightButton2("Sign Up", this);
    }

    private void initView() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        layout_progress = findViewById(R.id.layout_progress);
        input_layout = findViewById(R.id.input_layout);
        input_layout_name = (LinearLayout) findViewById(R.id.input_layout_name);
        input_layout_psw = (LinearLayout) findViewById(R.id.input_layout_psw);
        et_username = (EditText) findViewById(R.id.et_username);
        et_userpwd = (EditText) findViewById(R.id.et_userpwd);

        reveal_layout = (FrameLayout) findViewById(R.id.reveal_layout);
        scale_layout = (RelativeLayout) findViewById(R.id.scale_layout);
        layout_sign_up = (RelativeLayout) findViewById(R.id.layout_sign_up);
        action_bar = (LinearLayout) findViewById(R.id.action_bar);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        tv_success = (TextView) findViewById(R.id.tv_success);
        line = findViewById(R.id.line_view);
        right_btn2 = (TextView) findViewById(R.id.right_btn2);
        btn_login.setOnClickListener(this);

        String issignup = getIntent().getStringExtra("issignup");
        if (issignup != null && "true".equals(issignup)) {
            tv_sign_up.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    scale_layout.setVisibility(View.INVISIBLE);
                    action_bar.setVisibility(View.INVISIBLE);
                    layout_sign_up.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    startRevel();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            mWidth = btn_login.getMeasuredWidth();
            mHeight = btn_login.getMeasuredHeight();

            if (judgeInput()) {
                input_layout_name.setVisibility(View.INVISIBLE);
                input_layout_psw.setVisibility(View.INVISIBLE);
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(username);
                bmobUser.setPassword(userpwd);
                bmobUser.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            L.e(TAG + "--" + bmobUser.getEmail());
                            Contants.ISUP_SUCCESS = true;
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }else{
                            ToastUtils.showToast("创建数据失败：" + e.getMessage());
                            Contants.ISUP_SUCCESS = false;
                        }
                    }
                });
                inputAnimator(input_layout, mWidth, mHeight);
            }
        }

        if (id == R.id.right_btn2) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

        }
    }

    /**点击登录按钮出现的动画*/
    private void inputAnimator(final View view, float w, float h) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(input_layout, "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                layout_progress.setVisibility(View.VISIBLE);
                progressAnimator(layout_progress);
                input_layout.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        recovery();
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
        final ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        //animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if ( Contants.ISUP_SUCCESS) {
                    animator3.cancel();
                }
            }
        });



    }

    /**
     * 恢复初始状态
     */
    private void recovery() {
        layout_progress.setVisibility(View.GONE);
        input_layout.setVisibility(View.VISIBLE);
        input_layout_name.setVisibility(View.VISIBLE);
        input_layout_psw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) input_layout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        input_layout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(input_layout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

    /**判断输入框内容是否符合要求*/
    private boolean judgeInput(){
        boolean canLogin = true;
        username = et_username.getText().toString();
        userpwd = et_userpwd.getText().toString();
        boolean isuserspace = Pattern.compile("\\s+").matcher(username).find();
        boolean ispwdspace = Pattern.compile("\\s+").matcher(userpwd).find();
        L.e(TAG , "---" + username + "---" + userpwd + "---");

        if (username.trim().isEmpty()){
            ToastUtils.showToast("Email Can Not Be Empty");
            canLogin = false;
        }
        if (userpwd.trim().isEmpty()){
            ToastUtils.showToast("Password Can Not Be Empty");
            canLogin = false;
        }
        if (isuserspace) {
            ToastUtils.showToast("Email Can Not include spaces");
            canLogin = false;
        }
        if (ispwdspace) {
            ToastUtils.showToast("Password Can Not include spaces");
            canLogin = false;
        }
        return canLogin;
    }

    int measuredWidth;

    private void startRevel() {

        //得到successtextview的宽
        measuredWidth = tv_success.getMeasuredWidth();
        //拿到上一个activity中textview的属性
        Intent intent = getIntent();
        int left = intent.getIntExtra("left", 0);
        int top = intent.getIntExtra("top", 0);
        int width = intent.getIntExtra("width", 0);
        int height = intent.getIntExtra("height", 0);

        //获取到当前控件的属性
        int[] location = new int[2];
        layout_sign_up.getLocationOnScreen(location);
        int curX = location[0];
        int curY = location[1];
        //计算出差值
        int transX = left - curX;
        int transY = top - curY;
        //把当前的控件先移动到上一个activity中textview所处的位置
        layout_sign_up.setX(layout_sign_up.getX() + transX);
        layout_sign_up.setY(layout_sign_up.getY() + transY);

        int cx = left + width / 2;
        int cy = top - height / 2;
        float radius = (float) Math.hypot(reveal_layout.getWidth(), reveal_layout.getHeight());
        Animator animator;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        animator = ViewAnimationUtils.createCircularReveal(reveal_layout, cx, cy, 0, radius);
        animator.setDuration(700);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                reveal_layout.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
//        } else {


//        }

        Point start = new Point(layout_sign_up.getX(), layout_sign_up.getY());
        Point end = new Point(curX, curY - getStatusBarHeight(this));
        ValueAnimator animator1 = ValueAnimator.ofObject(new PointEvaluator(), start, end);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Point curPoint = (Point) animation.getAnimatedValue();

                layout_sign_up.setX(curPoint.getX());
                layout_sign_up.setY(curPoint.getY());
            }
        });
        animator1.setDuration(700);
        animator1.start();

        final ObjectAnimator lineAnim = ObjectAnimator.ofFloat(line, "ScaleX", 0f, 0.6f, 0.9f, 1f);
        lineAnim.setDuration(1500);
        lineAnim.setInterpolator(new DecelerateInterpolator());
        line.setPivotX(0);

        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                line.setVisibility(View.VISIBLE);
                lineAnim.start();
            }
        });

        lineAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                loopAnim();
            }
        });
    }

    int index = 0;

    private void loopAnim() {

        if (index % 2 == 0) {

            successEnter();
        } else {

            singupEnter();
        }
        index++;
    }

    //success进入
    private void successEnter() {

        if (tv_success.getVisibility() != View.VISIBLE) {
            tv_success.setVisibility(View.VISIBLE);
        }

        //得到success的宽度,然后设置起始位置以及终点位置
        ObjectAnimator sucessTran = ObjectAnimator.ofFloat(tv_success, "TranslationX", -measuredWidth * 1.2f, -measuredWidth * 0.2f, 0f);
        //透明度渐变从0-1
        ObjectAnimator sucessAlpha = ObjectAnimator.ofFloat(tv_success, "Alpha", 0.1f, 0.8f, 1f);

        //此处同理
        ObjectAnimator signupTran = ObjectAnimator.ofFloat(tv_sign_up, "TranslationX", 0, measuredWidth * 0.7f, measuredWidth * 1.5f);
        signupTran.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator signupAplha = ObjectAnimator.ofFloat(tv_sign_up, "Alpha", 1.0f, 1.0f, 0.4f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(sucessTran, sucessAlpha, signupTran, signupAplha);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessageDelayed(0, 800);
            }
        });
    }

    private void singupEnter() {

        ObjectAnimator signupTran = ObjectAnimator.ofFloat(tv_sign_up, "TranslationX", -measuredWidth * 1.2f, -measuredWidth * 0.2f, 0f);
        ObjectAnimator signupAplha = ObjectAnimator.ofFloat(tv_sign_up, "Alpha", 0.1f, 0.8f, 1f);

        ObjectAnimator successTran = ObjectAnimator.ofFloat(tv_success, "TranslationX", 0, measuredWidth * 0.7f, measuredWidth * 1.5f);
        successTran.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator successAlpha = ObjectAnimator.ofFloat(tv_success, "Alpha", 1.0f, 1.0f, 0.4f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(signupTran, signupAplha, successTran, successAlpha);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (index >= 3) {
                enterHome();
            } else {
                loopAnim();
            }
        }
    };

    private void enterHome() {

//        im.setVisibility(View.VISIBLE);
        action_bar.setVisibility(View.VISIBLE);
        scale_layout.setVisibility(View.VISIBLE);
        reveal_layout.setVisibility(View.GONE);
        layout_sign_up.setVisibility(View.GONE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(scale_layout.getHeight(), 0);
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                scale_layout.getLayoutParams().height = (int) animation.getAnimatedValue();
//                scale_layout.requestLayout();
//            }
//        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator,
                ObjectAnimator.ofFloat(right_btn2, "Alpha", 0.1f, 1.0f));
        animatorSet.setDuration(1200);
        animatorSet.start();
    }

    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
