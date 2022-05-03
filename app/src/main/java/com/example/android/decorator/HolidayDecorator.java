package com.example.android.decorator;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import com.example.android.dto.Holiday;
import com.example.android.util.MyUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

public class HolidayDecorator implements DayViewDecorator {

    private static final String HOLIDAY_TYPE = "National holiday";
    private int month;

    public HolidayDecorator(int month) {
        this.month = month;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // 오늘과 CalendarDay의 month값이 다르면 decorate적용 안함
        if(day.getMonth() != month) {
            return false;
        }
        boolean flag = false;
        List<Holiday> list = MyUtils.getHolidays(day.getYear());
        if(list != null) {
            for(int i = 0; i < list.size() && !flag; i++) {
                flag = list.get(i).getDate().equals(MyUtils.getStringFromDate(day.getDate()))
                        && String.valueOf(list.get(i).getType()[0]).equals(HOLIDAY_TYPE);
            }
        }
        return flag;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
