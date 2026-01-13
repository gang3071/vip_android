package com.timego.calculcator.webview;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.timego.calculcator.R;
import com.timego.calculcator.webview.jsInterface.AndroidJsWidgetInterface;
import com.timego.calculcator.webview.jsInterface.OpenBrowserInterface;
import com.just.agentweb.AgentWeb;
import com.timego.calculcator.webview.jsInterface.SaveInterface;

public class Setting {

    private AgentWeb mAgentWeb;
    private WebView webView;
    private WebSettings webSettings;
    private Activity activity;

    public Setting(AgentWeb mAgentWeb, Activity activity) {
        this.mAgentWeb = mAgentWeb;
        this.webView = mAgentWeb.getWebCreator().getWebView();
        this.webSettings = this.webView.getSettings();
        this.activity = activity;
    }

    public void doSet() {
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        //webView  获取WebView .

//		webView.setOnLongClickListener();

        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportMultipleWindows(true);

//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //优先使用网络
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //当webview调用requestFocus时为webview设置节点
        webSettings.setNeedInitialFocus(true);
        //将图片调整到适合webview的大小
        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 javascript 来操作这些数据。）
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        webSettings.setSavePassword(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

//        webSettings.setAppCacheEnabled(true);

        //允许webview对文件的操作
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.addJavascriptInterface(new OpenBrowserInterface(activity),"openBrowser");
//        webView.addJavascriptInterface(new PlayAudioInterface(activity),"enterdsfyxs");
        webView.addJavascriptInterface(new SaveInterface(),"savehtml");
//        webView.addJavascriptInterface(new HistoryInterface(activity),"android_history");
        webView.addJavascriptInterface(new AndroidJsWidgetInterface(activity),"androidMethod");
        webSettings.setMediaPlaybackRequiresUserGesture(false);
//        webSettings.setSupportMultipleWindows(true);
//        webView.setBackgroundResource(R.mipmap.webview_bg);
        webView.setBackgroundColor(activity.getResources().getColor(R.color.black));
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        WebView.setWebContentsDebuggingEnabled(true);

        EditorInfo e = new EditorInfo();
        e.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        webView.onCreateInputConnection(e);
        // 下拉刷新
//        srl.setOnRefreshListener(this);
//        srl.setScrollBoundaryDecider(this);
        // srl.autoRefresh();
    }

}
