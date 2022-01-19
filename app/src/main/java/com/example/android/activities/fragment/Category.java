package com.example.android.activities.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.adapter.CategoryAdapter;
import com.example.android.activities.adapter.CategoryTodoAdapter;
import com.example.android.activities.adapter.TodoViewHolder;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Category extends TodoFragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category, container, false);

        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        if(linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        }
        if(categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter();
        }
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        postView();
        refresh();
        super.onResume();
    }

    @Override
    public void onStop() {
        detachView();
        super.onStop();
    }

    public void detachView() {
        if(recyclerView.getAdapter() != null) {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
        }
    }

    public void postView() {
        if(recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(categoryAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public ArrayList<TodoViewHolder> getViewHolderList() {
        return new ArrayList<>(categoryAdapter.getViewHolderList());
    }

    @Override
    public void refresh() {
        categoryAdapter.refresh();
    }
}
