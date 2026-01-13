package com.timego.calculcator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.databinding.ActivityMain2Binding;
import com.timego.calculcator.databinding.ActivityTestBinding;
import com.timego.calculcator.driver.USBTransferUtil;

import java.security.Key;
import java.util.ArrayList;


public class Test extends AppCompatActivity {

    ActivityTestBinding activityBinding;
    boolean isRun = false;
    int number = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(Test.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(Test.this, "windows_color", "#000000")));
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
        activityBinding = ActivityTestBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
//        hideBottomUIMenu();
        initView();

    }


    private void startWorkMoney() {
        isRun = true;
        number = 0;
//        activityBinding.textnumber.setText(number + "");
//
//        activityBinding.textnumber.setFocusable(true);
//        activityBinding.startButton.setFocusable(false);
//        activityBinding.endButton.setFocusable(false);

    }


    private void stopWorkMoney() {
        isRun = false;
//        activityBinding.textnumber.setFocusable(false);
//        activityBinding.startButton.setFocusable(true);
//        activityBinding.endButton.setFocusable(false);

    }

    @SuppressLint({"NewApi", "WrongConstant"})
    protected void initView() {


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
