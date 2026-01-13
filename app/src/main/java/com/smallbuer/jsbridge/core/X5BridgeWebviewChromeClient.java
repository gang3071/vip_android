package com.smallbuer.jsbridge.core;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebChromeClient;
/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/4/2 18:01
 * 用途
 * **********************
 */
public class X5BridgeWebviewChromeClient extends WebChromeClient {
    private String TAG = "BridgeWebviewChromeClient";
    private BridgeTiny bridgeTiny;
    private X5BridgeWebView bridgeWebView;

    public X5BridgeWebviewChromeClient(X5BridgeWebView bridgeWebView, BridgeTiny bridgeTiny) {
        this.bridgeTiny = bridgeTiny;
        this.bridgeWebView = bridgeWebView;
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        BridgeLog.d(this.TAG, "message->" + message);
        this.bridgeTiny.onJsPrompt(this.bridgeWebView, message);
        result.confirm("do");
        return true;
    }
}
