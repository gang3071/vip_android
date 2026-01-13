package com.timego.calculcator.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.timego.calculcator.H5WebGameActivity;
import com.timego.calculcator.LogUtils;

public class WebViewClient extends com.just.agentweb.WebViewClient{

    private final static String TAG = "WebViewClient2";

    private String targetUrl;
    private H5WebGameActivity activity;
    private RelativeLayout rlCtl;

    public WebViewClient(){
        super();
    }
    public WebViewClient(String targetUrl, H5WebGameActivity activity, RelativeLayout rlCtl) {
        this.targetUrl = targetUrl;
        this.activity = activity;
        this.rlCtl = rlCtl;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        String cookies= AgentWebConfig.getCookiesByUrl(targetUrl);
//        Log.i(TAG, cookies);
        /*if (url.startsWith(Constants.DOMAIN)) {
            rlCtl.setVisibility(View.INVISIBLE);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            rlCtl.setVisibility(View.VISIBLE);
        }*/
        super.onPageStarted(view, url, favicon);

        Log.i(TAG, "onPageStarted===>"+url);
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // 防止比基尼视讯横屏
//        view.loadUrl("javascript:window.addEventListener(\"orientationchange\", function(event) {alert('祝你旗开得胜')}, false);");
        activity.hideFloat();
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        LogUtils.i("onRenderProcessGone22:" + detail.didCrash() + ";;" + detail.rendererPriorityAtExit());

//        view.reload();
        return false;
    }
}