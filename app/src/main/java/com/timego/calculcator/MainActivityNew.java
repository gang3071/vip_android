package com.timego.calculcator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.AppUpdateCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
//import com.tencent.smtt.export.external.interfaces.PermissionRequest;
//import com.tencent.smtt.export.external.interfaces.SslError;
//import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
//import com.tencent.smtt.export.external.interfaces.WebResourceError;
//import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
//import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
//import com.tencent.smtt.sdk.ValueCallback;
//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebView;
//import com.tencent.smtt.sdk.WebViewClient;
import com.smallbuer.jsbridge.core.OnBridgeCallback;
import com.timego.calculcator.api.ApiNew11;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.api.BaseObserver;
import com.timego.calculcator.api.Result;
import com.timego.calculcator.databinding.ActivityMain2Binding;
import com.timego.calculcator.databinding.ActivityMain2NewBinding;
import com.timego.calculcator.driver.USBTransferUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ZtlApi.Gpio;
import ZtlApi.ZtlManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.maybesix.xhlibrary.serialport.ComPortData;
import top.maybesix.xhlibrary.serialport.SerialPortHelper;


public class MainActivityNew extends AppCompatActivity {
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
    ActivityMain2NewBinding activityBinding;
    //USBTransferUtil USB;
    public static SerialPortHelper serialPort; //数钞机

    Map<String, String> headers;

    int inputMoney = 0;
    public ArrayList<Integer> numbers = new ArrayList<>();

    class GpioInfo {
        public Gpio gpio;
        Thread inputThread;

        //        Switch sGpioValue;
        boolean ThreadisSend = true;
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                try {
                    ComPortData data = (ComPortData) msg.obj;
                    onDataReceiver(data.getRecData());
                } catch (Exception e) {

                }

                // stringBuilder.append("\n" + "收到指令" + data);   //810DFF10  1000回复的指令  8109FE10  500回复的指令    8101FF10  100回复的指令
//                if (Integer.parseInt(data) >= 100) {
//                    numbers.add(Integer.parseInt(data));
//                } else if (data.equals("-7")) {
//                    if (numbers.size() > 0) {
//                        inputMoney += numbers.get(numbers.size() - 1);
//                    }
//                }
                // stringBuilder.append("\n" + "收钱数额" + data + "\r\n" + "：总收款：" + inputMoney);
//                activityBinding.txtLog.setText(stringBuilder.toString());
            }
        }
    };

    private int step = 0;
    private int data1 = 0;
    private int data2 = 0;
    private long lastReceiveTime = 0; // 记录上一次收到字节的时间点


    public void onDataReceiver(byte[] buffer) {
        if (buffer == null) return;

//        long currentTime = System.currentTimeMillis();
//
//        // --- 超时检测逻辑 ---
//        // 如果距离上一个字节超过 500 毫秒，且处于中间状态，强制归零
//        if (step != 0 && (currentTime - lastReceiveTime) > 500) {
//            step = 0;
//            Log.w("Serial", "指令接收超时，已自动重置状态机");
//             stringBuilder.append("\n" + "指令接收超时，已自动重置状态机");   //810DFF10  1000回复的指令  8109FE10  500回复的指令    8101FF10  100回复的指令
//            activityBinding.txtLog.setText(stringBuilder.toString());
//        }
//        lastReceiveTime = currentTime;

        for (byte b : buffer) {
            int val = b & 0xFF;

            switch (step) {
                case 0: // 等待头 81
                    if (val == 0x81) step = 1;
                    break;

                case 1: // 记录第 2 位
                    data1 = val;
                    step = 2;
                    break;

                case 2: // 记录第 3 位
                    data2 = val;
                    step = 3;
                    break;

                case 3: // 验证结尾 10
                    if (val == 0x10) {
                        handleAmount(data1, data2);
                    } else {
                        // 如果结尾不是 10，说明数据错位，尝试看当前位是不是新指令的 81
                        if (val == 0x81) {
                            step = 1;
                            continue; // 跳过最后的 step=0
                        }
                    }
                    step = 0; // 处理完或出错都重置
                    break;
            }
        }
    }

    private void handleAmount(int b2, int b3) {
        final String message;
        final int amount;
//        stringBuilder.append("\n" + "解析金额指令开始");   //810DFF10  1000回复的指令  8109FE10  500回复的指令    8101FF10  100回复的指令
//        activityBinding.txtLog.setText(stringBuilder.toString());
        // 1. 先在子线程计算出金额，确定提示内容
        if (b2 == 0x0D && b3 == 0xFF) {
            amount = 1000;
            message = "成功接收 1000 ";
        } else if (b2 == 0x09 && b3 == 0xFE) {
            amount = 500;
            message = "成功接收 500 ";
        } else if (b2 == 0x01 && b3 == 0xFF) {
            amount = 100;
            message = "成功接收 100 ";
        } else {
            amount = 0;
            message = null;
        }

        // 2. 如果金额有效，切换到主线程进行 UI 提示
        if (message != null) {
            inputMoney = inputMoney + amount;
            // 使用 runOnUiThread 切换线程
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    }

    //动态数组
    List<GpioInfo> gpios = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(MainActivityNew.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(MainActivityNew.this, "windows_color", "#000000")));
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
        activityBinding = ActivityMain2NewBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ZtlManager.GetInstance().setContext(this);
        hideBottomUIMenu();
        initView();
        activityBinding.backIv.setOnClickListener(view -> onBackPressed());

         getBaseUrls();
        openGPIO();
        serialPort = ((APPAplication) getApplication()).getSerialPortHelper();
        serialPort.setSerialPortReceivedListener(new SerialPortHelper.OnSerialPortReceivedListener() {
            @Override
            public void onSerialPortDataReceived(ComPortData comPortData) {
                try {
                    mHandler.sendMessage(mHandler.obtainMessage(1, comPortData));
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(1, "解析数据失败"));
                }

            }
        });
//        USB = USBTransferUtil.getInstance();
//        USB.init(this);
//        // 数据接收
//        USB.setOnUSBDateReceive(new USBTransferUtil.OnUSBDateReceive() {
//            @Override
//            public void onReceive(String data_str) {
//                if (Integer.parseInt(data_str) >= 100) {
//                    numbers.add(Integer.parseInt(data_str));
//                } else if (data_str.equals("-7")) {
//                    if (numbers.size() > 0) {
//                        inputMoney += numbers.get(numbers.size() - 1);
//                    }
//                }
//
//                LogUtils.i("receive: 收钱" + data_str + "\r\n" + "：总收款：" + inputMoney);
//            }
//
//            @Override
//            public void onNextMoney() {
//
//            }
//        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            // 禁用 AAudio，使用 AudioTrack
            System.setProperty("aaudio.mmap_exclusive.enabled", "false");
        }

    }

    private void getVersionNew() {
        ApiNew11.getInstance().getConfigs(ApiService.DownLoadUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Result<VersionBean>>() {

                    @Override
                    public void onSuccess(Result<VersionBean> feedbackResp) {
//                        if(feedbackResp!=null) {
//                            toNextActivity();
//                        }
                        LogUtils.i("获取到的数据：" + GsonUtils.beanToJSONString(feedbackResp));
                        if (feedbackResp != null && feedbackResp.data != null) {
                            if (feedbackResp.data.getVersion() > getVersionCode(MainActivityNew.this)) {
                                activityBinding.showLoadd.setVisibility(View.VISIBLE);
                                UpdateConfig config = new UpdateConfig();
                                config.setUrl(feedbackResp.data.getDonwload_url());
                                new AppUpdater(MainActivityNew.this, config)
                                        .setUpdateCallback(new AppUpdateCallback() {
                                            @Override
                                            public void onDownloading(boolean isDownloading) {
                                                if (isDownloading) {
                                                    activityBinding.showLoadd.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onStart(String url) {
                                                activityBinding.loadingTvs.setText("正在更新0%…");
                                            }

                                            @Override
                                            public void onProgress(long progress, long total, boolean isChange) {
                                                activityBinding.loadingTvs.setText("正在更新" + ((progress * 100) / total) + "%…");
                                            }

                                            @Override
                                            public void onFinish(File file) {
                                                activityBinding.showLoadd.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                activityBinding.showLoadd.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancel() {
                                                activityBinding.showLoadd.setVisibility(View.GONE);
                                            }
                                        })
                                        .start();

                            } else {
                                Toast.makeText(MainActivityNew.this, "當前已是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                });
    }

    /**
     * 获取版本号
     *
     * @return
     */

    public int getVersionCode(Context context) {

        int version = 0;
        PackageManager packagemanager = context.getPackageManager();

        PackageInfo packinfo = null;
        try {
            packinfo = packagemanager.getPackageInfo(context.getPackageName(), 0);
            version = packinfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return version;
        }

    }

    String[] GPIOName = {"GPIO1_A3", "GPIO1_A2", "GPIO1_A1", "GPIO1_A0"};

    public void openGPIO() {
        for (int i = 0; i < 4; i++) {
            final GpioInfo gpioInfo = new GpioInfo();
            gpios.add(gpioInfo);
            final Gpio gpioobj = new Gpio();
            gpioInfo.gpio = gpioobj;
            String gpio_name = GPIOName[i];
            boolean ss = gpioobj.open(gpio_name);

            if (!ss) {
                Toast.makeText(MainActivityNew.this, "打開失敗", Toast.LENGTH_SHORT).show();
                return;
            }


//            if (gpioInfo.inputThread != null)
//                return;
            gpioInfo.inputThread = new SendThread();
            gpioInfo.inputThread.start();
            gpioInfo.ThreadisSend = true;
//            outputLog(log[i] + "开始检测输入");
//            if (gpioobj.getValue() == -1) {
//                outputLog("该IO口已被删除");
//            }
        }
    }

    String[] log = {"直接開分", "選擇開分", "洗分", "拍拍樂"};

    private class SendThread extends Thread {

        SendThread() {
        }

        @Override
        public void run() {
            while (gpios.get(0).ThreadisSend) {
                for (int i = 0; i < gpios.size(); i++) {
                    final GpioInfo gi = gpios.get(i);
                    // 获取当前 GPIO 电平 (0 或 1)
                    final int value = gi.gpio.getValue();
                    final int index = i;

                    // 只有状态变化时才更新 UI 或打印日志，可以节省性能
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isChecked = (value == 0);
                            if (isChecked) {
                                if (index == 0) {
                                    activityBinding.webview.callHandler("onScore", "直接開分", new OnBridgeCallback() {
                                        @Override
                                        public void onCallBack(String s) {
                                            //處理完回來
                                        }
                                    });
                                } else if (index == 1) {
                                    activityBinding.webview.callHandler("selectScore", "選擇開分", new OnBridgeCallback() {
                                        @Override
                                        public void onCallBack(String s) {
                                            //處理完回來
                                        }
                                    });
                                } else if (index == 2) {
                                    activityBinding.webview.callHandler("onWinScore", "洗分", new OnBridgeCallback() {
                                        @Override
                                        public void onCallBack(String s) {
                                            //處理完回來
                                        }
                                    });
                                } else if (index == 3) {
                                    activityBinding.webview.callHandler("onPaiPaiLe", "拍拍樂", new OnBridgeCallback() {
                                        @Override
                                        public void onCallBack(String s) {
                                            //處理完回來
                                        }
                                    });
                                }


                            }
                            // 如果当前状态与 Switch 状态不一致，则更新
//                            if (gi.sGpioValue.isChecked() != isChecked) {
//                                gi.sGpioValue.setChecked(isChecked);
//                                // 只有在触发（低电平）时打印日志
//                                if (isChecked) {
//                                    outputLog("触发信号: " + log[index]);
//                                }
//                            }
                        }
                    });
                }
                try {
                    Thread.sleep(50); // 每秒检测 20 次，足以捕捉大部分纸币机信号
                } catch (InterruptedException e) {
                    break;
                }
            }

        }
    }

    public void close() {
        for (int i = 0; i < 3; i++) {
            final int gpiovalue = ZtlManager.GetInstance().gpioStringToInt(GPIOName[i]);
            GpioInfo gi = gpios.get(i);
            gi.ThreadisSend = false;
            if (gi.inputThread != null) {
                gi.inputThread.interrupt();
                gi.inputThread = null;
            }
            String root_unexport = "chmode 777 /sys/class/gpio/unexport";
            String delete_Gpio = "echo " + "\"" + gpiovalue + "\"" + ">sys/class/gpio/unexport";
            ZtlManager.GetInstance().execRootCmdSilent(delete_Gpio);
            ZtlManager.GetInstance().execRootCmdSilent(root_unexport);
        }


    }

    private void startWorkMoney() {
//        LogUtils.i("send: 3E" + "\r\n");
//        USB.write("3E");
//        LogUtils.i("send: 18" + "\r\n");
//        USB.write("18");
////        dataBinding.debugTv.append("\r\n纸钞机开始收钱");
        if (!serialPort.isOpen()) {
            serialPort.open();
        }
        serialPort.sendHex("3E");
        serialPort.sendHex("18");
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
        serialPort.sendHex("5E");
//        USB.write("5E");

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
            serialPort.sendHex("02");
//            USB.write("02");
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
       toLoadUrl(MainActivity.getString(MainActivityNew.this, "domain", ApiService.DEFAULT_WEB_URL));
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
        //activityBinding.webview.loadUrl(domain + "?isApp=1");
         activityBinding.webview.loadUrl(ApiService.DEFAULT_WEB_URL,headers);
//        activityBinding.webview.loadUrl("https://slot.kkcnw.cn/",headers);
       // activityBinding.webview.loadUrl("file:///android_asset/index.html");
//        activityBinding.webview.loadUrl(domain + "");

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
//        activityBinding.webview.setBackgroundColor(0); // 设置背景透明
        WebSettings settings = activityBinding.webview.getSettings();
        // 1. 核心：必须开启 JS
        settings.setJavaScriptEnabled(true);

// 2. 关键：Vue Router 和 Vuex 经常使用 LocalStorage
        settings.setDomStorageEnabled(true);

// 3. 关键：解决 Vue 动态加载组件时的路径问题
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

// 4. 解决部分打包后的 JS 资源加载不出来（针对 Prefetch/Preload）
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

// 5. 开启硬件加速渲染动画
        activityBinding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        settings.setJavaScriptEnabled(true);

// 1. 开启 DOM 存储 API (Vue/React 必开)
//        settings.setDomStorageEnabled(true);

// 2. 开启数据库存储 API
//        settings.setDatabaseEnabled(true);

// 3. 允许跨域访问本地资源 (解决 App.js 调用其他 JS 的问题)
//        settings.setAllowFileAccessFromFileURLs(true);
//        settings.setAllowUniversalAccessFromFileURLs(true);

// 4. 确保混合内容模式 (如果 JS 里有 http 请求)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }

       // activityBinding.webview.loadUrl("file:///android_asset/index.html");
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

        activityBinding.webview.setWebChromeClient(webChromeClient);
        activityBinding.webview.setWebViewClient(webViewClient);
        activityBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        activityBinding.webview.getSettings().setDomStorageEnabled(true);
        headers = new HashMap<>();
        headers.put("Accept-Language", "zh-TW,zh;q=0.9"); // 告诉服务器优先返回繁体

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activityBinding.webview.getSettings().setOffscreenPreRaster(false);
        }
        Log.i("XHXDEBUG", "XHXDEBUGURL:::" + url);

//        LogUtils.i("设备信息："+activityBinding.webview.getSettings().getUserAgentString());

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
////        activityBinding.webview.loadUrl(url);


//
        activityBinding.webview.addHandlerLocal("getMachineNo", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
                ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(context));
                LogUtils.d("执行了getMachineNo:" + GsonUtils.beanToJSONString(resultMachineBean));
                callBackFunction.onCallBack(GsonUtils.beanToJSONString(resultMachineBean));
            }
        });
        activityBinding.webview.addHandlerLocal("openGame", new BridgeHandler() {
            @Override
            public void handler(Context context, String s, CallBackFunction callBackFunction) {
//                ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(context));
                LogUtils.d("执行了openGame:" + s);
//                Toast.makeText(MainActivityNew.this,"即将进入游戏",Toast.LENGTH_SHORT).show();
                callBackFunction.onCallBack("");
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clipData = ClipData.newPlainText("label", s);
//                clipboard.setPrimaryClip(clipData);


                openGame(s);


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
                MainActivityNew.this.finish();
            }
        });


    }

    private void openGame(String s) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage("com.timego.gamecontainer");
        intent.putExtra("gameUrl", s);
        try {
            startActivity(intent);
            overridePendingTransition(0, 0);
        } catch (ActivityNotFoundException e) {
            // 如果目标应用未安装，则捕获异常并处理
            activityBinding.showLoadd.setVisibility(View.VISIBLE);
            UpdateConfig config = new UpdateConfig();
            config.setUrl("https://storage.googleapis.com/yjbfile/test/apk/vip-gamecontainer.apk");

            new AppUpdater(MainActivityNew.this, config)
                    .setUpdateCallback(new AppUpdateCallback() {
                        @Override
                        public void onDownloading(boolean isDownloading) {
                            if (isDownloading) {
//                                showToast("已经在下载中,请勿重复下载。");
                                activityBinding.showLoadd.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onStart(String url) {
                            activityBinding.loadingTvs.setText("正在加載遊戲框架0%…");
                        }

                        @Override
                        public void onProgress(long progress, long total, boolean isChange) {
                            activityBinding.loadingTvs.setText("正在加載遊戲框架" + ((progress * 100) / total) + "%…");

                        }

                        @Override
                        public void onFinish(File file) {
                            activityBinding.showLoadd.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            activityBinding.showLoadd.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancel() {
                            activityBinding.showLoadd.setVisibility(View.GONE);
                        }
                    })
                    .start();
        }


    }

    private void collectMoney(String object) {
        LogUtils.d("执行了collectMoney:" + object);
        activityBinding.webview.callHandler("collectMoney", object, s -> {
            LogUtils.i("收到的参数：" + s);
        });
//        activityBinding.webview.evaluateJavascript("collectMoney",object);
    }


    boolean hasSignIn = false;
    boolean isGameOver = false;
    WebViewClient webViewClient = new WebViewClient() {

        @SuppressLint("NewApi")
        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail renderProcessGoneDetail) {
//            webView.clearCache(false);
//            webView.clearHistory();
            LogUtils.i("onRenderProcessGone:" + renderProcessGoneDetail.didCrash() + ";;" + renderProcessGoneDetail.rendererPriorityAtExit());
            isGameOver = true;
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
                        activityBinding.webview.loadUrl(url, headers);
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
                Toast.makeText(MainActivityNew.this, "未知错误", Toast.LENGTH_SHORT).show();
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
                MainActivityNew.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        for (String permisson : request.getResources()) {
                            permissionRequest = request;
                            if (permisson.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                                if (ContextCompat.checkSelfPermission(MainActivityNew.this, Manifest.permission.CAMERA) != 0) {
                                    ActivityCompat.requestPermissions(MainActivityNew.this, PERMISSIONS_CAMERA, 1111);
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
        dimessWebView();
        close();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    private void dimessWebView() {
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
        // USB.connect();  // 当系统监测到usb插入动作后跳转到此页面时
        if (isGameOver) {
            isGameOver = false;
            startActivity(new Intent(this, MainActivityNew.class));
            finish();
            overridePendingTransition(0, 0);
        } else {
            activityBinding.webview.onResume();

        }
        super.onResume();

        changeWebView();

    }

    private void changeWebView() {
        ResultBean result2 = new ResultBean(200, null);
        LogUtils.d("执行了onFinish:" + GsonUtils.beanToJSONString(result2));
        activityBinding.webview.callHandler("updateUserInfo", GsonUtils.beanToJSONString(result2), s -> {
            LogUtils.i("收到的参数：" + s);
        });

//        activityBinding.webview.evaluateJavascript(
//                "window.updateUserInfo()",
//                null
//        );

    }

    @Override
    protected void onPause() {
        activityBinding.webview.onPause();
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
