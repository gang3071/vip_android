package com.timego.game.api;


import io.reactivex.Observable;
import retrofit2.http.GET;
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
    String DownLoadUrl = "https://oss.letschat2023.com/game_version.json";

    String BASE_URL = "https://api.baozhuangmall.com";

    @GET
    Observable<Result<VersionBean>> getConfigs(@Url String fullUrl);


}

