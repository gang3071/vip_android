package com.timego.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.timego.game.databinding.ActivityMain2GameBinding;


public class GameChromeActivity extends AppCompatActivity {

    ActivityMain2GameBinding activityBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("结果是啥", "结果是啥：11111");

        getWindow().setNavigationBarColor(Color.parseColor(getString(GameChromeActivity.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(getString(GameChromeActivity.this, "windows_color", "#000000")));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        activityBinding = ActivityMain2GameBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        hideBottomUIMenu();
        initView();
        isFullGame();

        getBaseUrls();


    }

    private void isFullGame() {
        if(getIntent().getStringExtra("gameUrl").endsWith("&display_mode=1")){

        }else{

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("结果是啥", "结果是啥：2222");

        getBaseUrls();

    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        if (context == null) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }


    private void getBaseUrls() {
        LogUtils.i("地址是啥："+getIntent().getStringExtra("gameUrl"));

        toLoadUrl(getIntent().getStringExtra("gameUrl"));
//        toLoadUrl("https://ts2gamesite.royalgaming777.com/EnterGame2?token=iraANuizPPu9hYdljkBZMz53WTnNncgOFkgLlOykuHpjmdPBBQ5v6y1YbkXnOKf_");

    }


    private void toLoadUrl(String domain) {
        Log.i("结果是啥", "结果是啥：" + domain);
        activityBinding.webview.loadUrl(domain);

        //
//        String baseInfos = "<!DOCTYPE html>" +
//                "<html>" +
//                "<head>" +
//                "<meta charset=\"utf-8\">" +
//                "<title>GAME</title>" +
//                "</head>" +
//                "<body style=\"margin: 0px;\">" +
//                "<iframe src=\""+domain+"\" width=100% height=1750px scrolling=\"yes\"  frameborder=\"0\"></iframe>" +
//                "" +
//                "</iframe>" +
//                "</body>" +
//                "</html>";
//
//        activityBinding.webview.loadDataWithBaseURL(null, baseInfos, "text/html", "utf-8", null);
    }


    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT <= 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT > 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }


    @Override
    public void onBackPressed() {
        destroyWebView();
        System.exit(0);
    }

    //    public static String url = "http://192.168.110.5:8848/test/game_test.html";
//    public static String url = "http://192.168.110.14:7456?isApp=1&ts=" + System.currentTimeMillis();
    public static String url = "http://192.168.110.14:7456/web-mobile/web-mobile/index.html?ts=" + System.currentTimeMillis();

    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA};

    @SuppressLint({"NewApi", "WrongConstant"})
    protected void initView() {

        WebSettings settings = activityBinding.webview.getSettings();
        settings.setDomStorageEnabled(true);
//        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        // 设置允许访问文件数据
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        activityBinding.webview.setFocusable(true);
        activityBinding.webview.setFocusableInTouchMode(true);
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        activityBinding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//
        LogUtils.i("设备信息："+activityBinding.webview.getSettings().getUserAgentString());

        activityBinding.webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36");
//        LogUtils.i("设备信息1："+activityBinding.webview.getSettings().getUserAgentString());
//        activityBinding.webview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 13; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Mobile Safari/537.36");
        settings.setSupportZoom(false);
//        activityBinding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webView.setHorizontalScrollbarOverlay(true);
        activityBinding.webview.setHorizontalScrollBarEnabled(true);
        activityBinding.webview.requestFocus();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setOffscreenPreRaster(false);
        }
        // 设置在WebView内部是否允许通过file url加载的 Js代码读取其他的本地文件
        // Android 4.1前默认允许，4.1后默认禁止
        settings.setAllowFileAccessFromFileURLs(true);
        // 设置WebView内部是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        // Android 4.1前默认允许，4.1后默认禁止
        settings.setAllowUniversalAccessFromFileURLs(true);
//

        activityBinding.webview.setWebChromeClient(webChromeClient);
        activityBinding.webview.setWebViewClient(webViewClient);
        activityBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        activityBinding.webview.getSettings().setDomStorageEnabled(true);
        Log.i("XHXDEBUG", "XHXDEBUGURL:::" + url);


        activityBinding.webview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
//            LogUtils.i("URL是啥onDownloadStart：" + url);

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
//        activityBinding.buttonPanel.setOnClickListener(view -> activityBinding.webview.loadUrl("chrome://crash"));

        activityBinding.backIv.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    initX = event.getRawX();
                    initY = event.getRawY();
//                    LogUtils.d("touchevent", "lastX=" + lastX + "  lastY" + lastY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = event.getRawX() - lastX;
                    float dy = event.getRawY() - lastY;
                    int left = activityBinding.backIv.getLeft() + (int) dx;
                    int top = activityBinding.backIv.getTop() + (int) dy;
                    int right = activityBinding.backIv.getRight() + (int) dx;
                    int bottom = activityBinding.backIv.getBottom() + (int) dy;
                    activityBinding.backIv.layout(left, top, right, bottom);
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    float upx = event.getRawX();
                    float upy = event.getRawY();
                    if (upx == initX && upy == initY) {
                        onBackPressed();
                    }

                    break;
            }
            return true;
        });


        activityBinding.webview.setOnGenericMotionListener((view, motionEvent) -> {
            //点击鼠标左键
            if (motionEvent.getActionButton() == MotionEvent.BUTTON_PRIMARY && motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS) {
                //切换view 的内容
//                    activityBinding.webview.setTo
                startX = motionEvent.getX();
                startY = motionEvent.getY();
                Log.i("焦点", "焦点：" + startX + ";" + startY);
                simulateClickStart(activityBinding.webview, startX, startY);
            } else if (motionEvent.getActionButton() == MotionEvent.BUTTON_PRIMARY && motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_RELEASE) {
                endX = motionEvent.getX();
                endY = motionEvent.getY();
                Log.i("焦点", "焦点：" + endX + ";" + endY);

                simulateClickStartEnd(activityBinding.webview, endX, endY);

            }
            return true;

        });


//        activityBinding.webview.setOnGenericMotionListener((view, motionEvent) -> {
//            //点击鼠标左键
//            if (motionEvent.getActionButton() == MotionEvent.BUTTON_PRIMARY) {
//                if (motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS) {
//                    startX = motionEvent.getX();
//                    startY = motionEvent.getY();
//                    Log.i("焦点", "焦点：" + startX + ";" + startY);
//                    simulateClickStart(activityBinding.webview, startX, startY);
//                } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_BUTTON_RELEASE) {
//                    endX = motionEvent.getX();
//                    endY = motionEvent.getY();
//                    Log.i("焦点", "焦点END：" + endX + ";" + endY);
//
//                    simulateClickStartEnd(activityBinding.webview, endX, endY);
//                }
//            }
//            return true;
//
//
//        });

    }

    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;

    private void simulateClick(View view, float x, float y, float endX, float endY) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, endX, endY, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    long downTime;

    private void simulateClickStart(View view, float x, float y) {
        downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        view.onTouchEvent(downEvent);
        downEvent.recycle();
    }


    private void simulateClickStartEnd(View view, float endX, float endY) {
        long moveTime = SystemClock.uptimeMillis();
        if (endX - startX > 200 || endY - startY > 200||endX - startX < -200 || endY - startY < -200) {
            Log.i("焦点", "焦点MOVE：" + endX + ";" + endY);

            final MotionEvent upEvent = MotionEvent.obtain(moveTime, moveTime, MotionEvent.ACTION_MOVE, endX, endY, 0);
            view.onTouchEvent(upEvent);
            upEvent.recycle();
        }
        moveTime+=1000;
        Log.i("焦点", "焦点ACTION_UP：" + endX + ";" + endY);
        final MotionEvent upEvent = MotionEvent.obtain(moveTime, moveTime, MotionEvent.ACTION_UP, endX, endY, 0);
        view.onTouchEvent(upEvent);
        upEvent.recycle();

    }


    float lastX, lastY;
    float initX, initY;

    boolean hasSignIn = false;
    WebViewClient webViewClient = new WebViewClient() {

        @SuppressLint("NewApi")
        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail renderProcessGoneDetail) {
//            webView.clearCache(false);
//            webView.clearHistory();
//            LogUtils.i("onRenderProcessGone111:"+renderProcessGoneDetail.didCrash()+";;"+renderProcessGoneDetail.rendererPriorityAtExit());
            getBaseUrls();
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
//            LogUtils.i("URL是啥加载完成：" + webView.getUrl());
            if (webView.getUrl().contains("hasSignIn")) {
                hasSignIn = true;
            }

            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            // 重新测量
            webView.measure(w, h);
            activityBinding.showTopLy.setVisibility(View.GONE);

            if (webView.getUrl().equals(url + "index") || webView.getUrl().equals(url + "/index")) {
                isAtGame = false;
            } else {
                if (isAtGame) {
                } else {

                }
            }

        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int errorCode = error.getErrorCode();
                String errorMessage = error.getDescription().toString();
                String currentUrl = request.getUrl().toString();
                if ((errorCode == -2 || errorCode == -6) && currentUrl.contains(url)) {
                    onShowErrorView(errorMessage);
                } else {
                    onShowNetView();
                }
            }
            activityBinding.progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if ((errorCode == -2 || errorCode == -6) && failingUrl.contains(url)) {
                    onShowErrorView(description);
                } else {
                    onShowNetView();
                }
            }
            activityBinding.progressbar.setVisibility(View.GONE);

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            return super.shouldOverrideUrlLoading(webView, s);
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }
    };

    private boolean isNetError = false;

    public void onShowErrorView(String errorMsg) { //网络不可用的情况
        //  topVvvv.setVisibility(View.GONE);
        activityBinding.webview.setVisibility(View.GONE);
        activityBinding.layoutError.setVisibility(View.VISIBLE);
        activityBinding.errormsg.setText(errorMsg);
        activityBinding.showTopLy.setVisibility(View.GONE);
        isNetError = true;

    }

    public void onShowNetView() {
        // topVvvv.setVisibility(View.GONE);
        activityBinding.webview.setVisibility(View.VISIBLE);
        activityBinding.errormsg.setVisibility(View.GONE);
        activityBinding.showTopLy.setVisibility(View.GONE);
        isNetError = false;
    }


    boolean isAtGame = false;
    //    String uuid = "", uuid1 = "";
    private static final int REQUEST_CODE_FILE_CHOOSER = 1;
    private ValueCallback<Uri> mUploadCallbackForLowApi;
    private ValueCallback<Uri[]> mUploadCallbackForHighApi;
    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onCreateWindow(WebView webViewdd, boolean b, boolean b1, Message resultMsg) {
//            LogUtils.i("URL是啥onCreateWindow：" + activityBinding.webview.getUrl());


            WebView newWebView = new WebView(webViewdd.getContext());
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (!(url.startsWith("http") || url.startsWith("https"))) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        activityBinding.webview.loadUrl(url);
                        return true;
                    }
                    return false;

                }


            });
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 更新进度条的进度
            activityBinding.progressbar.setProgress(newProgress);
            // 如果加载完成，隐藏进度条
            if (newProgress == 100) {
                activityBinding.progressbar.setVisibility(View.GONE);
            } else {
                activityBinding.progressbar.setVisibility(View.VISIBLE);
            }
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            LogUtils.i("数据接口：onShowFileChooser");
            mUploadCallbackForHighApi = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
            } catch (ActivityNotFoundException e) {
                mUploadCallbackForHighApi = null;
//                WidgetUtils.showToast(JsBridgeActivity.this, "未知错误", WidgetUtils.ToastType.ERROR);
                Toast.makeText(GameChromeActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        // For 3.0+
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            LogUtils.i("数据接口：openFileChooseracceptType");

            openFilerChooser(uploadMsg);
        }


        private void openFilerChooser(ValueCallback<Uri> uploadMsg) {
//            LogUtils.i("数据接口：openFileChooser");

            mUploadCallbackForLowApi = uploadMsg;
            startActivityForResult(Intent.createChooser(getFilerChooserIntent(), "File Chooser"), REQUEST_CODE_FILE_CHOOSER);
        }


        private Intent getFilerChooserIntent() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            return intent;
        }


        @Override
        public void onPermissionRequest(PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                GameChromeActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        for (String permisson : request.getResources()) {
                            permissionRequest = request;
                            if (permisson.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                                if (ContextCompat.checkSelfPermission(GameChromeActivity.this, Manifest.permission.CAMERA) != 0) {
                                    ActivityCompat.requestPermissions(GameChromeActivity.this, PERMISSIONS_CAMERA, 1111);
                                } else {
                                    request.grant(request.getResources());
                                    request.getOrigin();
                                }

                            }
                        }


                    }
                });


            }

        }

    };

    private PermissionRequest permissionRequest;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FILE_CHOOSER:
                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                    afterFileChooseGoing(resultCode, data);
                }
                break;


        }
    }


    /**
     * onActivityResult方法
     */

    private void afterFileChooseGoing(int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mUploadCallbackForHighApi == null) {
                return;
            }
            mUploadCallbackForHighApi.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadCallbackForHighApi = null;
        } else {
            if (mUploadCallbackForLowApi == null) {
                return;
            }
            Uri result = data == null ? null : data.getData();
            mUploadCallbackForLowApi.onReceiveValue(result);
            mUploadCallbackForLowApi = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case 1111:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissionRequest != null) {
                    permissionRequest.grant(permissionRequest.getResources());
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    private void destroyWebView() {
        if (activityBinding.webview != null) {
            //加载null内容
            activityBinding.webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            activityBinding.webview.clearHistory();
            //移除WebView
            ((ViewGroup) activityBinding.webview.getParent()).removeView(activityBinding.webview);
            //销毁VebView
            activityBinding.webview.destroy();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
