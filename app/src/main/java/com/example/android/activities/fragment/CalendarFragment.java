package com.example.android.activities.fragment;
import android.widget.CalendarView;
import android.widget.ImageButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.listener.OnExpandButtonClickListener;
import com.example.android.activities.adapter.DayAdapter;
import com.example.android.activities.adapter.TodoViewHolder;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CalendarFragment extends TodoFragment {

    private RecyclerView recyclerViewUncomplete;
    private DayAdapter myExpandableAdapterUncomplete;
    private LinearLayoutManager linearLayoutManagerUncomplete;
    private ImageButton expandUncomplete;

    private RecyclerView recyclerViewComplete;
    private DayAdapter myExpandableAdapterComplete;
    private LinearLayoutManager linearLayoutManagerComplete;
    private ImageButton expandComplete;
    private CalendarView calendarView;
    private long nowDate;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                MyUtils.setDate(i, i1, i2);
            }
        });
        nowDate = calendarView.getDate();

        expandUncomplete = view.findViewById(R.id.uncompleteExpandBtn);
        expandComplete = view.findViewById(R.id.completeExpandBtn);

        recyclerViewUncomplete = view.findViewById(R.id.calendarRecyclerViewUnComplete);

        if(linearLayoutManagerUncomplete == null) {
            linearLayoutManagerUncomplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterUncomplete == null) {
            myExpandableAdapterUncomplete = new DayAdapter(false);
        }
        recyclerViewComplete = view.findViewById(R.id.calendarRecyclerViewComplete);
        if(linearLayoutManagerComplete == null) {
            linearLayoutManagerComplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterComplete == null) {
            myExpandableAdapterComplete = new DayAdapter(true);
        }

        recyclerViewUncomplete.setLayoutManager(linearLayoutManagerUncomplete);
        recyclerViewUncomplete.setAdapter(myExpandableAdapterUncomplete);
        recyclerViewComplete.setLayoutManager(linearLayoutManagerComplete);
        recyclerViewComplete.setAdapter(myExpandableAdapterComplete);


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
        calendarView.setDate(nowDate);
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
