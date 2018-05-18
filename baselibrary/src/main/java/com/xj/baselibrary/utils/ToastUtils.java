package com.xj.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.baselibrary.R;


/**
 * Created by lin on 2017/7/17.
 */

public class ToastUtils {

    private static TextView message;
    private static Toast t;
    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            show((String) msg.obj);
        }
    };

    public static void showToast(String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Message msg = Message.obtain();
            msg.obj = text;
            handler.sendMessage(msg);
            return;
        }
        show(text);
    }

    private static void show(String text) {
        if (t == null) {
            View toastRoot = LayoutInflater.from(AppUtils.getAppContext()).inflate(R.layout.toast, null);
            message = (TextView) toastRoot.findViewById(R.id.message);
            message.setText(text);
            t = new Toast(AppUtils.getAppContext());
            t.setDuration(Toast.LENGTH_SHORT);
            t.setView(toastRoot);
        } else {
            message.setText(text);
        }
        t.show();
    }
}
