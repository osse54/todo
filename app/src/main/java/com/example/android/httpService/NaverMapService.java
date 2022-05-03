package com.example.android.httpService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NaverMapService {
    String URL = "https://naveropenapi.apigw.ntruss.com/";
    String CLIENT_ID_HEADER = "X-NCP-APIGW-API-KEY-ID: ";
    String CLIENT_ID = "";
    String CLIENT_SECRET_HEADER = "X-NCP-APIGW-API-KEY: ";
    String CLIENT_SECRET = "";

    // https://api.ncloud-docs.com/docs/ai-naver-mapsgeocoding-geocode
    @Headers({CLIENT_ID_HEADER + CLIENT_ID, CLIENT_SECRET_HEADER + CLIENT_SECRET})
    @GET("map-geocode/v2/geocode")
    Call<ResponseBody> getGeocode(@Query("query") String query, @Query("count") int count);

    // https://api.ncloud-docs.com/docs/ai-naver-mapsreversegeocoding-gc 왜 에러남?
    @Headers({CLIENT_ID_HEADER + CLIENT_ID, CLIENT_SECRET_HEADER + CLIENT_SECRET})
    @GET("map-reversegeocode/v2/gc")
    Call<ResponseBody> getReverseGeocode(@Query("coords") String coords, @Query("orders") String orders, @Query("output") String output);
}
