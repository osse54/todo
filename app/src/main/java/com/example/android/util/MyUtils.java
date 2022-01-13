package com.example.android.util;

import android.content.Intent;
import com.example.android.activities.MainActivity;
import com.example.android.dto.Todo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {

    // fragment 이동 시 마다 각 fragment가 가지는 adapter들을 할당해줘야 한다.
    public static Refreshable[] REFRESHABLES;
    private static MainActivity MAIN_ACTIVITY;

    public final static String DATE_PATTERN = "yyyyMMdd";

    // 해당 일정의 Intent를 자동 생성하여 return한다.
    public static Intent getIntent(Todo todo) {
        Intent intent = new Intent();
        intent.putExtra("name", todo.getName());
        intent.putExtra("category", todo.getCategory());
        intent.putExtra("explain", todo.getExplain());
        intent.putExtra("complete", todo.isComplete());
        return intent;
    }

    // DATE_PATTERN과 같은 패턴으로 날짜를 String으로 반환
    public static String getNow() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

    public static void refresh() {
        for(Refreshable temp : REFRESHABLES) {
            temp.refresh();
        }
    }

    public static MainActivity getMainActivity() {
        return MAIN_ACTIVITY;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MAIN_ACTIVITY = mainActivity;
    }
}
