package com.timego.calculcator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.smallbuer.jsbridge.core.OnBridgeCallback;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.timego.calculcator.api.Api;
import com.timego.calculcator.api.ApiNew10;
import com.timego.calculcator.api.ApiNew11;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.api.BaseObserver;
import com.timego.calculcator.api.BaseObserver1;
import com.timego.calculcator.api.DomainBean;
import com.timego.calculcator.api.Result;
import com.timego.calculcator.databinding.ActivityMain2Binding;
import com.timego.calculcator.databinding.ActivityMainBinding;
import com.timego.calculcator.driver.USBTransferUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity2 extends AppCompatActivity {
//    BridgeWebView webView;
//    TextView tvErrorMsg;
//    LinearLayout layoutError;
//    ImageView show_top_v;
//    ImageView menu_iv;
//    private ImageView helpIv;
//    private LinearLayout showTopLy;

    String downloadImageUrl = "";
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    //    private View topVvvv;
    private int index = 0;
//    private FrameLayout videoContainer;

    //    private ImageView backIv;
//    private ProgressBar progressBar;
    ActivityMain2Binding activityBinding;
    USBTransferUtil USB;
    int inputMoney = 0;
    public ArrayList<Integer> numbers = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(MainActivity2.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(MainActivity2.this, "windows_color", "#000000")));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onCreate(savedInstanceState);
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        View decor = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        activityBinding = ActivityMain2Binding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        hideBottomUIMenu();
        initView();
        activityBinding.backIv.setOnClickListener(view -> onBackPressed());

        getBaseUrls();

        USB = USBTransferUtil.getInstance();
        USB.init(this);
        // 数据接收
        USB.setOnUSBDateReceive(new USBTransferUtil.OnUSBDateReceive() {
            @Override
            public void onReceive(String data_str) {
                if (Integer.parseInt(data_str) >= 100) {
                    numbers.add(Integer.parseInt(data_str));
                } else if (data_str.equals("-7")) {
                    if (numbers.size() > 0) {
                        inputMoney += numbers.get(numbers.size() - 1);
                    }
                }

                LogUtils.i("receive: 收钱" + data_str + "\r\n"+"：总收款："+inputMoney);
            }

            @Override
            public void onNextMoney() {

            }
        });

    }


    private void startWorkMoney() {
        LogUtils.i("send: 3E" + "\r\n");
        USB.write("3E");
        LogUtils.i("send: 18" + "\r\n");
        USB.write("18");
//        dataBinding.debugTv.append("\r\n纸钞机开始收钱");

        if (myCountDownTimerXinTiao != null) {
            myCountDownTimerXinTiao.cancel();
        }
        myCountDownTimerXinTiao = new MyCountDownTimerXinTiao(1000 * 60 * 60 * 24, 2000);
        myCountDownTimerXinTiao.start();
    }


    private void stopWorkMoney() {
        if (myCountDownTimerXinTiao != null) {
            myCountDownTimerXinTiao.cancel();
        }
        myCountDownTimerXinTiao = null;
        LogUtils.i("send: 5E" + "\r\n");
        USB.write("5E");

    }

    MyCountDownTimerXinTiao myCountDownTimerXinTiao;

    //倒计时函数
    private class MyCountDownTimerXinTiao extends CountDownTimer {

        public MyCountDownTimerXinTiao(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            LogUtils.i("send: 02" + "\r\n");
            USB.write("02");
//            dataBinding.debugTv.append("\r\n纸钞机心跳");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            finish();

        }
    }

    private void getBaseUrls() {
        LogUtils.i("0000000000");
        toLoadUrl(MainActivity.getString(MainActivity2.this, "domain", ApiService.DEFAULT_WEB_URL));
//        Api.getInstance().getChannelInfo(ApiService.Site_Id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<Result<DomainBean>>() {
//
//                    @Override
//                    public void onSuccess(Result<DomainBean> feedbackResp) {
//                        APPAplication.saveString(MainActivity2.this,"domain", feedbackResp.data.getDomain());
//                        APPAplication.saveString(MainActivity2.this,"Client_version", feedbackResp.data.getClient_version());
//                        checkNet(feedbackResp.data);
//                    }
//
//                    @Override
//                    public void onError(int code, String msg) {
//                        toLoadUrl(APPAplication.getString(MainActivity2.this,"domain", ApiService.DEFAULT_WEB_URL));
//
//                    }
//                });
    }


    private void toLoadUrl(String domain) {
//        domain = "http://192.168.0.52:8848/test/game_test.html";
//        domain = "http://192.168.0.40:7456/";
        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        LogUtils.i("请求地址是啥：" + domain + "?isApp=1");
        activityBinding.webview.loadUrl(domain + "?isApp=1");
//        activityBinding.webview.loadUrl("http://192.168.0.58:7456/");
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

        if (activityBinding.webview.canGoBack()) {//当webview有多级能返回的时候
            activityBinding.webview.goBack();
        } else {//不能返回了 关闭进程 退出程序
            super.onBackPressed();
        }
    }


    //https://m.xiannvtu.com/
//    public static String url = "http://192.168.110.5:8848/test/game_test.html";
//    public static String url = "http://192.168.110.14:7456?isApp=1&ts=" + System.currentTimeMillis();
    public static String url = "http://192.168.110.14:7456/web-mobile/web-mobile/index.html?ts=" + System.currentTimeMillis();

    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA};

    private int userId = 4;


    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {

        // 没有写的权限，去申请写的权限，会弹出对话框
        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

    }


    @SuppressLint({"NewApi", "WrongConstant"})
    protected void initView() {

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
////        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        activityBinding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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

//        activityBinding.webview.setWebChromeClient(webChromeClient);
        activityBinding.webview.setWebViewClient(webViewClient);
        Log.i("XHXDEBUG", "XHXDEBUGURL:::" + url);


        activityBinding.webview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            LogUtils.i("URL是啥onDownloadStart：" + url);

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
//        activityBinding.webview.loadUrl(url);

        activityBinding.webview.addHandlerLocal("openGame", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(context));
                LogUtils.d("执行了openGame:" + s);
                callBackFunction.onCallBack("");
//                activityBinding.webview.loadUrl(s);
                Intent intent = new Intent(MainActivity2.this,GameActivity.class);
                intent.putExtra("gameUrl",s);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
//
        activityBinding.webview.addHandlerLocal("getMachineNo", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(context));
                LogUtils.d("执行了getMachineNo:" + GsonUtils.beanToJSONString(resultMachineBean));
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(resultMachineBean));
            }
        });


        activityBinding.webview.addHandlerLocal("startCollectMoney", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                MoneyBean moneyBean = new MoneyBean(0, 0);
                ResultBean result = new ResultBean(200, moneyBean);
                LogUtils.d("执行了startCollectMoney:" + GsonUtils.beanToJSONString(result));
                inputMoney = 0;
                numbers = new ArrayList<>();
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result));
                startWorkMoney();
            }
        });


        activityBinding.webview.addHandlerLocal("stopCollectMoney", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {

                MoneyBean moneyBean1 = new MoneyBean(1, inputMoney);
                ResultBean result1 = new ResultBean(200, moneyBean1);
                LogUtils.d("执行了stopCollectMoney:" + GsonUtils.beanToJSONString(result1));
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result1));
                stopWorkMoney();
            }
        });
        activityBinding.webview.addHandlerLocal("stopCollectMoneyAgain", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                MoneyBean moneyBean1 = new MoneyBean(1, inputMoney);
                ResultBean result1 = new ResultBean(200, moneyBean1);
                LogUtils.d("执行了stopCollectMoneyAgain:" + GsonUtils.beanToJSONString(result1));
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result1));
            }
        });


        activityBinding.webview.addHandlerLocal("collectMoney", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                LogUtils.d("执行了collectMoney:" + inputMoney + "");

                callBackFunction.onCallBack(inputMoney + "");
            }
        });


        activityBinding.webview.addHandlerLocal("onFinish", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                ResultBean result2 = new ResultBean(200, null);
                LogUtils.d("执行了onFinish:" + GsonUtils.beanToJSONString(result2));
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(result2));
                finish();
            }
        });


    }

    private void collectMoney(String object) {
        LogUtils.d("执行了collectMoney:" + object);
        activityBinding.webview.callHandler("collectMoney", object, s -> {
            LogUtils.i("收到的参数：" + s);
        });
//        activityBinding.webview.evaluateJavascript("collectMoney",object);
    }


    boolean hasSignIn = false;

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail renderProcessGoneDetail) {
//            webView.clearCache(false);
//            webView.clearHistory();
            LogUtils.i("onRenderProcessGone:"+renderProcessGoneDetail.didCrash()+";;"+renderProcessGoneDetail.rendererPriorityAtExit());
//            webView.reload();
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            LogUtils.i("URL是啥加载完成：" + webView.getUrl());
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
                activityBinding.topVvvv.setVisibility(View.GONE);
            } else {
                if (isAtGame) {
                    activityBinding.topVvvv.setVisibility(View.VISIBLE);
                } else {
                    activityBinding.topVvvv.setVisibility(View.GONE);

                }
            }

//            if (isNetError&&isAtGame) {
//                topVvvv.setVisibility(View.VISIBLE);
//            }

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
            LogUtils.i("URL是啥onCreateWindow：" + activityBinding.webview.getUrl());


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
//                        Intent browserIntent = new Intent(MainActivity2.this, WebViewActivity.class);
//                        browserIntent.putExtra("url", url);
//                        startActivity(browserIntent);
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
            LogUtils.i("数据接口：onShowFileChooser");
            mUploadCallbackForHighApi = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSER);
            } catch (ActivityNotFoundException e) {
                mUploadCallbackForHighApi = null;
//                WidgetUtils.showToast(JsBridgeActivity.this, "未知错误", WidgetUtils.ToastType.ERROR);
                Toast.makeText(MainActivity2.this, "未知错误", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        // For 3.0+
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            LogUtils.i("数据接口：openFileChooseracceptType");

            openFilerChooser(uploadMsg);
        }


        private void openFilerChooser(ValueCallback<Uri> uploadMsg) {
            LogUtils.i("数据接口：openFileChooser");

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
                MainActivity2.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        for (String permisson : request.getResources()) {
                            permissionRequest = request;
                            if (permisson.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CAMERA) != 0) {
                                    ActivityCompat.requestPermissions(MainActivity2.this, PERMISSIONS_CAMERA, 1111);
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

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
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

    /**
     * 设置网页中图片的点击事件
     *
     * @param view
     */
    public static void setWebImageClick(WebView view, String method) {


    }


    @Override
    public void onDestroy() {
        stopWorkMoney();
//        USB.disconnect();
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


        super.onDestroy();
    }

    @Override
    protected void onResume() {
        USB.connect();  // 当系统监测到usb插入动作后跳转到此页面时


        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //wp-caption alignnone


}
