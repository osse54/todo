package com.example.android.activities.fragment;
import android.widget.ImageButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.MainActivity;
import com.example.android.activities.OnExpandButtonClickListener;
import com.example.android.activities.adapter.DayAdapter;
import com.example.android.util.MyUtils;
import com.example.android.util.Refreshable;
import org.jetbrains.annotations.NotNull;

public class Calendar extends Fragment {

    private RecyclerView recyclerViewUncomplete;
    private DayAdapter myExpandableAdapterUncomplete;
    private LinearLayoutManager linearLayoutManagerUncomplete;
    private ImageButton expandUncomplete;

    private RecyclerView recyclerViewComplete;
    private DayAdapter myExpandableAdapterComplete;
    private LinearLayoutManager linearLayoutManagerComplete;
    private ImageButton expandComplete;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        expandUncomplete = view.findViewById(R.id.uncompleteExpandBtn);
        expandComplete = view.findViewById(R.id.completeExpandBtn);

        recyclerViewUncomplete = view.findViewById(R.id.calendarRecyclerViewUnComplete);
        linearLayoutManagerUncomplete = new LinearLayoutManager(getActivity());
        myExpandableAdapterUncomplete = new DayAdapter(false);
        recyclerViewComplete = view.findViewById(R.id.calendarRecyclerViewComplete);
        linearLayoutManagerComplete = new LinearLayoutManager(getActivity());
        myExpandableAdapterComplete = new DayAdapter(true);

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
        MyUtils.REFRESHABLES = new Refreshable[]{myExpandableAdapterUncomplete};
        super.onResume();
    }

    @Override
    public void onStop() {
        detachView();
        super.onStop();
    }

    public void detachView() {
        recyclerViewUncomplete.setAdapter(null);
    }

    public void postView() {
        recyclerViewUncomplete.setAdapter(myExpandableAdapterUncomplete);
    }

    private void setListener() {
        expandUncomplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewUncomplete, expandUncomplete));
        expandComplete.setOnClickListener(new OnExpandButtonClickListener(recyclerViewComplete, expandComplete));
    }
}
