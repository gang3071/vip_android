package com.timego.calculcator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import top.maybesix.xhlibrary.serialport.SerialPortHelper;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/4/2 17:05
 * 用途
 * **********************
 */
public class APPAplication extends Application {
    public static Context AppContext;


    public SerialPortHelper serialPort; //数钞机

    public static Context getAppContext() {
        return AppContext;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        AppContext = getApplicationContext();
        initSerialPort();
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        CrashReport.initCrashReport(getApplicationContext(), "af0e3d7852", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WebView.setDataDirectorySuffix("process_suffix");
        }
    }

    public SerialPortHelper getSerialPortHelper() {
        return serialPort;
    }

    private void initSerialPort() {
        serialPort = new SerialPortHelper("/dev/ttyS1", 9600);

        serialPort.open();
//        sendDataLianJie();
//        myCountDownTimer = new MyCountDownTimer(1000 * 60 * 60 * 24 * 365, 1000 * 10);
//        myCountDownTimer.start();

    }


    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        if (context == null) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

}
