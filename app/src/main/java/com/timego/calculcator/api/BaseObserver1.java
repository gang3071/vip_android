package com.timego.calculcator.api;


import com.timego.calculcator.R;
import com.google.gson.JsonParseException;
import com.timego.calculcator.APPAplication;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver1<String> extends DisposableObserver<String> {
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
    public void onNext(String o) {
        try {
            onSuccess(o);
        } catch (Exception e) {
            e.printStackTrace();
            onException(CONNECT_TIMEOUT);

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
            onException(CONNECT_TIMEOUT);
//            } else {
//                onError(407, SophixStubApplication.instance().getString(R.string.unknown_error_txt));
//            }
        }

    }





    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError(CONNECT_ERROR, (String) APPAplication.AppContext.getString(R.string.connection_error_txt));
                break;

            case CONNECT_TIMEOUT:
                onError(CONNECT_TIMEOUT, (String)APPAplication.AppContext.getString(R.string.connection_timed_out_txt));
                break;

            case BAD_NETWORK:
                onError(BAD_NETWORK,(String) APPAplication.AppContext.getString(R.string.network_problems_txt));
                break;

            case PARSE_ERROR:
                onError(PARSE_ERROR,(String) APPAplication.AppContext.getString(R.string.unknown_error_txt));
                break;

            default:
                onError(409, (String)APPAplication.AppContext.getString(R.string.unknown_error_txt));
                break;

        }

    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(String o);

    public abstract void onError(int code, String msg);

}
