package com.timego.game;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

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

    public static Context getAppContext() {
        return AppContext;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        AppContext = getApplicationContext();
        CrashReport.initCrashReport(getApplicationContext(), "af0e3d7852", true);

    }




    public static void saveString(Context context,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context,String key, String defValue) {
        if (context == null) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

}
