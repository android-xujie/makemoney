package com.xj.makemoney;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.xj.baselibrary.BaseActivity;
import com.xj.baselibrary.utils.AppUtils;
import com.xj.baselibrary.utils.ToastUtils;
import com.xj.makemoney.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;


public class XApplication extends Application {

    private static XApplication app;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;

    public static XApplication getApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AppUtils.init(app);
        init();
        getScreenSize();
    }

    private void init() {
        //第一：默认初始化
        Bmob.initialize(this, "629ea4985b048853c45f90064109f757");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    private void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
//        DIMEN_RATE = dm.density / 1.0f
//        DIMEN_DPI = dm.densityDpi
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    //踢出处理
    public Handler authHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//			if(isActivityInForground()){
//			while(!DemoHelper.getInstance().isLoggedIn()){
//
//			}
            switch (msg.what) {
                case 0:
                    ToastUtils.showToast("账号在其他设备登录");
                    break;
                case 2:
                    ToastUtils.showToast("注销成功");
                    break;
                default:
                    ToastUtils.showToast("帐号登录过期,请重新登录");
                    break;
            }
//            MyApplication.userData = null;
//            PreferencesUtils.clearUserInfo();
            Intent intent = new Intent(XApplication.getApplication(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finishActivity();
            XApplication.getApplication().startActivity(intent);
        }
    };

    /***************
     * activity 管理开始
     *********************/
    private static List<Activity> list = new ArrayList<Activity>();

    public void addActivity(BaseActivity baseActivity) {
        list.add(baseActivity);
    }

    public void removeActivity(BaseActivity baseActivity) {
        list.remove(baseActivity);
    }

    public void finishActivity() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).finish();
        }
    }

    public void finishOtherActivity(Activity activity) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(activity)) {
                list.get(i).finish();
            }
        }
    }

    public boolean isActivityInForground() {
        for (int i = 0; i < list.size(); i++) {
            if (((BaseActivity) list.get(i)).isForGround) {
                return true;
            }
        }
        return false;
    }

    /***************
     * activity 管理结束
     *********************/
}
