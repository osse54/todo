package com.example.android.decorator;
import java.util.Calendar;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SaturdayDecorator implements DayViewDecorator {
    private int month;
    public SaturdayDecorator(int month) {
        this.month = month;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // 오늘과 CalendarDay의 month값이 다르면 decorate적용 안함
        if(day.getMonth() != month) {
            return false;
        }
        // 아래 반환값이 1이면 토요일로 판단 https://chobopark.tistory.com/111
        return day.getCalendar().get(Calendar.DAY_OF_WEEK) == 7;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
