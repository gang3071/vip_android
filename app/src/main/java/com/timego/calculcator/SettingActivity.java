package com.timego.calculcator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.AppUpdateCallback;
import com.timego.calculcator.api.ApiNew11;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.api.BaseObserver;
import com.timego.calculcator.api.Result;
import com.timego.calculcator.databinding.ActivitySettingBinding;
import com.timego.calculcator.databinding.ActivityTestBinding;
import com.timego.calculcator.utils.VolumeUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding activityBinding;
    boolean isRun = false;
    int number = 0;
    VolumeUtil volumeUtil;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(Color.parseColor(MainActivity.getString(SettingActivity.this, "style_color", "#000000")));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(MainActivity.getString(SettingActivity.this, "windows_color", "#000000")));
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
        activityBinding = ActivitySettingBinding.inflate(LayoutInflater.from(this));

        setContentView(activityBinding.getRoot());
        initView();

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

    protected void initView() {
        activityBinding.topV.backButton.setOnClickListener(view -> finish());
        activityBinding.topV.titleTv.setText("設定");
        activityBinding.versionTv.setText("v"+getVersionName(SettingActivity.this)+(ApiService.isProduction?"":"beta"));
        activityBinding.versionLy.setOnClickListener(view -> getVersionNew());

        activityBinding.qrcodeLy.setOnClickListener(view -> startActivity(new Intent(SettingActivity.this,QrCodeActivity.class)));

        volumeUtil  = new VolumeUtil(SettingActivity.this);
        activityBinding.seekbarDuomeiti.setProgress(volumeUtil.getMediaVolume());
        activityBinding.seekbarDuomeiti.setMax(volumeUtil.getMediaMaxVolume());

        activityBinding.seekbarXitong.setProgress(volumeUtil.getAlermVolume());
        activityBinding.seekbarXitong.setMax(volumeUtil.getAlermMaxVolume());
        activityBinding.seekbarDuomeiti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeUtil.setMediaVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        activityBinding.seekbarXitong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeUtil.setAlermVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        hideBottomUIMenu();

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


    private void getVersionNew() {
        ApiNew11.getInstance().getConfigs(ApiService.isProduction?ApiService.DownLoadUrl:ApiService.DownLoadUrl_TEST)
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
                            if(feedbackResp.data.getVersion() > getVersionCode(SettingActivity.this)){
                                activityBinding.showLoadd.setVisibility(View.VISIBLE);
                                UpdateConfig config = new UpdateConfig();
                                config.setUrl( feedbackResp.data.getDonwload_url());
                                new AppUpdater(SettingActivity.this,config)
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
                                Toast.makeText(SettingActivity.this,"當前已是最新版本",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                });
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
