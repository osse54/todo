package com.example.android.activities.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.OnExpandButtonClickListener;
import com.example.android.activities.adapter.DayAdapter;
import com.example.android.activities.MainActivity;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

public class Daily extends Fragment {

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
        View view = inflater.inflate(R.layout.daily, container, false);

        expandUncomplete = view.findViewById(R.id.uncompleteExpandBtn);

        recyclerViewUncomplete = view.findViewById(R.id.calendarRecyclerViewUnComplete);
        linearLayoutManagerUncomplete = new LinearLayoutManager(getActivity());
        myExpandableAdapterUncomplete = new DayAdapter(false);

        recyclerViewUncomplete.setLayoutManager(linearLayoutManagerUncomplete);
        recyclerViewUncomplete.setAdapter(myExpandableAdapterUncomplete);

        myExpandableAdapterUncomplete.notifyDataSetChanged();

        expandComplete = view.findViewById(R.id.completeExpandBtn);

        recyclerViewComplete = view.findViewById(R.id.calendarRecyclerViewComplete);
        linearLayoutManagerComplete = new LinearLayoutManager(getActivity());
        myExpandableAdapterComplete = new DayAdapter(true);

        recyclerViewComplete.setLayoutManager(linearLayoutManagerComplete);
        recyclerViewComplete.setAdapter(myExpandableAdapterComplete);

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
        super.onResume();
    }

    @Override
    public void onStop() {
        detachView();
        super.onStop();
    }

    private void detachView() {
        Toast.makeText(MyUtils.getMainActivity(), "Daily, 뷰 제거", Toast.LENGTH_SHORT).show();
    }

    private void postView() {
        Toast.makeText(MyUtils.getMainActivity(), "Daily, 뷰 게시", Toast.LENGTH_SHORT).show();
    }
}
