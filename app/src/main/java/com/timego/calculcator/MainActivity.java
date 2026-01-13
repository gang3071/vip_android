package com.timego.calculcator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.updater.AppUpdater;
import com.timego.calculcator.api.ApiNew11;
import com.timego.calculcator.api.ApiService;
import com.timego.calculcator.api.BaseObserver;
import com.timego.calculcator.api.BaseObserver1;
import com.timego.calculcator.api.Result;
import com.timego.calculcator.databinding.ActivityMainBinding;
import com.timego.calculcator.webview.Setting;

import org.w3c.dom.Text;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding activityMainBinding;
    private StringBuilder currentInput=new StringBuilder("");
    private BigDecimal currentAnswer=new BigDecimal(0);
    private boolean hasCount=false;
//    private TextView inputTextView,outputTextView;
//    private Button activityMainBinding.btn0,activityMainBinding.btn1,activityMainBinding.btn2,activityMainBinding.btn3,activityMainBinding.btn4,activityMainBinding.btn5,activityMainBinding.btn6,activityMainBinding.btn7,activityMainBinding.btn8,activityMainBinding.btn9,
//            activityMainBinding.btnpoint,activityMainBinding.btnequal,activityMainBinding.btnadd,activityMainBinding.btnsubtract,activityMainBinding.btnmultiply,activityMainBinding.btndivide,activityMainBinding.btnpercent,activityMainBinding.btnbackspace,activityMainBinding.btnclear;


    @SuppressLint("NewApi")
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
        activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(activityMainBinding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hideBottomUIMenu();
        if(TextUtils.isEmpty(getString(MainActivity.this,"loc_pass","51797"))){
            startActivity(new Intent(this,StartActivity.class));
            finish();
            return;
        }


        setListener();

        getConf();
    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getConf();
//
//    }

    public void setListener(){
//        case R.id.v_button:
//        getVersionNew();
//        break;
//        case R.id.btn_0:
//        addInput("0");
//        break;
//        case R.id.btn_1:
//        addInput("1");
//        break;
//        case R.id.btn_2:
//        addInput("2");
//        break;
//        case R.id.btn_3:
//        addInput("3");
//        break;
//        case R.id.btn_4:
//        addInput("4");
//        break;
//        case R.id.btn_5:
//        addInput("5");
//        break;
//        case R.id.btn_6:
//        addInput("6");
//        break;
//        case R.id.btn_7:
//        addInput("7");
//        break;
//        case R.id.btn_8:
//        addInput("8");
//        break;
//        case R.id.btn_9:
//        addInput("9");
//        break;
//        case R.id.btn_point:
//        addInput(".");
//        break;
//        case R.id.btn_add:
//        addInput("+");
//        break;
//        case R.id.btn_subtract:
//        addInput("-");
//        break;
//        case R.id.btn_multiply:
//        addInput("*");
//        break;
//        case R.id.btn_divide:
//        addInput("/");
//        break;
//        case R.id.btn_backspace:
//        if(currentInput.length()>0){
//            currentInput.deleteCharAt(currentInput.length()-1);
//        }
//        displayInput();
//        break;
//        case R.id.btn_clear:
//        currentInput=new StringBuilder("");
//        displayInput();
//        activityMainBinding.outputText.setText("");
//        break;
//        case R.id.btn_equal:
//        if(TextUtils.isEmpty(activityMainBinding.inputText.getText().toString().trim())){
//            return;
//        }
//        StringBuilder result = compute(currentInput);
//        displayAnswer(result);
//        hasCount=true;
//        break;
        activityMainBinding.btn0.setOnClickListener(view -> addInput("0"));
        activityMainBinding.btn1.setOnClickListener(view -> addInput("1"));
        activityMainBinding.btn2.setOnClickListener(view -> addInput("2"));
        activityMainBinding.btn3.setOnClickListener(view -> addInput("3"));
        activityMainBinding.btn4.setOnClickListener(view -> addInput("4"));
        activityMainBinding.btn5.setOnClickListener(view -> addInput("5"));
        activityMainBinding.btn6.setOnClickListener(view -> addInput("6"));
        activityMainBinding.btn7.setOnClickListener(view -> addInput("7"));
        activityMainBinding.btn8.setOnClickListener(view -> addInput("8"));
        activityMainBinding.btn9.setOnClickListener(view -> addInput("9"));
        activityMainBinding.btnPoint.setOnClickListener(view -> addInput("."));
        activityMainBinding.btnEqual.setOnClickListener(view -> {
            if(TextUtils.isEmpty(activityMainBinding.inputText.getText().toString().trim())){
                return;
            }
            StringBuilder result = compute(currentInput);
            displayAnswer(result);
            hasCount=true;
        });
        activityMainBinding.btnAdd.setOnClickListener(view -> addInput("+"));
        activityMainBinding.btnSubtract.setOnClickListener(view -> addInput("-"));
        activityMainBinding.btnMultiply.setOnClickListener(view -> addInput("*"));
        activityMainBinding.btnDivide.setOnClickListener(view -> addInput("/"));
        activityMainBinding.btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentInput.length()>0){
                    currentInput.deleteCharAt(currentInput.length()-1);
                }
                displayInput();
            }
        });
        activityMainBinding.btnClear.setOnClickListener(view -> {
            currentInput=new StringBuilder("");
            displayInput();
            activityMainBinding.outputText.setText("");
        });
        activityMainBinding.vButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

//
//        ActivityManager activityManager = (ActivityManager) MainActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
//        LogUtils.i("内存大小："+activityManager.getMemoryClass());
//        LogUtils.i("内存大小1："+activityManager.getLargeMemoryClass());
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


    Handler handler = new Handler();
    public void displayInput(){
        activityMainBinding.inputText.setText(currentInput);
        if(currentInput.toString().equals(MainActivity.getString(MainActivity.this,"loc_pass","51797"))){
            toNextActivity();
        }else if(currentInput.toString().equals("888*999")){
            handler.postDelayed(() -> {
                currentInput=new StringBuilder("");
                activityMainBinding.inputText.setText("");
                startActivity(new Intent(MainActivity.this,QrCodeActivity.class));
//            finish();
            }, 500);
        }else if(currentInput.toString().equals("888888")){
            startActivity(new Intent(MainActivity.this,Test.class));

        }
    }


    private void getConf(){
        ApiNew11.getInstance().getChannelInfo(ApiService.Site_Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Result<PassBean>>() {

                    @Override
                    public void onSuccess(Result<PassBean> feedbackResp) {
                        if(feedbackResp!=null) {
                            saveString(MainActivity.this, "loc_pass", feedbackResp.data.getPassword());
                            saveString(MainActivity.this, "domain", feedbackResp.data.getDomain());
//                            saveString(MainActivity.this,"domain","http://192.168.0.58:7456/web-mobile/web-mobile-vip/index.html");
//                            saveString(MainActivity.this, "domain","http://192.168.0.58:7456/web-mobile/web-mobile-vip/index.html");
//                            saveString(MainActivity.this, "domain","https://store.qtalk666.top/");
//                            https://play.godeebxp.com/egames/98673eb698edc44116055395f776c7eb3f6a247b/game/?t=23c082ddb9594763965bd449d1a1a1fd&gn=egyptian-mythology&l=zh-tw&ct=slot&gt=slot-erase-any-times-1&socket_url=socket.godeebxp.com&ts=1754647257546&view_mode=portrait&wv=undefined&gv=250722&uniwebview=1
//                            "lobby_url": "https:\/\/wmtc.m8810.com\/?sid=41839415G8A50014A168M71346&ui=7&lang=tw&voice=cn&co=wm"
//                            saveString(MainActivity.this, "domain","https://play.godeebxp.com/egames/98673eb698edc44116055395f776c7eb3f6a247b/game/?t=23c082ddb9594763965bd449d1a1a1fd&gn=egyptian-mythology&l=zh-tw&ct=slot&gt=slot-erase-any-times-1&socket_url=socket.godeebxp.com&ts=1754647257546&view_mode=portrait&wv=undefined&gv=250722");
//                            saveString(MainActivity.this, "domain", "https://play.godeebxp.com/egames/c52fc36b276b9fc2f5c2d60dcbc1c7525edee61e/game/?t=d46403e70b0248a090a653d257b868a6&gn=scarlet-three-kingdoms&l=zh-tw&ct=slot&gt=slot-erase-any-times-1&socket_url=socket.godeebxp.com&ts=1754880294687&view_mode=portrait&wv=undefined&gv=250807&uniwebview=1");
//                            toNextActivity();
                        }

                        // {"code":200,"msg":"success","data":{"domain":"https:\/\/vip.wlokq.com","password":"17092"}}
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                });
    }

    private void toNextActivity() {
//        changeIcon(true);
        handler.postDelayed(() -> {
            currentInput=new StringBuilder("");
            activityMainBinding.inputText.setText("");
//            startActivity(new Intent(MainActivity.this,MainActivity2.class));
            startActivity(new Intent(MainActivity.this,MainActivityNew.class));
            overridePendingTransition(0, 0);
//            finish();
//            H5WebGameActivity.startH5WebGameActivity(MainActivity.this, getString(R.string.app_name), "");

//            finish();

//
//            Intent intent = new Intent(MainActivity.this, CocosActivity.class);
//            startActivity(intent);
//

        }, 200);
    }

    /**
     * 修改图标和名称的方法
     *
     * @param enable
     */
    public void changeIcon(boolean enable) {
        PackageManager pm = getApplicationContext().getPackageManager();

        if (enable) {
            //显示Test图标
            pm.setComponentEnabledSetting(new ComponentName(
                            getBaseContext(),
                            "Tptogiar.calculcator.MainActivityNew"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            //去掉HomeActivity图标
            pm.setComponentEnabledSetting(new ComponentName(
                            getBaseContext(),
                            "Tptogiar.calculcator.MainActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            Log.d("TAG", "换Test的图标");
        } else {
            //去掉HomeActivity图标
            pm.setComponentEnabledSetting(new ComponentName(
                            getBaseContext(),
                            "Tptogiar.calculcator.MainActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            //显示Test图标
            pm.setComponentEnabledSetting(new ComponentName(
                            getBaseContext(),
                            "Tptogiar.calculcator.Default"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);


            Log.d("TAG", "换Test的图标");
        }
    }


    public void displayAnswer(StringBuilder string){
        Pattern compile = Pattern.compile( "[^0-9.-]");
        StringBuilder result = new StringBuilder(compile.matcher(string).replaceAll(""));
        if(result.charAt(result.length()-1)=='-'){
            System.out.println(result.charAt(result.length()-1));
            result.deleteCharAt(result.length()-1);
        }
        System.out.println(result);
        activityMainBinding.outputText.setText(result);
    }

    public StringBuilder compute(StringBuilder str){
        Pattern pattern = Pattern.compile("([\\d.]+)\\s*([*/])\\s*([\\d.]+)");
        Matcher matcher=pattern.matcher(str.toString());
        while(matcher.find()){
            BigDecimal first = BigDecimal.valueOf(Double.valueOf(matcher.group(1)));
            BigDecimal second = BigDecimal.valueOf(Double.valueOf(matcher.group(3)));
            switch (matcher.group(2)){
                case "*":
                    first=first.multiply(second);
                    break;
                case "/":
                    first=first.divide(second);
                    break;
            }
            str.replace(matcher.start(),matcher.end(),first.toString());
            matcher.reset(str.toString());
        }

        pattern = Pattern.compile("([\\d.]+)\\s*([+-])\\s*([\\d.]+)");
        matcher=pattern.matcher(str.toString());
        while (matcher.find()){
            BigDecimal first = BigDecimal.valueOf(Double.valueOf(matcher.group(1)));
            BigDecimal second = BigDecimal.valueOf(Double.valueOf(matcher.group(3)));
            switch(matcher.group(2)){
                case "+":
                    first=first.add(second);
                    break;
                case "-":
                    first=first.subtract(second);
                    break;

            }
            str.replace(matcher.start(),matcher.end(),first.toString());
            matcher.reset(str.toString());
        }
        return str;
    }
    public void addInput(String string){
        if(hasCount==false){
            currentInput.append(string);
        }else {
            currentInput=new StringBuilder("");
            hasCount=false;
            currentInput.append(string);
        }
        displayInput();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.v_button:
//                getVersionNew();
//                break;
//            case R.id.btn_0:
//                addInput("0");
//                break;
//            case R.id.btn_1:
//                addInput("1");
//                break;
//            case R.id.btn_2:
//                addInput("2");
//                break;
//            case R.id.btn_3:
//                addInput("3");
//                break;
//            case R.id.btn_4:
//                addInput("4");
//                break;
//            case R.id.btn_5:
//                addInput("5");
//                break;
//            case R.id.btn_6:
//                addInput("6");
//                break;
//            case R.id.btn_7:
//                addInput("7");
//                break;
//            case R.id.btn_8:
//                addInput("8");
//                break;
//            case R.id.btn_9:
//                addInput("9");
//                break;
//            case R.id.btn_point:
//                addInput(".");
//                break;
//            case R.id.btn_add:
//                addInput("+");
//                break;
//            case R.id.btn_subtract:
//                addInput("-");
//                break;
//            case R.id.btn_multiply:
//                addInput("*");
//                break;
//            case R.id.btn_divide:
//                addInput("/");
//                break;
//            case R.id.btn_backspace:
//                if(currentInput.length()>0){
//                    currentInput.deleteCharAt(currentInput.length()-1);
//                }
//                displayInput();
//                break;
//            case R.id.btn_clear:
//                currentInput=new StringBuilder("");
//                displayInput();
//                activityMainBinding.outputText.setText("");
//                break;
//            case R.id.btn_equal:
//                if(TextUtils.isEmpty(activityMainBinding.inputText.getText().toString().trim())){
//                    return;
//                }
//                StringBuilder result = compute(currentInput);
//                displayAnswer(result);
//                hasCount=true;
//                break;
        }
    }



    public static void saveString(Context context,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context,String key, String defValue) {
        if (context == null) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }


    public static void saveBoolean(Context context,String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context,String key, Boolean defValue) {
        if (context == null) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences("InitApp", Activity.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

}