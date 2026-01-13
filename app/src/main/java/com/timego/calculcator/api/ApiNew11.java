package com.timego.calculcator.api;



import com.timego.calculcator.LogUtils;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 项目名:    TODO-MVVM
 * 包名       com.azhon.mvvm.api
 * 文件名:    Api
 * 创建时间:  2019-03-27 on 14:56
 * 描述:     TODO 使用Retrofit基础服务
 */

public class ApiNew11 extends BaseApi {

    private static final long CONNECT_TIMEOUT = 5;
    private static final long READ_TIMEOUT = 5;
    private static final long WRITE_TIMEOUT = 5;


    /**
     * 静态内部类单例
     */
    private static class ApiHolder {
        private static ApiNew11 api = new ApiNew11();

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

        // 创建一个信任所有证书的TrustManager
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };



        // 创建一个SSLSocketFactory

        // 创建一个HostnameVerifier，它不会验证主机名
        HostnameVerifier trustAllHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // 不验证主机名
            }
        };

        OkHttpClient.Builder builder;
        builder = new OkHttpClient()
                .newBuilder();


        // 初始化SSLContext
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

        } catch (Exception e) {
        }
        //禁止使用代理抓取数据
//        builder.proxy(Proxy.NO_PROXY);
        //设置超时
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(new HeaderInterceptor());
        builder.hostnameVerifier(trustAllHosts);

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

