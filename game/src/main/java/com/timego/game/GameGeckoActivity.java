package com.timego.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.timego.game.databinding.ActivityMain2GameGeckoBinding;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;


public class GameGeckoActivity extends AppCompatActivity {

    ActivityMain2GameGeckoBinding activityBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(GameActivity.getString(GameGeckoActivity.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(GameActivity.getString(GameGeckoActivity.this, "windows_color", "#000000")));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        activityBinding = ActivityMain2GameGeckoBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        hideBottomUIMenu();
        initView();
//        activityBinding.backIv.setOnClickListener(view -> onBackPressed());

        getBaseUrls();


    }


    private void getBaseUrls() {
        toLoadUrl(getIntent().getStringExtra("gameUrl"));
    }


    private void toLoadUrl(String domain) {
        //        activityBinding.webview.loadUri(domain);
        try {
            if (session == null) {
                session = new GeckoSession();
            }

            // Workaround for Bug 1758212
            session.setContentDelegate(new GeckoSession.ContentDelegate() {
            });

            session.setProgressDelegate(new GeckoSession.ProgressDelegate() {
                @Override
                public void onProgressChange(@NonNull GeckoSession session, int progress) {
                    GeckoSession.ProgressDelegate.super.onProgressChange(session, progress);
                    LogUtils.i("进度是啥："+progress);
                    if(progress>=99){
                        activityBinding.showTopLy.setVisibility(View.GONE);
//                        LogUtils.i("设备信息22："+session.getSettings().getUserAgentOverride());
//                        LogUtils.i("设备信息22："+GsonUtils.beanToJSONString(session.getUserAgent()));

                    }
                }
            });

            session.setPermissionDelegate(new GeckoSession.PermissionDelegate() {
                @Nullable
                @Override
                public GeckoResult<Integer> onContentPermissionRequest(@NonNull GeckoSession session, @NonNull ContentPermission perm) {
                    return GeckoResult.fromValue(ContentPermission.VALUE_ALLOW);
                }
            });


            if (sRuntime == null) {
                // GeckoRuntime can only be initialized once per process
                sRuntime = GeckoRuntime.create(this);
            }
            session.open(sRuntime);
            activityBinding.webview.setSession(session);
            session.loadUri(domain);
            LogUtils.i("设备信息22："+GsonUtils.beanToJSONString(session.getUserAgent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GeckoRuntime sRuntime;
    public static GeckoSession session;

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
        super.onBackPressed();
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

        activityBinding.backIv.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    initX = event.getRawX();
                    initY = event.getRawY();
                    LogUtils.d("touchevent", "lastX=" + lastX + "  lastY" + lastY);
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
    }

    float lastX, lastY;
    float initX, initY;


    @Override
    public void onDestroy() {
        if(session!=null){
            session.close();
            session = null;
        }
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
