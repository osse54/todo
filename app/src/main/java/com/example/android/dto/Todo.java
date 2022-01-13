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
import androidx.annotation.Nullable;
import com.example.android.util.MyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo {
    private static long NEXT = 0;
    public final long no;      // 식별자로써 사용

    private String name;        // 일정 제목
    private String category;    // 일정 카테고리
    private String explain;     // 일정 설명
    private boolean complete;   // 일정 완료 여부
    /**
        시작 시간, 종료 시간 사용 방법
        https://junghn.tistory.com/entry/Java-%EB%82%A0%EC%A7%9C-%EA%B3%84%EC%82%B0-%EB%B0%A9%EB%B2%95%EB%85%84-%EC%9B%94-%EC%9D%BC-%EB%8D%94%ED%95%98%EA%B3%A0-%EB%B9%BC%EB%8A%94-%EB%B0%A9%EB%B2%95
     */
    private String start;         // 일정 생성 시각
    private String end;           // 일정 종료 시각
    private String cycle;         // 일정의 주기

    public Todo(String name, String category, String explain, boolean complete) {
        no = NEXT++;    // NEXT가 가진 수를 no에 할당 후 +1
        this.name = name;
        this.category = category;
        this.explain = explain;
        this.complete = complete;
        start = MyUtils.getNow();
    }

    public Todo(String name, String category, String explain, boolean complete, String end) {
        no = NEXT++;    // NEXT가 가진 수를 no에 할당 후 +1
        this.name = name;
        this.category = category;
        this.explain = explain;
        this.complete = complete;
        this.end = end;
    }

    public Todo(String name, String category, String explain, boolean complete, String end, String cycle) {
        no = NEXT++;    // NEXT가 가진 수를 no에 할당 후 +1
        this.name = name;
        this.category = category;
        this.explain = explain;
        this.complete = complete;
        this.end = end;
        this.cycle = cycle;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
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

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if(obj == null || !(obj instanceof Todo)) {
            return false;
        }
        return ((Todo) obj).no == no;
    }
}
