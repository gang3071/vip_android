package com.smallbuer.jsbridge.core;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebViewClient;
import com.timego.calculcator.APPAplication;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/4/2 17:58
 * 用途
 * **********************
 */
public class X5BridgeWebView extends WebView implements IWebView {
    private String TAG = "BridgeWebView";
    private BridgeTiny bridgeTiny;
    private X5BridgeWebViewClient mClient;
    private X5BridgeWebviewChromeClient mChromeClient;
    private Map<String, BridgeHandler> mLocalMessageHandlers = new HashMap();

    public X5BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public X5BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    public X5BridgeWebView(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        this.clearCache(true);
        if (Build.VERSION.SDK_INT >= 19 && Bridge.INSTANCE.getDEBUG()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebSettings settings = this.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        // 设置允许访问文件数据
//        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setAppCacheMaxSize( 50 * 1024 * 1024 ); // 10MB
//        settings.setAppCacheMaxSize( 10 * 1024 * 1024 ); // 10MB
//        settings.setAppCachePath(APPAplication.getAppContext().getCacheDir().getAbsolutePath() );
        settings.setAllowFileAccess( true );
        settings.setAppCacheEnabled( true );
        settings.setJavaScriptEnabled( true );
        settings.setCacheMode( WebSettings.LOAD_DEFAULT );
        settings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.setWebContentsDebuggingEnabled(true);
        }

//        this.setLayerType();
        this.setDrawingCacheEnabled(true);
//        webSettings.setSslVersion(WebSettings.SslVersion.TLSv1_2); // 可以尝试设置为TLSv1_2，但这通常由系统自动管理

//        activityBinding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView.setHorizontalScrollbarOverlay(true);
//        this.setHorizontalScrollBarEnabled(true);
//        this.requestFocus();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        // 设置在WebView内部是否允许通过file url加载的 Js代码读取其他的本地文件
        // Android 4.1前默认允许，4.1后默认禁止
        settings.setAllowFileAccessFromFileURLs(true);
        // 设置WebView内部是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        // Android 4.1前默认允许，4.1后默认禁止
        settings.setAllowUniversalAccessFromFileURLs(true);



        this.bridgeTiny = new BridgeTiny(this);
        this.mClient = new X5BridgeWebViewClient(this, this.bridgeTiny);
        this.mChromeClient = new X5BridgeWebviewChromeClient(this, this.bridgeTiny);
        super.setWebViewClient(this.mClient);
        super.setWebChromeClient(this.mChromeClient);
    }

    public void setWebViewClient(WebViewClient client) {
        this.mClient.setWebViewClient(client);
    }

    public void destroy() {
        super.destroy();
        this.bridgeTiny.freeMemory();
    }

    public void addHandlerLocal(String handlerName, BridgeHandler bridgeHandler) {
        this.mLocalMessageHandlers.put(handlerName, bridgeHandler);
    }

    public Map<String, BridgeHandler> getLocalMessageHandlers() {
        return this.mLocalMessageHandlers;
    }

    public void evaluateJavascript(String var1, Object object) {
        if (Build.VERSION.SDK_INT >= 19) {
            super.evaluateJavascript(var1, (ValueCallback)object);
        }

    }

    public void callHandler(String handlerName, Object data, OnBridgeCallback responseCallback) {
        this.bridgeTiny.callHandler(handlerName, data, responseCallback);
    }
}
