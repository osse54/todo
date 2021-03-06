package com.example.android.activities.fragment;

import android.os.Handler;
import android.widget.ImageButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.adapter.CalendarDayAdapter;
import com.example.android.decorator.*;
import com.example.android.dto.Holiday;
import com.example.android.listener.OnExpandButtonClickListener;
import com.example.android.activities.adapter.TodoViewHolder;
import com.example.android.util.MyUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends TodoFragment {

    private RecyclerView recyclerViewUncomplete;
    private CalendarDayAdapter myExpandableAdapterUncomplete;
    private LinearLayoutManager linearLayoutManagerUncomplete;
    private ImageButton expandUncomplete;

    private RecyclerView recyclerViewComplete;
    private CalendarDayAdapter myExpandableAdapterComplete;
    private LinearLayoutManager linearLayoutManagerComplete;
    private ImageButton expandComplete;
    private TextView dateInfo;

    private MaterialCalendarView calendarView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        // MaterialCalendarView ??????
        calendarView = view.findViewById(R.id.calendarView);

        // ????????? ???????????? ????????? ??????
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();

        // MonthArray == 1??? 2??? ?????? ??????, WeekDay == ??????????????? ????????? ?????? ??????
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        calendarView.setCurrentDate(new Date());

        // ?????? ???????????? ???????????? ????????? ?????? ???????????? decorator??? ????????? ????????? ????????? ?????????.
        // ?????? ?????? ????????? ????????? ??? ????????? ???????????? ?????????.
        // setShowOtherDates(int showOtherDates) ?????? ???????????? ?????? ?????? ????????? ???????????? ?????? ????????? ????????? ?????? ??? ??????.
        // ?????? ????????? ????????? ?????? ?????? ????????? ????????????.
        int month = Calendar.getInstance().get(Calendar.MONTH);
        SundayDecorator sunday = new SundayDecorator(month);
        SaturdayDecorator saturday = new SaturdayDecorator(month);
        HolidayDecorator holiday = new HolidayDecorator(month);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                sunday.setMonth(date.getMonth());
                saturday.setMonth(date.getMonth());
                holiday.setMonth(date.getMonth());

                widget.removeDecorator(sunday);
                widget.removeDecorator(saturday);
                widget.removeDecorator(holiday);

                widget.addDecorator(sunday);
                widget.addDecorator(saturday);
                widget.addDecorator(holiday);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // decorator ??????
                calendarView.addDecorator(new TodayDecorator());
                calendarView.addDecorator(new TodoDecorator());
                calendarView.addDecorator(sunday);
                calendarView.addDecorator(saturday);
                calendarView.addDecorator(holiday);
            }
        });

        expandUncomplete = view.findViewById(R.id.uncompleteExpandBtn);
        expandComplete = view.findViewById(R.id.completeExpandBtn);

        recyclerViewUncomplete = view.findViewById(R.id.calendarRecyclerViewUncomplete);
        if(linearLayoutManagerUncomplete == null) {
            linearLayoutManagerUncomplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterUncomplete == null) {
            myExpandableAdapterUncomplete = new CalendarDayAdapter(false, MyUtils.getStringFromDate(CalendarDay.today().getDate()));
        }

        recyclerViewComplete = view.findViewById(R.id.calendarRecyclerViewComplete);
        if(linearLayoutManagerComplete == null) {
            linearLayoutManagerComplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterComplete == null) {
            myExpandableAdapterComplete = new CalendarDayAdapter(true, MyUtils.getStringFromDate(CalendarDay.today().getDate()));
        }

        recyclerViewUncomplete.setLayoutManager(linearLayoutManagerUncomplete);
        recyclerViewUncomplete.setAdapter(myExpandableAdapterUncomplete);
        recyclerViewComplete.setLayoutManager(linearLayoutManagerComplete);
        recyclerViewComplete.setAdapter(myExpandableAdapterComplete);

        // ?????? ?????? ????????????
        dateInfo = view.findViewById(R.id.dateInfo);

        // ????????? ????????? ????????? ???????????? ????????? ?????? // ?????? ?????? ??? ?????? ?????? ?????? ?????? ??? ?????? ????????? ????????? ??????
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull @NotNull MaterialCalendarView widget, @NonNull @NotNull CalendarDay date, boolean selected) {
                myExpandableAdapterComplete.setDay(MyUtils.getStringFromDate(date.getDate()));
                myExpandableAdapterUncomplete.setDay(MyUtils.getStringFromDate(date.getDate()));
                MyUtils.setDate(date.getYear(), date.getMonth(), date.getDay());
                List<Holiday> list = MyUtils.getHolidays(date.getYear());
                String holiday = "";
                if(list != null) {
                    for(int i = 0; i < list.size(); i++) {
                        if(MyUtils.getStringFromDate(date.getDate()).equals(list.get(i).getDate())) {
                            holiday = list.get(i).getName();
                        }
                    }
                    dateInfo.setText(holiday);
                    if(!holiday.equals("")) {
                        dateInfo.setVisibility(View.VISIBLE);
                    } else {
                        dateInfo.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        // ????????? ???????????? ?????? ??????
        calendarView.setSelectedDate(CalendarDay.today());

        myExpandableAdapterUncomplete.notifyDataSetChanged();
        myExpandableAdapterComplete.notifyDataSetChanged();
        setListener();

        return view;
    }

    @Override
    public void onResume() {
        postView();
        MyUtils.adapters = new RecyclerView.Adapter[]{myExpandableAdapterUncomplete, myExpandableAdapterComplete};
        myExpandableAdapterUncomplete.refresh();
        myExpandableAdapterComplete.refresh();
        super.onResume();
    }

    @Override
    public void onStop() {
        detachView();
        resetCalendarView();
        super.onStop();
    }

    private void resetCalendarView() {
        MyUtils.setDate(0, 0, 0);
        calendarView.setSelectedDate(CalendarDay.today());
    }

    public void detachView() {
        if(recyclerViewComplete.getAdapter() != null) {
            recyclerViewComplete.setAdapter(null);
            recyclerViewComplete.setLayoutManager(null);
        }
        if(recyclerViewUncomplete.getAdapter() != null) {
            recyclerViewUncomplete.setAdapter(null);
            recyclerViewUncomplete.setLayoutManager(null);
        }
    }

    public void postView() {
        if(recyclerViewComplete.getAdapter() == null) {
            recyclerViewComplete.setAdapter(myExpandableAdapterComplete);
            recyclerViewComplete.setLayoutManager(linearLayoutManagerComplete);
        }
        if(recyclerViewUncomplete.getAdapter() == null) {
            recyclerViewUncomplete.setAdapter(myExpandableAdapterUncomplete);
            recyclerViewUncomplete.setLayoutManager(linearLayoutManagerUncomplete);
        }
    }

    private void setListener() {
        expandUncomplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewUncomplete, expandUncomplete));
        expandComplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewComplete, expandComplete));
    }

    public ArrayList<TodoViewHolder> getViewHolderList() {
        ArrayList<TodoViewHolder> list = new ArrayList<>(myExpandableAdapterUncomplete.getViewHolderList());
        list.addAll(myExpandableAdapterComplete.getViewHolderList());
        return list;
    }

    @Override
    public void refresh() {
        myExpandableAdapterUncomplete.refresh();
        myExpandableAdapterComplete.refresh();
    }
}
