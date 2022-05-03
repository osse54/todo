package com.example.android.decorator;

import android.graphics.Color;
import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.List;

public class TodoDecorator implements DayViewDecorator {
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        boolean flag = false;
        List<Todo> list = MyUtils.getMainActivity().getList();
        for(Todo temp : list) {
            if(temp.getEnd() != null && temp.getEnd().equals(MyUtils.getStringFromDate(day.getDate()))) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void decorate(DayViewFacade view) {
        // 숫자 뒤에 f는 float, 16진수 표현식은 숫자 앞에 0x
        view.addSpan(new DotSpan(8f, Color.rgb(0xb3,0x79, 0xec)));
    }
}
