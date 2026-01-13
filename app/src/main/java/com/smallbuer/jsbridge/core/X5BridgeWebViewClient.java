package com.smallbuer.jsbridge.core;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/4/2 17:59
 * 用途
 * **********************
 */
public class X5BridgeWebViewClient extends WebViewClient {
    private WebViewClient mClient;
    private BridgeTiny bridgeTiny;
    private X5BridgeWebView bridgeWebView;

    public X5BridgeWebViewClient(X5BridgeWebView bridgeWebView, BridgeTiny bridgeTiny) {
        this.bridgeTiny = bridgeTiny;
        this.bridgeWebView = bridgeWebView;
    }

    public void setWebViewClient(WebViewClient client) {
        this.mClient = client;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return this.mClient != null ? this.mClient.shouldOverrideUrlLoading(view, url) : super.shouldOverrideUrlLoading(view, url);
    }

    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.loadUrl(request.getUrl().getAuthority());
        }

        return Build.VERSION.SDK_INT >= 24 && this.mClient != null ? this.mClient.shouldOverrideUrlLoading(view, request) : super.shouldOverrideUrlLoading(view, request);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (this.mClient != null) {
            this.mClient.onPageStarted(view, url, favicon);
        } else {
            super.onPageStarted(view, url, favicon);
        }

    }

    public void onPageFinished(WebView view, String url) {
        if (this.mClient != null) {
            this.mClient.onPageFinished(view, url);
        } else {
            super.onPageFinished(view, url);
        }

        this.bridgeTiny.webViewLoadJs(this.bridgeWebView);
    }

    public void onLoadResource(WebView view, String url) {
        if (this.mClient != null) {
            this.mClient.onLoadResource(view, url);
        } else {
            super.onLoadResource(view, url);
        }

    }

    public void onPageCommitVisible(WebView view, String url) {
        if (Build.VERSION.SDK_INT >= 23 && this.mClient != null) {
            this.mClient.onPageCommitVisible(view, url);
        } else {
            super.onPageCommitVisible(view, url);
        }

    }

    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return this.mClient != null ? this.mClient.shouldInterceptRequest(view, url) : super.shouldInterceptRequest(view, url);
    }

    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return Build.VERSION.SDK_INT >= 21 && this.mClient != null ? this.mClient.shouldInterceptRequest(view, request) : super.shouldInterceptRequest(view, request);
    }

    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (this.mClient != null) {
            this.mClient.onTooManyRedirects(view, cancelMsg, continueMsg);
        } else {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (this.mClient != null) {
            this.mClient.onReceivedError(view, errorCode, description, failingUrl);
        } else {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (Build.VERSION.SDK_INT >= 23 && this.mClient != null) {
            this.mClient.onReceivedError(view, request, error);
        } else {
            super.onReceivedError(view, request, error);
        }

    }

    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (Build.VERSION.SDK_INT >= 23 && this.mClient != null) {
            this.mClient.onReceivedHttpError(view, request, errorResponse);
        } else {
            super.onReceivedHttpError(view, request, errorResponse);
        }

    }

    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (this.mClient != null) {
            this.mClient.onFormResubmission(view, dontResend, resend);
        } else {
            super.onFormResubmission(view, dontResend, resend);
        }

    }

    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (this.mClient != null) {
            this.mClient.doUpdateVisitedHistory(view, url, isReload);
        } else {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        if (this.mClient != null) {
//            this.mClient.onReceivedSslError(view, handler, (com.tencent.smtt.export.external.interfaces.SslError) error);
//        } else {
//            super.onReceivedSslError(view, handler, (com.tencent.smtt.export.external.interfaces.SslError) error);
//        }
        handler.proceed();

    }
//
//    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
//        if (Build.VERSION.SDK_INT >= 21 && this.mClient != null) {
//            this.mClient.onReceivedClientCertRequest(view, request);
//        } else {
//            super.onReceivedClientCertRequest(view, request);
//        }
//
//    }

//    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
//        if (this.mClient != null) {
//            this.mClient.onReceivedHttpAuthRequest(view, handler, host, realm);
//        } else {
//            super.onReceivedHttpAuthRequest(view, handler, host, realm);
//        }
//
//    }

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return this.mClient != null ? this.mClient.shouldOverrideKeyEvent(view, event) : super.shouldOverrideKeyEvent(view, event);
    }

    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (this.mClient != null) {
            this.mClient.onUnhandledKeyEvent(view, event);
        } else {
            super.onUnhandledKeyEvent(view, event);
        }

    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (this.mClient != null) {
            this.mClient.onScaleChanged(view, oldScale, newScale);
        } else {
            super.onScaleChanged(view, oldScale, newScale);
        }

    }

    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        if (this.mClient != null) {
            this.mClient.onReceivedLoginRequest(view, realm, account, args);
        } else {
            super.onReceivedLoginRequest(view, realm, account, args);
        }

    }

    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return Build.VERSION.SDK_INT >= 26 && this.mClient != null ? this.mClient.onRenderProcessGone(view, detail) : super.onRenderProcessGone(view, detail);
    }

//    public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
//        if (Build.VERSION.SDK_INT >= 27 && this.mClient != null) {
//            this.mClient.onSafeBrowsingHit(view, request, threatType, callback);
//        } else {
//            super.onSafeBrowsingHit(view, request, threatType, callback);
//        }
//
//    }
}

