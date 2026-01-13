package com.timego.calculcator.webview.jsInterface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class OpenBrowserInterface {
    private Context context;
    public OpenBrowserInterface(Context context) {
        this.context = context;
    }

    /**
     * 添加注解 @JavascriptInterface
     * javascript 要调用的方法
     */
    @JavascriptInterface
    public void show(String targetUrl){
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(this.context, targetUrl + " 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        context.startActivity(intent);
    }
}
