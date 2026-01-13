package com.timego.calculcator.api;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by wmm on 2020/9/8
 */
public class HeaderInterceptor implements Interceptor {
    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .addHeader("Site-Id", ApiService.Site_Id)
                .build();
//        LogUtils.i("头文件language："+language);
        return chain.proceed(request);
    }
}