package com.timego.calculcator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.timego.calculcator.databinding.ActivityH5WebGameBinding;
import com.timego.calculcator.databinding.ActivityMain2Binding;

import java.io.IOException;

public class H5WebGameActivity extends AppCompatActivity {

    private int sx, sy, a, bb, c, d;
    private long betweenTime,down=0;
    private int maxHeight,maxWidth;
    private boolean isLandscape = true;
    private boolean isOut = false;
    private Handler handler = new Handler();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AgentWebFragment fragment;
    ActivityH5WebGameBinding dataBinding;
    public static String url = "https://100pokies.vip/";

    public static  long startTime = 0;

    public static void startH5WebGameActivity(Context context,  String url) {
        Intent intent = new Intent(context, H5WebGameActivity.class);
        intent.putExtra("gameUrl", url);
        context.startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(H5WebGameActivity.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(H5WebGameActivity.this, "windows_color", "#000000")));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        startTime = System.currentTimeMillis();


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
        dataBinding = ActivityH5WebGameBinding.inflate(LayoutInflater.from(this));
        setContentView(dataBinding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        url = getIntent().getStringExtra("gameUrl");
        LogUtils.i("地址是啥："+url);
//        url = MainActivity2.url;
//        url = "http://192.168.110.14:7456/web-mobile/web-mobile/index.html";
        initFragment();
        hideBottomUIMenu();




        dataBinding.backIv.setOnTouchListener((v, event) -> {

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
                    int left = dataBinding.backIv.getLeft() + (int) dx;
                    int top = dataBinding.backIv.getTop() + (int) dy;
                    int right = dataBinding.backIv.getRight() + (int) dx;
                    int bottom = dataBinding.backIv.getBottom() + (int) dy;
                    dataBinding.backIv.layout(left, top, right, bottom);
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

    public void  hideFloat(){
        dataBinding.showTopLy.setVisibility(View.GONE);
    }

    float lastX, lastY;
    float initX, initY;

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT  <=11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT  >19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    public void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new AgentWebFragment(url);
        fragmentTransaction.add(R.id.content_layout, fragment);
        fragmentTransaction.commit();
    }

    public void dimessFloat(){
        dataBinding.showTopLy.setVisibility(View.GONE);
    }

    private void showConfirmTip() {
//        ExitOutDialog exitOutDialog = new ExitOutDialog(this,"确定返回大厅吗");
//        exitOutDialog.setOnToVipListener(new ExitOutDialog.OnToToastListener() {
//            @Override
//            public void checkAudio() {
////                changeBgMusic();
//            }
//        });
//        exitOutDialog.setRightButton(v -> {
////            changeBgMusic();
//            exitOutDialog.dismiss();
//            dataBinding.ivShow0.setVisibility(View.VISIBLE);
//            dataBinding.llShow.setVisibility(View.INVISIBLE);
            onBackPressed();
//        });
//        exitOutDialog.setLeftButton(v -> {
////            changeBgMusic();
//            exitOutDialog.dismiss();
//            dataBinding.ivShow0.setVisibility(View.VISIBLE);
//            dataBinding.llShow.setVisibility(View.INVISIBLE);
//        });
//        exitOutDialog.show();
    }

    public void setMaxWH() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        defaultDisplay.getRealSize(outPoint);
        maxWidth = outPoint.x;
        maxHeight = outPoint.y;
    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_show0: // 打开
//                // 防止拖动完触发这里，体验不好
//                isOut = false;
//                if (betweenTime < 200) {
//                    if (maxHeight - dataBinding.rlCtl.getTop() < dataBinding.rlCtl.getHeight()) {
//                        dataBinding.rlCtl.layout(a,maxHeight - dataBinding.rlCtl.getHeight(),c,maxHeight);
//                        isOut = true;
//                    }
//                    dataBinding.ivShow0.setVisibility(View.INVISIBLE);
//                    dataBinding.llShow.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.iv_show2:     // 刷新
//                fragment.getWebView().reload();
//                break;
//            case R.id.iv_show3:     // 退出
//                showConfirmTip();
//                break;
//            case R.id.iv_show4:     // 旋转
//                setRequestedOrientation(isLandscape ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                isLandscape = !isLandscape;
//                dataBinding.ivShow0.setVisibility(View.VISIBLE);
//                dataBinding.llShow.setVisibility(View.INVISIBLE);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dataBinding.rlCtl.layout(0,0,dataBinding.rlCtl.getWidth(),dataBinding.rlCtl.getHeight());
//                    }
//                },100);
//                break;
//            case R.id.iv_show1:     // 收起
//                dataBinding.ivShow0.setVisibility(View.VISIBLE);
//                dataBinding.llShow.setVisibility(View.INVISIBLE);
//                if (isOut) {
//                    dataBinding.rlCtl.layout(a,bb,c,d);
//                }
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
////                maxWidth = getResources().getDisplayMetrics().widthPixels;
////                maxHeight = getResources().getDisplayMetrics().heightPixels;
//                setMaxWH();
//                sx = (int) event.getRawX();
//                sy = (int) event.getRawY();
//                if (down == 0) {
//                    down = System.currentTimeMillis();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
//                int x = (int) event.getRawX();
//                int y = (int) event.getRawY();
//                // 获取手指移动的距离
//                int dx = x - sx;
//                int dy = y - sy;
//                // 得到imageView最开始的各顶点的坐标
//                int l = dataBinding.rlCtl.getLeft();
//                int r = dataBinding.rlCtl.getRight();
//                int t = dataBinding.rlCtl.getTop();
//                int b = dataBinding.rlCtl.getBottom();
//                a = l + dx;
//                bb = t + dy;
//                c = r + dx;
//                d = b + dy;
//                // 更改imageView在窗体的位置
//                if (a > 0 && bb > 0 && c < maxWidth && bb < (maxHeight-dataBinding.ivShow0.getHeight())) {
//                    dataBinding.rlCtl.layout(a, bb, c, d);
//                }
//
//                // 获取移动后的位置
//                sx = (int) event.getRawX();
//                sy = (int) event.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
//                long up = System.currentTimeMillis();
//                betweenTime = up - down;
//                down = 0;
//                break;
//        }
//        return false;
//    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showConfirmTip();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
//
//    public void hideLoading() {
//        dismissDialog();
//    }
//
//    public void showLoading() {
//        showDialog();
//    }
}