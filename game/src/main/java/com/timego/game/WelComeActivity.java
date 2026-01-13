package com.timego.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
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

import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.AppUpdateCallback;
import com.timego.game.api.ApiNew11;
import com.timego.game.api.ApiService;
import com.timego.game.api.BaseObserver;
import com.timego.game.api.Result;
import com.timego.game.api.VersionBean;
import com.timego.game.databinding.ActivityMain2GameBinding;
import com.timego.game.databinding.ActivityWelcomeBinding;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class WelComeActivity extends AppCompatActivity {

    ActivityWelcomeBinding activityBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("结果是啥", "结果是啥：11111");

        getWindow().setNavigationBarColor(Color.parseColor(getString(WelComeActivity.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(getString(WelComeActivity.this, "windows_color", "#000000")));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        activityBinding = ActivityWelcomeBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        hideBottomUIMenu();
        initView();
        isFullGame();

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


    private void isFullGame() {
        String baseUrl = getIntent().getStringExtra("gameUrl");
        LogUtils.i("地址是啥：" + baseUrl);
        if (TextUtils.isEmpty(baseUrl)) {
            activityBinding.progressCircular.setVisibility(View.INVISIBLE);
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", baseUrl);
        clipboard.setPrimaryClip(clipData);

        if(baseUrl.endsWith("&display_mode=1")){
            Intent intent = new Intent(WelComeActivity.this,GameLandActivity.class);
            intent.putExtra("gameUrl",baseUrl.replace("&display_mode=1",""));
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);

        }else{
            Intent intent = new Intent(WelComeActivity.this, GameActivity.class);
            if(baseUrl.contains("loginType=2")) {
                 intent = new Intent(WelComeActivity.this, GameGeckoActivity.class);
            }else if(baseUrl.contains("godeebxp")){
                intent = new Intent(WelComeActivity.this, GameChromeActivity.class);
            }
            intent.putExtra("gameUrl",baseUrl.replace("&display_mode=2","").replace("&display_mode=3",""));
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

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
                        LogUtils.i("获取到的数据："+GsonUtils.beanToJSONString(feedbackResp));
                        if (feedbackResp!=null&&feedbackResp.data!=null) {
                            if(feedbackResp.data.getVersion() > getVersionCode(WelComeActivity.this)){
                                activityBinding.showLoadd.setVisibility(View.VISIBLE);
                                UpdateConfig config = new UpdateConfig();
                                config.setUrl( feedbackResp.data.getDonwload_url());
                                new AppUpdater(WelComeActivity.this,config)
                                        .setUpdateCallback(new AppUpdateCallback() {
                                            @Override
                                            public void onDownloading(boolean isDownloading) {
                                                if(isDownloading){
                                                    activityBinding.showLoadd.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onStart(String url) {
                                                activityBinding.loadingTvs.setText("正在更新0%…");
                                            }

                                            @Override
                                            public void onProgress(long progress, long total, boolean isChange) {
                                                activityBinding.loadingTvs.setText("正在更新"+((progress*100)/total)+"%…");
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

                            }else{
                                Toast.makeText(WelComeActivity.this,"當前已是最新版本",Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    float lastX, lastY;
    float initX, initY;

    @SuppressLint({"NewApi", "WrongConstant"})
    protected void initView() {


        activityBinding.banbenhaoTv.setText("v"+getVersionName(WelComeActivity.this));

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

        activityBinding.showTopV.setOnClickListener(view -> getVersionNew());

    }
    /**
     * 获取版本号
     *
     * @return
     */

    public String getVersionName(Context context) {

        String version = "1.0.0";
        PackageManager packagemanager = context.getPackageManager();

        PackageInfo packinfo = null;
        try {
            packinfo = packagemanager.getPackageInfo(context.getPackageName(), 0);
            version = packinfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return version;
        }

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
        if (endX - startX > 200 || endY - startY > 200 || endX - startX < -200 || endY - startY < -200) {
            Log.i("焦点", "焦点MOVE：" + endX + ";" + endY);

            final MotionEvent upEvent = MotionEvent.obtain(moveTime, moveTime, MotionEvent.ACTION_MOVE, endX, endY, 0);
            view.onTouchEvent(upEvent);
            upEvent.recycle();
        }
        moveTime += 1000;
        Log.i("焦点", "焦点ACTION_UP：" + endX + ";" + endY);
        final MotionEvent upEvent = MotionEvent.obtain(moveTime, moveTime, MotionEvent.ACTION_UP, endX, endY, 0);
        view.onTouchEvent(upEvent);
        upEvent.recycle();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
