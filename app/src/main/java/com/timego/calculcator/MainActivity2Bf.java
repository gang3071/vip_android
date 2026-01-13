//package com.timego.calculcator;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.webkit.PermissionRequest;
//import android.webkit.SslErrorHandler;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceError;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.smallbuer.jsbridge.core.BridgeHandler;
//import com.smallbuer.jsbridge.core.CallBackFunction;
//import com.timego.calculcator.databinding.ActivityMain2Binding;
//
//import java.io.IOException;
//
//
//public class MainActivity2Bf extends AppCompatActivity {
////    BridgeWebView webView;
////    TextView tvErrorMsg;
////    LinearLayout layoutError;
////    ImageView show_top_v;
////    ImageView menu_iv;
////    private ImageView helpIv;
////    private LinearLayout showTopLy;
//
//    String downloadImageUrl = "";
//    private int requestCode;
//    private String[] permissions;
//    private int[] grantResults;
////    private View topVvvv;
//    private int index = 0;
////    private FrameLayout videoContainer;
//
////    private ImageView backIv;
////    private ProgressBar progressBar;
//    private MyServer server;
//    ActivityMain2Binding activityBinding;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(MainActivity2Bf.this, "style_color", "#000000")));
//        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(MainActivity2Bf.this, "windows_color", "#000000")));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//        super.onCreate(savedInstanceState);
////        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        View decor = getWindow().getDecorView();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
////        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        activityBinding = ActivityMain2Binding.inflate(LayoutInflater.from(this));
//        setContentView(activityBinding.getRoot());
//        hideBottomUIMenu();
//        initView();
//        activityBinding.backIv.setOnClickListener(view -> onBackPressed());
//
//        // 创建服务器实例并启动
//        server = new MyServer(this);
//        try {
//            server.start();
//            LogUtils.i("Server", "Server started");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    protected void hideBottomUIMenu() {
//        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT  <=11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT  >19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//
//        }
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//
//        if (activityBinding.webview.canGoBack()) {//当webview有多级能返回的时候
//            String url = activityBinding.webview.getUrl();
//            // 在首页 就退出这个页面
//            if (activityBinding.webview.getUrl().equals(url + "index") || activityBinding.webview.getUrl().equals(url + "/index")) {
//                super.onBackPressed();
//            } else { //不在首页 回到首页
//                if (activityBinding.webview.getUrl().equals(url + "index") || activityBinding.webview.getUrl().equals(url + "/index")) {
//                    isAtGame = false;
//                }
//                activityBinding.topVvvv.setVisibility(View.GONE);
//                activityBinding.progressbar.setVisibility(View.VISIBLE);
//                //当有条过登录页面 只能重载 不然逻辑会异常
//                if (hasSignIn) {
//                    onShowNetView();
//                    activityBinding.webview.loadUrl(url);
//                } else {
//                    while (activityBinding.webview.canGoBack()) {
//                        activityBinding.webview.goBack();
//                    }
//
//                }
//            }
//        } else {//不能返回了 关闭进程 退出程序
////            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
////            homeIntent.addCategory(Intent.CATEGORY_HOME);
////            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(homeIntent);
////            android.os.Process.killProcess(android.os.Process.myPid());
////            System.exit(1);
////            finish();
//            super.onBackPressed();
//        }
//    }
//
//    public void removeChildView(int child) {
//        WebView childView = (WebView) activityBinding.webview.getChildAt(child - 1);
//        activityBinding.webview.removeView(childView);
//        if (activityBinding.webview.canGoBack()) {
//            activityBinding.webview.goBack();
//        }
//    }
//
//
//    //https://m.xiannvtu.com/
////    public static String url = "http://192.168.110.5:8848/test/game_test.html";
////    public static String url = "https://game-test.qtalk666.top?isApp=1";
//    public static String url = "https://www.xiaomi.cn/";
//
//
//    //先定义
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//
//    private static String[] PERMISSIONS_STORAGE = {
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE"};
//
//    private static String[] PERMISSIONS_CAMERA = {
//            Manifest.permission.CAMERA};
//
//    private int userId = 4;
//
//
//    //然后通过一个函数来申请
//    public static void verifyStoragePermissions(Activity activity) {
//
//        // 没有写的权限，去申请写的权限，会弹出对话框
//        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//
//    }
//
//
//    @SuppressLint({"NewApi", "WrongConstant"})
//    protected void initView() {
//
//        WebSettings settings = activityBinding.webview.getSettings();
//        settings.setDomStorageEnabled(true);
//        settings.setAppCacheEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        settings.setJavaScriptEnabled(true);
//        settings.setLoadWithOverviewMode(true);
//        // 设置允许访问文件数据
//        settings.setAllowFileAccess(true);
//        settings.setAllowContentAccess(true);
//        settings.setDatabaseEnabled(true);
//        settings.setSavePassword(true);
//        settings.setSaveFormData(true);
//        settings.setUseWideViewPort(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setPluginState(WebSettings.PluginState.ON);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        activityBinding.webview.setFocusable(true);
//        activityBinding.webview.setFocusableInTouchMode(true);
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//
//        settings.setSupportZoom(false);
////        activityBinding.webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
////        webView.setHorizontalScrollbarOverlay(true);
//        activityBinding.webview.setHorizontalScrollBarEnabled(true);
//        activityBinding.webview.requestFocus();
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            settings.setMediaPlaybackRequiresUserGesture(false);
//        }
//        // 设置在WebView内部是否允许通过file url加载的 Js代码读取其他的本地文件
//        // Android 4.1前默认允许，4.1后默认禁止
//        settings.setAllowFileAccessFromFileURLs(true);
//        // 设置WebView内部是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
//        // Android 4.1前默认允许，4.1后默认禁止
//        settings.setAllowUniversalAccessFromFileURLs(true);
//
//
//
//        activityBinding.webview.setWebChromeClient(webChromeClient);
//        activityBinding.webview.setWebViewClient(webViewClient);
//        Log.i("XHXDEBUG", "XHXDEBUGURL:::" + url);
//
////        new Handler().postDelayed(() -> showTopLy.setVisibility(View.GONE), 3000);
////        verifyStoragePermissions(this);
//
//        activityBinding.webview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
//            LogUtils.i("URL是啥onDownloadStart：" + url);
//
//            try {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        });
//        activityBinding.webview.loadUrl(url);
//
//
//
//        activityBinding.webview.addHandlerLocal("getMachineNo", new BridgeHandler() {
//            @Override
//            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                LogUtils.d("执行了getMachineNo:" + s);
//                ResultMachineBean resultMachineBean =  new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(context));
//                callBackFunction.onCallBack(GsonUtils.beanToJSONString(resultMachineBean));
//            }
//        });
//
//
//
//        activityBinding.webview.addHandlerLocal("startCollectMoney", new BridgeHandler() {
//            @Override
//            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                LogUtils.d("执行了startCollectMoney:" + s);
//                MoneyBean moneyBean = new MoneyBean(0, 0);
//                ResultBean result = new ResultBean(200, moneyBean);
//                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result));
//            }
//        });
//
//
//        activityBinding.webview.addHandlerLocal("stopCollectMoney", new BridgeHandler() {
//            @Override
//            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                LogUtils.d("执行了stopCollectMoney:" + s);
//                MoneyBean moneyBean1 = new MoneyBean(1, 100);
//                ResultBean result1 = new ResultBean(200, moneyBean1);
//                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result1));
//            }
//        });
//
//
//
//        activityBinding.webview.addHandlerLocal("onFinish", new BridgeHandler() {
//            @Override
//            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                LogUtils.d("执行了onFinish:" + s);
//                ResultBean result2 = new ResultBean(200, null);
//                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result2));
//                finish();
//            }
//        });
//
//    }
//
//
//    boolean hasSignIn = false;
//
//    WebViewClient webViewClient = new WebViewClient() {
//
//        @Override
//        public void onPageFinished(WebView webView, String s) {
//            super.onPageFinished(webView, s);
//            LogUtils.i("URL是啥加载完成：" + webView.getUrl());
//            if (webView.getUrl().contains("hasSignIn")) {
//                hasSignIn = true;
//            }
//
//            int w = View.MeasureSpec.makeMeasureSpec(0,
//                    View.MeasureSpec.UNSPECIFIED);
//            int h = View.MeasureSpec.makeMeasureSpec(0,
//                    View.MeasureSpec.UNSPECIFIED);
//            // 重新测量
//            webView.measure(w, h);
//            activityBinding.showTopLy.setVisibility(View.GONE);
//
//            if (webView.getUrl().equals(url + "index") || webView.getUrl().equals(url + "/index")) {
//                isAtGame = false;
//                activityBinding.topVvvv.setVisibility(View.GONE);
//            } else {
//                if (isAtGame) {
//                    activityBinding.topVvvv.setVisibility(View.VISIBLE);
//                } else {
//                    activityBinding.topVvvv.setVisibility(View.GONE);
//
//                }
//            }
//
////            if (isNetError&&isAtGame) {
////                topVvvv.setVisibility(View.VISIBLE);
////            }
//
//        }
//
//
//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int errorCode = error.getErrorCode();
//                String errorMessage = error.getDescription().toString();
//                String currentUrl = request.getUrl().toString();
//                if ((errorCode == -2 || errorCode == -6) && currentUrl.contains(url)) {
//                    onShowErrorView(errorMessage);
//                } else {
//                    onShowNetView();
//                }
//            }
//            activityBinding.progressbar.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                if ((errorCode == -2 || errorCode == -6) && failingUrl.contains(url)) {
//                    onShowErrorView(description);
//                } else {
//                    onShowNetView();
//                }
//            }
//            activityBinding.progressbar.setVisibility(View.GONE);
//
//        }
//
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
////            super.onReceivedSslError(view, handler, error);
//            handler.proceed();
//        }
////
////        @Override
////        public boolean shouldOverrideUrlLoading(WebView webView, String url1) {
////            return false;
////        }
////
////        @Override
////        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////            return false;
////        }
//    };
//
//    private boolean isNetError = false;
//
//    public void onShowErrorView(String errorMsg) { //网络不可用的情况
//        //  topVvvv.setVisibility(View.GONE);
//        activityBinding.webview.setVisibility(View.GONE);
//        activityBinding.layoutError.setVisibility(View.VISIBLE);
//        activityBinding.errormsg.setText(errorMsg);
//        activityBinding.showTopLy.setVisibility(View.GONE);
//        isNetError = true;
//
//    }
//
//    public void onShowNetView() {
//        // topVvvv.setVisibility(View.GONE);
//        activityBinding.webview.setVisibility(View.VISIBLE);
//        activityBinding.errormsg.setVisibility(View.GONE);
//        activityBinding.showTopLy.setVisibility(View.GONE);
//        isNetError = false;
//    }
//
//
//    boolean isAtGame = false;
//    //    String uuid = "", uuid1 = "";
//    private static final int REQUEST_CODE_FILE_CHOOSER = 1;
//    private ValueCallback<Uri> mUploadCallbackForLowApi;
//    private ValueCallback<Uri[]> mUploadCallbackForHighApi;
//    WebChromeClient webChromeClient = new WebChromeClient() {
//        @Override
//        public boolean onCreateWindow(WebView webViewdd, boolean b, boolean b1, Message resultMsg) {
//            LogUtils.i("URL是啥onCreateWindow：" + activityBinding.webview.getUrl());
//
//            WebView newWebView = new WebView(webViewdd.getContext());
//            newWebView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    if (!(url.startsWith("http") || url.startsWith("https"))) {
//                        try {
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(url));
//                            startActivity(intent);
//                            return true;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        activityBinding.webview.loadUrl(url);
//                        return true;
//                    }
//                    return false;
//
//                }
//
//
//            });
//            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//            transport.setWebView(newWebView);
//            resultMsg.sendToTarget();
//            return true;
//        }
//
//        @Override
//        public void onCloseWindow(WebView window) {
//            super.onCloseWindow(window);
//        }
//
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            // 更新进度条的进度
//            activityBinding.progressbar.setProgress(newProgress);
//            // 如果加载完成，隐藏进度条
//            if (newProgress == 100) {
//                activityBinding.progressbar.setVisibility(View.GONE);
//            } else {
//                activityBinding.progressbar.setVisibility(View.VISIBLE);
//            }
//        }
//
//
//        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            LogUtils.i("数据接口：onShowFileChooser");
//            mUploadCallbackForHighApi = filePathCallback;
//            Intent intent = fileChooserParams.createIntent();
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            try {
//                startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
//            } catch (ActivityNotFoundException e) {
//                mUploadCallbackForHighApi = null;
////                WidgetUtils.showToast(JsBridgeActivity.this, "未知错误", WidgetUtils.ToastType.ERROR);
//                Toast.makeText(MainActivity2Bf.this, "未知错误", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            return true;
//        }
//
//        // For 3.0+
//        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            LogUtils.i("数据接口：openFileChooseracceptType");
//
//            openFilerChooser(uploadMsg);
//        }
//
//
//        private void openFilerChooser(ValueCallback<Uri> uploadMsg) {
//            LogUtils.i("数据接口：openFileChooser");
//
//            mUploadCallbackForLowApi = uploadMsg;
//            startActivityForResult(Intent.createChooser(getFilerChooserIntent(), "File Chooser"), REQUEST_CODE_FILE_CHOOSER);
//        }
//
//
//        private Intent getFilerChooserIntent() {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            return intent;
//        }
//
//
//        @Override
//        public void onPermissionRequest(PermissionRequest request) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                MainActivity2Bf.this.runOnUiThread(new Runnable() {
//                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                    @Override
//                    public void run() {
//                        for (String permisson : request.getResources()) {
//                            permissionRequest = request;
//                            if (permisson.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
//                                if (ContextCompat.checkSelfPermission(MainActivity2Bf.this, Manifest.permission.CAMERA) != 0) {
//                                    ActivityCompat.requestPermissions(MainActivity2Bf.this, PERMISSIONS_CAMERA, 1111);
//                                } else {
//                                    request.grant(request.getResources());
//                                    request.getOrigin();
//                                }
//
//                            }
//                        }
//
//
//                    }
//                });
//
//
//            }
//
//        }
//
//    };
//
//    private PermissionRequest permissionRequest;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CODE_FILE_CHOOSER:
//                if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
//                    afterFileChooseGoing(resultCode, data);
//                }
//                break;
//
//
//        }
//    }
//
//
//    /**
//     * onActivityResult方法
//     */
//
//    private void afterFileChooseGoing(int resultCode, Intent data) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (mUploadCallbackForHighApi == null) {
//                return;
//            }
//            mUploadCallbackForHighApi.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//            mUploadCallbackForHighApi = null;
//        } else {
//            if (mUploadCallbackForLowApi == null) {
//                return;
//            }
//            Uri result = data == null ? null : data.getData();
//            mUploadCallbackForLowApi.onReceiveValue(result);
//            mUploadCallbackForLowApi = null;
//        }
//    }
//
//    public class JavaScriptinterface {
//        Context context;
//
//        public JavaScriptinterface(Context c) {
//            context = c;
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_EXTERNAL_STORAGE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                }
//                break;
//            case 1111:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissionRequest != null) {
//                    permissionRequest.grant(permissionRequest.getResources());
//                }
//                break;
//            default:
//                break;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    /**
//     * 设置网页中图片的点击事件
//     *
//     * @param view
//     */
//    public static void setWebImageClick(WebView view, String method) {
//
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//
//        if (activityBinding.webview != null) {
//            //加载null内容
//            activityBinding.webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            //清除历史记录
//            activityBinding.webview.clearHistory();
//            //移除WebView
////            ((ViewGroup) webView.getParent()).removeView(webView);
//            //销毁VebView
//            activityBinding.webview.destroy();
//        }
//
//        // 停止服务器
//        if (server != null) {
//            server.stop();
//        }
//
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//
//
//    /**
//     * 获取状态栏高度
//     *
//     * @return
//     */
//    public int getStatusBarHeight() {
//        int result = 0;
//        //获取状态栏高度的资源id
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//    //wp-caption alignnone
//
//
//}
