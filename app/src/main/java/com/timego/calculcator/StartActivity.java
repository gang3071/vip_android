package com.timego.calculcator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.timego.calculcator.databinding.ActivityMainBinding;
import com.timego.calculcator.databinding.ActivityStartBinding;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {


    private StringBuilder currentInput = new StringBuilder("");
    private BigDecimal currentAnswer = new BigDecimal(0);
    private boolean hasCount = false;
//    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_equal;
//    private TextView password1;
//    private TextView password2;
//    private TextView password3;
//    private TextView password4;
//    private TextView password5;
//    private TextView password6;
    ActivityStartBinding activityStartBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getColor(R.color.white));
        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        activityStartBinding = ActivityStartBinding.inflate(LayoutInflater.from(this));
        setContentView(activityStartBinding.getRoot());

        getNetUrl();
        setListener();

    }

    public void setListener() {




        activityStartBinding.btn0.setOnClickListener(view -> addInput("0",0));
        activityStartBinding.btn1.setOnClickListener(view -> addInput("1",0));
        activityStartBinding.btn2.setOnClickListener(view -> addInput("2",0));
        activityStartBinding.btn3.setOnClickListener(view -> addInput("3",0));
        activityStartBinding.btn4.setOnClickListener(view -> addInput("4",0));
        activityStartBinding.btn5.setOnClickListener(view -> addInput("5",0));
        activityStartBinding.btn6.setOnClickListener(view -> addInput("6",0));
        activityStartBinding.btn7.setOnClickListener(view -> addInput("7",0));
        activityStartBinding.btn8.setOnClickListener(view -> addInput("8",0));
        activityStartBinding.btn9.setOnClickListener(view -> addInput("9",0));
        activityStartBinding.btnEqual.setOnClickListener(view -> deleteInput());
    }

    String password = "";

    /**
     * 0 正常增加 1 删除 2 更新
     *
     * @param string
     * @param type
     */
    public void addInput(String string, int type) {
        if (type == 0) {
            if (password.length() < 6) {
                password += string;
            }
        } else if (type == 1) {
            password = password.substring(0, password.length() - 1);
        }
        if (password.length() == 6) {
            activityStartBinding.password6.setText(password.charAt(5) + "");
            toNext();
        } else {
            activityStartBinding.password6.setText("");
            if (password.length() == 5) {
                activityStartBinding.password5.setText(password.charAt(4) + "");
            } else {
                activityStartBinding.password5.setText("");
                if (password.length() == 4) {
                    activityStartBinding.password4.setText(password.charAt(3) + "");
                } else {
                    activityStartBinding.password4.setText("");
                    if (password.length() == 3) {
                        activityStartBinding.password3.setText(password.charAt(2) + "");
                    } else {
                        activityStartBinding.password3.setText("");
                        if (password.length() == 2) {
                            activityStartBinding. password2.setText(password.charAt(1) + "");
                        } else {
                            activityStartBinding.password2.setText("");
                            if (password.length() == 1) {
                                activityStartBinding.password1.setText(password.charAt(0) + "");
                            } else {
                                activityStartBinding.password1.setText("");
                            }
                        }
                    }
                }
            }
        }
    }

    public void getNetUrl(){
//        Api.getInstance().geUrl(System.currentTimeMillis())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<Result>() {
//                    @Override
//                    public void onSuccess(Result o) {
//                        LogUtils.i("获取的文件地址："+o.url);
//                        if(!TextUtils.isEmpty(o.url)){
//                            MainActivity.saveString(StartActivity.this,"base_url",o.url);
//                        }
//                    }
//
//                    @Override
//                    public void onError(int code, String msg) {
//
//                    }
//                });
    }

    Handler handler = new Handler();

    private void toNext() {
        MainActivity.saveString(StartActivity.this, "loc_pass", password);
        Intent intent = new Intent(StartActivity.this, MainActivity2.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        H5WebGameActivity.startH5WebGameActivity(StartActivity.this, getString(R.string.app_name), "");

        finish();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_0:
//                addInput("0", 0);
//                break;
//            case R.id.btn_1:
//                addInput("1", 0);
//                break;
//            case R.id.btn_2:
//                addInput("2", 0);
//                break;
//            case R.id.btn_3:
//                addInput("3", 0);
//                break;
//            case R.id.btn_4:
//                addInput("4", 0);
//                break;
//            case R.id.btn_5:
//                addInput("5", 0);
//                break;
//            case R.id.btn_6:
//                addInput("6", 0);
//                break;
//            case R.id.btn_7:
//                addInput("7", 0);
//                break;
//            case R.id.btn_8:
//                addInput("8", 0);
//                break;
//            case R.id.btn_9:
//                addInput("9", 0);
//                break;
//
//            case R.id.btn_equal:
//                deleteInput();
//                break;
        }
    }

    private void deleteInput() {
        if (TextUtils.isEmpty(password)) {
            addInput("", 2);
        } else {
            addInput(password, 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}