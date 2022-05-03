package com.example.android.decorator;

import android.graphics.Color;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import com.example.android.R;
import com.example.android.util.MyUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayDecorator implements DayViewDecorator {
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        CalendarDay today = CalendarDay.today();
        return today.getDay() == day.getDay() && today.getMonth() == day.getMonth() && today.getYear() == day.getYear();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(MyUtils.getMainActivity().getDrawable(R.drawable.today_background));
    }
}
