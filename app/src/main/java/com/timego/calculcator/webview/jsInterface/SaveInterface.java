package com.timego.calculcator.webview.jsInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import com.timego.calculcator.APPAplication;

import java.io.FileOutputStream;
import java.io.IOException;

public class SaveInterface {
    @JavascriptInterface
    public void write(String str, String url) {
        SharedPreferences sp = APPAplication.getAppContext().getSharedPreferences("htmls", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(url, str);
        edit.apply();
    }
    @JavascriptInterface
    public void write2(String str, String url) {
        FileOutputStream outStream = null;
        try {
            // /storage/emulated/0/Download
            String path = "/storage/emulated/0/Download/log.html";
//            ToastUtils.showCenterToast(path);
            outStream = new FileOutputStream(path);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
