package com.timego.calculcator.api;

import android.text.TextUtils;


import com.timego.calculcator.LogUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 项目名:    TODO-MVVM
 * 包名       com.azhon.mvvm.api
 * 文件名:    Api
 * 创建时间:  2019-03-27 on 14:56
 * 描述:     TODO 使用Retrofit基础服务
 */

public class Api extends BaseApi {

    private static final long CONNECT_TIMEOUT = 10;
    private static final long READ_TIMEOUT = 10;
    private static final long WRITE_TIMEOUT = 10;


    /**
     * 静态内部类单例
     */
    private static class ApiHolder {
        private static Api api = new Api();

        private final static ApiService apiService = api.initRetrofit(getBaseUrl())
                .create(ApiService.class);


    }

    public static ApiService getInstance() {
        return ApiHolder.apiService;
    }

    private static String getBaseUrl() {
        return ApiService.BASE_URL;

    }

    /**
     * 做自己需要的操作
     */
    @Override
    protected OkHttpClient setClient() {
        OkHttpClient.Builder builder;
        builder = new OkHttpClient()
                .newBuilder();
        //禁止使用代理抓取数据
//        builder.proxy(Proxy.NO_PROXY);
        //设置超时
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(new HeaderInterceptor());

//        if(Config.IS_DEBUG) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
            String text = message;
            LogUtils.i("OKHttp111111-----", text);
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
//        }
        return builder.build();

    }

}
