package com.timego.game.webview.jsInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;

import com.timego.game.APPAplication;

public class AndroidJsWidgetInterface {
    private Activity activity;
    public AndroidJsWidgetInterface(Activity activity) {
        this.activity = activity;
    }

    /**
     * 添加注解 @JavascriptInterface
     * javascript 要调用的方法
     */
    @JavascriptInterface
    public String getOpenshareParams(){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString("openshareData", "").replaceAll("&", ",");
    }
    @JavascriptInterface
    public String getAppversion(){
        Context context = APPAplication.getAppContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode+"";
    }
}
