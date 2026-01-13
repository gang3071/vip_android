package com.timego.game.api;


import com.google.gson.JsonParseException;
import com.timego.game.APPAplication;
import com.timego.game.R;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<T> {
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;

    @Override
    public void onNext(T o) {
        try {
            Result model = (Result) o;
            if (model.code == 200) {
                onSuccess(o);
            } else {
                //退出登录
                onError(model.code, model.msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(407, e.toString());

        }

    }


    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            //LogUtils.i("红包解析错误1="+(e instanceof JsonParseException)+"*2="+(e instanceof JSONException)+"*3="+(e instanceof ParseException));
            onException(PARSE_ERROR);
        } else if (e instanceof SocketTimeoutException) {
            onException(CONNECT_TIMEOUT);
        } else {
//            if (e != null) {
            onError(409, e.toString());
//            } else {
//                onError(407, SophixStubApplication.instance().getString(R.string.unknown_error_txt));
//            }
        }

    }
    public static String strDecode(Object str) {
        // 检查输入是否为字符串
        if (!(str instanceof String)) {
            return str.toString();
        }

        String input = (String) str;
        StringBuilder deStr = new StringBuilder();
        String[] stringArr = input.split("\\|");

        if (stringArr.length > 2) {
            String temp0 = stringArr[0];
            String temp1 = stringArr[1];
            String temp2 = stringArr[2];

            // 检查是否为数字且长度不超过3
            if (!isNumeric(temp0) || temp0.length() > 3) {
                return input;
            }
            if (!isNumeric(temp1) || temp1.length() > 3) {
                return input;
            }
            if (!isNumeric(temp2) || temp2.length() > 3) {
                return input;
            }
        } else {
            return input; // 没有截取到
        }

        int tempi = 1;

        for (String charStr : stringArr) {
            int tempn = tempi % 100;
            int aa = Integer.parseInt(charStr) - tempn;
            char ch1 = (char) aa;
            deStr.append(ch1);
            tempi++;
        }

        return deStr.length() != 0 ? deStr.toString() : input;
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }




    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError(CONNECT_ERROR, APPAplication.AppContext.getString(R.string.connection_error_txt));
                break;

            case CONNECT_TIMEOUT:
                onError(CONNECT_TIMEOUT, APPAplication.AppContext.getString(R.string.connection_timed_out_txt));
                break;

            case BAD_NETWORK:
                onError(BAD_NETWORK, APPAplication.AppContext.getString(R.string.network_problems_txt));
                break;

            case PARSE_ERROR:
                onError(PARSE_ERROR, APPAplication.AppContext.getString(R.string.unknown_error_txt));
                break;

            default:
                onError(409, APPAplication.AppContext.getString(R.string.unknown_error_txt));
                break;

        }

    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T o);

    public abstract void onError(int code, String msg);

}
