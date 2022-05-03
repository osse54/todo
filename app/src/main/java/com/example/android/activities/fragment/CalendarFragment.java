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

        // MaterialCalendarView 사용
        calendarView = view.findViewById(R.id.calendarView);

        // 시작이 월요일이 되도록 설정
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();

        // MonthArray == 1월 2월 표기 설정, WeekDay == 월화수목금 한국어 표기 설정
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        calendarView.setCurrentDate(new Date());

        // 현재 보여지는 달력에서 지정된 월이 아니라면 decorator를 없애는 기능을 위해서 추가함.
        // 해당 기능 있으면 달력의 월 변경에 딜레이가 생긴다.
        // setShowOtherDates(int showOtherDates) 해당 메소드를 통해 다른 날짜를 보여주지 않는 것으로 문제를 가릴 수 있다.
        // 해당 기능을 없애고 그냥 두는 방법을 사용한다.
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
                // decorator 추가
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

        // 일정 정보 텍스트뷰
        dateInfo = view.findViewById(R.id.dateInfo);

        // 선택된 날짜의 일정을 보여주는 것으로 선택 // 날짜 선택 후 일정 추가 화면 이동 시 기한 선택이 되게끔 설정
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

        // 오늘이 선택되어 있게 설정
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
