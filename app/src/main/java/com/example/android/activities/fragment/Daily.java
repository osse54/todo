package com.example.android.activities.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.Date;

public class Daily extends TodoFragment {

    private RecyclerView recyclerViewUncomplete;
    private DayAdapter myExpandableAdapterUncomplete;
    private LinearLayoutManager linearLayoutManagerUncomplete;
    private ImageButton expandUncomplete;

    private RecyclerView recyclerViewComplete;
    private DayAdapter myExpandableAdapterComplete;
    private LinearLayoutManager linearLayoutManagerComplete;
    private ImageButton expandComplete;

    private TextView dailyDateView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily, container, false);

        dailyDateView = view.findViewById(R.id.dailyDateView);

        expandUncomplete = view.findViewById(R.id.uncompleteExpandBtn);

        recyclerViewUncomplete = view.findViewById(R.id.calendarRecyclerViewUnComplete);
        if(linearLayoutManagerUncomplete == null) {
            linearLayoutManagerUncomplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterUncomplete == null) {
            myExpandableAdapterUncomplete = new DayAdapter(false);
        }

        expandComplete = view.findViewById(R.id.completeExpandBtn);

        recyclerViewComplete = view.findViewById(R.id.calendarRecyclerViewComplete);
        if(linearLayoutManagerComplete == null) {
            linearLayoutManagerComplete = new LinearLayoutManager(getActivity());
        }
        if(myExpandableAdapterComplete == null) {
            myExpandableAdapterComplete = new DayAdapter(true);
        }

        myExpandableAdapterUncomplete.notifyDataSetChanged();
        myExpandableAdapterComplete.notifyDataSetChanged();
        setListener();
        return view;
    }

    private void setListener() {
        expandUncomplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewUncomplete, expandUncomplete));
        expandComplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewComplete, expandComplete));
    }

    @Override
    public void onResume() {
        postView();
        MyUtils.adapters = new RecyclerView.Adapter[]{myExpandableAdapterUncomplete, myExpandableAdapterComplete};
        refresh();
        setDailyDateView();
        super.onResume();
    }

    @Override
    public void onStop() {
        detachView();
        super.onStop();
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

    public ArrayList<TodoViewHolder> getViewHolderList() {
        ArrayList<TodoViewHolder> list = new ArrayList<>();
        list.addAll(myExpandableAdapterUncomplete.getViewHolderList());
        list.addAll(myExpandableAdapterComplete.getViewHolderList());
        return list;
    }

    @Override
    public void refresh() {
        myExpandableAdapterUncomplete.refresh();
        myExpandableAdapterComplete.refresh();
    }

    private void setDailyDateView() {
        String time = MyUtils.getStringFromDate(new Date(), MyUtils.SHOWN_DATE_PATTERN);
        if(!time.equals(dailyDateView.getText().toString())) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    dailyDateView.setText(time);
                }
            });
        }
    }
}
