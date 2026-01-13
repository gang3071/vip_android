package com.timego.calculcator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.king.zxing.util.CodeUtils;

import java.math.BigDecimal;

public class QrCodeActivity extends AppCompatActivity {
    private LinearLayout backButton;
    private ImageView donghuaIv;
    private AppCompatTextView jiqimaTv;

    private String startSch = "yijibang://qr_code/";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getColor(R.color.color_101431));
        getWindow().getDecorView().setBackgroundColor(getColor(R.color.color_323563));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_qrcode);
        hideBottomUIMenu();
        backButton = (LinearLayout) findViewById(R.id.back_button);
        donghuaIv = (ImageView) findViewById(R.id.donghua_iv);
        jiqimaTv = (AppCompatTextView) findViewById(R.id.jiqima_tv);
        Bitmap bitmap = CodeUtils.createQRCode(startSch+GetAndroidUniqueMark.getUniqueId(QrCodeActivity.this),300, null);
        if (bitmap != null) {
            donghuaIv.setImageBitmap(bitmap);
        }
        jiqimaTv.setText("机器码："+GetAndroidUniqueMark.getUniqueId(QrCodeActivity.this));
        backButton.setOnClickListener(v -> finish());
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}