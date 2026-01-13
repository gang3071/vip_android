package com.timego.calculcator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.databinding.ActivityCocosBinding;
import com.timego.calculcator.databinding.ActivityMain2NewBinding;
import com.timego.calculcator.driver.USBTransferUtil;

import java.util.ArrayList;


public class CocosActivity extends AppCompatActivity {
    public static CocosActivity cocosActivity;
    ActivityCocosBinding activityBinding;
    public  USBTransferUtil USB;
    public  int inputMoney = 0;
    public  ArrayList<Integer> numbers = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityBinding = ActivityCocosBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        cocosActivity = this;
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

                LogUtils.i("receive: 收钱" + data_str + "\r\n" + "：总收款：" + inputMoney);
            }

            @Override
            public void onNextMoney() {

            }
        });


        activityBinding.shebeimaBt.setOnClickListener(view -> LogUtils.i(getMachineNo()));


        activityBinding.shouqianBt.setOnClickListener(view -> LogUtils.i(startCollectMoney()));


        activityBinding.jieshuBt.setOnClickListener(view -> LogUtils.i(stopCollectMoney()));


        activityBinding.jieshuagainBt.setOnClickListener(view -> LogUtils.i(stopCollectMoneyAgain()));

        activityBinding.shouqiansBt.setOnClickListener(view -> LogUtils.i(collectMoney()));
    }


    public static void startWorkMoney() {
        cocosActivity.startWorkMoneys();
    }

    public  void startWorkMoneys() {
        LogUtils.i("send: 3E" + "\r\n");
        USB.write("3E");
        LogUtils.i("send: 18" + "\r\n");
        USB.write("18");
        if (myCountDownTimerXinTiao != null) {
            myCountDownTimerXinTiao.cancel();
        }
        myCountDownTimerXinTiao = new MyCountDownTimerXinTiao(1000 * 60 * 60 * 24, 2000);
        myCountDownTimerXinTiao.start();
    }

    public static void stopWorkMoney() {
        if (cocosActivity.myCountDownTimerXinTiao != null) {
            cocosActivity.myCountDownTimerXinTiao.cancel();
        }
        cocosActivity. myCountDownTimerXinTiao = null;
        LogUtils.i("send: 5E" + "\r\n");
       cocosActivity.USB.write("5E");

    }

    public   MyCountDownTimerXinTiao myCountDownTimerXinTiao;

    //倒计时函数
    public  class MyCountDownTimerXinTiao extends CountDownTimer {

        public  MyCountDownTimerXinTiao(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            LogUtils.i("send: 02" + "\r\n");
            cocosActivity.USB.write("02");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {

        }
    }

    public static String getMachineNo(){
        ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(APPAplication.getAppContext()));
        LogUtils.d("执行了getMachineNo:" + GsonUtils.beanToJSONString(resultMachineBean));
        return GsonUtils.beanToJSONString(resultMachineBean);
    }

    public static String startCollectMoney(){
        MoneyBean moneyBean = new MoneyBean(0, 0);
        ResultBean result = new ResultBean(200, moneyBean);
        LogUtils.d("执行了startCollectMoney:" + GsonUtils.beanToJSONString(result));
        cocosActivity.inputMoney = 0;
        cocosActivity.numbers = new ArrayList<>();
//        callBackFunction.onCallBack(GsonUtils.beanToJSONString(result));
        startWorkMoney();
        return  GsonUtils.beanToJSONString(result);
    }

    public static String stopCollectMoney(){
        MoneyBean moneyBean1 = new MoneyBean(1,  cocosActivity.inputMoney);
        ResultBean result1 = new ResultBean(200, moneyBean1);
        LogUtils.d("执行了stopCollectMoney:" + GsonUtils.beanToJSONString(result1));
        stopWorkMoney();
        return GsonUtils.beanToJSONString(result1);
    }

    public static String stopCollectMoneyAgain(){
        MoneyBean moneyBean1 = new MoneyBean(1,  cocosActivity.inputMoney);
        ResultBean result1 = new ResultBean(200, moneyBean1);
        LogUtils.d("执行了stopCollectMoneyAgain:" + GsonUtils.beanToJSONString(result1));
        return GsonUtils.beanToJSONString(result1);
    }


    public static String collectMoney(){
        return  cocosActivity.inputMoney+"";
    }

    public static String onFinish(){
        ResultBean result2 = new ResultBean(200, null);
        LogUtils.d("执行了onFinish:" + GsonUtils.beanToJSONString(result2));
        return GsonUtils.beanToJSONString(result2);
    }


    @Override
    public void onDestroy() {
        stopWorkMoney();
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



}
