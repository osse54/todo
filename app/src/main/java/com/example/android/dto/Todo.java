package com.example.android.dto;

/*
카테고리
이름
설명
시작 시간
끝 시간
주기
이미지 또는 파일 - 보류
 */

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android.util.MyUtils;
import com.google.gson.annotations.Expose;
import com.naver.maps.geometry.LatLng;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo implements Comparable<Todo> {
    @Expose(serialize = false, deserialize = false)
    public static long NEXT = 0;

    @Expose(serialize = true, deserialize = true)
    public final long no;      // 식별자로써 사용

    private String name;        // 일정 제목
    private String category;    // 일정 카테고리
    private String explain;     // 일정 설명
    private boolean complete;   // 일정 완료 여부
    /**
        시작 시간, 종료 시간 사용 방법
        https://junghn.tistory.com/entry/Java-%EB%82%A0%EC%A7%9C-%EA%B3%84%EC%82%B0-%EB%B0%A9%EB%B2%95%EB%85%84-%EC%9B%94-%EC%9D%BC-%EB%8D%94%ED%95%98%EA%B3%A0-%EB%B9%BC%EB%8A%94-%EB%B0%A9%EB%B2%95
     */
    private String end;           // 일정 종료 시각
    private String locationName;    // 일정 위치
    private String latitude; // 위도
    private String longitude; // 경도

    private Todo(String name, String category, String explain, boolean complete, long no) {
        this.no = no;
        this.name = name;
        this.category = category;
        this.explain = explain;
        this.complete = complete;
    }

    public static Todo getTodo(String name, String category, String explain, boolean complete) {
        return new Todo(name, category, explain, complete, NEXT++);
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatLng(LatLng latLng) {
        this.latitude = String.valueOf(latLng.latitude);
        this.longitude = String.valueOf(latLng.longitude);
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if(obj == null || !(obj instanceof Todo)) {
            return false;
        }
        return ((Todo) obj).no == no;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return getName();
    }

    public long getNo() {
        return no;
    }

    @Override
    public int compareTo(Todo todo) {
        int result = 0;
        if(no > todo.no) {
            result = 1;
        } else if(no < todo.no) {
            result = -1;
        }
        return result;
    }
}
