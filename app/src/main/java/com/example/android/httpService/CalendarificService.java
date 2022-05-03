package com.example.android.httpService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CalendarificService {
    String baseURL = "https://calendarific.com/api/v2/";
    String apiKey = ""; // 할당받은 api key request에 항상 query로 붙어야 함

    @GET("holidays")
    Call<ResponseBody> getHolidays(@Query("api_key") String apiKey, @Query("country") String country, @Query("year") int year);
}
