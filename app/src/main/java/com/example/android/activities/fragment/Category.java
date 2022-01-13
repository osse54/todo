package com.example.android.activities.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.activities.MainActivity;
import com.example.android.activities.adapter.CategoryAdapter;
import com.example.android.util.MyUtils;
import org.jetbrains.annotations.NotNull;

public class Category extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category, container, false);

        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryAdapter.notifyDataSetChanged();

        return view;
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
        Toast.makeText(MyUtils.getMainActivity(), "Category, 뷰 제거", Toast.LENGTH_SHORT).show();
    }

    private void postView() {
        Toast.makeText(MyUtils.getMainActivity(), "Category, 뷰 게시", Toast.LENGTH_SHORT).show();
    }

}
