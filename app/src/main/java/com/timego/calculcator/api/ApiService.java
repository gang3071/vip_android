package com.timego.calculcator.api;

import com.timego.calculcator.PassBean;
import com.timego.calculcator.VersionBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 项目名:
 * 包名       com.azhon.mvvm.api
 * 文件名:    ApiService
 * 创建时间:  2019-03-27 on 14:55
 * 描述:
 *
 */

public interface ApiService {
    String NET_TEST_FILE ="/assets/main/config.json";
    String NET_TEST_FILE1 ="assets/main/config.json";

    /**
     * 生产环境和测试环境标记
     */
    boolean isProduction = true;

    //大陆站
//    String BASE_URL = "https://api.baozhuangmall.com";
    //测试服
    String BASE_URL = isProduction?"https://api.1ting.cn":"https://yjb-test-api.qtalk666.top";

    String Site_Id = "7e3c09b8-38ed-4092-b8ec-872fdec78499";
    //String DEFAULT_WEB_URL = "http://client.senboli.com";
    String DEFAULT_WEB_URL = "https://yxw.1ting.cn";

    String DownLoadUrl = "https://oss.letschat2023.com/version.json";

    String DownLoadUrl_TEST = "https://oss.letschat2023.com/version_test.json";

    @FormUrlEncoded
    @POST("/external/get-password")
    Observable<Result<PassBean>> getChannelInfo(@Field("Site_Id") String Site_Id);


    @GET
    Observable<Result<VersionBean>> getConfigs(@Url String fullUrl);



}

