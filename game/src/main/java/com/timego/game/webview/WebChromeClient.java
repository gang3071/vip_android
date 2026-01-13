package com.timego.game.webview;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebView;

public class WebChromeClient extends com.just.agentweb.WebChromeClient {

    private static final String TAG = "WebChromeClient";
    private Activity activity;

    public WebChromeClient(Activity activity) {
        this.activity = activity;
    }

    public WebChromeClient() {
    }


    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("WebView-js-console", consoleMessage.sourceId()+":"+consoleMessage.lineNumber()+":"+consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        String url = view.getUrl();
        Log.d(TAG, newProgress +" "+ url);
        super.onProgressChanged(view, newProgress);
//        H5WebGameActivity mainActivity = ((H5WebGameActivity) activity);
//        if (newProgress == 100) {
//            mainActivity.hideLoading();
//        } else {
//            mainActivity.showLoading();
//        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        callback.onCustomViewHidden();
    }
}
