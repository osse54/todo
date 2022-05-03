package com.example.android.activities.adapter;

import com.example.android.dto.Todo;
import com.example.android.util.MyUtils;

public class CalendarDayAdapter extends DayAdapter {
    private String day;
    public CalendarDayAdapter(boolean check, String day) {
        super(check);
        this.day = day;
    }

    public void setDay(String day) {
        this.day = day;
        refresh();
    }

    @Override
    public void refresh() {
        getMyList().clear();
        for(Todo temp : MyUtils.getMainActivity().getList()) {
            if(temp.getEnd() != null) {
                if(temp.isComplete() == isCheck() && temp.getEnd().equals(day)) {
                    getMyList().add(temp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
