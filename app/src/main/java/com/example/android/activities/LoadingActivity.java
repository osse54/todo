package com.example.android.activities;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.R;
import com.example.android.util.MyUtils;

import java.util.Calendar;
import java.util.Date;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startLoading();
    }
    private void startLoading() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // 휴일 정보 불러오기, 핸들러 안에서 하면 애니메이션이 많이 끊김
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyUtils.getHolidays(Calendar.getInstance().get(Calendar.YEAR));
                    }
                }).start();

                finish();
            }
        }, 1000);
    }
}