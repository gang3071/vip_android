package com.timego.calculcator;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.timego.calculcator.databinding.ActivityCocosBinding;
import com.timego.calculcator.driver.USBTransferUtil;
import com.timego.calculcator.utils.JavaHexStr;

import java.util.ArrayList;

import top.maybesix.xhlibrary.serialport.ComPortData;
import top.maybesix.xhlibrary.serialport.SerialPortHelper;


public class TestttysActivity extends AppCompatActivity {
    public static TestttysActivity cocosActivity;
    static ActivityCocosBinding activityBinding;

    public static SerialPortHelper serialPort; //数钞机
    public int inputMoney = 0;
    public ArrayList<Integer> numbers = new ArrayList<>();
    private static StringBuilder stringBuilder = new StringBuilder();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                ComPortData data = (ComPortData) msg.obj;
                onDataReceiver(data.getRecData());
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
        stringBuilder.append("\n" + "解析金额指令开始");   //810DFF10  1000回复的指令  8109FE10  500回复的指令    8101FF10  100回复的指令
        activityBinding.txtLog.setText(stringBuilder.toString());
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
            stringBuilder.append("\n" + message);
            activityBinding.txtLog.setText(stringBuilder.toString());
            // 使用 runOnUiThread 切换线程
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityBinding = ActivityCocosBinding.inflate(LayoutInflater.from(this));
        setContentView(activityBinding.getRoot());
        cocosActivity = this;
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

        activityBinding.connectBt.setOnClickListener(v -> {
            if (serialPort != null) {
                Toast.makeText(this, serialPort.isOpen() ? "连接成功，开始使用" : "连接失败", Toast.LENGTH_SHORT).show();
            }
        });

        activityBinding.clean.setOnClickListener(v -> {
            stringBuilder.setLength(0);
            activityBinding.txtLog.setText(stringBuilder.toString());
        });
        activityBinding.shebeimaBt.setOnClickListener(view -> LogUtils.i(getMachineNo()));


        activityBinding.shouqianBt.setOnClickListener(view -> LogUtils.i(startCollectMoney()));


        activityBinding.jieshuBt.setOnClickListener(view -> LogUtils.i(stopCollectMoney()));


        activityBinding.jieshuagainBt.setOnClickListener(view -> LogUtils.i(stopCollectMoneyAgain()));

        activityBinding.shouqiansBt.setOnClickListener(view -> LogUtils.i(collectMoney()));
    }


    public static void startWorkMoney() {
        if (!serialPort.isOpen()) {
            serialPort.open();
        }
        cocosActivity.startWorkMoneyss();
        // cocosActivity.startWorkMoneys();
    }

    public void startWorkMoneyss() {
        serialPort.sendHex("3E");
        serialPort.sendHex("18");
        stringBuilder.append("\n" + "发送指令" + "3E" + "\r\n" + "\n" + "发送指令" + "18" + "\r\n");
        activityBinding.txtLog.setText(stringBuilder.toString());
        if (myCountDownTimerXinTiao != null) {
            myCountDownTimerXinTiao.cancel();
        }
        myCountDownTimerXinTiao = new MyCountDownTimerXinTiao(1000 * 60 * 60 * 24, 2000);
        myCountDownTimerXinTiao.start();
    }
//    public void startWorkMoneys() {
//        LogUtils.i("send: 3E" + "\r\n");
//        USB.write("3E");
//        LogUtils.i("send: 18" + "\r\n");
//        USB.write("18");
//        if (myCountDownTimerXinTiao != null) {
//            myCountDownTimerXinTiao.cancel();
//        }
//        myCountDownTimerXinTiao = new MyCountDownTimerXinTiao(1000 * 60 * 60 * 24, 2000);
//        myCountDownTimerXinTiao.start();
//    }

    public static void stopWorkMoney() {
        if (cocosActivity.myCountDownTimerXinTiao != null) {
            cocosActivity.myCountDownTimerXinTiao.cancel();
        }
        cocosActivity.myCountDownTimerXinTiao = null;
        LogUtils.i("send: 5E" + "\r\n");
//        cocosActivity.USB.write("5E");
        stringBuilder.append("\n" + "发送指令" + "5E" + "\r\n");
        activityBinding.txtLog.setText(stringBuilder.toString());
        serialPort.sendHex("5E");
    }

    public MyCountDownTimerXinTiao myCountDownTimerXinTiao;

    //倒计时函数
    public class MyCountDownTimerXinTiao extends CountDownTimer {

        public MyCountDownTimerXinTiao(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            LogUtils.i("send: 02" + "\r\n");
            stringBuilder.append("\n" + "发送心跳指令" + "02" + "\r\n");
            activityBinding.txtLog.setText(stringBuilder.toString());
            // cocosActivity.USB.write("02");
            serialPort.sendHex("02");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {

        }
    }

    public static String getMachineNo() {
        ResultMachineBean resultMachineBean = new ResultMachineBean(200, GetAndroidUniqueMark.getUniqueId(APPAplication.getAppContext()));
        LogUtils.d("执行了getMachineNo:" + GsonUtils.beanToJSONString(resultMachineBean));
        return GsonUtils.beanToJSONString(resultMachineBean);
    }

    public static String startCollectMoney() {
        MoneyBean moneyBean = new MoneyBean(0, 0);
        ResultBean result = new ResultBean(200, moneyBean);
        LogUtils.d("执行了startCollectMoney:" + GsonUtils.beanToJSONString(result));
        cocosActivity.inputMoney = 0;
        cocosActivity.numbers = new ArrayList<>();
//        callBackFunction.onCallBack(GsonUtils.beanToJSONString(result));
        startWorkMoney();
        return GsonUtils.beanToJSONString(result);
    }

    public static String stopCollectMoney() {
        MoneyBean moneyBean1 = new MoneyBean(1, cocosActivity.inputMoney);
        ResultBean result1 = new ResultBean(200, moneyBean1);
        LogUtils.d("执行了stopCollectMoney:" + GsonUtils.beanToJSONString(result1));
        stopWorkMoney();
        return GsonUtils.beanToJSONString(result1);
    }

    public static String stopCollectMoneyAgain() {
        MoneyBean moneyBean1 = new MoneyBean(1, cocosActivity.inputMoney);
        ResultBean result1 = new ResultBean(200, moneyBean1);
        LogUtils.d("执行了stopCollectMoneyAgain:" + GsonUtils.beanToJSONString(result1));
        return GsonUtils.beanToJSONString(result1);
    }


    public static String collectMoney() {
        return cocosActivity.inputMoney + "";
    }

    public static String onFinish() {
        ResultBean result2 = new ResultBean(200, null);
        LogUtils.d("执行了onFinish:" + GsonUtils.beanToJSONString(result2));
        return GsonUtils.beanToJSONString(result2);
    }


    @Override
    public void onDestroy() {
        stopWorkMoney();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
//            mHandler = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        // USB.connect();  // 当系统监测到usb插入动作后跳转到此页面时
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
